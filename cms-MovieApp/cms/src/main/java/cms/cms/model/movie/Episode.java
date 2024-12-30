package cms.cms.model.movie;

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
@Table(name = "episode")
public class Episode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name="episode_number")
    private Integer episode_number;

    @Column(name = "description")
    private String description;

    @Column(name ="release_date")
    private Date release_date;

    @Column(name = "duration")
    private Double duration;

    @Column (name = "video_url")
    private String video_url;

    @Column(name = "status")
    private Boolean status; //0 là xóa, 1 là hoạt động

    @Column(name ="created_at")
    private Date created_at;

    @Column(name ="updated_at")
    private Date updated_at;

    @ManyToOne
    @JoinColumn(name = "movie_code", referencedColumnName = "movie_code", nullable = false)
    private Movie movie;

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", episode_number=" + episode_number +
                ", description='" + description + '\'' +
                ", release_date=" + release_date +
                ", duration=" + duration +
                ", video_url='" + video_url + '\'' +
                ", status=" + status +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                "movie=" + (movie != null ? movie.getMovie_code() : "null") +
                '}';
    }
}
