package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@CrossOrigin({
        "http://localhost:5173/",
        "http://127.0.0.1:5173/"})
public class BookingController {
}
