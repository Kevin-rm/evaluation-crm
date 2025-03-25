package site.easy.to.build.crm.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import site.easy.to.build.crm.response.ApiResponse;

@RestControllerAdvice(basePackages = "site.easy.to.build.crm.controller.api")
public class ApiExceptionHandler {

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(Exception e) {
        return ApiResponse.error(e.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).toResponseEntity();
    }
}
