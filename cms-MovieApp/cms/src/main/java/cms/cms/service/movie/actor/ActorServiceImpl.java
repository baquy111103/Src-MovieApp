package cms.cms.service.movie.actor;

import cms.cms.common.Helper;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Actor;
import cms.cms.repository.movie.actor.ActorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ActorServiceImpl implements ActorService{

    @Autowired
    ActorRepository repository;

    @Override
    public Page<Actor> getPage(String name,  Integer status, Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return repository.search(name, status, pageable);
    }

    @Override
    public void deleteActorById(Long id){
        try {
            log.info("Id actor cần xóa:"+id);
            repository.deleteActorById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public Actor save(Actor object){
        return repository.save(object);
    }

    @Override
    public Actor findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchActor(String input_) {
        List<Object[]> results = repository.ajaxSearch(Helper.processStringSearch(input_));
        return Helper.listAjax(results, 1);
    }

}
