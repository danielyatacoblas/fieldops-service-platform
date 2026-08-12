package com.portfolio.fieldops.web;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice class ApiExceptionHandler {
 @ExceptionHandler(IllegalArgumentException.class) ProblemDetail bad(IllegalArgumentException e){return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,e.getMessage());}
}
