package site.easy.to.build.crm.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Data
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer statusCode;
    private String  status;
    @Nullable
    private String message;
    @Nullable
    private T data;
    @Nullable
    private Object errors;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime datetime = LocalDateTime.now();

    public ResponseEntity<ApiResponse<?>> toResponseEntity() {
        return ResponseEntity.status(statusCode).body(this);
    }

    public static <T> ApiResponse<T> create(
        HttpStatus httpStatus, @Nullable String message, @Nullable T data, @Nullable Object errors
    ) {
        return ApiResponse.<T>builder()
            .statusCode(httpStatus.value())
            .status(httpStatus.getReasonPhrase())
            .message(message)
            .data(data)
            .errors(errors)
            .build();
    }

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data) {
        Assert.isTrue(httpStatus.is2xxSuccessful(), "HttpStatus must be 2xx");

        return create(httpStatus, message, data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return success(HttpStatus.OK, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(HttpStatus.OK, "Operation Successful", data);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message, Object errors) {
        Assert.isTrue(httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), "HttpStatus must be 4xx or 5xx");

        return create(httpStatus, message, null, errors);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        return error(httpStatus, message, null);
    }

    public static <T> ApiResponse<T> error(String message, Object errors) {
        return error(HttpStatus.BAD_REQUEST, message, errors);
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(HttpStatus.BAD_REQUEST, message, null);
    }
}
