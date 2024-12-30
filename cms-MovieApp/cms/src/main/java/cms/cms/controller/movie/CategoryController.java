package cms.cms.controller.movie;

import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.User;
import cms.cms.model.movie.Category;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/cmsmovie/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;


    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "description", required = false) String description,
                          Model model) {


        if (page == null) page = 1;

        if (pageSize == null) pageSize = 10;



        Page<Category> objectPage = categoryService.getPage(name, page, pageSize);
        List<Category> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.CATEGORY_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);

        return "index";
    }


    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> categorySearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(categoryService.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteCatogeryById(@PathVariable(required = false) Integer page,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(name = "id", required = true) Long id,
                                  RedirectAttributes redirectAttributes) {
        if (page == null) {
            page = 1;
        }

        log.info("Movie Id: " + id);
        categoryService.deleteCategoryById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/category/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model){
        Category dto = new Category();
        model.addAttribute(Layout.VIEW,Pages.CATEGORY_FORM.uri());
        model.addAttribute("model",dto);
        model.addAttribute("title", messageSource.getMessage("title.movie.create",
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
        Category dto = categoryService.findById(id);
        log.info("dtoUpdate:" + dto);
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.CATEGORY_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("model") Category dto,
                       Errors error, RedirectAttributes redirectAttributes) throws JsonProcessingException{
        Long id = dto.getId();
        log.info("error:"+error.hasErrors());
        if(!error.hasErrors()){
            Category object = new Category();
            User user = userService.getUserInfo();
            if(id == null){
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.createCategory.success", null, LocaleContextHolder.getLocale()));
                object.setCreated_at(new Date());
                object.setCreated_by(user.getId());
                object.setStatus(true);
            }else{
                object = categoryService.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.updateCategory.success", null, LocaleContextHolder.getLocale()));
                object.setUpdated_at(new Date());
                object.setUpdate_by(user.getId());
            }
            object.setDescription(dto.getDescription());
            object.setName(dto.getName());
            if (object.getCategory_code() != null) {
                object.setCategory_code(dto.getCategory_code());
            } else {
                try {
                    object.setCategory_code(Helper.generateCode(dto.getName()));
                } catch (Exception e) {
                    log.error("Lỗi khi tạo mã category: " + e.getMessage(), e);
                }
            }


            log.info("objectSave|" + object);
            categoryService.save(object);
        }else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.create.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/category/create";
            } else {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.update.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/category/update/" + "?id=" + dto.getId();
            }
        }
        return "redirect:/cmsmovie/category/index" ;
    }

}
