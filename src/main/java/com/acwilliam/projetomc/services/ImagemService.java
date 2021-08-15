package com.acwilliam.projetomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.acwilliam.projetomc.domain.enums.ExtensaoEnum;
import com.acwilliam.projetomc.services.exceptions.FileException;

@Service
public class ImagemService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String extensao = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!ExtensaoEnum.PNG.getDescricao().equals(extensao) && !ExtensaoEnum.JPG.getDescricao().equals(extensao)){
			throw new FileException("Somente imagens PNG e JPG são permitidas!");
		}
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if(ExtensaoEnum.PNG.getDescricao().equals(extensao)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arqvuio!");
		}
		
	}

	private BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImagem = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImagem.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImagem;
	}
	
	public InputStream getInputStream(BufferedImage img, String extensao) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extensao, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch(IOException e) {
			throw new FileException("Error ao ler o arquivo");
		}
	}
	
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(
			sourceImg, 
			(sourceImg.getWidth()/2) - (min/2), 
			(sourceImg.getHeight()/2) - (min/2), 
			min, 
			min);		
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
