package ec.telconet.persona.kafka;

import java.time.Duration;
import java.util.*;

import ec.telconet.microservicio.dependencia.util.kafka.KafkaProperties;
import ec.telconet.microservicio.dependencia.util.kafka.utils.KafkaResponseDeserializer;
import io.opentracing.contrib.kafka.TracingConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Configuración del producer sincrónico kafka
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Configuration
@EnableKafka
public class ProducerSynchroConfig {
	Logger log = LogManager.getLogger(this.getClass());

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${kafka.request.max.byte:26214400}")
	private Integer maxByteRequestKafka;

	@Value("${kafka.request-reply.timeout-ms:300s}")
	private String replyTimeout;

	@Autowired
	private KafkaProperties kafkaProperties;

	@Autowired
	public ProducerSynchroConfig(KafkaProperties kafkaProperties) {
		kafkaProperties.setTopicGroup(CoreComercialConstants.GROUP_PERSONA);
		log.info("Grupo kafka configurado: {}", kafkaProperties.getTopicGroup());
		Collection<String> colKafkaTopicReplySync = Collections.singletonList(CoreComercialConstants.REPLY_PERSONA_SYNC);
		Collection<String> topicReplySync = new ArrayList<>();
		if (kafkaProperties.getTopicSyncSufijo() != null) {
			colKafkaTopicReplySync.stream().distinct().forEach(v -> topicReplySync.add(v.concat(kafkaProperties.getTopicSyncSufijo())));
			kafkaProperties.setTopicReply(topicReplySync.toArray(new String[0]));
		} else {
			kafkaProperties.setTopicReply(colKafkaTopicReplySync.toArray(new String[0]));
		}
		log.info("Topic kafka reply sync configurado: {}", kafkaProperties::getTopicReply);
		this.kafkaProperties = kafkaProperties;
	}

	public <T> ProducerFactory<String, KafkaRequest<T>> producerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configMap.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxByteRequestKafka);
		return new DefaultKafkaProducerFactory<>(configMap);
	}

	public <T> DefaultKafkaConsumerFactory<String, KafkaResponse<T>> consumerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getTopicGroup());
		configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		configMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxByteRequestKafka);
		configMap.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, TracingConsumerInterceptor.class.getName());
		configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaResponseDeserializer.class);
		configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaResponseDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configMap);
	}

	@Bean
	public <T> KafkaMessageListenerContainer<String, KafkaResponse<T>> replyListenerSyncContainer() {
		ContainerProperties containerProperties = new ContainerProperties(kafkaProperties.getTopicReply());
		return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties);
	}

	@Bean
	public <T> ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyKafkaTemplate() {
		ReplyingKafkaTemplate<String, KafkaRequest<T>, KafkaResponse<T>> replyTemplate = new ReplyingKafkaTemplate<>(producerFactory(),
				replyListenerSyncContainer());
		replyTemplate.setDefaultReplyTimeout(Duration.parse("PT" + replyTimeout));
		replyTemplate.setSharedReplyTopic(true);
		return replyTemplate;
	}
}
