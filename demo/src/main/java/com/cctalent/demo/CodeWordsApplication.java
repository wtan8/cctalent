package com.cctalent.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class CodeWordsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodeWordsApplication.class, args);
	}
}
