package com.example.demo.Repository;

import com.example.demo.Model.Movie;
import com.example.demo.Model.Search_keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Search_keywordRepository extends JpaRepository<Search_keyword,Long> {
    @Query(
            value = "SELECT *" +
                    "FROM search_keyword " +
                    "WHERE status = 1 limit 7",
            nativeQuery = true
    )
    List<Search_keyword> hotKeyword();
}
