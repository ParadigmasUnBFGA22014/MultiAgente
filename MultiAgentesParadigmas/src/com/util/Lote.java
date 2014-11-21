package com.util;

import jade.util.leap.Serializable;

import java.util.ArrayList;

public class Lote  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer numeroLote;
	private Objeto objeto;
	
	private double lanceCorrente;
	private double lanceInicialValor;
	private double valorIncremento;
	
	public Lote(){}
	public Lote(Integer numeroLote, double lanceInicial,double valorIncremento, Objeto objeto)
	{
		this.setNumeroLote(numeroLote);
		this.lanceInicialValor=lanceInicialValor;
		this.valorIncremento=valorIncremento;
		this.setObjeto(objeto);
		
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
	
	public double getLanceCorrente() {
		return lanceCorrente;
	}
	public void setLanceCorrente(double lanceCorrente) {
		this.lanceCorrente = lanceCorrente;
	}
	public Objeto getObjeto() {
		return objeto;
	}
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}

}
