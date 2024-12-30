package cms.cms.service;

import cms.cms.dto.UserDto;
import cms.cms.model.User;

import cms.cms.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpSession session;

    @Override
    public User save(UserDto userDto) {
        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getPhone(),
                userDto.getEmail(),
                new Date(),
                userDto.getUpdated_at(),
                true,
                false
        );
        return userRepository.save(user);
    }

    @Override
    public User update(UserDto userDto) {
        // Cập nhật thông tin người dùng
        Optional<User> existingUser = userRepository.findById(userDto.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Cập nhật các trường thông tin cần thiết, ví dụ như tên, điện thoại, email, etc.
            user.setUsername(userDto.getUsername());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            user.setUpdated_at(new Date());

            // Chỉ thay đổi mật khẩu nếu có giá trị mới
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            return userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found for id " + userDto.getId());
        }
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findUserByUsername(email).isPresent();
    }

    @Override
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    @Override
    public User getUserInfo() {
        log.info("getUsername: " + getUserName());
        User user = userRepository.getUserInfoByUsername(getUserName());
        if (user != null) {
            log.info("session user: " + user);
            session.setAttribute("user", user);
        } else {
            log.warn("No user found for username: " + getUserName());
        }
        return user;
    }


    @Override
    public Page<User> getPage(String username , String email, String phone, Integer page, Integer pageSize ){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return userRepository.search(username,email,phone, pageable);
    }

    @Override
    public void deleteUserById(Long id){
        try {
            log.info("Id User cần xóa:"+id);
            userRepository.deleteUserById(id);
        } catch (Exception e){
            log.error("Lỗi xóa :"+e);
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
