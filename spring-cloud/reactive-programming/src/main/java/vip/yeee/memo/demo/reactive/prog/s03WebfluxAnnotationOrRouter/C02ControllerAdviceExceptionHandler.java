package vip.yeee.memo.demo.reactive.prog.s03WebfluxAnnotationOrRouter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
// import org.springframework.web.servlet.NoHandlerFoundException;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 *
 * 只有在spring-boot-starter-web被引入的情况下，才有用
 */
// @ControllerAdvice
public class C02ControllerAdviceExceptionHandler {
// public class C02ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {
   @RequiredArgsConstructor
   @Data
   public static class Error {
       private final List<InvalidField> invalidFields;
       private final List<String> errors;
   }

   @RequiredArgsConstructor
   @Data
   public static class InvalidField {
       private final String name;
       private final String message;
   }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers,
//                                                                  HttpStatus status,
//                                                                  WebRequest request) {
//        List<InvalidField> invalidFields = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> new InvalidField(error.getField(), error.getDefaultMessage()))
//                .collect(Collectors.toList());
//        List<String> errors = ex.getBindingResult().getGlobalErrors().stream()
//                .map(ObjectError::getDefaultMessage)
//                .collect(Collectors.toList());
//        Error error = new Error(invalidFields, errors);
//        return handleExceptionInternal(ex, error, headers, status, request);
//    }

//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
//                                                                   HttpHeaders headers,
//                                                                   HttpStatus status,
//                                                                   WebRequest request) {
//        Error error = new Error(Collections.emptyList(), Arrays.asList(
//                String.format("No handler for %s %s", ex.getHttpMethod(), ex.getRequestURL())
//        ));
//        return handleExceptionInternal(ex, error, headers, status, request);
//    }
}
