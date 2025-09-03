package com.example.communityboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommunityBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityBoardApplication.class, args);
	}

}
