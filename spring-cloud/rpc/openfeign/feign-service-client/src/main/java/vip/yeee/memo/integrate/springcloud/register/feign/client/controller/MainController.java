package vip.yeee.memo.integrate.springcloud.register.feign.client.controller;

import vip.yeee.memo.integrate.springcloud.register.feign.client.model.Book;
import vip.yeee.memo.integrate.springcloud.register.feign.client.feign.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController {
    private final BookService service;

    @PostMapping("/book")
    public Book createBook(@RequestBody Book book) {
        var theBook = service.createBook(book);
        if (theBook.getError() != null) {
            log.error("Failed to create book, got error message: {}", theBook.getError());
        }
        return theBook;
    }

    @GetMapping("/book/{isbn}")
    public Book findBook(@PathVariable String isbn) {
        var theBook = service.findBook(isbn);
        if (theBook.getError() != null) {
            log.error("Failed to find book, got error message: {}", theBook.getError());
        }
        return theBook;
    }
}
