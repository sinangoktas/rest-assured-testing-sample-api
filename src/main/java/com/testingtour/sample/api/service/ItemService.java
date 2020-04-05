package com.testingtour.sample.api.service;

import com.testingtour.sample.api.model.Item;
import com.testingtour.sample.api.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElse(null);
    }

    public Item createItem(Item item) {
       return itemRepository.save(item);
    }

    public Item updateItem(Long ItemId, Item itemDetails) {

        Optional<Item> item = itemRepository.findById(ItemId);

        if (item.isPresent()) {
            Item data = item.get();
            data.setType(itemDetails.getType());
            data.setName(itemDetails.getName());
            data.setPrice(itemDetails.getPrice());
            data.setCount(itemDetails.getCount());
            return itemRepository.save(data);
        }
        return null;

    }

    public Item deleteItem(Long itemId) {

        Optional<Item> Item = itemRepository.findById(itemId);

        if (Item.isPresent()) {
            itemRepository.delete(Item.get());
            return Item.get();
        }
        return null;

    }
}
