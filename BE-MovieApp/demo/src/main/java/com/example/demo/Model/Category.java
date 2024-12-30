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
@Table(name = "category")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category_code", unique = true)
    private String category_code;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name = "update_by")
    private Long update_by;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name ="updated_at")
    private Date updated_at;

    @Column(name ="status")
    private Boolean status; //0 là xóa, 1 là hoạt động
}
