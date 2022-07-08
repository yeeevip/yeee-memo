package vip.yeee.memo.integrate.springcloud.register.feign.server.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Book {
    @NotBlank
    @Pattern(regexp = "\\d+-\\d+-\\d+-\\d+", message = "Must be like 9-999-999-9")
    private String isbn;

    @NotBlank
    private String title;

    @NotNull
    private LocalDate dateOfPublish;

    @NotNull
    private BigDecimal price;
}
