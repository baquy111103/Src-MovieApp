package cms.cms.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class MovieDto {
    private String movie_code;
    private String movie_name;
    private String user_phone;
    private String description;
    private Date created_at;
    private Date updated_at;
    private Date release_date;
    private Double duration;
    private String category_id;
    private Boolean type; // 0 là movie , 1 là series
    private String image_url;
    private String video_url; // nếu là movie;
    private Boolean status; // 0 là xóa, 1 là hoạt động
    private String movie_genre;
    private int censorship;
    private String language;


}
