package com.example.aws_sqs_exe;

import com.example.aws_sqs_exe.config.AwsSQSConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan( excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ContextStackAutoConfiguration.class)})
@RestController
@Slf4j
public class AwsSqsExeApplication {

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	Logger logger= LoggerFactory.getLogger(AwsSqsExeApplication.class);

	@Value("${cloud.aws.end-point.uri}")
	private String endpoint;

	@GetMapping("/send/{message}")
	public void sendMessageToQueue(@PathVariable String message){
		queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());
	}

	@SqsListener("my-sqs-queue")
	public void loadMessageFromSQS(String message){
	logger.info("message from SQS Queue {}", message);
	}


	public static void main(String[] args) {
		SpringApplication.run(AwsSqsExeApplication.class, args);
	}


}
