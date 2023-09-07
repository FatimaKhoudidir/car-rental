package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Parking;

public class GestionParkingController implements Initializable{
	Connection cnx = null;
	PreparedStatement st;
	ResultSet resultat;
	static int num_parking;//va stoquer le num_parking selection pour l'utiliser pour afficher les vehicules trouvans dans parking correspondant
	static int capacite;
	static String nom_parking;
	@FXML
    private TextField txt_search_parking;

    @FXML
    private TextField txt_num_parking;

    @FXML
    private TextField txt_capacite;

    @FXML
    private TextField txt_rue;

    @FXML
    private TextField txt_arrondissement;

    @FXML
    private TableView<Parking> table_parking;

    @FXML
    private TableColumn<Parking, Integer> num_parking_col;

    @FXML
    private TableColumn<Parking, Integer> capacite_col;

    @FXML
    private TableColumn<Parking, String> rue_col;

    @FXML
    private TableColumn<Parking, String> arrondissement_col;

    @FXML
    private TableColumn<Parking, String> nom_parking_col;
    
    ObservableList<Parking> listParking = FXCollections.observableArrayList();
    
    private Parent fxml;
    
    @FXML
    void AfficherDetails() {
    	Stage detail_parking = new Stage();
    	try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/DetailParking.fxml"));
			Scene scene = new Scene(fxml);
			scene.setFill(Color.TRANSPARENT);
			detail_parking.setScene(scene);
			detail_parking.initStyle(StageStyle.TRANSPARENT);
			detail_parking.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void AddParking() {
    	String rue = txt_rue.getText();
    	String arrondissement = txt_arrondissement.getText();
    	int capacite = Integer.parseInt(txt_capacite.getText());
    	int num_utilisateur = SignUserController.GetUser(); // return le d'utilisateur connecte
    	
    	String requet = "insert into parking (rue,arrondissement,capacite,num_utilisateur) values(?,?,?,?)";
    	try {
			st = cnx.prepareStatement(requet);
			st.setString(1, rue);
			st.setString(2, arrondissement);
			st.setInt(3, capacite);
			st.setInt(4, num_utilisateur);
			st.execute();
			txt_num_parking.setText("");
			txt_rue.setText("");
			txt_arrondissement.setText("");
			txt_capacite.setText("");
			AfficherParkings();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    @FXML
    void AfficherLigne() {
    	Parking parking = table_parking.getSelectionModel().getSelectedItem();
    	num_parking=parking.getNum_parking();
    	capacite = parking.getCapacite();
    	nom_parking = parking.getNom_parking();
    	if(parking != null) {
    		String requet = "select * from parking where num_parking = ?";
        	try {
    			st = cnx.prepareStatement(requet);
    			st.setInt(1, num_parking);
    			resultat = st.executeQuery();
    			if(resultat.next()) {
    				txt_num_parking.setText(resultat.getString("num_parking"));
    				txt_rue.setText(resultat.getString("rue"));
    				txt_arrondissement.setText(resultat.getString("arrondissement"));
    				txt_search_parking.setText(resultat.getString("num_parking"));
    				txt_capacite.setText(resultat.getString("capacite"));
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }

    @FXML
    void ChercherParking() {
    	int numParking = Integer.parseInt(txt_search_parking.getText());
    	String requet = "select * from parking where num_parking = '"+numParking+"'";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if(resultat.next()) {
				txt_num_parking.setText(resultat.getString("num_parking"));
				txt_rue.setText(resultat.getString("rue"));
				txt_arrondissement.setText(resultat.getString("arrondissement"));
				txt_capacite.setText(resultat.getString("capacite"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void DeleteParking() {
    	int numParking = Integer.parseInt(txt_search_parking.getText());
    	String requet = "delete from parking where num_parking = '"+numParking+"'";
    	try {
			st = cnx.prepareStatement(requet);
			st.executeUpdate();
			txt_num_parking.setText("");
			txt_rue.setText("");
			txt_arrondissement.setText("");
			txt_capacite.setText("");
			AfficherParkings();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }

    @FXML
    void DisposerVoiture() {

    }

    @FXML
    void EditParking() {
    	String rue = txt_rue.getText();
    	String arrondissement = txt_arrondissement.getText();
    	int capacite = Integer.parseInt(txt_capacite.getText());
		String requet = "update parking set rue=?, arrondissement=?, capacite=? where num_parking = '"+txt_search_parking.getText()+"'";
    	try {
			st = cnx.prepareStatement(requet);
	    	st.setString(1, rue);
	    	st.setString(2, arrondissement);
	    	st.setInt(3, capacite);
	    	st.executeUpdate();
	    	txt_num_parking.setText("");
			txt_rue.setText("");
			txt_arrondissement.setText("");
			txt_capacite.setText("");
			AfficherParkings();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void SortirVoiture() {

    }
    
    void AfficherParkings(){
    	table_parking.getItems().clear();  //pour ne pas repeter l'ajout des encien valeur: on vider la table a chaque fois on veut fait la mise a jour
    	String requet = "select * from parking";
   	
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			
			while(resultat.next()) {
				listParking.add(new Parking(resultat.getInt("num_parking"),resultat.getInt("capacite"),resultat.getString("arrondissement"),resultat.getString("rue"),resultat.getString("nom_parking")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	num_parking_col.setCellValueFactory(new PropertyValueFactory<Parking, Integer>("num_parking"));
    	capacite_col.setCellValueFactory(new PropertyValueFactory<Parking, Integer>("capacite"));
    	arrondissement_col.setCellValueFactory(new PropertyValueFactory<Parking, String>("arrondissement"));
    	rue_col.setCellValueFactory(new PropertyValueFactory<Parking, String>("rue"));
    	nom_parking_col.setCellValueFactory(new PropertyValueFactory<Parking, String>("nom_parking"));
    	table_parking.setItems(listParking);
    }
    
    public int GetNomParking() {
    	Parking parking = table_parking.getSelectionModel().getSelectedItem();
    	return parking.getNum_parking();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherParkings();
		
	}

}
