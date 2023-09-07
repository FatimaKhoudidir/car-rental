package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Parking;
import models.Vehicule;

public class DetailParkingController implements Initializable{
	
	Connection cnx = null;
	PreparedStatement st;
	ResultSet resultat;
	
    @FXML
    private TableColumn<Vehicule, String> num_immatriculation_col;

    @FXML
    private TableColumn<Vehicule, String> marque_col;

    @FXML
    private ComboBox<String> box_num_immatriculation;

    @FXML
    private TableView<Vehicule> table_park;

    @FXML
    private TextField txt_marque;

    @FXML
    private Label txt_nom_parking;

    @FXML
    private Label txt_num_parking;

    @FXML
    private Label txt_capacite;

    @FXML
    private Label txt_nbr_vehicule;
    
    @FXML
    private AnchorPane fenetre_detail;
    
    Stage stage;
    
    ObservableList<Vehicule> listVehicule =FXCollections.observableArrayList();//cette liste va stoquer les num_immatriculation les marque corespondat pour les affisse sur detail table

    @FXML
    void close_detail_parking() {   // cette fonction permet fermer la fenetre detail parking
    	stage = (Stage) fenetre_detail.getScene().getWindow();
    	stage.close();
    }
    
    void RemplirBoxNumImmatriculation() {
    	box_num_immatriculation.getItems().clear();
    	
    	String requet = "select num_immatriculation from vehicule";
    	
    	ObservableList<String> listNumImmatriculation = FXCollections.observableArrayList();
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				String numImmatriculation = resultat.getString("num_immatriculation");
				listNumImmatriculation.add(numImmatriculation);
			}
			box_num_immatriculation.getItems().addAll(listNumImmatriculation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML 
    void GetMarque() {
    	String num_immatriculation = box_num_immatriculation.getValue();
    	String requet = "select marque from vehicule where num_immatriculation = '"+num_immatriculation+"'";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if( resultat.next())
				txt_marque.setText(resultat.getString("marque"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void AfficherLigne() {
    	Vehicule vehicule = table_park.getSelectionModel().getSelectedItem();
    	String num_immatriculation = vehicule.getNum_immatriculation();
    	if(vehicule != null) {
    		String requet = "select num_immatriculation from vehicule where num_immatriculation = "+num_immatriculation+"";
        	try {
    			st = cnx.prepareStatement(requet);
    			resultat = st.executeQuery();
    			if(resultat.next()) {
    				box_num_immatriculation.setValue(resultat.getString("num_immatriculation"));
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }

    @FXML
    void DisposerVoiture() {
    
    }


    @FXML
    void SortirVoiture() {

    }
    
    void AfficherVehicule(){  //cette va afficher le vehicule qui elles sont dans un parking
    	
    	String requet = "select parking.*, num_immatriculation, marque from vehicule, parking "
    			+ "where vehicule.num_parking =  '"+Integer.toString(GestionParkingController.num_parking)+"'"
    			+ " and vehicule.num_parking = parking.num_parking";
    	
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				listVehicule.add(new Vehicule(resultat.getString("num_immatriculation"),resultat.getString("marque")));
				//listVehicule.add(resultat.getString("marque"));
			}
			table_park.getItems().addAll(listVehicule);
				
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	num_immatriculation_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("num_immatriculation"));
		marque_col.setCellValueFactory(new PropertyValueFactory<Vehicule, String>("marque"));
		table_park.setItems(listVehicule);
		txt_num_parking.setText(Integer.toString(GestionParkingController.num_parking)); //la num_parking de parking qu'on souhait voir ses details
		txt_nom_parking.setText(GestionParkingController.nom_parking);
		txt_capacite.setText(Integer.toString(GestionParkingController.capacite));
		
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherVehicule();
		RemplirBoxNumImmatriculation();	
		
	}

}
