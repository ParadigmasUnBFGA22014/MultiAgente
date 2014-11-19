package com.util.database.pojos;

import jade.util.leap.Serializable;

import java.util.ArrayList;

public class Lote  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer numeroLote;
	private ArrayList<Objeto> objetosList;
	
	private double lanceCorrente;
	private double lanceInicialValor;
	private double valorIncremento;
	
	public Lote(){}
	public Lote(Integer numeroLote,ArrayList<Objeto> objetosList, double lanceInicial,double valorIncremento)
	{
		this.setNumeroLote(numeroLote);
		this.setObjetosList(objetosList);
		this.lanceInicialValor=lanceInicialValor;
		this.valorIncremento=valorIncremento;
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
	public Integer getNumeroLote() {
		return numeroLote;
	}
	public void setNumeroLote(Integer numeroLote) {
		this.numeroLote = numeroLote;
	}
	public ArrayList<Objeto> getObjetosList() {
		return objetosList;
	}
	public void setObjetosList(ArrayList<Objeto> objetosList) {
		this.objetosList = objetosList;
	}
	public double getLanceCorrente() {
		return lanceCorrente;
	}
	public void setLanceCorrente(double lanceCorrente) {
		this.lanceCorrente = lanceCorrente;
	}

}
