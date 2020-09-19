package com.udacity.project4.api.rest;

import com.udacity.project4.domain.model.Item;
import com.udacity.project4.domain.repository.ItemRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Item> getItemById(@PathVariable Long id) throws ResourceNotFoundException {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Item not found for the provided id :: %s" , id)));
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) throws ResourceNotFoundException{
        List<Item> items = itemRepository.findByName(name);
        if (items == null || items.isEmpty())
            throw new ResourceNotFoundException(String.format("Item not found for the provided name :: %s" , name));

        return ResponseEntity.ok(items);

    }

}