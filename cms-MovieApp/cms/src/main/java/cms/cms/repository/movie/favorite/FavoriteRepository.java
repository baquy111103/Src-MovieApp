package cms.cms.repository.movie.favorite;

import cms.cms.model.movie.Category;
import cms.cms.model.movie.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    @Query(value = "SELECT * FROM favorite " +
            "WHERE (:active IS NULL OR active = :active) " +
            "ORDER BY favorite_day DESC",
            countQuery = "SELECT COUNT(*) FROM favorite " +
                    "WHERE (:active IS NULL OR active = :active) ",
            nativeQuery = true)
    Page<Favorite> search(@Param("active") Integer active,
                          Pageable pageable);

}
