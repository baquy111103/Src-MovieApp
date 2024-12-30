package cms.cms.service.movie.search_keyword;

import cms.cms.model.movie.Search_keyword;
import org.springframework.data.domain.Page;

public interface SearchKeywordService {
    Page<Search_keyword> getPage(String keyword, Integer page, Integer pageSize );

    void deleteKeywordById(Long id);

    Search_keyword findById(Long id);

    Search_keyword save(Search_keyword searchKeyword);
}
