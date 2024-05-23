package io.github.normandoalmeida.vendas.service.impl;

import io.github.normandoalmeida.vendas.domain.entity.Cliente;
import io.github.normandoalmeida.vendas.domain.entity.ItemPedido;
import io.github.normandoalmeida.vendas.domain.entity.Pedido;
import io.github.normandoalmeida.vendas.domain.entity.Produto;
import io.github.normandoalmeida.vendas.domain.enums.StatusPedido;
import io.github.normandoalmeida.vendas.domain.repository.ClienteRepository;
import io.github.normandoalmeida.vendas.domain.repository.ItemPedidoRepository;
import io.github.normandoalmeida.vendas.domain.repository.PedidoRepository;
import io.github.normandoalmeida.vendas.domain.repository.ProdutoRepository;
import io.github.normandoalmeida.vendas.exception.PedidoNaoEncontradoException;
import io.github.normandoalmeida.vendas.exception.RegraNegocioException;
import io.github.normandoalmeida.vendas.rest.dto.ItemPedidoDTO;
import io.github.normandoalmeida.vendas.rest.dto.PedidoDTO;
import io.github.normandoalmeida.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itensPedidoRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCLiente = dto.getCliente();
        Cliente cliente = clienteRepository
                .findById(idCLiente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIAZADO);

        List<ItemPedido> itensPedido = converterItems(pedido, dto.getItens());
        repository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(
                        pedido -> {
                            pedido.setStatus(statusPedido);
                            return repository.save(pedido);
                        }
                ).orElseThrow(()-> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {

        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepository
                            .findById(idProduto)
                            .orElseThrow(()-> new RegraNegocioException("Código de produto inválidp"
                            +idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }



}
