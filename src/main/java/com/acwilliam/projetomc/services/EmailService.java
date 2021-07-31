package com.acwilliam.projetomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.acwilliam.projetomc.domain.Pedido;

public interface EmailService {
	
	void emailDeConfirmacaoDePedido(Pedido obj);
	
	void envioEmail(SimpleMailMessage msg);
		
	
}
