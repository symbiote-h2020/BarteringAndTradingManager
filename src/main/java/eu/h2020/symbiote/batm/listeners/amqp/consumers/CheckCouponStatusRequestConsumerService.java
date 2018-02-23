package eu.h2020.symbiote.batm.listeners.amqp.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import eu.h2020.symbiote.batm.dto.CouponDTO;
import eu.h2020.symbiote.batm.listeners.amqp.request.CheckCouponStatusRequest;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.services.CouponService;
import eu.h2020.symbiote.batm.utils.Rest;
import eu.h2020.symbiote.security.commons.exceptions.custom.InvalidArgumentsException;
import eu.h2020.symbiote.security.commons.exceptions.custom.UserManagementException;
import eu.h2020.symbiote.security.communication.payloads.ErrorResponseContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CheckCouponStatusRequestConsumerService extends DefaultConsumer {

    private static Log logger = LogFactory.getLog(CheckCouponStatusRequestConsumerService.class);

    private final String adminUsername;
    private final String adminPassword;

    @Value("#{'${BTCore.url:localhost:8081}'}")
    private String coreUrl;

    @Value("#{'${batm.platformID:myself}'}")
    private String myPlatformId;



    @Autowired
    private CouponService couponService;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public CheckCouponStatusRequestConsumerService(Channel channel, String adminUsername, String adminPassword) {
        super(channel);
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }



    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {

        /**
         *  get request from RAP
         *
         *  request coupon
         *
         *  if (new coupon)
         *      ask core if coupon is valid
         *      validate deal
         *      store coupon
         *
         *  consume coupon
         *  notify core about coupon consumption
         *  RAP ack
         *
         *
         */

        CheckCouponStatusRequest request;


        String message = new String(body, "UTF-8");
        ObjectMapper om = new ObjectMapper();
        String response="";

        if (properties.getReplyTo() != null || properties.getCorrelationId() != null) {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(properties.getCorrelationId())
                    .build();

            Rest<CouponDTO> rest = new Rest();
            try {
                request = om.readValue(message, CheckCouponStatusRequest.class);

                if (request.getAdminCredentials() == null)
                    throw new InvalidArgumentsException();
                // and if they don't match the admin credentials from properties
                if (!request.getAdminCredentials().getUsername().equals(this.adminUsername)
                        || !request.getAdminCredentials().getPassword().equals(this.adminPassword))
                    throw new UserManagementException(HttpStatus.UNAUTHORIZED);

                // ask platform for coupon
                CouponDTO couponDto = rest.get(request.getPlatformURL(),null,null, CouponDTO.class );
                Coupon c = null;
                if (couponDto.isNew) {
                    //  ask core if coupon is valid
                    Boolean isValid = rest.postReturningBooelanOrNull(this.coreUrl+"/isvalid",null,null, couponDto.coupon );

                    if (isValid){
                        //  validate deal
                        // TODO -- validate deal

                        //  consume
                        String beneficiary=couponDto.coupon.getBeneficiary().isEmpty()? "someone":couponDto.coupon.getBeneficiary();
                        couponDto.coupon.addToUsedByList(beneficiary);

                        //  store coupon
                        c = couponService.createCoupon(couponDto.coupon);
                    }
                }

                 c = (c != null) ? c : couponService.findById(couponDto.coupon.id);


                if (c != null ){

                    // consume coupon
                    c.addToUsedByList(c.getBeneficiary().isEmpty()? "someone":c.getBeneficiary());
                    couponService.update(c);


                    // notify core about coupon consumption
                    Boolean use = rest.postReturningBooelanOrNull(this.coreUrl+"/use/"+myPlatformId,null,null, couponDto.coupon );
                }


            } catch (InvalidArgumentsException | UserManagementException e) {
                response = (new ErrorResponseContainer(e.getErrorMessage(), e.getStatusCode().ordinal())).toJson();
            } finally {
                // RAP ack
                response = response.isEmpty()?"OK":response;
                this.getChannel().basicPublish("", properties.getReplyTo(), replyProps, response.getBytes());
            }

        }else{
            logger.error("Received RPC message without ReplyTo or CorrelationId properties.");
        }


        // RAP ack
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

}

