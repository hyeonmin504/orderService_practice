package jpabook.jpashop;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Transactional
public class JpashopApplication {
	public static void main(String[] args) {

		Member m = new Member();
		SpringApplication.run(JpashopApplication.class, args);
	}


}
