package com.zerozero.configuration.swagger;

import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.presentation.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@OpenAPIDefinition(
    info = @Info(title = "Zerozero API",
        description = "Zerozero : API 명세서",
        version = "v1.0.0"))
@Configuration
public class SwaggerConfiguration {

  @Bean
  public OpenAPI openAPI(){
    SecurityScheme securityScheme = new SecurityScheme()
        .type(Type.HTTP).scheme("bearer").bearerFormat("JWT")
        .in(In.HEADER).name("Authorization");
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
        .security(Arrays.asList(securityRequirement));
  }

  @Bean
  public OperationCustomizer operationCustomizer() {
    return (Operation operation, HandlerMethod handlerMethod) -> {
      ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);
      if (apiErrorCode != null) {
        generateErrorCodeResponseExample(operation, apiErrorCode.value());
      }
      return operation;
    };
  }

  private void generateErrorCodeResponseExample(Operation operation, Class<? extends BaseErrorCode>[] types) {
    ApiResponses responses = operation.getResponses();
    List<ExampleHolder> exampleHolders = new ArrayList<>();

    for (Class<? extends BaseErrorCode> type : types) {
      BaseErrorCode[] errorCodes = type.getEnumConstants();
      Arrays.stream(errorCodes).map(
          baseErrorCode -> ExampleHolder.builder()
              .holder(getSwaggerExample(baseErrorCode))
              .code(baseErrorCode.getHttpStatus().value())
              .name(baseErrorCode.name())
              .build()
      ).forEach(exampleHolders::add);
    }

    Map<Integer, List<ExampleHolder>> statusWithExampleHolders = new HashMap<>(
        exampleHolders.stream()
            .collect(Collectors.groupingBy(ExampleHolder::getCode)));

    addExamplesToResponses(responses, statusWithExampleHolders);
  }


  private Example getSwaggerExample(BaseErrorCode baseErrorCode) {
    ErrorResponse errorResponse = ErrorResponse.createErrorCode()
        .statusCode(baseErrorCode.getHttpStatus().value())
        .exception(baseErrorCode.toException())
        .build();
    Example example = new Example();
    example.setValue(errorResponse);
    return example;
  }

  private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
    statusWithExampleHolders.forEach(
        (status, value) -> {
          Content content = new Content();
          MediaType mediaType = new MediaType();
          ApiResponse apiResponse = new ApiResponse();
          value.forEach(exampleHolder -> mediaType.addExamples(exampleHolder.getName(),
              exampleHolder.getHolder()));
          content.addMediaType("application/json", mediaType);
          apiResponse.setContent(content);
          responses.addApiResponse(status.toString(), apiResponse);
        }
    );
  }

  @Getter
  @Builder
  public static class ExampleHolder {

    private Example holder;
    private int code;
    private String name;
  }
}