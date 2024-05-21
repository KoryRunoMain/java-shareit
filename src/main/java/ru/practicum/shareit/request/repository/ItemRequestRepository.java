package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /*
    findAllBy RequestorId
     */
    List<ItemRequest> findAllByRequestorId(Long requestorId, Pageable page);

    /*
    findAllBy RequestorId Is Not
     */
    List<ItemRequest> findAllByRequestorIdIsNot(Long requestorId,Pageable page);

}
