package vip.yeee.memo.integrate.springcloud.register.feign.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Book implements Response {
    private String isbn;
    private String title;
    private LocalDate dateOfPublish;
    private BigDecimal price;

    @JsonIgnore
    private Error error;
}
