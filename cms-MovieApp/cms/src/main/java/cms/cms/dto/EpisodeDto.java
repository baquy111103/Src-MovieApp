package cms.cms.dto;

import cms.cms.model.movie.Episode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EpisodeDto {
    private Long id;
    private Integer episode_number;
    private String description;
    private String release_date;
    private Double duration;
    private String video_url;
    private Boolean status; // 0 là xóa, 1 là hoạt động
    private Date created_at;
    private Date updated_at;
    private String movie_code;
    private String movie_name;

    public EpisodeDto(Long id, Integer episode_number, String description, String release_date, Double duration,
                      String video_url, Boolean status, Date created_at, Date updated_at, String movie_code, String movie_name) {
        this.id = id;
        this.episode_number = episode_number;
        this.description = description;
        this.release_date = release_date;
        this.duration = duration;
        this.video_url = video_url;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.movie_code = movie_code;
        this.movie_name = movie_name;
    }

    public EpisodeDto(Episode episode) {
        if (episode != null) {
            this.id = episode.getId();
            this.episode_number = episode.getEpisode_number();
            this.description = episode.getDescription();
            this.release_date = String.valueOf(episode.getRelease_date());
            this.duration = episode.getDuration();
            this.video_url = episode.getVideo_url();
            this.status = episode.getStatus();
            this.created_at = episode.getCreated_at();
            this.updated_at = episode.getUpdated_at();
            this.movie_code = episode.getMovie() != null ? episode.getMovie().getMovie_code() : null;
            this.movie_name = episode.getMovie() != null ? episode.getMovie().getMovie_name() : null;
        }
    }

}
