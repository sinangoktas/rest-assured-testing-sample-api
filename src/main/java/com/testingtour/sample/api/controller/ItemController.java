package com.testingtour.sample.api.controller;

import com.testingtour.sample.api.model.Item;
import com.testingtour.sample.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable(value = "id") Long ItemId) {
        Item item = itemService.getItemById(ItemId);
        if (item != null) {
            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity createItem(@Valid @RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(itemService.createItem(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity <Item> updateItem(@PathVariable(value = "id") Long ItemId,
                                               @Valid @RequestBody Item itemDetails) {

        Item updatedItem = itemService.updateItem(ItemId, itemDetails);

        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();


    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable(value = "id") Long itemId) {

        Item item = itemService.deleteItem(itemId);

        if (item != null) {
            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.notFound().build();

    }
}