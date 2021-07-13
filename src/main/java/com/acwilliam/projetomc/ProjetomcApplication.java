package com.acwilliam.projetomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acwilliam.projetomc.domain.Categoria;
import com.acwilliam.projetomc.repositories.CategoriaRepository;

@SpringBootApplication
public class ProjetomcApplication implements CommandLineRunner {
	
	@Autowired
	CategoriaRepository categoriaRespository;

	public static void main(String[] args) {
		SpringApplication.run(ProjetomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		categoriaRespository.saveAll(Arrays.asList(cat1, cat2));
		
	}
	
	
}
