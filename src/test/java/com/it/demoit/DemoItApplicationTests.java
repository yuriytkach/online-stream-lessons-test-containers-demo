package com.it.demoit;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoItApplicationTests {

  @Container
  static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:latest")
    .withDatabaseName("itemsdb")
    .withUsername("hello")
    .withPassword("world");
  @LocalServerPort
  int localPort;
  @Autowired
  ItemRepository itemRepository;

  @DynamicPropertySource
  static void loadProps(final DynamicPropertyRegistry registry) {
    System.out.println(CONTAINER.getJdbcUrl());

    registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", () -> "hello");
    registry.add("spring.datasource.password", () -> "world");
  }

  @BeforeEach
  void setupRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = localPort;
  }

  @Test
  void shouldSaveItem() {
    final var responseBody = RestAssured.given()
      .contentType(ContentType.JSON)
      .body(new Item("abc"))
      .post("/items")
      .then()
      .statusCode(HttpStatus.CREATED.value())
      .body("name", Matchers.equalTo("abc"))
      .body("id", Matchers.notNullValue())
      .extract()
      .body()
      .asString();

    System.out.println(responseBody);

    final Optional<ItemEntity> byId = itemRepository.findById(1L);

    Assertions.assertTrue(byId.isPresent());
  }

}
