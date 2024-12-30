package cms.cms.repository.movie.movie_actor;

import cms.cms.model.movie.Movie_actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MovieActorRepository extends JpaRepository<Movie_actor,Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM movie_actor WHERE movie_code = :movie_code", nativeQuery = true)
    void deleteAllBMovieId(@Param("movie_code") String movie_code);
}
