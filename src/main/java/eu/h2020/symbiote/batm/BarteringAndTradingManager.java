package eu.h2020.symbiote.batm;



import eu.h2020.symbiote.batm.listeners.amqp.RabbitManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



@EnableDiscoveryClient    
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "eu.h2020.symbiote.batm")
public class BarteringAndTradingManager {

	private static Log logger = LogFactory.getLog(BarteringAndTradingManager.class);


	public static void main(String[] args) {
		SpringApplication.run(BarteringAndTradingManager.class, args);
    }


	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}


	@Component
	public static class CLR implements CommandLineRunner {

		private final RabbitManager rabbitManager;

		@Autowired
		public CLR(RabbitManager rabbitManager) {
			this.rabbitManager = rabbitManager;
		}

		@Override
		public void run(String... args) throws Exception {
//
			//message retrieval - start rabbit exchange and consumers
			this.rabbitManager.init();
			logger.info("CLR run() and Rabbit Manager init()");
		}
	}
}
