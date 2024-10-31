package co.usco.user_management.services;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import co.usco.user_management.repositories.RoleRepository;
import co.usco.user_management.models.RoleModel;
import co.usco.user_management.models.UserModel;
import co.usco.user_management.repositories.UserRepository;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // GET ALL USERS SERVICE
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // GET USER BY USERNAME SERVICE
    public UserModel getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // GET USER BY ID SERVICE
    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // GET ALL USERS EXCLUDING ADMIN SERVICE
    public List<UserModel> getAllUsersExcludingAdmin() {
    return userRepository.findAll().stream()
        .filter(user -> user.getRoles().stream()
            .noneMatch(role -> role.getRolename().equals("ADMIN")))
        .collect(Collectors.toList());
    }


    // CREATE USER SERVICE
    public UserModel createUser(String username, String password, String email, String postalCode, String birthdate) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        RoleModel roleUser = roleRepository.findByRolename("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        UserModel newUser = UserModel.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .roles(Collections.singleton(roleUser))
            .birthdate(LocalDate.parse(birthdate))
            .email(email)
            .createdHour(LocalTime.now())
            .postalCode(postalCode)
            .build();
        return userRepository.save(newUser);
    }

    // UPDATE USER SERVICE
    public UserModel updateUser(Long id, String username, String email, String postalCode, String birthdate) {
        UserModel newUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            newUser.setUsername(username);
            newUser.setBirthdate(LocalDate.parse(birthdate));
            newUser.setEmail(email);
            newUser.setPostalCode(postalCode);
        return userRepository.save(newUser);
    }

    // DELETE USER SERVICE
    public void deleteUser(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.save(user);  
        userRepository.deleteById(id);  
    }

}
