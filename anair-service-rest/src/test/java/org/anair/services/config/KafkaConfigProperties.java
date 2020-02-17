package org.anair.services.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Configuration
@ConfigurationProperties("spring.kafka")
@Validated
public class KafkaConfigProperties {

	@NotBlank
	private String bootstrapServers;
	@NotEmpty
	private Map<String, String> ssl;
	@NotEmpty
	private Map<String, String> producer;
	@Min(0) @Max(60000)
	private int transactiontimeoutMs;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public Map<String, String> getSsl() {
		return ssl;
	}

	public void setSsl(Map<String, String> ssl) {
		this.ssl = ssl;
	}

	public Map<String, String> getProducer() {
		return producer;
	}

	public void setProducer(Map<String, String> producer) {
		this.producer = producer;
	}

	public int getTransactiontimeoutMs() {
		return transactiontimeoutMs;
	}

	public void setTransactiontimeoutMs(int transactiontimeoutMs) {
		this.transactiontimeoutMs = transactiontimeoutMs;
	}
}
