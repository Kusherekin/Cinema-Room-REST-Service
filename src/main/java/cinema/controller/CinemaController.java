package cinema.controller;


import cinema.DTOs.CinemaDTO;
import cinema.DTOs.RowColumnDTO;
import cinema.DTOs.TokenDTO;
import cinema.service.CinemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public ResponseEntity<CinemaDTO> getSeats() {
        return cinemaService.getCinemaDTO();
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseSeat(@RequestBody RowColumnDTO rowColumnDTO) {
        return cinemaService.purchaseSeat(rowColumnDTO);
    }

    @PostMapping("/return")
    public ResponseEntity returnTicket(@RequestBody TokenDTO tokenDTO) {
        return cinemaService.returnTicket(tokenDTO);
    }

    @PostMapping("/stats")
    public ResponseEntity getStatistics(@RequestParam(required = false) String password) {
        return cinemaService.getStatistics(password);
    }


}
