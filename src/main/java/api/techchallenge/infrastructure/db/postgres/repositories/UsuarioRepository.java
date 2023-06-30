package api.techchallenge.infrastructure.db.postgres.repositories;

import api.techchallenge.infrastructure.db.postgres.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByUserName(String username);
}
