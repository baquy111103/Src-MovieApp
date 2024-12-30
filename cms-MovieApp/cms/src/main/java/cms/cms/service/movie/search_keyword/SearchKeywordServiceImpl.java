package cms.cms.service.movie.search_keyword;

import cms.cms.model.movie.Search_keyword;
import cms.cms.repository.movie.search_keyword.SearchKeywordReposioty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SearchKeywordServiceImpl implements SearchKeywordService{
    @Autowired
    private SearchKeywordReposioty reposioty;

    @Override
    public Page<Search_keyword> getPage(String keyword, Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return reposioty.search(keyword, pageable);
    }

    @Override
    public void deleteKeywordById(Long id){
        try {
            log.info("Id keyword cần xóa:"+id);
            reposioty.deleteKeywordById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public Search_keyword findById(Long id) { return reposioty.findById(id).orElse(null);}

    @Override
    public Search_keyword save(Search_keyword searchKeyword){
        return reposioty.save(searchKeyword);
    }

}
