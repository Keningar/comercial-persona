package ec.telconet.persona.kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import ec.telconet.microservicio.core.comercial.kafka.cons.CoreComercialConstants;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaResponse;
import io.opentracing.contrib.kafka.TracingConsumerInterceptor;

import org.springframework.kafka.support.serializer.JsonSerializer;

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

	@Value(value = "${kafka.request.max.byte:15728640}")
	private Integer maxByteRequestKafka;

	private final String uuidGrupoKafka = UUID.randomUUID().toString();

	@Bean
	public <T> ProducerFactory<String, KafkaRequest<T>> replyProducerSyncFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxByteRequestKafka);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public <T> ConsumerFactory<String, KafkaResponse<T>> replyConsumerSyncFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, CoreComercialConstants.GROUP_PERSONA.concat(uuidGrupoKafka));
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxByteRequestKafka);
		props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, TracingConsumerInterceptor.class.getName());
		props.put("key.serializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseSerializer");
		props.put("value.serializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseSerializer");
		props.put("key.deserializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseDeserializer");
		props.put("value.deserializer", "ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseDeserializer");
		return new DefaultKafkaConsumerFactory<>(props);
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
	public <T> ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyKafkaTemplate() {
		ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyTemplate = new ReplyingKafkaTemplate<>(replyProducerSyncFactory(), replyListenerSyncContainer());
		replyTemplate.setDefaultReplyTimeout(Duration.parse("PT" + replyTimeout));
		replyTemplate.setSharedReplyTopic(true);
		return replyTemplate;
	}
}
