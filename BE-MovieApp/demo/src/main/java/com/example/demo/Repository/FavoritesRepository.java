package com.example.demo.Repository;

import com.example.demo.Model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FavoritesRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f WHERE f.active = true")
    List<Favorite> findAllFavorite();


    @Query("SELECT f FROM Favorite f WHERE f.movie.movie_code = :movie_code")
    Optional<Favorite> findByMovieCode(@Param("movie_code") String movie_code);
}
