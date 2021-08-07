package com.acwilliam.projetomc.services;

import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.acwilliam.projetomc.domain.Cliente;
import com.acwilliam.projetomc.repositories.ClienteRepository;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	private Random rand = new Random();
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;

	public void sendNewPassword(String email){
		 
		Cliente cliente = clienteRepository.findByEmail(email);
		if(Objects.isNull(cliente)) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassoword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	 }

	private String newPassoword() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt ==0) {//gera um digito
			return (char) (rand.nextInt(10) + 48);
		}
		else if(opt ==1) {//gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else {//gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}
	
	
}
