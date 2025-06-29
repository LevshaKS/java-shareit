package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    void deleteByUserIdAndId(long userId, long id);

    Collection<ItemRequest> findByUserId(long id);

    Collection<ItemRequest> findByDescriptionContainingIgnoreCase(String description);


}
