package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("select c from Comment as c where c.item.id in ?1")
    Collection<Comment> findAllByItemId(Collection<Long> itemId);
}
