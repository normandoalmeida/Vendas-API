package io.github.normandoalmeida.vendas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cliente_id")
    private Integer id;

    @Column(length = 100)
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @Column(name="cpf", length = 12)
    @NotEmpty(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    private String cpf;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<Pedido> pedidos;

}
