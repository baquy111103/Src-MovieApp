package cms.cms.repository.movie.actor;

import cms.cms.model.movie.Actor;
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
public interface ActorRepository extends JpaRepository<Actor,Long> {

    @Query(value = "SELECT * FROM actor " +
            "WHERE (:name IS NULL OR name LIKE CONCAT('%', :name, '%')) " +
            "AND (:status IS NULL OR status = :status) " +
            "AND active = 1 " +
            "ORDER BY created_at DESC ",
            countQuery = "SELECT count(*) FROM actor " +
                    "WHERE (:name IS NULL OR name LIKE CONCAT('%', :name, '%')) " +
                    "AND (:status IS NULL OR status = :status) " +
                    "AND active = 1",
            nativeQuery = true)
    Page<Actor> search(@Param("name") String name,
                       @Param("status") Integer status,
                       Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE actor SET active = 0 WHERE id = :id", nativeQuery = true)
    void deleteActorById(@Param("id") Long id);



    @Query(value = "SELECT actor_code AS `id`, name AS `text` " +
            "FROM actor " +
            "WHERE (:input_ IS NULL OR actor_code = :input_ OR actor_code LIKE CONCAT('%', :input_, '%')) " +
            "AND ACTIVE = 1 " +
            "LIMIT 20",
            nativeQuery = true)
    List<Object[]> ajaxSearch(@Param("input_") String input_);

    @Query(value = "SELECT actor_code FROM actor where active = 1", nativeQuery = true)
    List<String> getAllActorCode();
}
