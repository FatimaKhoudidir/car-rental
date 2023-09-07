package models;

public class User{
	private int num_utilisateur;
	private String cin;
	private String nomPrenom;
	private String phone;
	private String gmail;
	private String password;
	private boolean suspend;

	public User(int num_utilisateur, String cin, String nomPrenom, String phone, String gmail, String password, boolean suspend ) {
		this.num_utilisateur = num_utilisateur;
		this.cin = cin;
		this.nomPrenom = nomPrenom;
		this.phone = phone;
		this.gmail = gmail;
		this.password = password;
		this.suspend = suspend;
	}

	public boolean isSuspend() {
		return suspend;
	}

	public void setSuspend(boolean suspend) {
		this.suspend = suspend;
	}
	
	public int getNum_utilisateur() {
		return num_utilisateur;
	}
	public void setNum_utilisateur(int num_utilisateur) {
		this.num_utilisateur = num_utilisateur;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
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
	public String getGmail() {
		return gmail;
	}
	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
