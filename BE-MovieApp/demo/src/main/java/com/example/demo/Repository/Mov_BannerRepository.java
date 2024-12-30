package com.example.demo.Repository;

import com.example.demo.Model.Mov_banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface Mov_BannerRepository extends JpaRepository<Mov_banner, Long> {
    @Query("SELECT b FROM Mov_banner b " +
            "WHERE b.start_date <= :currentDate AND b.end_date >= :currentDate " +
            "AND b.status = true " +
            "ORDER BY b.position ASC")
    List<Mov_banner> findActiveBanners(@Param("currentDate") Date currentDate);
}
