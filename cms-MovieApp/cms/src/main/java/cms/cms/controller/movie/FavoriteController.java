package cms.cms.controller.movie;


import cms.cms.common.Layout;
import cms.cms.common.Pages;
import cms.cms.model.User;
import cms.cms.model.movie.Favorite;
import cms.cms.repository.UserRepository;
import cms.cms.service.movie.favorite.FavoriteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/cmsmovie/favorite")
public class FavoriteController {

    @Autowired
    FavoriteService service;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getPage(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "active", required = false) Integer active,
            Model model) {

        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        Page<Favorite> objectPage = service.getPage(active, page, pageSize);
        List<Favorite> list = objectPage.toList();

        model.addAttribute(Layout.VIEW, Pages.FAVORITE_INDEX.uri());
        model.addAttribute("currentPage", page);
        model.addAttribute("active", active);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        return "index";
    }
}
