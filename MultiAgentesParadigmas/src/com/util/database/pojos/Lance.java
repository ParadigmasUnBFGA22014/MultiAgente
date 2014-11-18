package com.util.database.pojos;

import jade.util.leap.Serializable;

public class Lance  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int id_objeto;
	private int id_arrematande;
	private double valorLance;
	
	public Lance(){}
	public Lance(int id,int id_objeto,int id_arrematante,double valorLance)
	{
		this.setId(id);
		this.setId_objeto(id_objeto);
		this.setId_arrematande(id_arrematante);
		this.setValorLance(valorLance);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_objeto() {
		return id_objeto;
	}
	public void setId_objeto(int id_objeto) {
		this.id_objeto = id_objeto;
	}
	public int getId_arrematande() {
		return id_arrematande;
	}
	public void setId_arrematande(int id_arrematande) {
		this.id_arrematande = id_arrematande;
	}
	public double getValorLance() {
		return valorLance;
	}
	public void setValorLance(double valorLance) {
		this.valorLance = valorLance;
	}
	
	

}
