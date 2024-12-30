package cms.cms.service.movie.category;

import cms.cms.common.Helper;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Category;
import cms.cms.repository.movie.category.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Category> getPage(String name, Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return repository.search(name, pageable);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input_) {
        // Gọi repository.ajaxSearch để lấy kết quả dưới dạng List<Object[]>
        List<Object[]> results = repository.ajaxSearch(Helper.processStringSearch(input_));

        // Sử dụng Helper.listAjax để chuyển đổi từ List<Object[]> thành List<AjaxSearchDto>
        return Helper.listAjax(results, 1);  // '1' là kiểu bạn muốn, có thể thay đổi theo nhu cầu
    }


    @Override
    public void deleteCategoryById(Long id){
        try {
            log.info("Id category cần xóa:"+id);
            repository.deleteCategoryById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public Category findById(Long id) { return repository.findById(id).orElse(null);}

    @Override
    public Category save(Category object){
        return repository.save(object);
    }
}
