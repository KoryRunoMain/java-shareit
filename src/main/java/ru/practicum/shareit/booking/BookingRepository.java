package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;

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

    /*
    For user / case: ALL
    findBy BookerId OrderBy Start Desc
     */
    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId,
                                                 Pageable pageRequest);

    /*
    For user / case: CURRENT
    findBy BookerId And StartBefore And EndAfter OrderBy Start Desc
     */
    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId,
                                                                              LocalDateTime startTime,
                                                                              LocalDateTime endTime,
                                                                              Pageable pageRequest);

    /*
    For user / case: PAST
    findBy BookerId And EndIsBefore OrderBy Start Desc
     */
    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId,
                                                               LocalDateTime startTime,
                                                               Pageable pageRequest);

    /*
    For user / case: FUTURE
    findBy BookerId And StartIsAfter OrderBy Start Desc
     */
    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId,
                                                                LocalDateTime endTime,
                                                                Pageable pageRequest);

    /*
    For user / case: WAITING
    findBy BookerId And StartIsAfter And Status OrderBy Start Desc
     */
    Page<Booking> findByBookerIdAndStartIsAfterAndStatusOrderByStartDesc(Long bookerId,
                                                                         LocalDateTime time,
                                                                         BookingStatus status,
                                                                         Pageable pageRequest);

    /*
    For user / case: REJECTED
    findBy BookerId And Status OrderBy Start Desc
     */
    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId,
                                                          BookingStatus status,
                                                          Pageable pageRequest);

    /*
    For owner / case: ALL
    findBy ItemOwnerId OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId,
                                                    Pageable pageRequest);

    /*
    For owner / case: CURRENT
    findBy ItemOwnerId And StartIsBefore And EndIsAfter OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId,
                                                                                 LocalDateTime start,
                                                                                 LocalDateTime end,
                                                                                 Pageable pageRequest);

    /*
    For owner / case: PAST
    findBy ItemOwnerId And EndIsBefore OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId,
                                                                  LocalDateTime time,
                                                                  Pageable pageRequest);

    /*
    For owner / case: FUTURE
    findBy ItemOwnerId And StartIsAfter OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId,
                                                                   LocalDateTime time,
                                                                   Pageable pageRequest);

    /*
    For owner / case: WAITING
    findBy ItemOwnerId And StartIsAfter And Status OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdAndStartIsAfterAndStatusOrderByStartDesc(Long ownerId,
                                                                            LocalDateTime time,
                                                                            BookingStatus status,
                                                                            Pageable pageRequest);

    /*
    For owner / case: REJECTED
    findBy ItemOwnerId And Status OrderBy Start Desc
     */
    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId,
                                                             BookingStatus status,
                                                             Pageable pageRequest);

}
