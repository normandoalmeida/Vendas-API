package io.github.normandoalmeida.vendas.service;

import io.github.normandoalmeida.vendas.domain.entity.Pedido;
import io.github.normandoalmeida.vendas.domain.enums.StatusPedido;
import io.github.normandoalmeida.vendas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
