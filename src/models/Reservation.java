package models;

import java.util.Date;

public class Reservation {
	private int num_reservation;
	private Date date_reservation;
	private Date date_depart;
	private Date date_retour;
	private String num_immatriculation;
	private int num_client;
	private boolean activated;
	
	
	public Reservation(int num_reservation, Date date_reservation, Date date_depart, Date date_retour, 
			String num_immatriculation, int num_client, boolean activated) {
		this.num_reservation = num_reservation;
		this.date_reservation = date_reservation;
		this.date_depart = date_depart;
		this.date_retour = date_retour;
		this.num_immatriculation = num_immatriculation;
		this.num_client = num_client;
		this.activated = activated;
	}
	
	public int getNum_reservation() {
		return num_reservation;
	}
	public void setNum_reservation(int num_reservation) {
		this.num_reservation = num_reservation;
	}
	public Date getDate_reservation() {
		return date_reservation;
	}
	public void setDate_reservation(Date date_reservation) {
		this.date_reservation = date_reservation;
	}
	public Date getDate_depart() {
		return date_depart;
	}
	public void setDate_depart(Date date_depart) {
		this.date_depart = date_depart;
	}
	public Date getDate_retour() {
		return date_retour;
	}
	public void setDate_retour(Date date_retour) {
		this.date_retour = date_retour;
	}

	public String getNum_immatriculation() {
		return num_immatriculation;
	}

	public void setNum_immatriculation(String num_immatriculation) {
		this.num_immatriculation = num_immatriculation;
	}

	public int getNum_client() {
		return num_client;
	}

	public void setNum_client(int num_client) {
		this.num_client = num_client;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	
}
