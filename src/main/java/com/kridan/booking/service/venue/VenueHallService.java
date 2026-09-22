package com.kridan.booking.service.venue;


import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.entity.VenueSeat;
import com.kridan.booking.repository.VenueHallRepository;
import com.kridan.booking.repository.VenueSeatRepository;
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
                venueHallCreationDto.description())
        );

        Map<String, Integer> nextNumberBySector = new HashMap<>();
        List<VenueSeat> seats = new ArrayList<>();

        for (VenueSeatUnitCreationDto vsuсd: venueHallCreationDto.venueSeatList()){
            String sector = vsuсd.sector().trim();
            String type = vsuсd.type().trim();

            //Set counter
            int number = nextNumberBySector.getOrDefault(sector, 1);

            // Create one type seats
            for (int i = 0; i < vsuсd.amount(); i++) {
                seats.add(new VenueSeat(sector, type, number++, venueHall));
            }

            // Save counter
            nextNumberBySector.put(sector, number);
        }

        venueSeatRepository.saveAll(seats);
        return venueHall;
    }

    public List<VenueHall> getHalls(){
        List<VenueHall> venueHalls = venueHallRepository.findAll();
        return venueHalls;
    }
}
