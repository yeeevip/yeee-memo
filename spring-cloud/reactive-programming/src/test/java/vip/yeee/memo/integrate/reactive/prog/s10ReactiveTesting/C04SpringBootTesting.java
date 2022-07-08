package vip.yeee.memo.integrate.reactive.prog.s10ReactiveTesting;

import java.math.BigDecimal;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import vip.yeee.memo.integrate.reactive.prog.ReactiveSpringTutorialApplication;
import vip.yeee.memo.integrate.reactive.prog.domain.Book;

@SpringBootTest(classes = ReactiveSpringTutorialApplication.class)
@AutoConfigureWebTestClient
public class C04SpringBootTesting {
	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	public void testWebFlux() {
		var book = Book.builder()
				.category("title category")
				.isbn("1234567")
				.price(BigDecimal.valueOf(19.99))
				.title("test title")
				.build();
		webTestClient.post()
			.uri("/routed-r2dbc/book")
			.bodyValue(book)
			.header("Authorization", "Basic " +
					Base64Utils.encodeToString("admin:secret".getBytes())
			).exchange()
			.expectStatus()
			.isCreated();
	}

}
