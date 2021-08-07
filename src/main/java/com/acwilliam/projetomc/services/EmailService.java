package com.acwilliam.projetomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.acwilliam.projetomc.domain.Cliente;
import com.acwilliam.projetomc.domain.Pedido;

public interface EmailService {
	
	void emailDeConfirmacaoDePedido(Pedido obj);
	
	void envioEmail(SimpleMailMessage msg);
	
	void emailDeConfirmacaoHtmlEmail(Pedido obj);
	
	void envioHtmlEmail(MimeMessage msg);
		
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
