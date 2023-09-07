package models;

public class Sanction {
	private double montant;
	private int num_sanction;
	private int num_client;
	
	public Sanction(int num_sanction, double montant, int num_client) {
		this.montant = montant;
		this.num_sanction = num_sanction;
		this.num_client = num_client;
	}
	
	
	
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public int getNum_sanction() {
		return num_sanction;
	}
	public void setNum_sanction(int num_sanction) {
		this.num_sanction = num_sanction;
	}



	public int getNum_client() {
		return num_client;
	}



	public void setNum_client(int num_client) {
		this.num_client = num_client;
	}
	
	
}
