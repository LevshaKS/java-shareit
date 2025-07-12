package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //поиск по пользователу
    Collection<Booking> findByBookerIdOrderByStartDesc(long userId); //запрос найтиСписокБронированиеПоИдПользователяСортированПоСтартВремени

    Collection<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, Status status); //запрос найтиСписокБронированиеПоИдПользователяИСтатусуСортированПоСтартВремени

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start < ?2 and b.end > ?3 order by b.start Desc")
    Collection<Booking> findCurrentByBooker_idOrderByTimeDesc(long userId, Instant startTime, Instant endTime); //запрос найтиСписокБронированиеПоидПользователяТекущиеСортировкаПоВремени

    @Query("select b from Booking as b where b.booker.id = ?1 and b.end < ?2 order by b.start Desc")
    Collection<Booking> findPastByBooker_idOrderByTimeDesc(long userId, Instant endTime); // запрос найтиСписокБронированияПоИдПользователяЗавершенныеСортировкаПоСтартВремени

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start > ?2 order by b.start Desc")
    Collection<Booking> findFutureByBooker_idOrderByTimeDesc(long userId, Instant startTime); // запрос найтиСписокБронированияПоИдПользователяБудущиеСортировкваПоСтрартВремени

    //поиск по списку вещей пользователя
    @Query("select b from Booking as b where b.item.id in ?1 and b.status = ?2 order by b.start Desc")
    Collection<Booking> findDistinctByItem_idInAndStatusOrderByStart_dateDesc(Collection<Long> userItemId, Status status); //запрос найтУникальныеСписокБронированяПоПереданомуСпискуВещейВиСостатусом

    Collection<Booking> findDistinctByItemIdInOrderByStartDesc(Collection<Long> itemId); //запросПопереданоммуСпискуДляБронированияВещейупользвоателяОсортированомуПоДАтеначалоСОртировкаСПОследних

    @Query("select b from Booking as b where b.item.id in ?1 and b.start < ?2 and b.end > ?3 order by b.start Desc")
    Collection<Booking> findCurrentByItem_idTimeDesc(Collection<Long> userItemId, Instant startTime, Instant endTime); //запросПопереданномуСпискуВещейДлябронированияПользователяТекущееВремяБольшеСтарМеньшеИндСОртировакаПоСтарту

    @Query("select b from Booking as b join fetch b.item where b.item.id in ?1 and b.end <= ?2 order by b.start Desc")
   Collection<Booking> findPastByItem_idTimeDesc(Collection<Long> userItemId, Instant endTime); //запросПопереданномуСпискуВещейДлябронированияПользователяЗавершеныеЗаказы(Текущее времябольше endtime)СортировкаПоСтарту

    @Query("select b from Booking as b join fetch b.item where b.item.id in ?1 and b.start > ?2 order by b.start Desc")
    Collection<Booking> findFutureByItem_idTimeDesc(Collection<Long> userItemId, Instant startTime); //запросПопереданномуСпискуВещейДлябронированияПользователяБудущиеЗаказы(Текущее времябольше starttime)СортировкаПоСтарту

}
