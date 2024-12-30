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
@Table(name = "mov_banner")
public class Mov_banner implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name ="movie_banner_code")
    private String movie_banner_code;

    @Column(name ="banner_image")
    private String banner_image;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "status")
    private Boolean status;

    @Column(name ="position")
    private Integer position;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name ="updated_by")
    private Long updated_by;
}
