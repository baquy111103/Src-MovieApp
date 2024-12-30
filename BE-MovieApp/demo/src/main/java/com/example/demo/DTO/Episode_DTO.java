package com.example.demo.DTO;

import com.example.demo.Model.Episode;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Episode_DTO {
    private Integer episode_number;
    private String description;
    private String video_url;
    private Double duration;
    private Date release_date;
    private Boolean status;
    private String movie_code;
    private String movie_name;

    public Episode_DTO(Integer episode_number, String description, String video_url, Double duration, Date release_date, Boolean status, String movie_code, String movie_name) {
        this.episode_number = episode_number;
        this.description = description;
        this.video_url = video_url;
        this.duration = duration;
        this.release_date = release_date;
        this.status = status;
        this.movie_code = movie_code;
        this.movie_name = movie_name;
    }

    public Episode_DTO(Episode episode) {
        if (episode != null) {
            this.episode_number = episode.getEpisode_number();
            this.description = episode.getDescription();
            this.release_date = episode.getRelease_date();
            this.duration = episode.getDuration();
            this.video_url = episode.getVideo_url();
            this.status = episode.getStatus();
            this.movie_code = episode.getMovie() != null ? episode.getMovie().getMovie_code() : null;
            this.movie_name = episode.getMovie() != null ? episode.getMovie().getMovie_name() : null;
        }
    }
}

