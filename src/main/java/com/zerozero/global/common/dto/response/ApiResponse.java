package com.zerozero.global.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.StringJoiner;
import java.util.function.Supplier;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private boolean success;
  private String code;
  private Supplier<T> result;
  private String message;

  private ApiResponse(boolean success, String code, String message, Supplier<T> result) {
    this.success = success;
    this.code = code;
    this.result = result;
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getCode() {
    return code;
  }

  public T getResult() {
    return result.get();
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ApiResponse.class.getSimpleName() + "[", "]")
        .add("success=" + success)
        .add("code='" + code + "'")
        .add("result=" + result)
        .add("message=" + message)
        .toString();
  }


  public static ApiResponse<String> ok() {
    return ok("ok");
  }

  public static <T> ApiResponse<T> ok(T result) {
    return new ApiResponse<>(true, "200", "", () -> result);
  }

  public static <T> ApiResponse<T> fail(String code, T result) {
    return new ApiResponse<>(false, code, "", () -> result);
  }

  public static <T> ApiResponse<T> fail(int code, T result) {
    return new ApiResponse<>(false, String.valueOf(code), "", () -> result);
  }

  public static <T> ApiResponse<T> fail(String code, String message, T result) {
    return new ApiResponse<>(false, code, message, () -> result);
  }

  public static <T> ApiResponse<T> fail(int code, String message, T result) {
    return new ApiResponse<>(false, String.valueOf(code), message, () -> result);
  }

}
