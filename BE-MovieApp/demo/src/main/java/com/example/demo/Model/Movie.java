package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "movie")
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "movie_code")
    private String movie_code;

    @Column(name = "movie_name")
    private String movie_name;

    @Column(name = "user_phone")
    private String user_phone;

    @Column(name ="description")
    private String description;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "release_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date release_date;

    @Column(name= "duration")
    private Double duration;

    @Column(name = "category_id")
    private String category_id;

    @Column(name = "type")
    private Boolean type; // 0 là movie , 1 là series

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "video_url")
    private String video_url;

    @Column(name = "status")
    private Boolean status; // 0 là xóa, 1 là hoạt động

    @Column(name ="movie_genre")
    private String movie_genre;

    @Column(name = "censorship")
    private Integer censorship;

    @Column(name = "language")
    private String language;

    @Column(name = "is_hot")
    private Integer is_hot;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Episode> episodes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Movie_actor> movieActors;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movie_code='" + movie_code + '\'' +
                ", movie_name='" + movie_name + '\'' +
                ", movieActorsSize=" + (movieActors != null ? movieActors.size() : 0) +
                '}';
    }
}
