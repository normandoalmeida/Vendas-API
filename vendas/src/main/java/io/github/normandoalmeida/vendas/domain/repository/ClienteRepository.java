package io.github.normandoalmeida.vendas.domain.repository;

import io.github.normandoalmeida.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
