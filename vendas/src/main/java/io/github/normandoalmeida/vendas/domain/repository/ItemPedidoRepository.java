package io.github.normandoalmeida.vendas.domain.repository;

import io.github.normandoalmeida.vendas.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}
