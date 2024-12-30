package cms.cms.repository.movie.category;

import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query(value = "SELECT * FROM category " +
            "WHERE (:name IS NULL OR name LIKE CONCAT('%', :name, '%')) " +
            "AND status = 1 " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM category " +
                    "WHERE (:name IS NULL OR name LIKE CONCAT('%', :name, '%')) " +
                    "AND status = 1",
            nativeQuery = true)
    Page<Category> search(@Param("name") String name,
                          Pageable pageable);


    @Query(value = "SELECT category_code AS `id`, name AS `text` " +
            "FROM category " +
            "WHERE (:input_ IS NULL OR category_code = :input_ OR name LIKE CONCAT('%', :input_, '%')) " +
            "AND STATUS = 1 " +
            "LIMIT 20",
            nativeQuery = true)
    List<Object[]> ajaxSearch(@Param("input_") String input_);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category SET status = 0 WHERE id = :id", nativeQuery = true)
    void deleteCategoryById(@Param("id") Long id);
}
