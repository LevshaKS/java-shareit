package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    void deleteByUserIdAndId(long userId, long id);

    Collection<Item> findByUserId(long id);

    Collection<Item> findByNameContainingIgnoreCaseAndAvailable(String name, boolean available);

}
