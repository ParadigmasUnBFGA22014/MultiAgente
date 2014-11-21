package com.util.database.pojos;

import jade.util.leap.Serializable;

public class Objeto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nome;

	
	public Objeto(){}
	public Objeto(int id, String nome)
	{
		this.id=id;
		this.nome=nome;
		
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
