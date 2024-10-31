package co.usco.user_management.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.usco.user_management.models.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long>{
    
    Optional<RoleModel> findByRolename(String rolename);

}
