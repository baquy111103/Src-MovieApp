package com.example.demo.DTO;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String movie_code;
    private String movie_name;
    private String description;
    private Date release_date;
    private Double duration;
    private String image_url;
    private String video_url;
    private Boolean status;
    private String language;
    private String movie_genre;
    private Integer censorship;
    private String category_id;
    private Boolean type;
    private List<Episode_DTO> episodes;
    private List<Movie_ActorDTO> movieActors;
}

