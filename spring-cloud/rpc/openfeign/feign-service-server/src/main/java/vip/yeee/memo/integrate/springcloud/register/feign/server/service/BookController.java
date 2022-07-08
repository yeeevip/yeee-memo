package vip.yeee.memo.integrate.springcloud.register.feign.server.service;

import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class BookController {

    private final Map<String, Book> inMemoryDataStore = new ConcurrentHashMap<>();

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        inMemoryDataStore.put(book.getIsbn(), book);
        return ResponseEntity.created(URI.create("/book/" + book.getIsbn())).body(book);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<Book> findBook(@Valid @PathVariable String isbn) {
        var book = inMemoryDataStore.get(isbn);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book);
    }

}
