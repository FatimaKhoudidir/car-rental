package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
import models.Facture;

public class GestionFactureController implements Initializable{
	
	Connection cnx = null;
	PreparedStatement st,st1;
	ResultSet resultat,resultat1;

	@FXML
    private TextField txt_search_facture;

    @FXML
    private TableView<Facture> table_facture;

    @FXML
    private TableColumn<Facture, Integer> num_facture_col;

    @FXML
    private TableColumn<Facture, Double> montant_col;

    @FXML
    private TableColumn<Facture, Date> date_facture_col;

    @FXML
    private TableColumn<Facture, Integer> num_client_col;
    
    @FXML
    private ComboBox<Integer> box_num_contrat;

    @FXML
    private TextField txt_num_facture;

    @FXML
    private TextField txt_montant;

    @FXML
    private DatePicker picker_date_facture;

    @FXML
    private TextField txt_num_client;

    @FXML
    private TextField txt_num_immatriculation;

    @FXML
    private TextField txt_nom_complet;
    
    ObservableList<Facture> listFacture = FXCollections.observableArrayList();
    
    //cette methode va nous permetre de remplire un combox pour selection le num de contrat qui convient avec un facture
    void RemplirBoxNumContrat() {
    	String requet = "select num_contrat from contrat where num_contrat not in (select num_contrat from facture)";
    	ObservableList<Integer> listNumContrat = FXCollections.observableArrayList();
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				int numContrat = resultat.getInt("num_contrat");
				listNumContrat.add(numContrat);
			}
			box_num_contrat.getItems().addAll(listNumContrat);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void AddFacture() {
    	//String montant_txt = txt_montant.getText();
    	
    	//convetire date from java format to sql format
    	Date dateFacture = Date.from(picker_date_facture.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date date_facture = new java.sql.Date(dateFacture.getTime());
    	try {
    		String requet = "select date_contrat, date_echeance, prix from contrat, vehicule "
    				+ "where contrat.num_immatriculation = vehicule.num_immatriculation "
    				+ "and num_contrat = '"+box_num_contrat.getValue()+"'";
    		st = cnx.prepareStatement(requet);
    		resultat = st.executeQuery();
    		if(resultat.next()) {
    			Date date_contrat = resultat.getDate("date_contrat");
	    		Date date_echeance = resultat.getDate("date_echeance");
	    		double prix = resultat.getDouble("prix");
	    		
	    		 //getTime return nbr de milliseconde depuis 1/1/1970 a 00:00:00
	    		int nbr_jour = (int) Math.abs((date_contrat.getTime() - date_echeance.getTime())/(1000*60*60*24));
	    		double montant = (double)nbr_jour * prix;
	    		
	    		String requet2 = "insert into facture (montant,date_facture,num_contrat) values(?,?,?)";
		    	st = cnx.prepareStatement(requet2);	
		    	
		    	st.setDouble(1, montant);
		    	st.setDate(2, date_facture);
		    	st.setInt(3, box_num_contrat.getValue());
		    	st.execute();
		    	txt_nom_complet.setText("");
		    	txt_nom_complet.setText("");
				txt_num_client.setText("");
				txt_num_facture.setText("");
				txt_montant.setText("");
				txt_num_immatriculation.setText("");
				picker_date_facture.setValue(null);
				box_num_contrat.setValue(null);
				AfficherFactures();
    		}

    	 // pour afficher le client ajouter directement dans la table
			
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void SearchFacture() {
    	int numFacture = Integer.parseInt(txt_search_facture.getText());
    	String requet = "select facture.*, client.num_client, client.nom_complet, vehicule.num_immatriculation from facture, contrat , client, vehicule "
    			+ "where facture.num_facture = '"+numFacture+"'";
    	/*"select facture.*, client.num_client, client.nom_complet, vehicule.num_immatriculation from facture, contrat , client, vehicule "
    			+ "where facture.num_facture = '"+numFacture+"'"
    			+ "and contrat.num_contrat = facture.num_contrat "
    			+ "and client.num_client = contrat.num_client "
    			+ "and vehicule.num_immatriculation = contrat.num_immatriculation";*/
    	
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if(resultat.next()) {
				txt_nom_complet.setText(resultat.getString("nom_complet"));
				txt_num_client.setText(resultat.getString("num_client"));
				txt_num_facture.setText(resultat.getString("num_facture"));
				txt_montant.setText(resultat.getString("montant"));
				txt_num_immatriculation.setText(resultat.getString("num_immatriculation"));
				Date date = resultat.getDate("date_facture");
				picker_date_facture.setValue(((java.sql.Date) date).toLocalDate());
				box_num_contrat.setValue(resultat.getInt("num_contrat"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void DeleteFacture() {
    	int numFacture = Integer.parseInt(txt_search_facture.getText());
    	String requet = "delete from facture where num_facture = '"+numFacture+"'";
    	try {
			st = cnx.prepareStatement(requet);
			st.executeUpdate();
			
			txt_search_facture.setText("");
			txt_nom_complet.setText("");
			txt_num_client.setText("");
			txt_num_facture.setText("");
			txt_montant.setText("");
			txt_num_immatriculation.setText("");
			picker_date_facture.setValue(null);
			box_num_contrat.setValue(null);
			AfficherFactures();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void EditFacture() {
    	java.util.Date date = java.util.Date.from((picker_date_facture.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    java.sql.Date sql_date_facture = new java.sql.Date(date.getTime());
	    int num_contrat = box_num_contrat.getValue();
	    
		String requet = "update facture set  date_facture=?, num_contrat=? where num_facture = '"+txt_search_facture.getText()+"'";
    	try {
			st = cnx.prepareStatement(requet);
	    	st.setDate(1, sql_date_facture);
	    	st.setInt(2, num_contrat);
	    	st.executeUpdate();
	    	txt_search_facture.setText("");
	    	txt_nom_complet.setText("");
	    	txt_nom_complet.setText("");
			txt_num_client.setText("");
			txt_num_facture.setText("");
			txt_montant.setText("");
			txt_num_immatriculation.setText("");
			picker_date_facture.setValue(null);
			box_num_contrat.setValue(null);
			AfficherFactures();        // pour mise a jour le tableau apres la modification
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void PrintFacture() {
    	Document doc = new Document();
    	
    	try {
			PdfWriter.getInstance(doc, new FileOutputStream("facture.pdf"));
			doc.open();
			SimpleDateFormat formater = new SimpleDateFormat("dd/mm/yy hh:mm");
			Date date = new Date();
			//com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("C:\\Users\\moi\\Desktop\\me.jpg");
			//img.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
			//doc.add(img);
			Paragraph para = new Paragraph("\n\n\n Nom agence: NotreProjet "
					+ "\n documente à:  Agadir"
					+ "\n Nom Complet:  "+txt_nom_complet.getText()+""
					+ "\n N° Facture:  "+txt_num_facture.getText()+""
					+ "\n N° Contrat:  "+box_num_contrat.getValue()+""
					+ "\n N° Immatriculation:  "+txt_num_immatriculation.getText()+""
					+ "\n Montant total: "+txt_montant.getText()+"\n"
					+ "\t\t\t Date Facture: "+picker_date_facture.getValue()+"",FontFactory.getFont(FontFactory.TIMES_ROMAN,14,Font.NORMAL,BaseColor.BLACK));
			
			doc.add(para);
			doc.close();
			Desktop.getDesktop().open(new File("facture.pdf"));
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void AfficherLigne() {
    	Facture facture = table_facture.getSelectionModel().getSelectedItem();
    	if(facture != null) {
    		String requet = "select facture.*, client.num_client, client.nom_complet, vehicule.num_immatriculation "
    				+ "from facture, contrat , client, vehicule "
        			+ "where facture.num_facture = ? ";
        	try {
    			st = cnx.prepareStatement(requet);
    			st.setInt(1, facture.getNum_facture());
    			resultat = st.executeQuery();
    			if(resultat.next()){
    				txt_nom_complet.setText(resultat.getString("nom_complet"));
    				txt_num_facture.setText(resultat.getString("num_facture"));
    				txt_num_client.setText(resultat.getString("num_client"));
    				txt_num_immatriculation.setText(resultat.getString("num_immatriculation"));
    				txt_montant.setText(resultat.getString("montant"));
    				java.sql.Date sqlDate = resultat.getDate("date_facture");
    				picker_date_facture.setValue((sqlDate).toLocalDate());
    				txt_search_facture.setText(resultat.getString("num_facture"));
    				box_num_contrat.setValue(resultat.getInt("num_contrat"));
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    void AfficherFactures() {
    	table_facture.getItems().clear();
	    	String requet = "select * from facture";
//	    	"select * from facture, contrat, client "
//			+ "where contrat.num_contrat=facture.num_contrat "
//			+ "and client.num_client = contrat.num_client";
		try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			while(resultat.next()) {
				listFacture.add(new Facture(resultat.getInt("num_facture"), resultat.getDate("date_facture"), resultat.getDouble("montant")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		num_facture_col.setCellValueFactory(new PropertyValueFactory<Facture, Integer>("num_facture"));
		date_facture_col.setCellValueFactory(new PropertyValueFactory<Facture, Date>("date_facture"));
		montant_col.setCellValueFactory(new PropertyValueFactory<Facture, Double>("montant"));
		num_client_col.setCellValueFactory(new PropertyValueFactory<Facture, Integer>("num_client"));
		table_facture.setItems(listFacture);
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherFactures();
		RemplirBoxNumContrat();
	}

}
