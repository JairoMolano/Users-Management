package co.usco.user_management.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.usco.user_management.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{

    Optional<UserModel> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
}
