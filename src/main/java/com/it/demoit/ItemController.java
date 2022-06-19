package com.it.demoit;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemRepository repository;

  @PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ItemEntity create(
    @RequestBody final Item item
  ) {
    log.info("Creating {}", item);

    final var entity = new ItemEntity();
    entity.setName(item.getName());

    return repository.save(entity);
  }

}
