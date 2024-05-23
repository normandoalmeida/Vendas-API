package io.github.normandoalmeida.vendas.domain.repository;

import io.github.normandoalmeida.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
