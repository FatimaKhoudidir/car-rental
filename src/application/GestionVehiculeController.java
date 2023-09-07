package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Vehicule;

public class GestionVehiculeController implements Initializable{
	
	@FXML
    private TextField txt_searchVoiture;

    @FXML
    private TextField txt_num_immatriculation;

    @FXML
    private TextField txt_marque;

    @FXML
    private TextField txt_compteur;

    @FXML
    private DatePicker picker_date_circulation;

    @FXML
    private TextField txt_type;

    @FXML
    private ComboBox<Integer> box_num_parking;

    @FXML
    private TextField txt_carburant;


    @FXML
    private ComboBox<Boolean> box_available;
    
    @FXML
    private TextField txt_prix;
    
    @FXML
    private TextField txt_search_vehicule;

    @FXML
    private TableColumn<Vehicule, String> num_immatriculation_col;

    @FXML
    private TableColumn<Vehicule, String> marque_col;

    @FXML
    private TableColumn<Vehicule, String> type_col;

    @FXML
    private TableColumn<Vehicule, String> carburant_col;

    @FXML
    private TableColumn<Vehicule, Integer> compteur_col;

    @FXML
    private TableColumn<Vehicule, Date> date_circulation_col;

    @FXML
    private TableColumn<Vehicule, Integer> num_parking_col;

    @FXML
    private TableColumn<Vehicule, Boolean> available_col;
    
    @FXML
    private TableView<Vehicule> table_vehicule;
    
    Connection cnx = null;
    PreparedStatement st;
    ResultSet resultat;
    
    ObservableList<Vehicule> listVehicule = FXCollections.observableArrayList();
    
    
//    @FXML
//    void Get_num_parking() { // remplire le combox par les num de parking libre
//    	String requet = "select num_parking from parking";
//    	ObservableList<Integer> list_num_parking = FXCollections.observableArrayList();
//    	try {
//			st = cnx.prepareStatement(requet);
//			resultat = st.executeQuery();
//			while(resultat.next()) {
//				list_num_parking.add(resultat.getInt("num_parking"));
//			}
//			box_num_parking.getItems().addAll(list_num_parking);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    }
    
    @FXML
    void AddVoiture() {
    	String type = txt_type.getText();
    	String marque = txt_marque.getText();
    	String carburant = txt_carburant.getText();
    	String num_immatriculation = txt_num_immatriculation.getText();
    	int num_utilisateur = SignUserController.GetUser();
    	double prix = Double.parseDouble(txt_prix.getText());
    	int compteur = Integer.parseInt(txt_compteur.getText());
    	int num_parking = box_num_parking.getValue();
    	boolean available = box_available.getValue();
    	java.util.Date date_circulation = java.util.Date.from((picker_date_circulation.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date sqlDate = new java.sql.Date(date_circulation.getTime());
		String requet = "insert into vehicule (type,marque,carburant,num_immatriculation,prix,compteur,date_circulation,num_parking,available,num_utilisateur) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
    	try {
			st = cnx.prepareStatement(requet);
	    	st.setString(1, type);
	    	st.setString(2, marque);
	    	st.setString(3, carburant);
	    	st.setString(4, num_immatriculation);
	    	st.setDouble(5, prix);
	    	st.setInt(6, compteur);
	    	st.setDate(7, sqlDate);
	    	st.setInt(8, num_parking);
	    	st.setBoolean(9, available);
	    	st.setInt(10, num_utilisateur);
	    	st.executeUpdate();
	    	box_num_parking.setValue(null);
			txt_type.setText("");
			txt_marque.setText("");
			txt_carburant.setText("");
			txt_search_vehicule.setText("");
			txt_compteur.setText("");
			txt_prix.setText("");
			picker_date_circulation.setValue(null);;
			txt_num_immatriculation.setText("");
			box_available.setValue(null);
	    	AfficherVehicules(); // mise l'affichage de taleau
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void AfficherLigne() {
    	Vehicule vehicule = table_vehicule.getSelectionModel().getSelectedItem();
    	if(vehicule != null) {
    		String requet = "select * from vehicule where num_immatriculation = ?";
        	try {
    			st = cnx.prepareStatement(requet);
    			st.setString(1, vehicule.getNum_immatriculation());
    			resultat = st.executeQuery();
    			if(resultat.next()) {
    				box_num_parking.setValue(resultat.getInt("num_parking"));
    				txt_type.setText(resultat.getString("type"));
    				txt_marque.setText(resultat.getString("marque"));
    				txt_carburant.setText(resultat.getString("carburant"));
    				txt_search_vehicule.setText(resultat.getString("num_immatriculation"));
    				txt_compteur.setText(resultat.getString("compteur"));
    				txt_prix.setText(resultat.getString("prix"));
    				java.sql.Date date_circulation = resultat.getDate("date_circulation");
    				picker_date_circulation.setValue(date_circulation.toLocalDate());;
    				txt_num_immatriculation.setText(resultat.getString("num_immatriculation"));
    				box_available.setValue(resultat.getBoolean("available"));
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }

    @FXML
    void ChercherVoiture() {
    	String num_immatriculation = txt_search_vehicule.getText();
    	String requet = "select * from vehicule "
    			+ "where num_immatriculation = '"+num_immatriculation+"'";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if(resultat.next()) {
				box_num_parking.setValue(resultat.getInt("num_parking"));
				txt_type.setText(resultat.getString("type"));
				txt_marque.setText(resultat.getString("marque"));
				txt_carburant.setText(resultat.getString("carburant"));
				txt_search_vehicule.setText(resultat.getString("num_immatriculation"));
				txt_compteur.setText(resultat.getString("compteur"));
				txt_prix.setText(resultat.getString("prix"));
				java.sql.Date date_circulation = resultat.getDate("date_circulation");
				picker_date_circulation.setValue(date_circulation.toLocalDate());;
				txt_num_immatriculation.setText(resultat.getString("num_immatriculation"));
				box_available.setValue(resultat.getBoolean("available"));
				AfficherVehicules();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void DeleteVoiture() {
    	String num_immatriculation = txt_search_vehicule.getText();
    	String requet = "delete from vehicule where num_immatriculation = '"+num_immatriculation+"'";
    	try {
			st = cnx.prepareStatement(requet);
			st.executeUpdate();
			
			box_num_parking.setValue(null);
			txt_type.setText("");
			txt_marque.setText("");
			txt_carburant.setText("");
			txt_search_vehicule.setText("");
			txt_compteur.setText("");
			txt_prix.setText("");
			picker_date_circulation.setValue(null);;
			txt_num_immatriculation.setText("");
			box_available.setValue(null);
			AfficherVehicules();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void EditVoiture() {
    	String type = txt_type.getText();
    	String marque = txt_marque.getText();
    	String carburant = txt_carburant.getText();
    	String num_immatriculation = txt_num_immatriculation.getText();
    	double prix = Double.parseDouble(txt_prix.getText());
    	int compteur = Integer.parseInt(txt_compteur.getText());
    	int num_parking = box_num_parking.getValue();
    	boolean available = box_available.getValue();
    	java.util.Date date_circulation = java.util.Date.from((picker_date_circulation.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date sqlDate = new java.sql.Date(date_circulation.getTime());
		String requet = "update vehicule set type=?, marque=?, carburant=?,num_immatriculation=?,prix=?,compteur=?,date_circulation=?,num_parking=?,available=?"
				+ " where num_immatriculation = '"+txt_search_vehicule.getText()+"'";
    	try {
			st = cnx.prepareStatement(requet);
	    	st.setString(1, type);
	    	st.setString(2, marque);
	    	st.setString(3, carburant);
	    	st.setString(4, num_immatriculation);
	    	st.setDouble(5, prix);
	    	st.setInt(6, compteur);
	    	st.setDate(7, sqlDate);
	    	st.setInt(8, num_parking);
	    	st.setBoolean(9, available);
	    	st.executeUpdate();
	    	box_num_parking.setValue(null);
			txt_type.setText("");
			txt_marque.setText("");
			txt_carburant.setText("");
			txt_search_vehicule.setText("");
			txt_compteur.setText("");
			txt_prix.setText("");
			picker_date_circulation.setValue(null);;
			txt_num_immatriculation.setText("");
			box_available.setValue(null);
	    	AfficherVehicules(); // mise l'affichage de taleau
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void LouerVoiture() {

    }

    @FXML
    void RestituerVoiture() {

    }
    
    void AfficherVehicules(){
    	int num_utilisateur = SignUserController.GetUser();
    	table_vehicule.getItems().clear();
    	String requet = "select * from vehicule";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				listVehicule.add(new Vehicule(resultat.getString("num_immatriculation"),resultat.getString("marque"), resultat.getString("type"),
						resultat.getString("carburant"),resultat.getInt("compteur"), resultat.getDate("date_circulation"),resultat.getDouble("prix"),
						resultat.getBoolean("available"),num_utilisateur,resultat.getInt("num_parking")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	num_immatriculation_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("num_immatriculation"));
		type_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("type"));
		carburant_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("carburant"));
		compteur_col.setCellValueFactory(new PropertyValueFactory<Vehicule, Integer>("compteur"));
		marque_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("marque"));
		available_col.setCellValueFactory(new PropertyValueFactory<Vehicule, Boolean>("available"));
		num_parking_col.setCellValueFactory(new PropertyValueFactory<Vehicule, Integer>("num_parking"));
		date_circulation_col.setCellValueFactory(new PropertyValueFactory<Vehicule, Date>("date_circulation"));
		table_vehicule.setItems(listVehicule);
    } 

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherVehicules();
		Boolean value[] = {true , false};
		box_available.getItems().addAll(value);
		//Get_num_parking();
		String requet = "select num_parking from parking";
    	ObservableList<Integer> list_num_parking = FXCollections.observableArrayList();
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				list_num_parking.add(resultat.getInt("num_parking"));
			}
			box_num_parking.getItems().addAll(list_num_parking);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
