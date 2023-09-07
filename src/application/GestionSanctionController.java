package application;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import models.Client;
import models.Sanction;

public class GestionSanctionController implements Initializable{
	
	@FXML
    private TextField txt_search_sanction;

    @FXML
    private TableView<Sanction> table_sanction;

    @FXML
    private TableColumn<Sanction, Integer> num_sanction_col;

    @FXML
    private TableColumn<Sanction, Integer> num_client_col;

    @FXML
    private TableColumn<Sanction, String> nom_complet_col;

    @FXML
    private TableColumn<Sanction, Date> date_retour_col;

    @FXML
    private TableColumn<Sanction, Integer> jour_retarde_col;

    @FXML
    private TableColumn<Sanction, Double> totale_sanction_col;

    @FXML
    private TextField txt_montant;

    @FXML
    private TextField txt_jours_retarde;

    @FXML
    private TextField txt_nom_complet;

    @FXML
    private TextField txt_num_immatriculation;

    @FXML
    private TextField txt_num_contrat;
    
    Connection cnx = null;
	PreparedStatement st,st1;
	ResultSet resultat;
	
	ObservableList<Sanction> listSanction = FXCollections.observableArrayList()
;
    @FXML
    void AfficherLigne() {
    	Sanction sanction = table_sanction.getSelectionModel().getSelectedItem();
    	if(sanction != null) {
    		String requet = "select sanction.*, nom_complet, num_immatriculation, num_contrat from sanction, client, contrat "
    				+ "where sanction.num_client = client.num_client = contrat.num_client = ?";
        	try {
    			st = cnx.prepareStatement(requet);
    			st.setInt(1, sanction.getNum_client());
    			resultat = st.executeQuery();
    			if(resultat.next()) {
    				txt_search_sanction.setText(Integer.toString(sanction.getNum_client())); 
    				txt_nom_complet.setText(resultat.getString("nom_complet"));
    				txt_num_immatriculation.setText(resultat.getString("num_immatriculation"));
    				txt_num_contrat.setText(resultat.getString("num_contrat"));
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }

    @FXML
    void ChercherClient() {
    	int num_client = Integer.parseInt(txt_search_sanction.getText());
    	String requet = "select sanction.*, nom_complet from sanction, client "
    			+ "where sanction.num_client = client.num_client = "+num_client+"";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if(resultat.next()) {
				txt_nom_complet.setText(resultat.getString("nom_complet"));
				txt_search_sanction.setText(Integer.toString(num_client)); 
				//txt_num_immatriculatio.setText(resultat.getString("num_immatriculation"));
				//txt_num_contrat.setText(resultat.getString("num_contrat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void EditMontant() {

    }
    
    void Sanctione() {
    	int jours_retarde;
    	//datediff(d1 , d2): d1 - d2 (nombre de jours)
    	String requet = "select datediff(date_echeance, now()) as jours_retarde, date_echeance, num_client from contrat ";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			
			while(resultat.next()) {
				jours_retarde = resultat.getInt("jours_retarde");
				if(jours_retarde < 0) {
					String requet1 = "delete from sanction where num_client = '"+resultat.getInt("num_client")+"'";
					st = cnx.prepareStatement(requet1);
					st.executeUpdate();
					String requet2 = "insert into sanction (num_client, montant) values(?,?) ";
					st1 = cnx.prepareStatement(requet2);
					st1.setInt(1, resultat.getInt("num_client"));
					st1.setInt(2, Math.abs(jours_retarde)*2000);
					st1.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    
    void AfficherSanctions() {
    	table_sanction.getItems().clear();
    	String requet = "select * from sanction";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				listSanction.add(new Sanction(resultat.getInt("num_sanction"), resultat.getInt("montant"),resultat.getInt("num_client")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	num_sanction_col.setCellValueFactory(new PropertyValueFactory<Sanction, Integer>("num_sanction"));
    	totale_sanction_col.setCellValueFactory(new PropertyValueFactory<Sanction, Double>("montant"));
    	num_client_col.setCellValueFactory(new PropertyValueFactory<Sanction, Integer>("num_client"));
    	table_sanction.setItems(listSanction);
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		Sanctione();
		AfficherSanctions();
		
		
	}

}
