package cms.cms.dto;

import cms.cms.model.movie.Mov_banner;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Mov_bannerDto {
    private Long id;
    private String movie_banner_code;
    private String banner_image;
    private String title;
    private String start_date;
    private String end_date;
    private Boolean status; //0 là xóa, 1 là hoạt động
    private Integer position;
    private Date created_at;
    private Date updated_at;
    private Long created_by;
    private Long updated_by;

    public Mov_bannerDto(Mov_banner banner) {
        this.id = banner.getId();
        this.movie_banner_code = banner.getMovie_banner_code();
        this.banner_image = banner.getBanner_image();
        this.title = banner.getTitle();
        this.start_date = String.valueOf(banner.getStart_date());
        this.end_date = String.valueOf(banner.getEnd_date());
        this.status = banner.getStatus();
        this.position = banner.getPosition();
        this.created_at = banner.getCreated_at();
        this.updated_at = banner.getUpdated_at();
        this.created_by = banner.getCreated_by();
    }
}
