package ec.telconet.persona.kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import ec.telconet.microservicio.core.comercial.kafka.cons.CoreComercialConstants;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaResponse;

/**
 * Configuración del producer sincronico persona kafka
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Configuration
@EnableKafka
public class ProducerPersonaSynchroConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${kafka.request-reply.timeout-ms:300s}")
	private String replyTimeout;
	
	private String uuidGrupoKafka = UUID.randomUUID().toString();
	
	@Bean
	public <T> ConsumerFactory<String, KafkaResponse<T>> replyConsumerSyncFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, CoreComercialConstants.GROUP_PERSONA.concat(uuidGrupoKafka));
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put("key.serializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseSerializer");
		props.put("value.serializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseSerializer");
		props.put("key.deserializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseDeserializer");
		props.put("value.deserializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseDeserializer");
		return new DefaultKafkaConsumerFactory<String, KafkaResponse<T>>(props);
	}
	
	@Bean
	public <T> KafkaMessageListenerContainer<String, KafkaResponse<T>> replyListenerSyncContainer() {
		ContainerProperties containerProperties = new ContainerProperties(CoreComercialConstants.REPLY_PERSONA_SYNC);
		return new KafkaMessageListenerContainer<>(replyConsumerSyncFactory(), containerProperties);
	}
	
	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public <T> ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyKafkaTemplate(ProducerFactory<String, KafkaRequest<T>> pf,
			KafkaMessageListenerContainer<String, KafkaResponse<T>> container) {
		ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyTemplate = new ReplyingKafkaTemplate<>(pf, container);
		replyTemplate.setDefaultReplyTimeout(Duration.parse("PT" + replyTimeout));
		replyTemplate.setSharedReplyTopic(true);
		return replyTemplate;
	}
}
