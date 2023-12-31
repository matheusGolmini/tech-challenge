package api.techchallenge.infrastructure.db.repositories;

import api.techchallenge.infrastructure.db.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {
    List<ItemEntity> findByPedidoId(UUID pedidoId);
    List<ItemEntity> findByIdIn(List<UUID> ids);
}