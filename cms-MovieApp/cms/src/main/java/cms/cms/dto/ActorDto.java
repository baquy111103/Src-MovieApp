package cms.cms.dto;

import cms.cms.model.movie.Actor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ActorDto {
    private Long id;
    private String actor_code;
    private String name;
    private String bio;
    private String date_of_birth;
    private String avatar;
    private Boolean active;
    private Boolean status;
    private Date created_at;
    private Date updated_at;
    private Long created_by;
    private Long updated_by;
    private List<String> movieCodes;

    // Constructor đầy đủ
    public ActorDto(Long id, String actor_code, String name, String bio, String date_of_birth,
                    String avatar, Boolean active, Boolean status, Date created_at,
                    Date updated_at, Long created_by, Long updated_by, List<String> movieCodes) {
        this.id = id;
        this.actor_code = actor_code;
        this.name = name;
        this.bio = bio;
        this.date_of_birth = date_of_birth;
        this.avatar = avatar;
        this.active = active;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }


    public ActorDto(Actor actor) {
        this.id = actor.getId();
        this.actor_code = actor.getActor_code();
        this.name = actor.getName();
        this.bio = actor.getBio();
        this.date_of_birth = String.valueOf(actor.getDate_of_birth());
        this.avatar = actor.getAvatar();
        this.active = actor.getActive();
        this.status = actor.getStatus();
        this.created_at = actor.getCreated_at();
        this.updated_at = actor.getUpdated_at();
        this.created_by = actor.getCreated_by();
        this.updated_by = actor.getUpdated_by();
    }
}