package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;

public class GestionUserController implements Initializable{
		
	Connection cnx = null;
	public PreparedStatement st;
	public ResultSet resultat;
	
	 @FXML
	    private TextField txt_search_user;

	    @FXML
	    private TextField txt_nom_complet;

	    @FXML
	    private TextField txt_num_utilisateur;

	    @FXML
	    private TextField txt_cin;

	    @FXML
	    private TextField txt_phone;

	    @FXML
	    private TextField txt_gmail;

	    @FXML
	    private TextField txt_password;

	    @FXML
	    private TableView<User> table_user;

	    @FXML
	    private TableColumn<User, Integer> num_utilisateur_col;

	    @FXML
	    private TableColumn<User, String> nom_complet_col;

	    @FXML
	    private TableColumn<User, String> cin_col;

	    @FXML
	    private TableColumn<User, String> phone_col;

	    @FXML
	    private TableColumn<User, String> gmail_col;

	    @FXML
	    private TableColumn<User, String> password_col;
	    
	    @FXML
	    private TableColumn<User, Boolean> suspend_col;
	    
	    public ObservableList<User> listUser = FXCollections.observableArrayList();

	    @FXML
	    void AddUser() {
	    	
	    	String nom_complet = txt_nom_complet.getText();// to converti txt_num_utilisateur to integer
	    	String cin = txt_cin.getText();
	    	String gmail = txt_gmail.getText();
	    	String password = txt_password.getText();
	    	String phone = txt_phone.getText();
	    	
	    	String requet = "insert into user (nom_complet, cin, gmail, phone, password) values(?,?,?,?,?)";
	    	try {
				st = cnx.prepareStatement(requet);
				st.setString(1, nom_complet);
				st.setString(2, cin);
				st.setString(3, gmail);
				st.setString(4, phone);
				st.setString(5, password);
				st.execute();
				txt_nom_complet.setText("");
				txt_num_utilisateur.setText("");
				txt_cin.setText("");
				txt_gmail.setText("");
				txt_phone.setText("");
				txt_password.setText("");
				txt_search_user.setText("");
				AfficherUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }

	    @FXML
	    void AfficherLigne() {
	    	User user = table_user.getSelectionModel().getSelectedItem();
	    	if(user != null) {
	    		String requet = "select * from user where nom_complet = ?";
		    	try {
					st = cnx.prepareStatement(requet);
					st.setString(1, user.getNomPrenom());
					resultat = st.executeQuery();
					if(resultat.next()) {
						txt_nom_complet.setText(resultat.getString("nom_complet"));
						txt_num_utilisateur.setText(resultat.getString("num_utilisateur"));
						txt_cin.setText(resultat.getString("cin"));
						txt_gmail.setText(resultat.getString("gmail"));
						txt_phone.setText(resultat.getString("phone"));
						txt_password.setText(resultat.getString("password"));
						txt_search_user.setText(resultat.getString("nom_complet"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    	}
	    }

	    @FXML
	    void ChercherUser() {
	    	String requet = "select * from user where nom_complet = '"+txt_search_user.getText()+"'";
	    	try {
				st = cnx.prepareStatement(requet);
				resultat = st.executeQuery();
				if(resultat.next()) {
					txt_nom_complet.setText(resultat.getString("nom_complet"));
					txt_num_utilisateur.setText(resultat.getString("num_utilisateur"));
					txt_cin.setText(resultat.getString("cin"));
					txt_gmail.setText(resultat.getString("gmail"));
					txt_phone.setText(resultat.getString("phone"));
					txt_password.setText(resultat.getString("password"));
					txt_search_user.setText(resultat.getString("nom_complet"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void DeleteUser() {
	    	String requet = "delete from user where nom_complet = '"+txt_search_user.getText()+"'";
	    	try {
				st = cnx.prepareStatement(requet);
				st.executeUpdate();
				txt_nom_complet.setText("");
				txt_num_utilisateur.setText("");
				txt_cin.setText("");
				txt_gmail.setText("");
				txt_phone.setText("");
				txt_password.setText("");
				txt_search_user.setText("");
				AfficherUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void EditUser() {
	    	String nom_complet = txt_nom_complet.getText();
	    	int num_user = Integer.parseInt(txt_num_utilisateur.getText()); // to converti txt_num_utilisateur to integer
	    	String cin = txt_cin.getText();
	    	String gmail = txt_gmail.getText();
	    	String password = txt_password.getText();
	    	String phone = txt_phone.getText();
	    	
	    	String requet = "update user set nom_complet=?, num_utilisateur=?, cin=?, gmail=?, password=?, phone=? where nom_complet = '"+txt_search_user.getText()+"'";
	    	try {
				st = cnx.prepareStatement(requet);
				st.setString(1, nom_complet);
				st.setInt(2, num_user);
				st.setString(3, cin);
				st.setString(4, gmail);
				st.setString(5, password);
				st.setString(6, phone);
				st.executeUpdate();
				txt_nom_complet.setText("");
				txt_num_utilisateur.setText("");
				txt_cin.setText("");
				txt_gmail.setText("");
				txt_phone.setText("");
				txt_password.setText("");
				txt_search_user.setText("");
				AfficherUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }

	    @FXML
	    void SuspendUser() {
	    	String requet = "update user set suspend=? where nom_complet = '"+txt_search_user.getText()+"'";
	    	
	    	try {
	    		st = cnx.prepareStatement(requet);
				boolean suspend = resultat.getBoolean("suspend");
				if(suspend) {
					st.setBoolean(1, false);
				}
				else
					st.setBoolean(1, true);
				st.executeUpdate();
				AfficherUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    void AfficherUsers() {
	    	table_user.getItems().clear();
	    	String requet = "select * from user";
	    	
	    	try {
				st = cnx.prepareStatement(requet);
				resultat = st.executeQuery();
				while(resultat.next()) {
					listUser.add(new User( resultat.getInt("num_utilisateur"),  resultat.getString("cin"),  resultat.getString("nom_complet"), 
							resultat.getString("phone"),  resultat.getString("gmail"),  resultat.getString("password"), resultat.getBoolean("suspend") ));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	num_utilisateur_col.setCellValueFactory(new PropertyValueFactory<User, Integer>("num_utilisateur"));
	    	cin_col.setCellValueFactory(new PropertyValueFactory<User, String>("cin"));
	    	nom_complet_col.setCellValueFactory(new PropertyValueFactory<User, String>("nomPrenom"));
	    	phone_col.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
	    	gmail_col.setCellValueFactory(new PropertyValueFactory<User, String>("gmail"));
	    	password_col.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
	    	suspend_col.setCellValueFactory(new PropertyValueFactory<User ,Boolean>("suspend"));
	    	table_user.setItems(listUser);
	    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherUsers();
		
	}

}
