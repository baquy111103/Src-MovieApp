package com.example.demo.Service;

import com.example.demo.DTO.Episode_DTO;
import com.example.demo.DTO.MovieDTO;
import com.example.demo.DTO.Movie_ActorDTO;
import com.example.demo.Model.Episode;
import com.example.demo.Model.Movie;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.EpisodeRepository;
import com.example.demo.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EpisodeRepository episodeRepository;


    @Transactional
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findByStatus(true);  // true tương ứng với status = 1

        return movies.stream().map(movie -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(movie.getId());
            dto.setMovie_code(movie.getMovie_code());
            dto.setMovie_name(movie.getMovie_name());
            dto.setDescription(movie.getDescription());
            dto.setRelease_date(movie.getRelease_date());
            dto.setDuration(movie.getDuration());
            dto.setImage_url(movie.getImage_url());
            dto.setVideo_url(movie.getVideo_url());
            dto.setStatus(movie.getStatus());
            dto.setLanguage(movie.getLanguage());
            dto.setMovie_genre(movie.getMovie_genre());
            dto.setCensorship(movie.getCensorship());
            dto.setType(movie.getType());
            return dto;
        }).collect(Collectors.toList());
    }


    @Transactional
    public List<MovieDTO> getMoviesByType(Boolean type) {
        List<Movie> movies = movieRepository.findMoviesByType(type);

        return movies.stream().map(movie -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(movie.getId());
            dto.setMovie_code(movie.getMovie_code());
            dto.setMovie_name(movie.getMovie_name());
            dto.setDescription(movie.getDescription());
            dto.setRelease_date(movie.getRelease_date());
            dto.setDuration(movie.getDuration());
            dto.setImage_url(movie.getImage_url());
            dto.setVideo_url(movie.getVideo_url());
            dto.setStatus(movie.getStatus());
            dto.setLanguage(movie.getLanguage());
            dto.setMovie_genre(movie.getMovie_genre());
            dto.setCensorship(movie.getCensorship());
            dto.setType(movie.getType());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDTO> searchMovies(String movieName, String movieGenre, String actorName) {
        List<Movie> movies = movieRepository.searchMovies(movieName, movieGenre, actorName);

        return movies.stream().map(movie -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(movie.getId());
            dto.setMovie_code(movie.getMovie_code());
            dto.setMovie_name(movie.getMovie_name());
            dto.setDescription(movie.getDescription());
            dto.setRelease_date(movie.getRelease_date());
            dto.setDuration(movie.getDuration());
            dto.setImage_url(movie.getImage_url());
            dto.setVideo_url(movie.getVideo_url());
            dto.setStatus(movie.getStatus());
            dto.setLanguage(movie.getLanguage());
            dto.setMovie_genre(movie.getMovie_genre());
            dto.setCensorship(movie.getCensorship());
            dto.setType(movie.getType());

            List<Movie_ActorDTO> actorDTOs = movie.getMovieActors().stream()
                    .map(actor -> new Movie_ActorDTO(actor.getActor().getActor_code(), actor.getActor().getName(),actor.getActor().getAvatar(),actor.getActor().getStatus()))
                    .collect(Collectors.toList());
            dto.setMovieActors(actorDTOs);

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public MovieDTO getMovieByCode(String movieCode) {
        Movie movie = movieRepository.findByMovieCode(movieCode)
                .orElseThrow(() -> new RuntimeException("Movie not found with movie_code: " + movieCode));
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setMovie_code(movie.getMovie_code());
        dto.setMovie_name(movie.getMovie_name());
        dto.setDescription(movie.getDescription());
        dto.setRelease_date(movie.getRelease_date());
        dto.setDuration(movie.getDuration());
        dto.setImage_url(movie.getImage_url());
        dto.setVideo_url(movie.getVideo_url());
        dto.setStatus(movie.getStatus());
        dto.setLanguage(movie.getLanguage());
        dto.setMovie_genre(movie.getMovie_genre());
        dto.setCensorship(movie.getCensorship());
        dto.setType(movie.getType());

        List<Movie_ActorDTO> actorDTOs = movie.getMovieActors().stream()
                .map(actor -> new Movie_ActorDTO(actor.getActor().getActor_code(), actor.getActor().getName(),actor.getActor().getAvatar(),actor.getActor().getStatus()))
                .collect(Collectors.toList());
        dto.setMovieActors(actorDTOs);
        return dto;
    }

    @Transactional
    public List<MovieDTO> getHotMovies() {
        List<Movie> hotMovies = movieRepository.findHotMovies();

        return hotMovies.stream().map(movie -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(movie.getId());
            dto.setMovie_code(movie.getMovie_code());
            dto.setMovie_name(movie.getMovie_name());
            dto.setDescription(movie.getDescription());
            dto.setRelease_date(movie.getRelease_date());
            dto.setDuration(movie.getDuration());
            dto.setImage_url(movie.getImage_url());
            dto.setVideo_url(movie.getVideo_url());
            dto.setStatus(movie.getStatus());
            dto.setLanguage(movie.getLanguage());
            dto.setMovie_genre(movie.getMovie_genre());
            dto.setCensorship(movie.getCensorship());
            dto.setType(movie.getType());
            return dto;
        }).collect(Collectors.toList());
    }


    @Transactional
    public List<Episode_DTO> getAllEpisodesByMovieCode(String movie_code) {
        List<Episode> episodes = episodeRepository.findEpisodesByMovieCode(movie_code);

        return episodes.stream().map(episode -> {
            Episode_DTO dto = new Episode_DTO();
            dto.setEpisode_number(episode.getEpisode_number());
            dto.setMovie_name(episode.getMovie().getMovie_name());
            dto.setStatus(episode.getStatus());
            dto.setRelease_date(episode.getRelease_date());
            dto.setDuration(episode.getDuration());
            dto.setDescription(episode.getDescription());
            dto.setVideo_url(episode.getVideo_url());
            return dto;
        }).collect(Collectors.toList());
    }


    public List<MovieDTO> getMoviesByCategoryName(String category_name) {
        List<Movie> movies = movieRepository.findMoviesByCategoryName(category_name);

        return movies.stream().map(movie -> new MovieDTO(
                movie.getId(),
                movie.getMovie_code(),
                movie.getMovie_name(),
                movie.getDescription(),
                movie.getRelease_date(),
                movie.getDuration(),
                movie.getImage_url(),
                movie.getVideo_url(),
                movie.getStatus(),
                movie.getLanguage(),
                movie.getMovie_genre(),
                movie.getCensorship(),
                movie.getCategory_id(),
                movie.getType(),
                null,
                null
        )).toList();
    }
}
