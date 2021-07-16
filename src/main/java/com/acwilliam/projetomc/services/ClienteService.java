package com.acwilliam.projetomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Cliente;
import com.acwilliam.projetomc.repositories.ClienteRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}
