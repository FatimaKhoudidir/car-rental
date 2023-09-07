package models;

import java.util.Date;

public class Client {
	private int num_client;
	private String cin;
	private String nomPrenom;
	private String phone;
	private Date dateNaissance;
	private String ville;
	private String adresse;
	private int num_utilisateur;
	
	public Client(int num_client, String cin, String nomPrenom, String phone, Date dateNaissance, String ville, String adresse, int num_utilisateur) {
		this.num_client = num_client;
		this.cin = cin;
		this.nomPrenom = nomPrenom;
		this.phone = phone;
		this.dateNaissance = dateNaissance;
		this.ville = ville;
		this.adresse = adresse;
		this.num_utilisateur = num_utilisateur;
	}

	public int getNum_client() {
		return num_client;
	}

	public void getNum_client(int num_client) {
		this.num_client = num_client;
	}

	public String getcin() {
		return cin;
	}

	public void setcin(String cin) {
		this.cin = cin;
	}

	public String getNomPrenom() {
		return nomPrenom;
	}

	public void setNomPrenom(String nomPrenom) {
		this.nomPrenom = nomPrenom;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getNum_utilisateur() {
		return num_utilisateur;
	}

	public void setNum_utilisateur(int num_utilisateur) {
		this.num_utilisateur = num_utilisateur;
	}
}
