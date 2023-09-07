package application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Blob;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Client;

public class GestionClientController implements Initializable{
	
	Connection cnx = null;
	public PreparedStatement st,st2;
	public ResultSet resultat,resultat2;
	
	//------------- variable concernat la table client-------
	
	@FXML
    private TableView<Client> table_client;

    @FXML
    private TableColumn<Client, Integer> num_client_col;

    @FXML
    private TableColumn<Client, String> nom_complet_col;

    @FXML
    private TableColumn<Client, String> cin_col;

    @FXML
    private TableColumn<Client, String> phone_col;

    @FXML
    private TableColumn<Client, String> ville_col;

    @FXML
    private TableColumn<Client, String> adresse_col;

    @FXML
    private TableColumn<Client, Date> date_naissance_col;
    
  //------------- variable concernat les textField -------

    @FXML
    private TextField txt_nom_complet;

    @FXML
    private TextField txt_cin;

    @FXML
    private TextField txt_phone;

    @FXML
    private TextField txt_ville;

    @FXML
    private TextField txt_adresse;
    
    @FXML
    private TextField txt_search_client;
    
    @FXML
    private DatePicker picker_date_naissance;

    //-----------buttons---------

    @FXML
    private Button uploat_btn;

    @FXML
    private Button add_btn;

    @FXML
    private Button edit_btn;

    @FXML
    private Button delete_btn;
    
    @FXML
    private ImageView permis;
    
    public ObservableList<Client> listClient = FXCollections.observableArrayList();
    
    private File file;

    @FXML
    void AddClient() {
    	Blob image_permis = null;
    	String nom_complet = txt_nom_complet.getText();
    	String phone = txt_phone.getText();
    	String cin = txt_cin.getText();
    	String ville = txt_ville.getText();
    	String adresse = txt_adresse.getText();
		
		int num_utilisateur = SignUserController.GetUser();// return le numero d'utilisateur connecte
		
    	String requet = "insert into client (cin, nom_complet,phone,adresse,ville,date_naissance,num_utilisateur,permis) values(?,?,?,?,?,?,?,?)";
    	try {
	    	st = cnx.prepareStatement(requet);	
	    	java.util.Date date = java.util.Date.from((picker_date_naissance.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	    	st.setString(1, cin);
	    	st.setString(2, nom_complet);
	    	st.setString(3, phone);
	    	st.setString(4, adresse);
	    	st.setString(5, ville);
	    	st.setDate(6, sqlDate);
	    	st.setInt(7, num_utilisateur);
	    	FileInputStream fileStream = new FileInputStream(file);//ajouter le image selectionee a la base de donnee
	    	st.setBinaryStream(8, fileStream, file.length());
	    	st.execute();
	    	txt_nom_complet.setText("");
			txt_cin.setText("");
			txt_ville.setText("");
			txt_adresse.setText("");
			txt_phone.setText("");
			txt_search_client.setText("");
			picker_date_naissance.setValue(null);
			afficherClients();                // pour afficher le client ajouter directement dans la table
    	}catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
    	

    }

    @FXML
    void DeleteClient() {
    	String requet = "delete from client where nom_complet = '"+txt_search_client.getText()+"'";
    	
    	try {
			st = cnx.prepareStatement(requet);
			st.executeUpdate();
			
	    	txt_nom_complet.setText(""); // pour vider les textes Feilds apres la suppression
			txt_cin.setText("");
			txt_ville.setText("");
			txt_adresse.setText("");
			txt_phone.setText("");
			txt_search_client.setText("");
			picker_date_naissance.setValue(null);
			//permis.setImage(null);
			afficherClients();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void EditClient() {
    	String nom_complet = txt_nom_complet.getText();
    	String phone = txt_phone.getText();
    	String cin = txt_cin.getText();
    	String ville = txt_ville.getText();
    	String adresse = txt_adresse.getText();
    	java.util.Date date = java.util.Date.from((picker_date_naissance.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		String requet = "update client set cin=?, nom_complet=?, phone=?, ville=?, adresse=?, date_naissance=?,permis=? where nom_complet = '"+txt_search_client.getText()+"'";
    	try {
			st = cnx.prepareStatement(requet);
	    	st.setString(1, cin);
	    	st.setString(2, nom_complet);
	    	st.setString(3, phone);
	    	st.setString(4, adresse);
	    	st.setString(5, ville);
	    	st.setDate(6, sqlDate);
	    	FileInputStream fileStream = new FileInputStream(file);//ajouter le image selectionee a la base de donnee
	    	st.setBinaryStream(7, fileStream, file.length());
	    	st.executeUpdate();
	    	txt_nom_complet.setText("");
			txt_cin.setText("");
			txt_ville.setText("");
			txt_adresse.setText("");
			txt_phone.setText("");
			txt_search_client.setText("");
			picker_date_naissance.setValue(null);
			permis.setImage(null);;
			
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
    	afficherClients();
    }
    

    @FXML
    void SearchClient() {
    	String requet = "select * from client where nom_complet = '"+txt_search_client.getText()+"'";
    	try {
    		Blob image_permis;
	    	byte byteImage[];
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			if(resultat.next()) {
				txt_nom_complet.setText(resultat.getString("nom_complet"));
				txt_cin.setText(resultat.getString("cin"));
				txt_ville.setText(resultat.getString("ville"));
				txt_adresse.setText(resultat.getString("adresse"));
				txt_phone.setText(resultat.getString("phone"));
				Date date = resultat.getDate("date_naissance");
				picker_date_naissance.setValue(((java.sql.Date) date).toLocalDate());
				image_permis = resultat.getBlob("permis");
				byteImage = image_permis.getBytes(1,(int)image_permis.length());
				Image img = new Image(new ByteArrayInputStream(byteImage),permis.getFitHeight(),permis.getFitWidth(),true,true);
				permis.setImage(img);
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }

    @FXML
    void UploadPermis() {
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("choisez une photo de permis");//le message qui va s'afficher comme titre de la fentre
    	FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("formats autorisée", "*.jpg","*.png"); // extention des image permise
    	chooser.getExtensionFilters().add(filter);
    	file = chooser.showOpenDialog(null);
    	if (file != null) {
    		Image image = new Image(file.getAbsoluteFile().toURI().toString());
			permis.setImage(image);
		}
    }
    
    @FXML
    void afficher_ligne() {
    	Client client = table_client.getSelectionModel().getSelectedItem();
    	if(client != null) {
    		String requet = "select * from client where num_client = ?";
        	try {
    			st = cnx.prepareStatement(requet);
    			st.setInt(1, client.getNum_client());
    			resultat = st.executeQuery();
    			if(resultat.next()) {
    		    	Blob image_permis;
    		    	byte byteImage[];
    				txt_nom_complet.setText(resultat.getString("nom_complet"));
    				txt_cin.setText(resultat.getString("cin"));
    				txt_phone.setText(resultat.getString("phone"));
    				txt_ville.setText(resultat.getString("ville"));
    				txt_adresse.setText(resultat.getString("adresse"));
    				java.sql.Date sqlDate = resultat.getDate("date_naissance");
    				picker_date_naissance.setValue((sqlDate).toLocalDate());
    				txt_search_client.setText(resultat.getString("nom_complet"));
    				
    				image_permis = resultat.getBlob("permis");
    				byteImage = image_permis.getBytes(1, (int) image_permis.length());
    				Image img = new Image(new ByteArrayInputStream(byteImage),permis.getFitHeight(),permis.getFitWidth(),true,true);
    				permis.setImage(img);
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public void afficherClients() {
    	table_client.getItems().clear();  //pour ne pas repeter l'ajout des encien valeur: on vider la table a chaque fois on veut fait la mise a jour
    	String requet2 = "select * from client";
   	
    	try {
			st = cnx.prepareStatement(requet2);
			resultat = st.executeQuery();
			
			while(resultat.next()) {
				listClient.add(new Client(resultat.getInt("num_client"),resultat.getString("cin"),resultat.getString("nom_complet"),resultat.getString("phone"),
						resultat.getDate("date_naissance"),resultat.getString("ville"),resultat.getString("adresse"),resultat.getInt("num_utilisateur")));
				
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	num_client_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("num_client"));
    	nom_complet_col.setCellValueFactory(new PropertyValueFactory<Client, String>("nomPrenom"));
    	phone_col.setCellValueFactory(new PropertyValueFactory<Client, String>("phone"));
    	ville_col.setCellValueFactory(new PropertyValueFactory<Client, String>("ville"));
    	adresse_col.setCellValueFactory(new PropertyValueFactory<Client, String>("adresse"));
    	date_naissance_col.setCellValueFactory(new PropertyValueFactory<Client, Date>("dateNaissance"));
    	cin_col.setCellValueFactory(new PropertyValueFactory<Client, String>("cin"));
    	table_client.setItems(listClient);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		afficherClients();
	}

}
