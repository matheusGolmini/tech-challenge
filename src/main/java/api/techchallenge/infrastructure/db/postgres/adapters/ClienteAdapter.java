package api.techchallenge.infrastructure.db.postgres.adapters;


import api.techchallenge.domain.core.exception.RecursoNaoEncontratoException;
import api.techchallenge.infrastructure.db.postgres.entity.ClienteEntity;
import api.techchallenge.application.mappers.ClienteEntityParaClienteMapper;
import api.techchallenge.application.mappers.ClienteParaClienteEntityMapper;
import api.techchallenge.infrastructure.db.postgres.repositories.ClienteRepository;
import api.techchallenge.domain.core.domain.Cliente;
import api.techchallenge.domain.ports.out.ClienteAdapterPort;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import static java.text.MessageFormat.format;

@Component
@AllArgsConstructor
public class ClienteAdapter implements ClienteAdapterPort {
    private final ClienteRepository clienteRepository;

    private final ClienteEntityParaClienteMapper clienteEntityParaClienteMapper;

    private final ClienteParaClienteEntityMapper clienteParaClienteEntityMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientes() {
        var clientesEntity = this.clienteRepository.findAll();
        List<Cliente> clientes = new ArrayList<Cliente>();

        for (ClienteEntity clienteEntity: clientesEntity) {
            clientes.add(this.clienteEntityParaClienteMapper.mapper(clienteEntity));
        }
        return clientes;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Cliente> buscarClientePorId(UUID id) throws RecursoNaoEncontratoException {
        var clienteEntity = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontratoException(format("Registro não encontrado com código {0}", id)));

        return Optional.of(this.clienteEntityParaClienteMapper.mapper(clienteEntity));
    }

    @Transactional
    @Override
    public void deletarCliente(UUID id) {
        this.clienteRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Cliente salvarCliente(Cliente cliente) {
        var clienteEntity = this.clienteParaClienteEntityMapper.mapper(cliente);
        return clienteEntityParaClienteMapper.mapper(clienteRepository.save(clienteEntity));
    }
}
