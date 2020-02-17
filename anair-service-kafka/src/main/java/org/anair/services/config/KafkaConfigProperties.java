package org.anair.services.config;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
public class KafkaConfigProperties {

	@NotBlank
	private String bootstrapServers;
	@NotEmpty
	private Map<String, String> ssl;
	@NotEmpty
	private Map<String, String> producer;
	@Min(0) @Max(60000)
	private int transactiontimeoutMs;

}
