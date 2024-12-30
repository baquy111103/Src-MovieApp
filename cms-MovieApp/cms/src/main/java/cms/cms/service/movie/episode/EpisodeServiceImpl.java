package cms.cms.service.movie.episode;

import cms.cms.model.movie.Episode;
import cms.cms.repository.movie.episode.EpisodeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EpisodeServiceImpl implements EpisodeService{

    @Autowired
    EpisodeRepository repository;

    @Override
    public Page<Episode> getPage(Integer episode_number,String movie_name,Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return repository.search(episode_number,movie_name, pageable);
    }

    @Override
    public Episode findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Episode save (Episode episode){
        return repository.save(episode);
    }

    @Override
    public void deleteEpisodeById(Long id){
        try{
            log.info("Id Episode cần xóa:"+ id);
             repository.deleteEpisodeById(id);
        }catch (Exception e){
            log.info("Lỗi xóa:"+ e);
        }
    }
}
