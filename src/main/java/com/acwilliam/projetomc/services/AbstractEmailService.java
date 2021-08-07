package com.acwilliam.projetomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.acwilliam.projetomc.domain.Cliente;
import com.acwilliam.projetomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${deafault.enviar}")
	private String enviar;

	@Autowired
	private TemplateEngine templeteEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void emailDeConfirmacaoDePedido(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		envioEmail(sm);
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

	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context();
		context.setVariable("pedido", obj);
		return templeteEngine.process("email/confirmacaoPedido", context);
	}

	@Override
	public void emailDeConfirmacaoHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			envioHtmlEmail(mm);
		} catch (MessagingException e) {
			emailDeConfirmacaoDePedido(obj);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(enviar);
		mmh.setSubject("Pedido Confirmado! Codigo " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mimeMessage;
	}

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage em = prepareNewPassWordEamil(cliente, newPass);
		envioEmail(em);
	}

	protected SimpleMailMessage prepareNewPassWordEamil(Cliente cliente, String newPass) {
		SimpleMailMessage em = new SimpleMailMessage();
		em.setTo(cliente.getEmail());
		em.setFrom(enviar);
		em.setSubject("Solicitação de nova senha ");
		em.setSentDate(new Date(System.currentTimeMillis()));
		em.setText("Nova Senha: " + newPass);
		return em;
	}
}
