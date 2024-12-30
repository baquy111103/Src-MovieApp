package com.example.demo.Model;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "favorite")
public class Favorite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "favorite_day")
    private Date favorite_day;

    @Column(name = "unfavorite_day")
    private Date unfavorite_day;

    @ManyToOne
    @JoinColumn(name = "movie_code", referencedColumnName = "movie_code", nullable = false)
    private Movie movie;

}