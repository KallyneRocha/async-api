package br.com.compass.pb.asyncapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableAsync
@SpringBootApplication
public class AsyncapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncapiApplication.class, args);
	}

}
