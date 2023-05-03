package cinema.service;

import cinema.DTOs.*;
import cinema.models.Cinema;
import cinema.models.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {

    private final int TOTAL_ROWS = 9;
    private final int TOTAL_COLUMNS = 9;

    private final Cinema cinema;

    public CinemaService() {
        List<Seat> availableSeats = new ArrayList<>();
        for (int row = 1; row <= TOTAL_ROWS; row++) {
            for (int column = 1; column <= TOTAL_COLUMNS; column++) {
                availableSeats.add(new Seat(row, column, false, row <= 4 ? 10 : 8));
            }
        }
        cinema = new Cinema(TOTAL_ROWS, TOTAL_COLUMNS, availableSeats);
    }

    public ResponseEntity<CinemaDTO> getCinemaDTO() {
        return new ResponseEntity<>(new CinemaDTO(cinema.getTotalRows(), cinema.getTotalColumns(), cinema.getAvailableSeats().stream().map(this::parseSeatToSeatDTO).toList()), HttpStatus.OK);
    }

    public ResponseEntity purchaseSeat(RowColumnDTO rowColumnDTO) {
        Optional<Seat> seatByRowAndColumn = findSeatByRowAndColumn(rowColumnDTO.row(), rowColumnDTO.column());
        if (seatByRowAndColumn.isEmpty())
            return new ResponseEntity<>(new ErrorDTO("The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        Seat seat = seatByRowAndColumn.get();
        if (seat.isBooked())
            return new ResponseEntity<>(new ErrorDTO("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        seat.setBooked(true);
        seat.setToken();
        return new ResponseEntity<>(new TokenTicketDTO(seat.getToken().toString(), new RowColumnPriceDTO(rowColumnDTO.row(), rowColumnDTO.column(), seat.getPrice())), HttpStatus.OK);
    }

    private Optional<Seat> findSeatByRowAndColumn(int row, int column) {
        return cinema.getAvailableSeats().stream().filter(seat -> seat.getRow() == row && seat.getColumn() == column).findAny();
    }

    private Optional<Seat> findSeatByToken(String token) {
        return cinema.getAvailableSeats().stream().filter(seat -> seat.getToken().toString().equals(token)).findAny();
    }

    private SeatDTO parseSeatToSeatDTO(Seat seat) {
        return new SeatDTO(seat.getRow(), seat.getColumn(), seat.getPrice());
    }

    public ResponseEntity returnTicket(TokenDTO tokenDTO) {
        Optional<Seat> seatByToken = findSeatByToken(tokenDTO.token());
        if (seatByToken.isEmpty())
            return new ResponseEntity(new ErrorDTO("Wrong token!"), HttpStatus.BAD_REQUEST);
        Seat seat = seatByToken.get();
        seat.setBooked(false);
        seat.setToken();
        return new ResponseEntity(new ReturnedTicketDTO(new RowColumnPriceDTO(seat.getRow(), seat.getColumn(), seat.getPrice())), HttpStatus.OK);
    }

    public ResponseEntity getStatistics(String password) {
        if (password == null)
            return new ResponseEntity(new ErrorDTO("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        if (!password.equals("super_secret"))
            return new ResponseEntity(new ErrorDTO("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity(new CurrentIncomeNumberOfAvailableSeatsNumberOfPurchasedTicketsDTO(getCurrentIncome(), getNumberOfAvailableSeats(), getNumberOfPurchasedTickets()), HttpStatus.OK);

    }

    private int getCurrentIncome() {
        return cinema.getAvailableSeats().stream().filter(Seat::isBooked).mapToInt(Seat::getPrice).sum();
    }

    private long getNumberOfAvailableSeats() {
        return cinema.getAvailableSeats().stream().filter(seat -> !seat.isBooked()).count();
    }

    private long getNumberOfPurchasedTickets() {
        return cinema.getAvailableSeats().stream().filter(Seat::isBooked).count();
    }
}