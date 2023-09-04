package api.techchallenge.domain.useCases;

import api.techchallenge.domain.entities.Pedido;
import api.techchallenge.domain.enums.PedidoSortingOptions;
import api.techchallenge.domain.enums.StatusPagamento;
import api.techchallenge.domain.enums.StatusPedido;
import api.techchallenge.domain.exception.RecursoNaoEncontratoException;
import api.techchallenge.domain.ports.in.PedidoUseCasesPort;
import api.techchallenge.domain.ports.out.PedidoAdapterPort;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.text.MessageFormat.format;

public class PedidoUseCases implements PedidoUseCasesPort {

    private final PedidoAdapterPort pedidoAdapterPort;

    public PedidoUseCases(PedidoAdapterPort pedidoAdapterPort) {
        this.pedidoAdapterPort = pedidoAdapterPort;
    }

    @Override
    public List<Pedido> buscarPedidos(Optional<PedidoSortingOptions> sortingProperty, Optional<Sort.Direction> direction) {
        return this.pedidoAdapterPort.buscarPedidos(sortingProperty, direction);
    }

    @Override
    public List<Pedido> buscarFilaDePedidos() {
        List<StatusPedido> statusPedidoList = new ArrayList<>();
        statusPedidoList.add(StatusPedido.PRONTO);
        statusPedidoList.add(StatusPedido.EM_PREPARACAO);
        statusPedidoList.add(StatusPedido.RECEBIDO);

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, PedidoSortingOptions.DATA_RECEBIMENTO.getString()));

        var pedidos = this.pedidoAdapterPort.buscarPedidosPorStatusPedido(statusPedidoList, Sort.by(orders));
        return ordenarPedidosPorStatus(pedidos);
    }

    private List<Pedido> ordenarPedidosPorStatus(List<Pedido> pedidos) {
        Collections.sort(pedidos, Comparator.comparing(Pedido::getStatusPedido));
        return pedidos;
    }

    @Override
    public Pedido buscarPedidoPorId(UUID id) throws RecursoNaoEncontratoException {
        return pedidoAdapterPort.buscarPedidoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontratoException(format("Registro não encontrado com código {0}", id)));
    }

    @Override
    public Pedido salvarPedido(Pedido pedido) {
        pedido.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));
        pedido.setDataAtualizacao(LocalDateTime.now(ZoneId.of("UTC")));

        if (pedido.getStatusPagamento() == null) {
            pedido.setStatusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
        }
        if (pedido.getStatusPedido() == null) {
            pedido.setStatusPedido(StatusPedido.NAO_RECEBIDO);
        }

        return this.pedidoAdapterPort.salvarPedido(pedido);
    }

    @Override
    public Pedido atualizarStatusPedido(StatusPedido statusPedido, UUID id) {
        var pedido = this.buscarPedidoPorId(id);
        pedido.setDataAtualizacao(LocalDateTime.now(ZoneId.of("UTC")));
        pedido.setStatusPedido(statusPedido);
        return this.pedidoAdapterPort.salvarPedido(pedido);
    }

    @Override
    public void deletarPedido(UUID id) {
        this.pedidoAdapterPort.deletarPedido(id);
    }
}
