package cms.cms.service.movie;

import cms.cms.dto.AjaxSearchDto;
import cms.cms.dto.MovieDto;
import cms.cms.model.movie.Movie;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface MovieService {
    Page<Movie> getPage(String movie_name, Integer is_hot, Integer type, Integer page, Integer pageSize );

    void deleteMovieById(Long id);

    void save(Movie movie);

    void saveMovie(Movie movie,List<String> listActor);

    Movie findById(Long id);

    List<AjaxSearchDto> ajaxSearchMovie(String input_);

    Movie findMovieByMovieCode(String movie_code);
}
