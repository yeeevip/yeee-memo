package vip.yeee.memo.integrate.ai.chatgpt.params.edit.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsEditModelValidator.class)
public @interface IsEditModel {
    String message() default "错误的模型参数";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
