package cms.cms.service.movie;

import cms.cms.common.Helper;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.movie.Actor;
import cms.cms.model.movie.Movie;
import cms.cms.model.movie.Movie_actor;
import cms.cms.repository.movie.MovieRepository;
import cms.cms.repository.movie.actor.ActorRepository;
import cms.cms.repository.movie.movie_actor.MovieActorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Log4j2
@Service
public class MovieServiceImpl implements MovieService{

    @Autowired
    MovieRepository repository;

    @Autowired
    MovieActorRepository movieActorRepository;

    @Autowired
    ActorRepository actorRepository;

    @Override
    public Page<Movie> getPage(String movie_name, Integer is_hot, Integer type, Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return repository.search(movie_name,
                is_hot,type, pageable);
    }

    @Override
    public void deleteMovieById(Long id){
        try {
            log.info("Id movie cần xóa:"+id);
            repository.deleteMovieById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public void save(Movie object){
         repository.save(object);
    }

    @Override
    public Movie findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Movie findMovieByMovieCode(String movie_code){
        log.info("Movie_code:"+movie_code);
        return repository.findMovieByMovieCode(movie_code);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchMovie(String input_) {
        List<Object[]> results = repository.ajaxSearch(Helper.processStringSearch(input_));
        return Helper.listAjax(results, 1);
    }


    @Override
    public void saveMovie(Movie movie, List<String> listActor) {
        try{
            log.info("listActor:"+listActor);
            String movie_code = movie.getMovie_code();
            log.info("movie_code: " + movie_code);
            save(movie);
            processListActor(listActor, movie_code);
        }catch (Exception e){
            log.error("Error saving movie and actors: {}", e.getMessage(), e);
        }
    }


    private void processListActor(List<String> listActor, String movie_code){
        movieActorRepository.deleteAllBMovieId(movie_code);
        List<String> actorListId = actorRepository.getAllActorCode();
        for (String a : listActor) {
            for (int j = 0; j < actorListId.size(); j++) {
                if (a.equals(actorListId.get(j))) {
                    Movie_actor movieActor = new Movie_actor();
                    movieActor.setMovie_code(movie_code);
                    movieActor.setActor_code(String.valueOf(actorListId.get(j)));
                    movieActorRepository.save(movieActor);
                    break;
                }
                if (j == actorListId.size() - 1) {
                    Actor actor = new Actor();
                    actor.setName(a);
                    actor.setActive(false);
                    actorRepository.save(actor);
                    Movie_actor movieActor = new Movie_actor();
                    movieActor.setMovie_code(movie_code);
                    movieActor.setActor_code(actor.getActor_code());
                    movieActorRepository.save(movieActor);
                }
            }
        }
    }
}
