package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie_actor")
public class Movie_actor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "movie_code")
    private String movie_code;

    @Column(name = "actor_code")
    private String actor_code;

    @ManyToOne
    @JoinColumn(name = "movie_code", referencedColumnName = "movie_code", insertable = false, updatable = false)
    @JsonBackReference
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "actor_code", referencedColumnName = "actor_code", insertable = false, updatable = false)
    @JsonIgnore
    private Actor actor;

    @Override
    public String toString() {
        return "Movie_actor{" +
                "id=" + id +
                ", movie_code='" + movie_code + '\'' +
                ", actor_code='" + actor_code + '\'' +
                '}';
    }
}

