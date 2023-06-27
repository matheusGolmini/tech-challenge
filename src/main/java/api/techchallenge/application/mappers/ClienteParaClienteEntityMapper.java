package api.techchallenge.application.mappers;

import api.techchallenge.infrastructure.db.postgres.entity.ClienteEntity;
import api.techchallenge.domain.core.domain.Cliente;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClienteParaClienteEntityMapper {
    public ClienteEntity mapper(Cliente cliente){
        var clienteEntity = new ClienteEntity();
        BeanUtils.copyProperties(cliente, clienteEntity);
        return clienteEntity;
    }
}
