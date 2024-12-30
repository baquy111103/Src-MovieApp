package com.example.demo.Controller;

import com.example.demo.Model.Mov_banner;
import com.example.demo.Service.Mov_bannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class Mov_bannerController {
    @Autowired
    private Mov_bannerService movBannerService;

    @GetMapping("/active")
    public ResponseEntity<List<Mov_banner>> getActiveBanners() {
        List<Mov_banner> banners = movBannerService.getActiveBanners();
        return ResponseEntity.ok(banners);
    }
}
