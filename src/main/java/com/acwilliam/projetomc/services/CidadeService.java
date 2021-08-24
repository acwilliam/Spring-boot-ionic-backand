package com.acwilliam.projetomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Cidade;
import com.acwilliam.projetomc.repositories.CidadeRepository;


	@Service
	public class CidadeService {
		
		@Autowired
		private CidadeRepository repo;

		public List<Cidade> findByEstado(Integer estadoId) {
			return repo.findCidades(estadoId);
		}
	}