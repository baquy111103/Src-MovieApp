package cms.cms.service.movie.banner;

import cms.cms.model.movie.Category;
import cms.cms.model.movie.Mov_banner;
import cms.cms.repository.movie.banner.BannerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerRepository repository;

    @Override
    public Page<Mov_banner> getPage(String titie, Integer position, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return repository.search(titie,position,pageable);
    }

    @Override
    public void deleteBannerById(Long id){
        try {
            log.info("Id banner cần xóa:"+id);
            repository.deleteBannerById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public Mov_banner findById(Long id) { return repository.findById(id).orElse(null);}

    @Override
    public Mov_banner save(Mov_banner movBanner){
        return repository.save(movBanner);
    }
}
