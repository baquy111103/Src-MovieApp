package cms.cms.service.movie.favorite;

import cms.cms.model.movie.Favorite;
import org.springframework.data.domain.Page;

public interface FavoriteService {

    Page<Favorite> getPage(Integer active, Integer page, Integer pageSize );

}
