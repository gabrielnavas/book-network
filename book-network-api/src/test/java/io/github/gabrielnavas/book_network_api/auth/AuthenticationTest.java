package io.github.gabrielnavas.book_network_api.auth;

import com.github.javafaker.Faker;
import io.github.gabrielnavas.book_network_api.configs.Configs;
import io.github.gabrielnavas.book_network_api.role.Role;
import io.github.gabrielnavas.book_network_api.role.RoleRepository;
import io.github.gabrielnavas.book_network_api.security.JwtService;
import io.github.gabrielnavas.book_network_api.testcontainers.AbstractIntegrationTest;
import io.github.gabrielnavas.book_network_api.user.Token;
import io.github.gabrielnavas.book_network_api.user.TokenRepository;
import io.github.gabrielnavas.book_network_api.user.UserRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationTest extends AbstractIntegrationTest {

    RequestSpecification specification;
    ObjectMapper objectMapper;
    Faker faker;
    Token token;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtService jwtService;

    RegistrationRequest registrationRequest;


    @Autowired
    TokenRepository tokenRepository;

    private void deleteBefore() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private void saveBefore() {
        roleRepository.save(Role.builder().name("USER").build());
    }

    private void initGlobalVariables() {

        faker = new Faker();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //disable fails of the attributes without on object deserialization
    }

    public void beforeRegistration() {
        deleteBefore();
        saveBefore();
        initGlobalVariables();


        registrationRequest = RegistrationRequest.builder()
                .email(faker.internet().emailAddress())
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .password("12345678")
                .build();
    }

    @Test
    @Order(1)
    void registration() throws IOException {
        beforeRegistration();

        specification = new RequestSpecBuilder()
                .addHeader(Configs.HEADER_PARAM_ORIGIN, Configs.ORIGIN_LOCALHOST)
                .setBasePath("/api/v1/auth/register")
                .setPort(Configs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(registrationRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCode());

        final String email = registrationRequest.getEmail();
        Optional<Token> optionalToken = tokenRepository.findByUserEmail(email);
        assertTrue(optionalToken.isPresent());
    }
}
