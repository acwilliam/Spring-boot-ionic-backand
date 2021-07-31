package com.acwilliam.projetomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.acwilliam.projetomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${deafault.enviar}")
	private String enviar;

	@Override
	public void emailDeConfirmacaoDePedido(Pedido obj) {
		SimpleMailMessage em = prepareSimpleMailMessageFromPedido(obj);
		envioEmail(em);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage em = new SimpleMailMessage();
		em.setTo(obj.getCliente().getEmail());
		em.setFrom(enviar);
		em.setSubject("Pedido Confirmado! Codigo " + obj.getId());
		em.setSentDate(new Date(System.currentTimeMillis()));
		em.setText(obj.toString());	
		return em;
	}
}
