package com.util.database.pojos;

public class Objeto {
	
	private int id;
	private String nome;
	private Byte foto;
	private double lanceInicialValor;
	private double valorIncremento;
	
	
	public Objeto(){}
	public Objeto(int id, String nome,double lanceInicialValor,double valorIncremento)
	{
		this.id=id;
		this.nome=nome;
		this.lanceInicialValor=lanceInicialValor;
		this.valorIncremento=valorIncremento;
		
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
	public double getLanceInicialValor() {
		return lanceInicialValor;
	}
	public void setLanceInicialValor(double lanceInicialValor) {
		this.lanceInicialValor = lanceInicialValor;
	}
	public double getValorIncremento() {
		return valorIncremento;
	}
	public void setValorIncremento(double valorIncremento) {
		this.valorIncremento = valorIncremento;
	}
	

}
