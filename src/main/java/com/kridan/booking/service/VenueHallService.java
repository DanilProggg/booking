package com.kridan.booking.service;


import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.entity.VenueSeat;
import com.kridan.booking.repository.VenueHallRepository;
import com.kridan.booking.repository.VenueSeatRepository;
import com.kridan.booking.service.dto.VenueHallCreationDto;
import com.kridan.booking.service.dto.VenueSeatUnitCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VenueHallService {
    private final VenueHallRepository venueHallRepository;
    private final VenueSeatRepository venueSeatRepository;


    @Transactional
    public VenueHall addHall(VenueHallCreationDto venueHallCreationDto){
        VenueHall venueHall = venueHallRepository.save(new VenueHall(
                venueHallCreationDto.name().trim(),
                venueHallCreationDto.description()));



        Map<String, Integer> nextNumberBySector = new HashMap<>();
        List<VenueSeat> seats = new ArrayList<>();

        for (VenueSeatUnitCreationDto vhsud: venueHallCreationDto.venueSeatUnitCreationDtoList()){
            String sector = vhsud.sector().trim();
            String type = vhsud.type().trim();
            int number = nextNumberBySector.getOrDefault(sector, 1);

            for (int i = 0; i < vhsud.amount(); i++) {
                seats.add(new VenueSeat(sector, type, number++, venueHall));
            }

            nextNumberBySector.put(sector, number);
        }

        venueSeatRepository.saveAll(seats);
        return venueHall;
    }
}
