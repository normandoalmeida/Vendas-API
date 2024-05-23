package io.github.normandoalmeida.vendas.rest.controller;

import io.github.normandoalmeida.vendas.domain.entity.ItemPedido;
import io.github.normandoalmeida.vendas.domain.entity.Pedido;
import io.github.normandoalmeida.vendas.domain.enums.StatusPedido;
import io.github.normandoalmeida.vendas.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.normandoalmeida.vendas.rest.dto.InfoItemPedidoDTO;
import io.github.normandoalmeida.vendas.rest.dto.InfoPedidoDTO;
import io.github.normandoalmeida.vendas.rest.dto.PedidoDTO;
import io.github.normandoalmeida.vendas.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@RequestBody AtualizacaoStatusPedidoDTO dto,
                             @PathVariable Integer id){
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));

    }

    @GetMapping("{id}")
    public InfoPedidoDTO getById(@PathVariable Integer id){
        return service.obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado"));
    }

    private InfoPedidoDTO converter(Pedido pedido) {
       return InfoPedidoDTO.builder()
                .codigo(pedido.getId())
                .nomeCliente(pedido.getCliente().getNome())
                .cpf(pedido.getCliente().getCpf())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InfoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InfoItemPedidoDTO.builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .quantidade(item.getQuantidade())
                        .precoUnitario(item.getProduto().getPreco()).build()
        ).collect(Collectors.toList());
    }

}


