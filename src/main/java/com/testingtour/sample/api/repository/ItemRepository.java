package com.testingtour.sample.api.repository;

import java.util.List;
import java.util.Optional;

import com.testingtour.sample.api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByName(String name);

    Optional<Item> findById(long id);
}