package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /*
    findBy ItemId OrderBy Start Desc
     */
    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    /*
    findBy ItemId And BookerId And Status And EndIsBefore
     */
    List<Booking> findByItemIdAndBookerIdAndStatusAndEndIsBefore(Long itemId, Long bookerId,
                                                                 BookingStatus status,
                                                                 LocalDateTime endTime);

//    /*
//    findBy ItemId And ItemOwnerId And EndBefore OrderBy Start Desc
//     */
//    List<Booking> findByItemIdAndItemOwnerIdAndEndBeforeOrderByStartDesc(long itemId, long ownerId,
//                                                                         LocalDateTime endTime);

//    /*
//    findBy ItemId And ItemOwnerId And StartAfter OrderBy Start Desc
//     */
//    List<Booking> findByItemIdAndItemOwnerIdAndStartAfterOrderByStartDesc(long itemId, long ownerId,
//                                                                          LocalDateTime startTime);

    /*
    For user / case: ALL
    findBy BookerId OrderBy Start Desc
     */
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    /*
    For user / case: CURRENT
    findBy BookerId And StartBefore And EndAfter OrderBy Start Desc
     */
    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime startTime,
                                                                              LocalDateTime endTime);

    /*
    For user / case: PAST
    findBy BookerId And EndIsBefore OrderBy Start Desc
     */
    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    /*
    For user / case: FUTURE
    findBy BookerId And StartIsAfter OrderBy Start Desc
     */
    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime time);

    /*
    For user / case: WAITING
    findBy BookerId And StartIsAfter And Status OrderBy Start Desc
     */
    List<Booking> findByBookerIdAndStartIsAfterAndStatusOrderByStartDesc(Long bookerId, LocalDateTime time,
                                                                         BookingStatus status);

    /*
    For user / case: REJECTED
    findBy BookerId And Status OrderBy Start Desc
     */
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    /*
    For owner / case: ALL
    findBy ItemOwnerId OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    /*
    For owner / case: CURRENT
    findBy ItemOwnerId And StartIsBefore And EndIsAfter OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId, LocalDateTime start,
                                                                                   LocalDateTime end);

    /*
    For owner / case: PAST
    findBy ItemOwnerId And EndIsBefore OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime time);

    /*
    For owner / case: FUTURE
    findBy ItemOwnerId And StartIsAfter OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime time);

    /*
    For owner / case: WAITING
    findBy ItemOwnerId And StartIsAfter And Status OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdAndStartIsAfterAndStatusOrderByStartDesc(Long ownerId, LocalDateTime time,
                                                                            BookingStatus status);

    /*
    For owner / case: REJECTED
    findBy ItemOwnerId And Status OrderBy Start Desc
     */
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

}
