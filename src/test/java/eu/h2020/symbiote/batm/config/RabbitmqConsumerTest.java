package eu.h2020.symbiote.batm.config;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import eu.h2020.symbiote.batm.BarteringAndTradingManager;
import eu.h2020.symbiote.batm.listeners.amqp.RabbitManager;
import eu.h2020.symbiote.batm.listeners.amqp.consumers.CheckCouponStatusRequestConsumerService;
import eu.h2020.symbiote.batm.listeners.amqp.request.CheckCouponStatusRequest;
import eu.h2020.symbiote.batm.services.CouponService;
import eu.h2020.symbiote.security.communication.payloads.Credentials;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {BarteringAndTradingManager.class,AppConfig.class})
public class RabbitmqConsumerTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private RabbitManager rabbitmanager;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private Channel initChannel(){
        String queue_name = rabbitmanager.getCheckCouponStatusQueue();

        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel=null;

        try {

            factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("rabbitmq");
            factory.setPassword("rabbitmq");
            factory.setVirtualHost("/");
            factory.setPort(5672);

            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return channel;
    }


//    @Test(expected = IllegalStateException.class)
    public void illegalStateExceptionTest() throws IOException {
        String queue_name = rabbitmanager.getCheckCouponStatusQueue();


        Channel channel = initChannel();
        CheckCouponStatusRequest couponRequest = new CheckCouponStatusRequest(new Credentials(rabbitmanager.getAdminUsername(), rabbitmanager.getAdminPassword()),"resourceType" ,"empty");

        BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(java.util.UUID.randomUUID().toString()).build();
        channel.basicPublish(null, null, null, "{}".getBytes());


    }

//    @Test
    public void okTest() throws IOException {
        String queue_name = rabbitmanager.getCheckCouponStatusQueue();

        Channel channel = initChannel();

        CheckCouponStatusRequest couponRequest = new CheckCouponStatusRequest(new Credentials(rabbitmanager.getAdminUsername(), rabbitmanager.getAdminPassword()),"resourceType", "empty");

        BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(java.util.UUID.randomUUID().toString()).build();

        channel.basicPublish("", queue_name, (AMQP.BasicProperties) props, new Gson().toJson(couponRequest).getBytes());

        // start the consumer
        Consumer consumer = new CheckCouponStatusRequestConsumerService(channel,rabbitmanager.getAdminUsername(), rabbitmanager.getAdminPassword());
        channel.basicConsume(queue_name, false, consumer);

    }

}
