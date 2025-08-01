package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {


    Collection<ItemRequest> findAllByRequesterId(long id);


    Collection<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(long id);


}
