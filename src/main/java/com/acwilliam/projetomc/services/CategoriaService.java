package com.acwilliam.projetomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.repositories.CategoriaRepository;
import com.acwilliam.projetomc.services.exceptions.DataIntegrityException;
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
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}
}
