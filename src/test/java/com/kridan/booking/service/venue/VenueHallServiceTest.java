package com.kridan.booking.service.venue;

import com.kridan.booking.entity.VenueHall;
import com.kridan.booking.entity.VenueSeat;
import com.kridan.booking.repository.VenueHallRepository;
import com.kridan.booking.repository.VenueSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueHallServiceTest {
    @Mock
    private VenueHallRepository venueHallRepository;

    @Mock
    private VenueSeatRepository venueSeatRepository;

    @InjectMocks
    private VenueHallService venueHallService;


    @Test
    void getHalls_shouldReturnHallsFromRepository() {

        List<VenueHall> expectedHalls = List.of(
                new VenueHall("Большой зал", "Основной зал"),
                new VenueHall("Малый зал", "Малый зал")
        );

        when(venueHallRepository.findAll()).thenReturn(expectedHalls);

        List<VenueHall> actualHalls = venueHallService.getHalls();

        assertEquals(expectedHalls, actualHalls);
        verify(venueHallRepository).findAll();
    }

    @Test
    void addHall_shouldReturnHallFromRepository(){
        VenueHallCreationDto venueHallCreationDto = new VenueHallCreationDto(
                "Большой зал", "Основной", List.of(
                        new VenueSeatUnitCreationDto(10,"A", "VIP"),
                        new VenueSeatUnitCreationDto(10,"A", "Standard")
                )
        );

        when(venueHallRepository.save(any(VenueHall.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0)
        );

        VenueHall result = venueHallService.addHall(venueHallCreationDto);

        assertThat(result.getName()).isEqualTo(venueHallCreationDto.name());
        assertThat(result.getDescription()).isEqualTo(venueHallCreationDto.description());

    }



    @Test
    void addHall_shouldAddSeatsInTheirRepository(@Captor ArgumentCaptor<List<VenueSeat>> venueSeatCaptor) {

        VenueHallCreationDto venueHallCreationDto = new VenueHallCreationDto(
                "Большой зал", "Основной", List.of(
                new VenueSeatUnitCreationDto(10,"A", "VIP"),
                new VenueSeatUnitCreationDto(10,"B", "Standard"))
        );


        venueHallService.addHall(venueHallCreationDto);

        //Capture arguments
        verify(venueSeatRepository).saveAll(venueSeatCaptor.capture());

        int seatAmount = venueHallCreationDto.venueSeatList().stream().mapToInt(VenueSeatUnitCreationDto::amount).sum();

        assertThat(seatAmount).isEqualTo();
    }
}
