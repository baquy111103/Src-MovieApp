package com.example.demo.Service;

import com.example.demo.Model.Mov_banner;
import com.example.demo.Repository.Mov_BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class Mov_bannerService {
    @Autowired
    private Mov_BannerRepository movBannerRepository;

    public List<Mov_banner> getActiveBanners() {
        Date currentDate = new Date(); // Lấy ngày hiện tại
        return movBannerRepository.findActiveBanners(currentDate);
    }
}
