package com.util;

public class Arrematante {
	
	private int Id;
	private String nome;
	private double valorDinheiro;
	
	
	public Arrematante(){}
	
	public Arrematante(String nome, double valorDinheiro)
	{
		this.nome=nome;
		this.valorDinheiro=valorDinheiro;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getValorDinheiro() {
		return valorDinheiro;
	}
	public void setValorDinheiro(double valorDinheiro) {
		this.valorDinheiro = valorDinheiro;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	

}
