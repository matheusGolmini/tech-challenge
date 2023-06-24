package api.techchallenge.infrastructure.db.postgres.repositories;

import api.techchallenge.infrastructure.db.postgres.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, UUID> { }
