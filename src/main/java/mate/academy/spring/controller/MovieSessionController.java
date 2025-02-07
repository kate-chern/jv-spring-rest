package mate.academy.spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.dto.MovieSessionRequestDto;
import mate.academy.spring.dto.MovieSessionResponseDto;
import mate.academy.spring.mapper.MovieSessionMapper;
import mate.academy.spring.model.MovieSession;
import mate.academy.spring.service.MovieSessionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final MovieSessionMapper movieSessionMapper;

    public MovieSessionController(MovieSessionService movieSessionService,
                                  MovieSessionMapper movieSessionMapper) {
        this.movieSessionService = movieSessionService;
        this.movieSessionMapper = movieSessionMapper;
    }

    @PostMapping
    public MovieSessionResponseDto create(@RequestBody MovieSessionRequestDto movieSession) {
        return movieSessionMapper.toDto(movieSessionService
                .add(movieSessionMapper.toModel(movieSession)));
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> get(@RequestParam Long movieId,
                                             @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy")
                                             LocalDate dateTime) {
        return movieSessionService.findAvailableSessions(movieId, dateTime).stream()
                .map(movieSessionMapper::toDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieSessionService.delete(movieSessionService.get(id));
    }

    @PutMapping({"/{id}"})
    public MovieSessionResponseDto update(@PathVariable Long id,
                                          @RequestBody MovieSessionRequestDto
                                                  movieSessionRequestDto) {
        MovieSession movieSession = movieSessionMapper.toModel(movieSessionRequestDto);
        movieSession.setId(id);
        MovieSession updatedMovieSeesion = movieSessionService.update(movieSession);
        return movieSessionMapper.toDto(updatedMovieSeesion);
    }
}
