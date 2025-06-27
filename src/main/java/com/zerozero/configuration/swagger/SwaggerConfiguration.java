package com.zerozero.configuration.swagger;

import com.zerozero.core.support.error.ErrorMessage;
import com.zerozero.core.support.error.ErrorType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
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
import lombok.Builder;
import lombok.Getter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.*;
import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(title = "Zerozero API",
                description = "Zerozero : API 명세서",
                version = "v1.0.0"), servers = {@Server(url = "${springdoc.server-url}", description = "Default Server URL")})
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
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

    private void generateErrorCodeResponseExample(Operation operation, Class<? extends ErrorType>[] types) {
        ApiResponses responses = operation.getResponses();
        List<ExampleHolder> exampleHolders = new ArrayList<>();

        for (Class<? extends ErrorType> type : types) {
            ErrorType[] errorTypes = type.getEnumConstants();
            Arrays.stream(errorTypes).map(
                    errorType -> ExampleHolder.builder()
                            .holder(getSwaggerExample(errorType))
                            .code(errorType.getStatus().value())
                            .name(errorType.getCode())
                            .build()
            ).forEach(exampleHolders::add);
        }

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = new HashMap<>(
                exampleHolders.stream()
                        .collect(Collectors.groupingBy(ExampleHolder::getCode)));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }


    private Example getSwaggerExample(ErrorType errorType) {
        ErrorMessage errorMessage = new ErrorMessage(errorType);
        Example example = new Example();
        example.setValue(errorMessage);
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
