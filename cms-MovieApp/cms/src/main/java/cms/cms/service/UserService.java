package cms.cms.service;

import cms.cms.dto.UserDto;
import cms.cms.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User save(UserDto userDto);
    boolean emailExists(String email);

    User getUserInfo();

    String getUserName();

    Page<User> getPage(String username , String email, String phone, Integer page, Integer pageSize );

    void deleteUserById(Long id);

    User findById(Long id);

    User update(UserDto userDto);

}
