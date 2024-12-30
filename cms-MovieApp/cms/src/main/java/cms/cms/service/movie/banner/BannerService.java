package cms.cms.service.movie.banner;

import cms.cms.model.movie.Mov_banner;
import org.springframework.data.domain.Page;

public interface BannerService {
    Page<Mov_banner> getPage(String title,Integer position, Integer page, Integer pageSize);

    void deleteBannerById(Long id);

    Mov_banner findById(Long id);

    Mov_banner save(Mov_banner movBanner);

}
