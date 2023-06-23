package vip.yeee.memo.demo.scloud.rpc.feign02.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import vip.yeee.memo.demo.scloud.rpc.feign02.model.Book;
import vip.yeee.memo.demo.scloud.rpc.feign02.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feign-service-server", path = "/fcs", fallbackFactory = BookService.FallbackImpl.class)
public interface BookService {
    Book FALLBACK_BOOK = new Book(); // ==

    @PostMapping("/book")
    Book createBook(@RequestBody Book book);

    @GetMapping("/book/{isbn}")
    Book findBook(@PathVariable(value = "isbn") String isbn);

    @Slf4j
    @Component
    @RequiredArgsConstructor
    class FallbackImpl implements FallbackFactory<BookService> {
        private final ObjectMapper objectMapper;

        @Override
        public BookService create(Throwable cause) {
            return new BookService() {
                @Override
                public Book createBook(Book book) {
                    return Response.fallback(objectMapper, cause, Book.class, FALLBACK_BOOK,
                            log, "Failed to create book");
                }

                @Override
                public Book findBook(String isbn) {
                    return Response.fallback(objectMapper, cause, Book.class, FALLBACK_BOOK,
                            log, "Failed to find book");
                }
            };
        }
    }
}
