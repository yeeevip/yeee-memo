package vip.yeee.memo.demo.reactive.prog.s10ReactiveTesting;

import java.math.BigDecimal;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import vip.yeee.memo.demo.reactive.prog.ReactiveSpringTutorialApplication;
import vip.yeee.memo.demo.reactive.prog.domain.Book;
import vip.yeee.memo.demo.reactive.prog.s05ReactiveDataAccesWithR2dbc.C01BookRepository;
import vip.yeee.memo.demo.reactive.prog.s05ReactiveDataAccesWithR2dbc.C03R2dbcConfiguration;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
		classes = {
				ReactiveSpringTutorialApplication.class,
				C03R2dbcConfiguration.class,
				C01BookRepository.class
		}
)
@DataR2dbcTest
public class C02R2dbcTesting {
	@Autowired
	private C01BookRepository bookRepository;

	@Test
	public void testInsert() {
		var book = Book.builder()
				.category("title category")
				.isbn("1234567")
				.price(BigDecimal.valueOf(19.99))
				.title("test title")
				.build();
		var mono = bookRepository.insert(book)
			.then(bookRepository.findById("1234567"));
		StepVerifier.create(mono)
			.expectNext(book)
			.expectComplete()
			.verify();
	}
	

}
