package cms.cms.repository.movie.banner;

import cms.cms.model.movie.Mov_banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BannerRepository extends JpaRepository<Mov_banner,Long> {
    @Query(value = "SELECT * FROM mov_banner " +
            "WHERE (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) " +
            "AND (:position is null or position LIKE CONCAT('%', :position, '%')) " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM mov_banner " +
                    "WHERE (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) " +
                    "AND (:position IS NULL OR position LIKE CONCAT('%', :position, '%')) " ,
            nativeQuery = true)
    Page<Mov_banner> search(@Param("title") String title,
                       @Param("position") Integer position,
                       Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE mov_banner SET status = 0 WHERE id = :id", nativeQuery = true)
    void deleteBannerById(@Param("id") Long id);
}
