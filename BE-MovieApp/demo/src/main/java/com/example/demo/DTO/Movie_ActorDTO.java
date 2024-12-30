package com.example.demo.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie_ActorDTO {
    private String actor_code;
    private String actor_name;// Lấy thêm tên diễn viên từ bảng Actor
    private String avatar;
    private Boolean status;
}
