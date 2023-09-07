package models;

public class Parking {
	private int num_parking;
	private int capacite;
	private String arrondissement;
	private String rue;
	private String nom_parking;
	
	public Parking(int num_parking, int capacite, String arrondissement, String rue, String nom_parking) {
		this.num_parking = num_parking;
		this.capacite = capacite;
		this.arrondissement = arrondissement;
		this.rue = rue;
		this.nom_parking = nom_parking;
	}
	
	public int getNum_parking() {
		return num_parking;
	}
	public void setNum_parking(int num_parking) {
		this.num_parking = num_parking;
	}
	public int getCapacite() {
		return capacite;
	}
	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	public String getArrondissement() {
		return arrondissement;
	}
	public void setArrondissement(String arrondissement) {
		this.arrondissement = arrondissement;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getNom_parking() {
		return nom_parking;
	}

	public void setNom_parking(String nom_parking) {
		this.nom_parking = nom_parking;
	}
	
	
	
}
