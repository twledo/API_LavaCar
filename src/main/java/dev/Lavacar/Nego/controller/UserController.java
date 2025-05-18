package dev.Lavacar.Nego.controller;

import dev.Lavacar.Nego.model.Users;
import dev.Lavacar.Nego.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<Users> getAllUsers() {return userRepository.findAll();}

    @PostMapping
    public Users createdUser(@RequestBody Users user) {return userRepository.save(user);}

    @GetMapping("/{id}")
    public Users findUserByID(@PathVariable Long id) {return userRepository.findById(id).orElse(null);}

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        Users newUser = userRepository.findById(id).orElse(null);
        if (newUser != null) {
            newUser.setUsername(userDetails.getUsername());
            newUser.setPassword(userDetails.getPassword());
            return userRepository.save(newUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {userRepository.deleteById(id);}
}
