package com.acwilliam.projetomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.repositories.CategoriaRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
}
