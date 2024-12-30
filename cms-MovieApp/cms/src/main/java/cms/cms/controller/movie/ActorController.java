package cms.cms.controller.movie;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.common.UploadFile;
import cms.cms.dto.ActorDto;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.User;
import cms.cms.model.movie.Actor;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import cms.cms.service.movie.actor.ActorService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/cmsmovie/actor")
public class ActorController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActorService service;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "status", required = false) Integer status,
            Model model) {

        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;

        Page<Actor> objectPage = service.getPage(name, status, page, pageSize);
        List<Actor> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.ACTOR_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);

        return "index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteActorById(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id", required = true) Long id,
            RedirectAttributes redirectAttributes) {
        if (page == null) page = 1;

//        log.info("Deleting actor with ID: " + id);
        service.deleteActorById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/actor/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        ActorDto dto = new ActorDto();
        model.addAttribute(Layout.VIEW, Pages.ACTOR_FORM.uri());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.actor.create", null, LocaleContextHolder.getLocale()));
        return "index";
    }

    @GetMapping("/update/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        Actor actor = service.findById(id);
        ActorDto dto = new ActorDto(actor);

        log.info("Updating actor with ID: " + id);
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.ACTOR_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("model") ActorDto dto,
            @RequestParam String thumbUpload,
            Errors errors, RedirectAttributes redirectAttributes) throws JsonProcessingException {

        log.info("Errors: " + errors.hasErrors());
        if (!errors.hasErrors()) {
            Actor actor;
            User user = userService.getUserInfo();

            if (dto.getId() == null) {
                actor = new Actor();
                actor.setActive(true);
                actor.setCreated_at(new Date());
                actor.setCreated_by(user.getId());
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.createActor.success", null, LocaleContextHolder.getLocale()));
            } else {
                actor = service.findById(dto.getId());
                actor.setUpdated_at(new Date());
                actor.setUpdated_by(user.getId());
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.updateActor.success", null, LocaleContextHolder.getLocale()));
            }

            if (thumbUpload != null && !thumbUpload.isEmpty()) {
                String fileNameHeaderAvatar = uploadFile.createImageFile(thumbUpload, "avatar");
                log.info("fileNameHeaderAvatar ------> " + fileNameHeaderAvatar);
                actor.setAvatar(fileNameHeaderAvatar);

            } else if (dto.getId() != null) {
                actor.setAvatar(dto.getAvatar());
            }
            actor.setName(dto.getName());
            actor.setBio(dto.getBio());
            if (actor.getActor_code() != null) {
                actor.setActor_code(dto.getActor_code());
            } else {
                try {
                    actor.setActor_code(Helper.generateCode(dto.getName()));
                } catch (Exception e) {
                    log.error("Lỗi khi tạo mã actor: " + e.getMessage(), e);
                }
            }
            boolean status = dto.getStatus();
            actor.setStatus(status);
            actor.setDate_of_birth(Helper.standardDateV2(dto.getDate_of_birth()));

            service.save(actor);
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.save.error", null, LocaleContextHolder.getLocale()));
            return dto.getId() == null ? "redirect:/cmsmovie/actor/create" : "redirect:/cmsmovie/actor/update/" + dto.getId();
        }

        return "redirect:/cmsmovie/actor/index";
    }

    @GetMapping("/ajax-search-actor")
    public ResponseEntity<List<AjaxSearchDto>> movieSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearchActor(input), HttpStatus.OK);
    }
}
