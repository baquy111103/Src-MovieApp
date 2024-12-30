package cms.cms.service.movie.category;

import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Page<Category> getPage(String name, Integer page, Integer pageSize );

    List<AjaxSearchDto>ajaxSearch(String input_);

    void deleteCategoryById(Long id);

    Category findById(Long id);

    Category save(Category category);


}
