package dev.Lavacar.Nego.service;

import dev.Lavacar.Nego.model.Users;
import dev.Lavacar.Nego.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEnconder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEnconder) {
        this.userRepository = userRepository;
        this.passwordEnconder = passwordEnconder;
    }

    //Post
    public Users registerUser(String username, String password) {
        String passwordEncrypted = passwordEnconder.encode(password);
        Users user = new Users(username, passwordEncrypted);
        return userRepository.save(user);
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
