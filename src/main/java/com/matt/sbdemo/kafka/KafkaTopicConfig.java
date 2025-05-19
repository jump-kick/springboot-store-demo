package com.matt.sbdemo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matt.sbdemo.model.KafkaMetrics;
import static com.matt.sbdemo.constants.KakfaConstants.SB_SHOPPING_METRICS;

@Configuration
public class KafkaTopicConfig {
    
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Autowired
    private ProducerFactory<String, String> producerFactory;
    
    @Bean
    public NewTopic topic1() {
        return new NewTopic(SB_SHOPPING_METRICS, 4, (short) 3);
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
    	return new KafkaTemplate<String, String>(producerFactory);
    }
    
    @Bean
    public ObjectMapper objectMapper() {
    	ObjectMapper om = new ObjectMapper();
    	om.registerModule(new JavaTimeModule());
    	return om;
    }
}