package api.techchallenge.application.controllers;

import api.techchallenge.application.presenters.mappers.ClienteMapper;
import api.techchallenge.application.presenters.mappers.GenericMapper;
import api.techchallenge.application.presenters.requests.cliente.AtualizarClienteRequest;
import api.techchallenge.application.presenters.requests.cliente.CriarClienteRequest;
import api.techchallenge.application.presenters.responses.cliente.CriarClienteResponse;
import api.techchallenge.domain.entities.Cliente;
import api.techchallenge.domain.exception.RecursoJaExisteException;
import api.techchallenge.domain.ports.in.ClienteServicePort;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteServicePort clienteServicePort;
    private final GenericMapper genericMapper;
    private final ClienteMapper clienteMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CriarClienteResponse> buscarClientes() {
        return clienteMapper.listClienteToListCriarClienteResponse(this.clienteServicePort.buscarClientes());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{clienteId}")
    public CriarClienteResponse buscarClientePorId(@PathVariable(value = "clienteId") String clienteId) throws UserPrincipalNotFoundException {
        return clienteMapper.clienteToCriarClienteResponse(this.clienteServicePort.buscarClientePorId(UUID.fromString(clienteId)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/porCpf/{cpf}")
    public CriarClienteResponse buscarClientePorCpf(@PathVariable(value = "cpf") String cpf) throws UserPrincipalNotFoundException {
        return clienteMapper.clienteToCriarClienteResponse(this.clienteServicePort.buscarClientePorCpf(cpf));
    }

    @PostMapping
    public CriarClienteResponse salvarCliente(@RequestBody @Valid CriarClienteRequest clienteRequest) throws RecursoJaExisteException {
        return clienteMapper.clienteToCriarClienteResponse(
                this.clienteServicePort.criarNovoCliente(clienteRequest.getNome(), clienteRequest.getEmail(), clienteRequest.getCpf())
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{clienteId}")
    public void deletar(@PathVariable(value = "clienteId") String clienteId) {
        this.clienteServicePort.deletarCliente(UUID.fromString(clienteId));
    }

    @PatchMapping(value = "/{clienteId}")
    public CriarClienteResponse atualizarCliente(
            @PathVariable(value = "clienteId") String clienteId,
            @RequestBody @Valid AtualizarClienteRequest atualizarClienteRequest
    ) throws UserPrincipalNotFoundException {
        var cliente = genericMapper.toTransform(atualizarClienteRequest, Cliente.class);
        return clienteMapper.clienteToCriarClienteResponse(this.clienteServicePort.atualizarCliente(Optional.of(cliente), UUID.fromString(clienteId)));
    }

}