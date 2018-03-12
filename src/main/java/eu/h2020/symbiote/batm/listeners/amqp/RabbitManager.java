package eu.h2020.symbiote.batm.listeners.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import eu.h2020.symbiote.batm.listeners.amqp.consumers.CheckCouponStatusRequestConsumerService;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityMisconfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Manages AMQP listeners
 * <p>
 */
@Component
public class RabbitManager {

    private static Log logger = LogFactory.getLog(RabbitManager.class);

    private Connection connection;

    @Value("${batm.deployment.owner.username}")
    private String adminUsername;
    @Value("${batm.deployment.owner.password}")
    private String adminPassword;

    @Value("${rabbit.host}")
    private String rabbitHost;
    @Value("${rabbit.username}")
    private String rabbitUsername;
    @Value("${rabbit.password}")
    private String rabbitPassword;
    @Value("${rabbit.exchange.batm.name}")

    private String exchangeName;
    @Value("${rabbit.exchange.batm.type}")
    private String exchangeType;
    @Value("${rabbit.exchange.batm.durable}")
    private boolean exchangeDurable;
    @Value("${rabbit.exchange.batm.autodelete}")
    private boolean exchangeAutodelete;
    @Value("${rabbit.exchange.batm.internal}")
    private boolean exchangeInternal;


    @Value("${rabbit.queue.checkCouponStatus.request}")
    private String checkCouponStatusQueue;
    @Value("${rabbit.routingKey.checkCouponStatus.request}")
    private String checkCouponStatusRoutingKey;

    @Autowired
    public RabbitManager() {
    }


    /**
     * Initiates connection with Rabbit server using parameters from Bootstrap Properties
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public Connection getConnection() throws IOException, TimeoutException {
        if (connection == null) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.rabbitHost);
            factory.setUsername(this.rabbitUsername);
            factory.setPassword(this.rabbitPassword);
            this.connection = factory.newConnection();
        }
        return this.connection;
    }


    /**
     * Closes given channel if it exists and is open.
     *
     * @param channel rabbit channel to close
     */
    private void closeChannel(Channel channel) {
        try {
            if (channel != null && channel.isOpen())
                channel.close();
        } catch (IOException | TimeoutException e) {
            logger.error(e);
        }
    }


    /**
     * Method gathers all of the rabbit consumer starter methods
     */
    private void startConsumers() throws SecurityMisconfigurationException {
        try {
            // start coupon status queue consumers
            startConsumerOfCheckCouponStatus();

        } catch (IOException e) {
            logger.error(e);
        }
    }





    /**
     * Method creates channel and declares Rabbit exchanges for BATM features.
     * It triggers start of all consumers used in with BATM communication.
     */
    public void init() throws SecurityMisconfigurationException {
        Channel channel = null;

        try {
            getConnection();
        } catch (IOException | TimeoutException e) {
            logger.error(e);
        }

        if (connection != null) {
            try {
                channel = this.connection.createChannel();

                channel.exchangeDeclare(this.exchangeName,
                        this.exchangeType,
                        this.exchangeDurable,
                        this.exchangeAutodelete,
                        this.exchangeInternal,
                        null);
                startConsumers();
            } catch (IOException e) {
                logger.error(e);
            } finally {
                closeChannel(channel);
            }
        }
    }


    @PreDestroy
    public void cleanup() {
        logger.info("Rabbit cleaned!");
        try {
            Channel channel;
            if (this.connection != null && this.connection.isOpen()) {
                channel = connection.createChannel();

                channel.queueUnbind(this.checkCouponStatusQueue, this.exchangeName,
                        this.checkCouponStatusRoutingKey);
                channel.queueDelete(this.checkCouponStatusQueue);

                closeChannel(channel);
                this.connection.close();
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**************
     *  Consumers *
     **************/

    private void startConsumerOfCheckCouponStatus() throws IOException {

        String queueName = this.checkCouponStatusQueue;

        Channel channel;

        try {
            channel = this.connection.createChannel();
            channel.queueDeclare(queueName, this.exchangeDurable, false, this.exchangeAutodelete, null);
            channel.queueBind(queueName, this.exchangeName, this.checkCouponStatusRoutingKey);

            logger.info("Batering and Trading Manager waiting for check coupon status request messages");

            // start the consumer
            Consumer consumer = new CheckCouponStatusRequestConsumerService(channel,this.adminUsername, this.adminPassword);
            channel.basicConsume(queueName, false, consumer);
        } catch (IOException e) {
            logger.error(e);
        }
    }


    public String getExchangeName(){return this.exchangeName;}
    public String getCheckCouponStatusRoutingKey(){return this.exchangeName;}
    public String getCheckCouponStatusQueue(){return this.checkCouponStatusQueue;}
    public String getAdminUsername(){return this.adminUsername;}
    public String getAdminPassword(){return this.adminPassword;}


}