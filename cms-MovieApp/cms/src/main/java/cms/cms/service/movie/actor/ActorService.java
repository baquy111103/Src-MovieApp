package cms.cms.service.movie.actor;

import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Actor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActorService {

    Page<Actor> getPage(String name, Integer status, Integer page, Integer pageSize );
    void deleteActorById(Long id);

    Actor save(Actor actor);

    Actor findById(Long id);


    List<AjaxSearchDto> ajaxSearchActor(String input_);

}
