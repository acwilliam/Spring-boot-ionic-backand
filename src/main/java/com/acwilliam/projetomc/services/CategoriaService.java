package com.acwilliam.projetomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}

}
