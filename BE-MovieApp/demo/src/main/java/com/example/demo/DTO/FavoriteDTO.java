package com.example.demo.DTO;

import com.example.demo.Model.Favorite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteDTO {
    private Boolean active;
    private String favorite_day;
    private String unfavorite_day;
    private String movie_code;
    private String movie_name;
    private Date release_date;
    private String image_url;
    private String movie_genre;
    private Boolean type;
    private Double duration;

    public FavoriteDTO(Boolean active,String favorite_day, String unfavorite_day,String movie_code,String movie_name,Date release_date,String image_url,String movie_genre,Boolean type,Double duration){
        this.active = active;
        this.favorite_day = favorite_day;
        this.unfavorite_day = unfavorite_day;
        this.movie_code = movie_code;
        this.movie_name = movie_name;
        this.release_date = release_date;
        this.image_url = image_url;
        this.movie_genre = movie_genre;
        this.type = type;
        this.duration = duration;
    }

    public FavoriteDTO(Favorite favorite){
        if(favorite != null){
            this.active = favorite.getActive();
            this.favorite_day = String.valueOf(favorite.getFavorite_day());
            this.unfavorite_day = String.valueOf(favorite.getUnfavorite_day());
            this.movie_code = favorite.getMovie() != null ? favorite.getMovie().getMovie_code() : null;
            this.movie_name = favorite.getMovie() != null ? favorite.getMovie().getMovie_name() : null;
            this.release_date = favorite.getMovie() != null ? favorite.getMovie().getRelease_date() : null;
            this.image_url = favorite.getMovie() != null ? favorite.getMovie().getImage_url() : null;
            this.movie_genre = favorite.getMovie() != null ? favorite.getMovie().getMovie_genre() : null;
            this.type = favorite.getMovie() != null ? favorite.getMovie().getType() : null;
            this.duration = favorite.getMovie() != null ? favorite.getMovie().getDuration() : null;
        }
    }
}
