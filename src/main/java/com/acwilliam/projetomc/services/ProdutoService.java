package com.acwilliam.projetomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.domain.Produto;
import com.acwilliam.projetomc.repositories.CategoriaRepository;
import com.acwilliam.projetomc.repositories.ProdutoRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class ProdutoService {
	
	@Autowired//instanciamos os objetos no spring usando anotação
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> procurar(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction ){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNameContainingAndCategoriasIn(nome, categorias, pageRequest);
		
	}
}
