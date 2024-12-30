package cms.cms.service.user;

import cms.cms.model.User;
import cms.cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to find user with username: {}", username);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User with username '{}' not found", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User with username '{}' found: {}", username, user);

        return new CustomUserDetail(user);
    }

}
