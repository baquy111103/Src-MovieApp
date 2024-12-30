package com.example.demo.Service;

import com.example.demo.Model.Search_keyword;
import com.example.demo.Repository.Search_keywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Search_keywordService {
    @Autowired
    private Search_keywordRepository searchKeywordRepository;

    @Transactional
    public List<Search_keyword> getActiveKeyword(){
        return searchKeywordRepository.hotKeyword();
    }

}
