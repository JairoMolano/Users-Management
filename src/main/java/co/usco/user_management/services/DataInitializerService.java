package co.usco.user_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.usco.user_management.models.PermissionModel;
import co.usco.user_management.models.RoleModel;
import co.usco.user_management.models.UserModel;
import co.usco.user_management.repositories.RoleRepository;
import co.usco.user_management.repositories.UserRepository;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DataInitializerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void initializeData() {
        
        // PERMISOS
        PermissionModel readPermission = PermissionModel.builder()
            .name("READ")
            .build();

        PermissionModel writePermission = PermissionModel.builder()
            .name("WRITE")
            .build();

            PermissionModel deletePermission = PermissionModel.builder()
            .name("DELETE")
            .build();

        PermissionModel adminPermission = PermissionModel.builder()
            .name("ADMIN")
            .build();

        // ROLES
        RoleModel roleUser = RoleModel.builder()
            .rolename("USER")
            .permissions(Set.of(readPermission, writePermission))
            .build();

        RoleModel roleAdmin = RoleModel.builder()
            .rolename("ADMIN")
            .permissions(Set.of(readPermission, writePermission, deletePermission, adminPermission))
            .build();

        roleRepository.saveAll(List.of(roleUser, roleAdmin));

        // USUARIOS
        UserModel userUsuario = UserModel.builder()
            .username("USER")
            .password(passwordEncoder.encode("1234"))
            .email("user@mail.com")
            .birthdate(LocalDate.of(2000, 2, 2))
            .createdHour(LocalTime.now())
            .postalCode("0000000")
            .roles(Set.of(roleUser))
            .build();
        
        UserModel userAdministrador = UserModel.builder()
            .username("ADMIN")
            .password(passwordEncoder.encode("1234"))
            .email("admin@mail.com")
            .birthdate(LocalDate.of(2000, 2, 2))
            .createdHour(LocalTime.now())
            .postalCode("0000000")
            .roles(Set.of(roleAdmin))
            .build();

        userRepository.saveAll(List.of(userUsuario, userAdministrador));
    }
}
