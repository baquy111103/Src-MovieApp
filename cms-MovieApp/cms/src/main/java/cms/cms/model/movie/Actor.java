package cms.cms.model.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "actor")
public class Actor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name ="actor_code", unique = true)
    private String actor_code;

    @Column(name ="name")
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "avatar")
    private String avatar;

    @Column(name= "active")
    private Boolean active; //0 là xóa, 1 là hoạt động

    @Column(name = "status")
    private Boolean status; //0 là diễn viên, 1 là tác giả

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_by")
    private Long created_by;

    @Column(name ="updated_by")
    private Long updated_by;

    @JsonIgnore  // Ngừng tuần tự hóa mối quan hệ này
    @OneToMany(mappedBy = "actor")
    private List<Movie_actor> movieActors;

}
