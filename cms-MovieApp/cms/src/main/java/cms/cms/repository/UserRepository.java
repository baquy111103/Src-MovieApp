package cms.cms.repository;

import cms.cms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE email = :email AND active = 1", nativeQuery = true)
    Optional<User> findUserByUsername(String email);

    @Query(value = "Select * from user where email = :username  AND active = 1", nativeQuery = true)
    User getUserInfoByUsername(@Param(value = "username") String username);

    @Query(value = "SELECT * FROM user " +
            "WHERE (:username IS NULL OR username LIKE CONCAT('%', :username, '%')) " +
            "AND (:email IS NULL OR email LIKE CONCAT('%', :email, '%')) " +
            "AND (:phone IS NULL OR phone LIKE CONCAT('%', :phone, '%')) " +
            "AND active = 1 " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM user " +
                    "WHERE (:username IS NULL OR username LIKE CONCAT('%', :username, '%')) " +
                    "AND (:email IS NULL OR email LIKE CONCAT('%', :email, '%')) " +
                    "AND (:phone IS NULL OR phone LIKE CONCAT('%', :phone, '%'))"+
                    "AND active = 1 " ,
            nativeQuery = true)
    Page<User> search(@Param("username") String username,
                      @Param("email") String email,
                      @Param("phone") String phone,
                      Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET active = 0 WHERE id = :id", nativeQuery = true)
    void deleteUserById(@Param("id") Long id);
}
