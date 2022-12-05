package vip.yeee.memo.integrate.springcloud.register.feign.client.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.springcloud.register.feign.client.model.Book;
import vip.yeee.memo.integrate.springcloud.register.feign.client.feign.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.springcloud.register.feign.client.domain.mysql.entity.TestEntity;
import vip.yeee.memo.integrate.springcloud.register.feign.client.domain.mysql.service.TestEntityService;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController {
    private final BookService service;
    private final TestEntityService testEntityService;

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

    @GetMapping("test/seata/service1")
    public CommonResult<Void> testSeataService1() {
        TestEntity testEntity = new TestEntity();
        testEntity.setField1(DateUtil.format(new Date(), DatePattern.CHINESE_DATE_TIME_PATTERN));
        testEntityService.save(testEntity);
        log.info("testSeataService1 SUCCESS！！！");
        return CommonResult.success(null);
    }

}
