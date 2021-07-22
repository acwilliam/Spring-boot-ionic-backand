package com.acwilliam.projetomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Pedido;
import com.acwilliam.projetomc.repositories.PedidoRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private PedidoRepository repo;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
