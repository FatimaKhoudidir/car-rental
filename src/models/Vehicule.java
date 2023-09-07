package models;

import java.util.Date;

public class Vehicule {
	private String num_immatriculation;
	private String marque;
	private String type;
	private String carburant;
	private int compteur;
	private Date date_circulation;
	private double prix;
	private boolean available;
	private int num_utilisateur;
	private int num_parking;
	
	public Vehicule(String num_immatriculation, String marque, String type, String carburant, int compteur,
			Date date_circulation, double prix, boolean available, int num_utilisateur, int num_parking) {
 		this.num_immatriculation = num_immatriculation;
		this.marque = marque;
		this.type = type;
		this.carburant = carburant;
		this.compteur = compteur;
		this.date_circulation = date_circulation;
		this.prix = prix;
		this.available = available;
		this.num_utilisateur = num_utilisateur;
		this.num_parking = num_parking; 
	}
	
	
	public Vehicule(String num_immatriculation, String marque) {
		super();
		this.num_immatriculation = num_immatriculation;
		this.marque = marque;
	}



	public String getNum_immatriculation() {
		return num_immatriculation;
	}
	public void setNum_immatriculation(String num_immatriculation) {
		this.num_immatriculation = num_immatriculation;
	}
	public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCarburant() {
		return carburant;
	}
	public void setCarburant(String carburant) {
		this.carburant = carburant;
	}
	public int getCompteur() {
		return compteur;
	}
	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
	public Date getDate_circulation() {
		return date_circulation;
	}
	public void setDate_circulation(Date date_circulation) {
		this.date_circulation = date_circulation;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}


	public int getNum_utilisater() {
		return num_utilisateur;
	}


	public void setNum_utilisater(int num_utilisater) {
		this.num_utilisateur = num_utilisater;
	}


	public int getNum_parking() {
		return num_parking;
	}


	public void setNum_parking(int num_parking) {
		this.num_parking = num_parking;
	}
	
	
}
