package models;

import java.util.Date;

public class Facture {
	private int num_facture;
	private Date date_facture;
	//private int num_contrat;
	private double montant;
	
	
	public Facture(int num_facture, Date date_facture, double montant) {
		this.num_facture = num_facture;
		this.date_facture = date_facture;
		this.montant = montant;
	}
		
	
	public int getNum_facture() {
		return num_facture;
	}
	public void setNum_facture(int num_facture) {
		this.num_facture = num_facture;
	}
	public Date getDate_facture() {
		return date_facture;
	}
	public void setDate_facture(Date date_facture) {
		this.date_facture = date_facture;
	}

	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
}
