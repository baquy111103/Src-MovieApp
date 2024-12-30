package cms.cms.repository.movie;

import cms.cms.dto.MovieDto;
import cms.cms.model.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    @Query(value = "SELECT * FROM movie " +
            "WHERE (:movie_name IS NULL OR movie_name LIKE CONCAT('%', :movie_name, '%')) " +
            "AND (:is_hot IS NULL OR is_hot = :is_hot) " +
            "AND (:type IS NULL OR type = :type) " +
            "AND status = 1 " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM movie " +
                    "WHERE (:movie_name IS NULL OR movie_name LIKE CONCAT('%', :movie_name, '%')) " +
                    "AND (:is_hot IS NULL OR is_hot = :is_hot) " +
                    "AND (:type IS NULL OR type = :type) " +
                    "AND status = 1",
            nativeQuery = true)
    Page<Movie> search(@Param("movie_name") String movie_name,
                          @Param("is_hot") Integer is_hot,
                          @Param("type") Integer type,
                          Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE movie SET status = 0 WHERE id = :id", nativeQuery = true)
    void deleteMovieById(@Param("id") Long id);

    @Query(value = "SELECT movie_code AS `id`, movie_name AS `text` " +
            "FROM movie " +
            "WHERE (:input_ IS NULL OR movie_code = :input_ OR movie_name LIKE CONCAT('%', :input_, '%')) " +
            "AND STATUS = 1 " +
            "AND TYPE = 1 " +
            "LIMIT 20",
            nativeQuery = true)
    List<Object[]> ajaxSearch(@Param("input_") String input_);

    @Query(value = "SELECT * " +
            "FROM movie " +
            "WHERE (:movie_code IS NULL OR movie_code LIKE CONCAT('%', :movie_code, '%')) " +
            "AND STATUS = 1 " +
            "AND TYPE = 1 " +
            "LIMIT 20",
            nativeQuery = true)
    Movie findMovieByMovieCode(@Param("movie_code") String movie_code);



}
