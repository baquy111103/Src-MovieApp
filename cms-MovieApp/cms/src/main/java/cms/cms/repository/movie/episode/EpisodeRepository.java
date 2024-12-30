package cms.cms.repository.movie.episode;

import cms.cms.model.movie.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EpisodeRepository extends JpaRepository<Episode,Long> {
//    @Query(value = "SELECT * FROM episode  " +
//            "WHERE (:episode_number IS NULL OR episode_number LIKE CONCAT('%', :episode_number, '%')) " +
//            "AND status = 1 " +
//            "ORDER BY episode_number DESC",
//            countQuery = "SELECT COUNT(*) FROM category " +
//                    "WHERE (:episode_number IS NULL OR episode_number LIKE CONCAT('%', :episode_number, '%')) " +
//                    "AND status = 1",
//            nativeQuery = true)
//    Page<Episode> search(@Param("episode_number") Integer episode_number,
//                          Pageable pageable);
    @Query(value = "SELECT e.*, m.movie_name FROM episode e " +
            "JOIN movie m ON e.movie_code = m.movie_code " +
            "WHERE (:episode_number IS NULL OR e.episode_number LIKE CONCAT('%', :episode_number, '%')) " +
            "AND e.status = 1 " +
            "AND (:movie_name IS NULL OR m.movie_name LIKE CONCAT('%', :movie_name, '%')) " +
            "ORDER BY e.episode_number DESC",
            countQuery = "SELECT COUNT(*) FROM episode e " +
                    "JOIN movie m ON e.movie_code = m.movie_code " +
                    "WHERE (:episode_number IS NULL OR e.episode_number LIKE CONCAT('%', :episode_number, '%')) " +
                    "AND e.status = 1 " +
                    "AND (:movie_name IS NULL OR m.movie_name LIKE CONCAT('%', :movie_name, '%'))",
            nativeQuery = true)
    Page<Episode> search(@Param("episode_number") Integer episode_number,
                         @Param("movie_name") String movie_name,
                         Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE episode SET status = 0 WHERE id = :id", nativeQuery = true)
    void deleteEpisodeById(@Param("id") Long id);

}
