package cms.cms.repository.movie.search_keyword;

import cms.cms.model.movie.Search_keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SearchKeywordReposioty  extends JpaRepository<Search_keyword,Long> {
    @Query(value = "SELECT * FROM search_keyword " +
            "WHERE (:keyword IS NULL OR keyword LIKE CONCAT('%', :keyword, '%')) " +
            "AND status = 1 " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM search_keyword " +
                    "WHERE (:keyword IS NULL OR keyword LIKE CONCAT('%', :keyword, '%')) " +
                    "AND status = 1",
            nativeQuery = true)
    Page<Search_keyword> search(@Param("keyword") String keyword,
                       Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE search_keyword SET status = 0 WHERE id = :id", nativeQuery = true)
    void deleteKeywordById(@Param("id") Long id);
}
