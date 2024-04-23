package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin({
        "http://localhost:5173/",
        "http://127.0.0.1:5173/"})
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
}
