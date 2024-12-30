package com.example.demo.Controller;

import com.example.demo.DTO.Episode_DTO;
import com.example.demo.DTO.MovieDTO;
import com.example.demo.DTO.Movie_ActorDTO;
import com.example.demo.Model.Episode;
import com.example.demo.Model.Movie;
import com.example.demo.Service.EpisodeService;
import com.example.demo.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private EpisodeService episodeService;

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/type")
    public ResponseEntity<List<MovieDTO>> getMoviesByType(@RequestParam Boolean type) {
        List<MovieDTO> movieDTOs = movieService.getMoviesByType(type);
        return ResponseEntity.ok(movieDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(
            @RequestParam(required = false) String movie_name,
            @RequestParam(required = false) String movie_genre,
            @RequestParam(required = false) String actor_name) {

        List<MovieDTO> movies = movieService.searchMovies(movie_name, movie_genre, actor_name);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/details/{movie_code}")
    public ResponseEntity<MovieDTO> getMovieDetails(@PathVariable String movie_code) {
        MovieDTO movie = movieService.getMovieByCode(movie_code);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<MovieDTO>> getHotMovies() {
        List<MovieDTO> hotMovies = movieService.getHotMovies();
        return ResponseEntity.ok(hotMovies);
    }

    @GetMapping("/episodes")
    public ResponseEntity<List<Episode_DTO>> getAllEpisodesByMovieCode(@RequestParam String movie_code) {
        List<Episode_DTO> episodes = movieService.getAllEpisodesByMovieCode(movie_code);

        if (episodes == null || episodes.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/category/{category_name}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCategoryName(@PathVariable String category_name) {
        List<MovieDTO> movies = movieService.getMoviesByCategoryName(category_name);

        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }
}
