package com.example.demo.Controller;

import com.example.demo.Model.Search_keyword;
import com.example.demo.Repository.Search_keywordRepository;
import com.example.demo.Service.Search_keywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class Search_keywordController {
    @Autowired
    private Search_keywordService searchKeywordService;

    @GetMapping("/active")
    public ResponseEntity<List<Search_keyword>> getActiveKeywords() {
        List<Search_keyword> keywords = searchKeywordService.getActiveKeyword();
        if (keywords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(keywords);
    }
}
