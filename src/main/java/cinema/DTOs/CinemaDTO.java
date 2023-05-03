package cinema.DTOs;

import cinema.models.Seat;

import java.util.List;

public record CinemaDTO(int totalRows, int totalColumns, List<SeatDTO> availableSeats) {
}
