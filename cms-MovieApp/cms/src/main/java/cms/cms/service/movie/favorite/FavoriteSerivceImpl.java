package cms.cms.service.movie.favorite;


import cms.cms.model.movie.Favorite;
import cms.cms.repository.movie.favorite.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoriteSerivceImpl implements FavoriteService {

    @Autowired
    FavoriteRepository repository;

    @Override
    public Page<Favorite> getPage(Integer active, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return repository.search(active, pageable);
    }

}
