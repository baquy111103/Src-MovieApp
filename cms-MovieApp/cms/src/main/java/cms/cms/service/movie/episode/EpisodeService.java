package cms.cms.service.movie.episode;

import cms.cms.model.movie.Episode;
import org.springframework.data.domain.Page;

public interface EpisodeService {
    Page<Episode> getPage(Integer episode_number,String movie_name,Integer page, Integer pageSize );

    Episode findById(Long id);

    Episode save (Episode episode);

    void deleteEpisodeById(Long id);
}
