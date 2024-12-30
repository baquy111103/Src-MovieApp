package com.example.demo.Repository;

import com.example.demo.DTO.Movie_ActorDTO;
import com.example.demo.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(
            value = "SELECT *" +
                    "FROM movie " +
                    "WHERE type = :type " +
                    "AND status = 1 " +
                    "ORDER BY created_at DESC",
            nativeQuery = true
    )
    List<Movie> findMoviesByType(@Param("type") Boolean type);

    @Query(
            value = "SELECT DISTINCT m.* FROM movie m " +
                    "LEFT JOIN movie_actor ma ON ma.movie_code = m.movie_code " +
                    "LEFT JOIN actor a ON a.actor_code = ma.actor_code " +
                    "WHERE (:movie_name IS NULL OR m.movie_name LIKE CONCAT('%', :movie_name, '%')) " +
                    "AND (:movie_genre IS NULL OR m.movie_genre LIKE CONCAT('%', :movie_genre, '%')) " +
                    "AND (:actor_name IS NULL OR a.name LIKE CONCAT('%', :actor_name, '%')) " +
                    "AND m.status = 1 " +
                    "ORDER BY m.created_at DESC",
            countQuery = "SELECT count(DISTINCT m.id) FROM movie m " +
                    "LEFT JOIN movie_actor ma ON ma.movie_code = m.movie_code " +
                    "LEFT JOIN actor a ON a.actor_code = ma.actor_code " +
                    "WHERE (:movie_name IS NULL OR m.movie_name LIKE CONCAT('%', :movie_name, '%')) " +
                    "AND (:movie_genre IS NULL OR m.movie_genre LIKE CONCAT('%', :movie_genre, '%')) " +
                    "AND (:actor_name IS NULL OR a.name LIKE CONCAT('%', :actor_name, '%')) " +
                    "AND m.status = 1",
            nativeQuery = true
    )
    List<Movie> searchMovies(
            @Param("movie_name") String movieName,
            @Param("movie_genre") String movieGenre,
            @Param("actor_name") String actorName
    );



    @Query(
            value = "SELECT * FROM movie m WHERE m.movie_code = :movie_code AND m.status = 1",
            nativeQuery = true
    )
    Optional<Movie> findByMovieCode(@Param("movie_code") String movieCode);

    @Query(
            value = "SELECT * FROM movie m WHERE m.is_hot = 1 AND m.status = 1 ORDER BY m.created_at DESC",
            nativeQuery = true
    )
    List<Movie> findHotMovies();

    List<Movie> findByStatus(Boolean status);

    @Query(value = "SELECT m.* " +
            "FROM Movie m " +
            "JOIN Category c ON m.category_id = c.category_code " +
            "WHERE c.name = :category_name AND m.status = 1",
            nativeQuery = true)
    List<Movie> findMoviesByCategoryName(@Param("category_name") String category_name);
}
