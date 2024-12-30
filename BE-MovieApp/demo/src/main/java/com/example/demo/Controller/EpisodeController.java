package com.example.demo.Controller;

import com.example.demo.DTO.EpisodeDto;
import com.example.demo.Service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

//    @GetMapping("/movie")
//    public ResponseEntity<List<EpisodeDto>> getEpisodesByMovieCode(@RequestParam String movie_code) {
//        List<EpisodeDto> episodes = episodeService.getEpisodesByMovieCode(movie_code);
//        return ResponseEntity.ok(episodes);
//    }
//
//        @GetMapping("/{movie_code}/{episode_number}/video")
//    public ResponseEntity<String> getVideoUrl(@PathVariable String movie_code, @PathVariable Integer episode_number) {
//        String videoUrl = episodeService.getVideoUrlByEpisode(movie_code, episode_number);
//
//        // Nếu không tìm thấy videoUrl, trả về 404
//        if (videoUrl == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(videoUrl);
//    }
}
