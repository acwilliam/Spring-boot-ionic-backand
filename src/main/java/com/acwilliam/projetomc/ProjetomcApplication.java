package com.acwilliam.projetomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.domain.Produto;
import com.acwilliam.projetomc.repositories.CategoriaRepository;
import com.acwilliam.projetomc.repositories.ProdutoRepository;

@SpringBootApplication
public class ProjetomcApplication implements CommandLineRunner {
	
	@Autowired
	CategoriaRepository categoriaRespository;//dependencia
	
	@Autowired
	ProdutoRepository produtoRepository;//dependencia

	public static void main(String[] args) {
		SpringApplication.run(ProjetomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		
		//associação categorias e produtos
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat1.getProdutos().addAll(Arrays.asList(p2));
		//associação podutos e categorias
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRespository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
	}
	
	
}
