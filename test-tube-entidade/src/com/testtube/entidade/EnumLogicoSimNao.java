package com.testtube.entidade;

public enum EnumLogicoSimNao {
	SIM("Sim"),
	NAO("N�o");

	private String descricao;

	private EnumLogicoSimNao(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
