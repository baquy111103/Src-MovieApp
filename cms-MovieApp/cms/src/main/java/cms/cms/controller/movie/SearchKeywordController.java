package cms.cms.controller.movie;


import cms.cms.common.Helper;
import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.dto.AjaxSearchDto;
import cms.cms.model.User;
import cms.cms.model.movie.Category;
import cms.cms.model.movie.Search_keyword;
import cms.cms.repository.UserRepository;
import cms.cms.service.UserService;
import cms.cms.service.movie.search_keyword.SearchKeywordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
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
@RequestMapping("/cmsmovie/searchkeyword")
public class SearchKeywordController {

    @Autowired
    SearchKeywordService searchKeywordService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(name = "keyword", required = false) String keyword,
                          Model model) {


        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;

        Page<Search_keyword> objectPage = searchKeywordService.getPage(keyword, page, pageSize);
        List<Search_keyword> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.SEARCHKEYWORD_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        return "index";
    }



    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String deleteKeywordById(@PathVariable(required = false) Integer page,
                                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "id", required = true) Long id,
                                     RedirectAttributes redirectAttributes) {
        if (page == null) {
            page = 1;
        }

        log.info("Keyword Id: " + id);
        searchKeywordService.deleteKeywordById(id);

        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));

        return "redirect:/cmsmovie/searchkeyword/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model){
        Search_keyword dto = new Search_keyword();
        model.addAttribute(Layout.VIEW,Pages.SEARCHKEYWORD_FORM.uri());
        model.addAttribute("model",dto);
        model.addAttribute("title", messageSource.getMessage("title.keyword.create",
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
        Search_keyword dto = searchKeywordService.findById(id);
        log.info("dtoUpdate:" + dto);
        model.addAttribute("model", dto);
        model.addAttribute(Layout.VIEW, Pages.SEARCHKEYWORD_FORM.uri());
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("model") Search_keyword dto,
                       Errors error, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        Long id = dto.getId();
        log.info("error:"+error.hasErrors());
        if(!error.hasErrors()){
            Search_keyword object = new Search_keyword();
            User user = userService.getUserInfo();
            if(id == null){
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.createKeyword.success", null, LocaleContextHolder.getLocale()));
                object.setCreated_at(new Date());
                object.setStatus(true);
            }else{
                object = searchKeywordService.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.updateKeyword.success", null, LocaleContextHolder.getLocale()));
                object.setUpdate_at(new Date());
            }
            object.setDescription(dto.getDescription());
            object.setKeyword(dto.getKeyword());
            log.info("objectSave|" + object);
            searchKeywordService.save(object);
        }else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.create.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/searchkeyword/create";
            } else {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.update.error", null, LocaleContextHolder.getLocale()));
                return "redirect:/cmsmovie/searchkeyword/update/" + "?id=" + dto.getId();
            }
        }
        return "redirect:/cmsmovie/searchkeyword/index" ;
    }


}
