package cms.cms.controller.user;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.dto.ActorDto;
import cms.cms.dto.UserDto;
import cms.cms.model.User;
import cms.cms.model.movie.Actor;
import cms.cms.model.movie.Category;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cmsmovie/user")
@Log4j2
public class UserPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone,
            Model model) {

        if (page == null) page = 1;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findUserByUsername(accountName);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
        log.info("Admin:" + isAdmin);


        Page<User> objectPage = userService.getPage(username,email,phone, page, pageSize);
        List<User> list = objectPage.toList();


        model.addAttribute(Layout.VIEW, Pages.USER_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("username", username);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("models", list);
        return "index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteUserById(@PathVariable(required = false) Integer page,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(name = "id", required = true) Long id,
                                  RedirectAttributes redirectAttributes) {
        if (page == null) {
            page = 1;
        }

        log.info("User Id: " + id);
        userService.deleteUserById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/user/index/" + page + "?pageSize=" + pageSize;
    }


    @GetMapping("/create")
    public String create(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findUserByUsername(accountName);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
        log.info("Admin:" + isAdmin);
        User dto = new User();
        model.addAttribute(Layout.VIEW,Pages.USER_FORM.uri());
        model.addAttribute("model",dto);
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("title", messageSource.getMessage("title.user.create",
                null, LocaleContextHolder.getLocale()));
        return "index";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name ="id") Long id,
                         Model model,
                         @RequestParam(name = "page",required = false) Integer page,
                         @RequestParam(name = "pageSize",required = false) Integer pageSize){
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findUserByUsername(accountName);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
        log.info("Admin:" + isAdmin);
        User dto = userService.findById(id);
        UserDto userDto = new UserDto(dto);
        log.info("dtoUpdate:" + dto);
        model.addAttribute("model", userDto);
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute(Layout.VIEW, Pages.USER_FORM.uri());
        return "index";
    }
    @PostMapping("/save")
    public String save(
            @ModelAttribute("model") UserDto userDto,
            Errors errors, RedirectAttributes redirectAttributes) throws JsonProcessingException {

        log.info("Errors: " + errors.hasErrors());

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.save.error", null, LocaleContextHolder.getLocale()));
            return userDto.getId() == null ? "redirect:/cmsmovie/user/create" : "redirect:/cmsmovie/user/update/" + userDto.getId();
        }

        Long id = userDto.getId();
        if (id != null) {
            userDto.setUpdated_at(new Date());
            userService.update(userDto);
        } else {
            userDto.setCreated_at(new Date());
            if (userService.emailExists(userDto.getEmail())) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("email.exists.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/user/create";
            }
            userService.save(userDto);
        }

        return "redirect:/cmsmovie/user/index";
    }

}
