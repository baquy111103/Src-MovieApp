package cms.cms.controller.movie;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.common.UploadFile;
import cms.cms.dto.Mov_bannerDto;
import cms.cms.model.User;
import cms.cms.model.movie.Mov_banner;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import cms.cms.service.movie.banner.BannerService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/cmsmovie/banner")
public class BannerController {

    @Autowired
    private BannerService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "position", required = false) Integer position,
            Model model) {

        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;

        Page<Mov_banner> objectPage = service.getPage(title, position, page, pageSize);
        List<Mov_banner> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.BANNER_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("title", title);
        model.addAttribute("position", position);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);

        return "index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteBannerById(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id", required = true) Long id,
            RedirectAttributes redirectAttributes) {
        if (page == null) page = 1;

        log.info("Deleting banner with ID: " + id);
        service.deleteBannerById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/banner/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        Mov_bannerDto dto = new Mov_bannerDto();
        model.addAttribute(Layout.VIEW, Pages.BANNER_FORM.uri());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.movie.create", null, LocaleContextHolder.getLocale()));
        return "index";
    }

    @GetMapping("/update/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        Mov_banner banner = service.findById(id);
        Mov_bannerDto dto = new Mov_bannerDto(banner);

        log.info("Updating banner with ID: " + id);
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.BANNER_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("model") Mov_bannerDto dto,
            @RequestParam String thumbUpload,
            Errors errors, RedirectAttributes redirectAttributes) throws JsonProcessingException {

        log.info("Errors: " + errors.hasErrors());
        if (!errors.hasErrors()) {
            Mov_banner banner;
            User user = userService.getUserInfo();

            if (dto.getId() == null) {
                banner = new Mov_banner();
                banner.setCreated_at(new Date());
                banner.setCreated_by(user.getId());
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.createCategory.success", null, LocaleContextHolder.getLocale()));
            } else {
                banner = service.findById(dto.getId());
                banner.setUpdated_at(new Date());
                banner.setUpdated_by(user.getId());
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.updateCategory.success", null, LocaleContextHolder.getLocale()));
            }
            // Xử lý ảnh
            if (thumbUpload != null && !thumbUpload.isEmpty()) {
                String fileNameHeaderBanner = uploadFile.createImageFile(thumbUpload, "banner");
                log.info("fileNameHeaderBanner ------> " + fileNameHeaderBanner);
                banner.setBanner_image(fileNameHeaderBanner);
            } else if (dto.getId() != null) {
                banner.setBanner_image(dto.getBanner_image());
            }
            banner.setTitle(dto.getTitle());
            if (banner.getMovie_banner_code() != null) {
                banner.setMovie_banner_code(dto.getMovie_banner_code());
            } else {
                try {
                    banner.setMovie_banner_code(Helper.generateCode(dto.getTitle()));
                } catch (Exception e) {
                    log.error("Lỗi khi tạo mã banner: " + e.getMessage(), e);
                }
            }

            boolean status = dto.getStatus();
            banner.setStatus(status);
            banner.setPosition(dto.getPosition());
            banner.setStart_date(Helper.standardDateV2(dto.getStart_date()));
            banner.setEnd_date(Helper.standardDateV2(dto.getEnd_date()));

            service.save(banner);
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.save.error", null, LocaleContextHolder.getLocale()));
            return dto.getId() == null ? "redirect:/cmsmovie/banner/create" : "redirect:/cmsmovie/banner/update/" + dto.getId();
        }

        return "redirect:/cmsmovie/banner/index";
    }
}
