package models;

import java.util.Date;

public class Contrat {
	private int num_contrat;
	private Date date_contrat;
	private Date date_echeance;
	private int num_client;
	private String num_immatriculation;
	
	public Contrat(int num_contrat, Date date_contrat, Date date_echeance, int num_client, String num_immatriculation) {
		this.num_contrat = num_contrat;
		this.date_contrat = date_contrat;
		this.date_echeance = date_echeance;
		this.num_client = num_client;
		this.num_immatriculation = num_immatriculation;
	}
	public int getNum_contrat() {
		return num_contrat;
	}
	public void setNum_contrat(int num_contrat) {
		this.num_contrat = num_contrat;
	}
	public Date getDate_contrat() {
		return date_contrat;
	}
	public void setDate_contrat(Date date_contrat) {
		this.date_contrat = date_contrat;
	}
	public Date getDate_echeance() {
		return date_echeance;
	}
	public void setDate_echeance(Date date_echeance) {
		this.date_echeance = date_echeance;
	}
	public int getNum_client() {
		return num_client;
	}
	public void setNum_client(int num_client) {
		this.num_client = num_client;
	}
	public String getNum_immatriculation() {
		return num_immatriculation;
	}
	public void setNum_immatriculation(String num_immatriculation) {
		this.num_immatriculation = num_immatriculation;
	}
	
	
}
