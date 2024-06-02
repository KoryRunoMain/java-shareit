package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    findById Not And Email
     */
    Optional<User> findByIdNotAndEmail(Long userId, String email);

}
