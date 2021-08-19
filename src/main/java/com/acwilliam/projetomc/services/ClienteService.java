package com.acwilliam.projetomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.acwilliam.projetomc.domain.Cidade;
import com.acwilliam.projetomc.domain.Cliente;
import com.acwilliam.projetomc.domain.Endereco;
import com.acwilliam.projetomc.domain.enums.Perfil;
import com.acwilliam.projetomc.domain.enums.TipoCliente;
import com.acwilliam.projetomc.dto.ClienteDTO;
import com.acwilliam.projetomc.dto.ClienteNewDTO;
import com.acwilliam.projetomc.repositories.ClienteRepository;
import com.acwilliam.projetomc.repositories.EnderecoRepository;
import com.acwilliam.projetomc.security.UserSS;
import com.acwilliam.projetomc.services.exceptions.AuthorizationException;
import com.acwilliam.projetomc.services.exceptions.DataIntegrityException;
import com.acwilliam.projetomc.services.exceptions.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	

	@Autowired
	private ImagemService imagemService;
	
	@Value("${img.prefix.client.profile}")
	private String prefixo;
	
	@Value("${img.profile.size}")
	private Integer size;
	

	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(Objects.isNull(user) || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um cliente que possui pedidos relacionados");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction ){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}	
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getName(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getName(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),pe.encode(objDto.getSenha()));
		Cidade cid  = new Cidade(objDto.getCidadeId(),null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(),cli , cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setName(obj.getName());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (Objects.isNull(user)) {
			throw new AuthorizationException("Acesso negado");
		}

		BufferedImage jpgImage = imagemService.getJpgImageFromFile(multipartFile);
		String fileName = prefixo + user.getId() + ".jpg";

		return s3Service.uploadFile(imagemService.getInputStream(jpgImage, "jpg"), fileName, "imagem");
	}
}
