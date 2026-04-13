package com.example.hoteltestapp.service;

import com.example.hoteltestapp.entity.Hotel;
import com.example.hoteltestapp.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> update(Long id, Hotel updated) {
        return hotelRepository.findById(id).map(hotel -> {
            hotel.setName(updated.getName());
            hotel.setAddress(updated.getAddress());
            hotel.setCity(updated.getCity());
            hotel.setStars(updated.getStars());
            return hotelRepository.save(hotel);
        });
    }

    public boolean delete(Long id) {
        return hotelRepository.findById(id).map(hotel -> {
            hotelRepository.delete(hotel);
            return true;
        }).orElse(false);
    }
}
