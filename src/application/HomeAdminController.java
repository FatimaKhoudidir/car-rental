 package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeAdminController extends HomeUserController implements Initializable{
	
	@FXML
	private AnchorPane anchorpane;
	

    @FXML
    private Label txt_gestion;
	
	private Parent fxml;
	Stage stage;
	
	
	
	@FXML
    void close_espace_admin() {
		stage = (Stage) anchorpane.getScene().getWindow();
		stage.close();
    }
	
	
  @FXML
    void client() {
	  try {
		  fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionClient.fxml"));
		  anchorpane.getChildren().removeAll();
		  anchorpane.getChildren().setAll(fxml);  
	  }catch (IOException e1) {
			e1.printStackTrace();
		}
	  txt_gestion.setText("Gestion Clients");
	}

    
    @FXML
    void contrat() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionContrat.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Contrats");
    }

    @FXML
    void facture() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionFactures.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Factures");
    }

    @FXML
    void parking() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionPark.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Parkings");
    	
    }

    @FXML
    void reservation() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionReservation.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Reservations");
    }

    @FXML
    void sanction() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionSanction.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Sanctions");
    }

    @FXML
    void utilisateur() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionUsers.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Utilisateurs");
    }

    @FXML
    void vehicule() {
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/GestionVehicule.fxml"));
    		anchorpane.getChildren().removeAll();
    		anchorpane.getChildren().setAll(fxml);
    	}catch(IOException e1) {
    		e1.printStackTrace();
    	}
    	txt_gestion.setText("Gestion Vehicules");
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		client();
		
	}

}
