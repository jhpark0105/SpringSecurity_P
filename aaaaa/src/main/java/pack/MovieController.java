package pack;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @PostMapping 
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @GetMapping("/recommend/{genre}")
    public List<Movie> recommendMovies(@PathVariable(name="genre") String genre) {
        return movieRepository.findByGenre(genre);
    }
}
