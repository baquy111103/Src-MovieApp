package cms.cms.controller.movie;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.common.UploadFile;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.User;
import cms.cms.model.movie.Movie;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import cms.cms.service.movie.MovieService;
import cms.cms.service.movie.category.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cmsmovie/movie")
@Log4j2
public class MovieController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieService service;

    @Autowired
    UserService userService;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(name = "movie_name", required = false) String movie_name,
                          @RequestParam(name = "type", required = false) Integer type,
                          @RequestParam(name = "is_hot", required = false) Integer is_hot,
                          @RequestParam(name = "language", required = false) String language,
                          Model model) {


        if (page == null) page = 1;

        if (pageSize == null) pageSize = 10;


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findUserByUsername(accountName);
        User curUser = user.get();


        Page<Movie> objectPage = service.getPage(movie_name, is_hot, type, page, pageSize);
        List<Movie> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.MOVIE_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("movie_name", movie_name);
        model.addAttribute("language",language);
        model.addAttribute("type", type);
        model.addAttribute("is_hot", is_hot);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);

        return "index";
    }


    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteMovieById(@PathVariable(required = false) Integer page,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(name = "id", required = true) Long id,
                                  RedirectAttributes redirectAttributes) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) pageSize = 10;

        log.info("Movie Id: " + id);
        service.deleteMovieById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/movie/index/" + page + "?pageSize=" + pageSize;
    }


    @GetMapping("/create")
    public String create(Model model){
        Movie dto =new Movie();
        model.addAttribute(Layout.VIEW, Pages.MOVIE_FORM.uri());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.movie.create",
                null, LocaleContextHolder.getLocale()));
        return "index";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model,
                         @RequestParam(name = "page", required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Movie dto = service.findById(id);
        log.info("dtoUpdate|" + dto);
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.MOVIE_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("model") Movie dto,
                       @RequestParam String videoName,
                       @RequestParam("videoPath") MultipartFile videoFile,
                       @RequestParam String thumbUpload,
                       @RequestParam(name = "listActor", required = false) List<String> listActor,
                       Errors error, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        Long id= dto.getId();
        log.info("error:"+error.hasErrors());
        if(!error.hasErrors()){
            Movie object = new Movie();
            User user = userService.getUserInfo();

            if(id== null){
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.createMovie.success", null, LocaleContextHolder.getLocale()));
                object.setCreated_at(new Date());
                object.setUser_phone(user.getPhone());
                object.setStatus(true);
            }else{
                object = service.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
                object.setUpdated_at(new Date());
            }


            if (thumbUpload != null && !thumbUpload.isEmpty()) {
                String fileNameHeaderBanner = uploadFile.createImageFile(thumbUpload, "banner");
                log.info("fileNameHeaderBanner ------> " + fileNameHeaderBanner);
                object.setImage_url(fileNameHeaderBanner);

            } else if (dto.getId() != null) {
                object.setImage_url(dto.getImage_url());
            }
            object.setCensorship(dto.getCensorship());
            object.setMovie_name(dto.getMovie_name());
            if (object.getMovie_code() != null) {
                object.setMovie_code(dto.getMovie_code());
            } else {
                try {
                    object.setMovie_code(Helper.generateCode(dto.getMovie_name()));
                } catch (Exception e) {
                    log.error("Lỗi khi tạo mã Movie: " + e.getMessage(), e);
                }
            }
            object.setDescription(dto.getDescription());
            object.setRelease_date(dto.getRelease_date());
//            object.setRelease_date(Helper.standardDateV2(dto.getRelease_date()));
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

            object.setCensorship(dto.getCensorship());
            object.setLanguage(dto.getLanguage());
            object.setMovie_genre(dto.getMovie_genre());

            object.setIs_hot(dto.getIs_hot());
            log.info("ishot:"+dto.getIs_hot());
            if(dto.getType()== true){
                object.setType(true);
            }else {
                object.setType(false);
            }

            object.setCategory_id(dto.getCategory_id());
            log.info("objectSave|" + object);
            log.info(getListActor() + "|" + listActor);
            service.saveMovie(object,listActor);
        }else{
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.create.error", null, LocaleContextHolder.getLocale()));
                log.error("create failed");
                return "redirect:/cmsmovie/" + Pages.MOVIE_CREATE.uri();
            } else {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.update.error", null, LocaleContextHolder.getLocale()));
                log.error("update failed");
                return "redirect:/cmsmovie/" + Pages.MOVIE_UPDATE.uri() + "?id=" + dto.getId();
            }
        }
        return "redirect:/cmsmovie/" + Pages.MOVIE_INDEX.uri();
    }

    private static String getListActor() {
        return "ListActor";
    }

    @GetMapping("/ajax-search-movie")
    public ResponseEntity<List<AjaxSearchDto>> movieSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearchMovie(input), HttpStatus.OK);
    }

}
