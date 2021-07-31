package com.acwilliam.projetomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acwilliam.projetomc.domain.ItemPedido;
import com.acwilliam.projetomc.domain.PagamentoComBoleto;
import com.acwilliam.projetomc.domain.Pedido;
import com.acwilliam.projetomc.domain.enums.EstadoPagamento;
import com.acwilliam.projetomc.repositories.ItemPedidoRepository;
import com.acwilliam.projetomc.repositories.PagamentoRepository;
import com.acwilliam.projetomc.repositories.PedidoRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService podutoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido itemPedido : obj.getItens()){
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(podutoService.find(itemPedido.getProduto().getId()));
			itemPedido.setPrice(itemPedido.getProduto().getPrice());
			itemPedido.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		System.out.println(obj);
		return obj;
	}
}
