package com.example.demo.Service;

import com.example.demo.DTO.EpisodeDto;
import com.example.demo.Repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;

//    // Lấy danh sách tập bằng Native Query
//    public List<EpisodeDto> getEpisodesByMovieCode(String movie_code) {
//        List<Object[]> results = episodeRepository.findEpisodesByMovieCode(movie_code);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return results.stream().map(result -> new EpisodeDto(
//                ((Number) result[0]).longValue(),      // id
//                ((Number) result[1]).intValue(),       // episode_number
//                (String) result[2],                    // description
//                result[3] != null
//                        ? dateFormat.format((Date) result[3])  // Chuyển đổi Timestamp thành String
//                        : null,                   // release_date
//                ((Number) result[4]).doubleValue(),    // duration
//                (String) result[5],                    // video_url
//                (Boolean) result[6],                   // status
//                (Date) result[7],                      // created_at
//                (Date) result[8],                      // updated_at
//                (String) result[9],                    // movie_code
//                (String) result[10]                    // movie_name
//        )).collect(Collectors.toList());
//    }
//
//    public String getVideoUrlByEpisode(String movie_code, Integer episode_number) {
//        // Sử dụng truy vấn Native Query để lấy danh sách các episode
//        List<Object[]> results = episodeRepository.findVideoUrl(movie_code, episode_number);
//
//        // Nếu không tìm thấy kết quả, trả về null
//        if (results.isEmpty()) {
//            return null;
//        }
//
//        // Giả sử kết quả trả về là một danh sách với một phần tử duy nhất
//        Object[] result = results.get(0);
//
//        // Chuyển đổi kết quả thành EpisodeDto
//        String videoUrl = (String) result[5];  // video_url nằm ở vị trí thứ 5 trong mảng Object[]
//
//        // Trả về video_url
//        return videoUrl;
//    }
}
