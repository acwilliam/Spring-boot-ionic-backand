package com.acwilliam.projetomc.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExtensaoEnum {
	
	PNG("png"),
	JPG("JPG");
	
	private static final Map<String, ExtensaoEnum> LOOKUP = new HashMap<>();

	static {
		for (ExtensaoEnum e : ExtensaoEnum.values()) {
			LOOKUP.put(e.getDescricao(), e);
		}
	}
	
	private String descricao;

	private ExtensaoEnum(final String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
	

