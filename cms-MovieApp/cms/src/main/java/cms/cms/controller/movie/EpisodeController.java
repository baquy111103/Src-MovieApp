package cms.cms.controller.movie;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.common.UploadFile;
import cms.cms.dto.EpisodeDto;
import cms.cms.model.User;
import cms.cms.model.movie.Episode;
import cms.cms.model.movie.Movie;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import cms.cms.service.movie.MovieService;
import cms.cms.service.movie.episode.EpisodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/cmsmovie/episode")
public class EpisodeController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    EpisodeService service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    MovieService movieService;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "episode_number", required = false) Integer episode_number,
            @RequestParam(name = "movie_name", required = false) String movie_name,
            Model model) {

        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        Page<Episode> objectPage = service.getPage(episode_number,movie_name, page, pageSize);
        List<Episode> list = objectPage.toList();

        List<String> movieNames = new ArrayList<>();

        for (Episode episode : list) {
            String movieName = episode.getMovie() != null ? episode.getMovie().getMovie_name() : "Unknown";
            movieNames.add(movieName);
        }

        model.addAttribute(Layout.VIEW, Pages.EPISODE_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("episode_number", episode_number);
        model.addAttribute("move_name",movie_name);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        model.addAttribute("movieNames", movieNames);
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model){
//        Episode dto =new Episode();
        EpisodeDto dto = new EpisodeDto();
        model.addAttribute(Layout.VIEW, Pages.EPISODE_FORM.uri());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.movie.create",
                null, LocaleContextHolder.getLocale()));
        return "index";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model,
                         @RequestParam(name = "page", required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Episode episode = service.findById(id);
        EpisodeDto dto = new EpisodeDto(episode);
        log.info("Id episode:"+id);
        log.info("dtoUpdate|" + dto);
        String video_url = episode.getVideo_url();
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.EPISODE_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("model") EpisodeDto dto,
                       @RequestParam String videoName,
                       @RequestParam("videoPath") MultipartFile videoFile,
                       Errors errors, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        Long id = dto.getId();
        log.info("error:"+errors.hasErrors());
        if(!errors.hasErrors()){
            Episode object = new Episode();
            User user = userService.getUserInfo();
            if(id==null){
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.createEpisode.success",
                        null, LocaleContextHolder.getLocale()));
                object.setCreated_at(new Date());
                object.setStatus(true);
            }else{
                object = service.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.createEpisode.success",
                        null, LocaleContextHolder.getLocale()));
                object.setUpdated_at(new Date());
            }
            object.setEpisode_number(dto.getEpisode_number());
            object.setDescription(dto.getDescription());

            object.setRelease_date(Helper.standardDateV2(dto.getRelease_date()));
            object.setDuration(dto.getDuration());
            log.info("getVideo_url:"+dto.getVideo_url());
            log.info("videoName:"+videoName);
            log.info("videoFile:"+videoFile);
            if (!videoFile.isEmpty()) {
                log.info("videoName:"+videoName);
                log.info("videoFile:"+videoFile);
                String videoUrl = uploadFile.uploadVideoToS3(videoName, videoFile);
                log.info("videoUrl:"+videoUrl);
                object.setVideo_url(videoUrl);
            } else {
                log.info("getVideo_url:"+dto.getVideo_url());
                object.setVideo_url(dto.getVideo_url());

            }

            if (dto.getMovie_code()!= null) {
                log.info("Movie_code_episode:" + dto.getMovie_code());
                Movie movie = movieService.findMovieByMovieCode(dto.getMovie_code());
                log.info("Movie:"+ movie.toString());
                if (movie != null) {
                    log.info("Thành công !!");
                    object.setMovie(movie);
                } else {
                    return "redirect:/cmsmovie/episode/create";
                }
            }else{
                return "redirect:/cmsmovie/episode/create";
            }
            log.info("objectSave|" + object);
            service.save(object);
        }else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.create.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/episode/create";
            } else {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.update.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/episode/update/" + "?id=" + dto.getId();
            }
        }
        return "redirect:/cmsmovie/episode/index" ;
    }
}
