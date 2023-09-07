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
import models.Contrat;

public class GestionContratController implements Initializable{
	
	Connection cnx = null;
	PreparedStatement st1,st2;
	ResultSet resultat1,resultat2;

	 @FXML
	    private TextField txt_search_contrat;

	    @FXML
	    private TextField txt_num_contrat;

	    @FXML
	    private TextField txt_num_client;

	    @FXML
	    private DatePicker picker_date_contrat;

	    @FXML
	    private DatePicker picker_date_echeance;

	    @FXML
	    private TextField txt_nom_complet;

	    @FXML
	    private TableView<Contrat> table_contrat;
	    
	    @FXML
	    private ComboBox<Integer> box_num_client;
	    @FXML
	    private ComboBox<String> box_num_immatriculation;

	    @FXML
	    private TableColumn<Contrat, Integer> num_contrat_col;

	    @FXML
	    private TableColumn<Contrat, Date> date_contrat_col;

	    @FXML
	    private TableColumn<Contrat, Date> date_echeance_col;

	    @FXML
	    private TableColumn<Contrat, Integer> num_client_col;

	    @FXML
	    private TableColumn<Contrat, String> num_immatriculation_col;
	    
	    public ObservableList<Contrat> listContrat = FXCollections.observableArrayList();
	    
	    void RemplirBoxNumImmatriculation() {
	    	box_num_immatriculation.getItems().clear();
	    	
	    	String requet = "select distinct vehicule.num_immatriculation from vehicule, reservation, client "
	    					+ "where available = 1 "
	    					+ "or (reservation.num_client = '"+box_num_client.getValue()+"' "
	    							+ "and reservation.num_immatriculation = vehicule.num_immatriculation)";
	    	
	    	ObservableList<String> listNumImmatriculation = FXCollections.observableArrayList();
	    	try {
				st2 = cnx.prepareStatement(requet);
				resultat2 = st2.executeQuery();
				while(resultat2.next()) {
					String numImmatriculation = resultat2.getString("num_immatriculation");
					listNumImmatriculation.add(numImmatriculation);
				}
				box_num_immatriculation.getItems().addAll(listNumImmatriculation);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	     
	    @FXML
	    void Vehicule_reserve_par_client() { // remplire le box_num_immatriculation par les vechicule reserve par un sepecifique et afficher le nom client correspondant a sont numero
	    	RemplirBoxNumImmatriculation();
	    	
	    	//determiner le nom complet correspondant au numero de client selectionnee
		    	
		    	if(box_num_client.getValue() != null) {
		    		int num_client = box_num_client.getValue();
		    		String requet2 = "select nom_complet from client where num_client = '"+num_client+"'";
		    	try {
					st2 = cnx.prepareStatement(requet2);
					resultat2 = st2.executeQuery();
					if( resultat2.next())
						txt_nom_complet.setText(resultat2.getString("nom_complet"));
					
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Vehicule_reserve_par_client");
				}
		    	}
		    	
	    }
	    
	    void RemplirBoxNumClient() {
	    	String requet = "select num_client from client";
	    	ObservableList<Integer> listNumClient = FXCollections.observableArrayList();
	    	try {
				st1 = cnx.prepareStatement(requet);
				resultat1 = st1.executeQuery();
				while(resultat1.next()) {
					int NumClient = resultat1.getInt("num_client");
					listNumClient.add(NumClient);
				}
				box_num_client.getItems().addAll(listNumClient);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("RemplirBoxNumClient");
			}
	    }

	    @FXML
	    void AddContrat() {
	    	int num_client = box_num_client.getValue();
	    	String num_immatriculation = box_num_immatriculation.getValue();
	    	
	    	//convert date java to date en format sql
	    	Date dateContrat = new Date();
	    	java.sql.Date date_contrat = new java.sql.Date(dateContrat.getTime());
	    	
	    	java.util.Date dateEcheance = java.util.Date.from((picker_date_echeance.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_echeance = new java.sql.Date(dateEcheance.getTime());
	    	
	    	String requet = "insert into contrat (num_client, num_immatriculation, date_contrat, date_echeance) values(?,?,?,?)";
	    	try {
		    	st1 = cnx.prepareStatement(requet);	
		    	
		    	st1.setInt(1, num_client);
		    	st1.setString(2, num_immatriculation);
		    	st1.setDate(3, date_contrat);
		    	st1.setDate(4, date_echeance);
		    	st1.execute();
		    	txt_nom_complet.setText("");
				box_num_client.setValue(null);
				txt_num_contrat.setText("");
				picker_date_echeance.setValue(null);
				picker_date_contrat.setValue(null);
				txt_search_contrat.setText("");
				box_num_immatriculation.setValue(null);
				AfficherContrats();
				
	    	}catch (SQLException e) {
				e.printStackTrace();
				System.out.println("AddContrat");
			}
	    	RemplirBoxNumImmatriculation();
	    	
	    }

	    @FXML
	    void DeleteContrat() {
	    	int numContrat = Integer.parseInt(txt_search_contrat.getText());
	    	String requet = "delete from contrat where num_contrat = '"+numContrat+"'";
	    	try {
				st1 = cnx.prepareStatement(requet);
				st1.executeUpdate();
				
				txt_nom_complet.setText("");
				box_num_client.setValue(null);
				box_num_immatriculation.setValue(null);
				picker_date_contrat.setValue(null);
				picker_date_echeance.setValue(null);
				box_num_client.setValue(null);
				box_num_immatriculation.setValue(null);
				txt_num_contrat.setText("");
				AfficherContrats();
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("DeleteContrat");
			}
	    	RemplirBoxNumImmatriculation();
	    }

	    @FXML
	    void EditContrat() {
	    	int num_client = box_num_client.getValue();
	    	String num_immatriculation = box_num_immatriculation.getValue();
	    	
	    	//convert date java to date en format sql
	    	java.util.Date dateEcheance = java.util.Date.from((picker_date_echeance.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_echeance = new java.sql.Date(dateEcheance.getTime());
	    	
	    	java.util.Date dateContrat = java.util.Date.from((picker_date_contrat.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_contrat = new java.sql.Date(dateContrat.getTime());
	    	
			String requet = "update contrat set num_immatriculation=?, num_client=?, date_contrat=?, date_echeance=? "
					+ " where num_contrat = '"+txt_search_contrat.getText()+"'";
	    	try {
				st1 = cnx.prepareStatement(requet);
		    	st1.setString(1, num_immatriculation);
		    	st1.setInt(2, num_client);
		    	st1.setDate(3, date_contrat);
		    	st1.setDate(4, date_echeance);
		    	st1.executeUpdate();
		    	// pour vider les champ apres chaque modification
		    	txt_nom_complet.setText("");
				box_num_client.setValue(null);
				box_num_immatriculation.setValue(null);
				picker_date_contrat.setValue(null);
				picker_date_echeance.setValue(null);
				box_num_client.setValue(null);
				box_num_immatriculation.setValue(null);
				txt_num_contrat.setText("");
				txt_search_contrat.setText("");
		    	 // mise l'affichage de taleau
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("EditContrat");
			}
	    	AfficherContrats();
//	    	RemplirBoxNumImmatriculation();
	    }

	    @FXML
	    void PrintContrat() {

	    }

	    @FXML
	    void afficherLigne() {
	    	Contrat contrat = table_contrat.getSelectionModel().getSelectedItem();
	    	if(contrat != null) {
	    		String requet = "select * from contrat "
		    			+ "where num_contrat = ? ";
		    	
//		    	 "select client.num_client, nom_complet, contrat.* from client, contrat "
//	 			+ "where num_contrat = ? "
//	 			+ "and client.num_client = contrat.num_client";
		    	try {
					st1 = cnx.prepareStatement(requet);
					st1.setInt(1, contrat.getNum_contrat());// remplacer ? par le numero de contrat
					resultat1 = st1.executeQuery();
					
					//afficher les inforamtion cherché dans les champs de textes qui convient
					if(resultat1.next()) {
				    	txt_num_contrat.setText(resultat1.getString("num_contrat"));
						java.sql.Date dateContrat = resultat1.getDate("date_contrat"); // to convert date.sql to Date in java
						picker_date_contrat.setValue(dateContrat.toLocalDate());
						java.sql.Date dateEcheance = resultat1.getDate("date_echeance");
						picker_date_echeance.setValue(dateEcheance.toLocalDate());
						//txt_nom_complet.setText(resultat1.getString("nom_complet"));
						box_num_client.setValue(resultat1.getInt("num_client"));
						box_num_immatriculation.setValue(resultat1.getString("num_immatriculation"));
						txt_search_contrat.setText(resultat1.getString("num_contrat"));
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("afficherLigne");
				}
	    	}
	    	
	    }

	    @FXML
	    void chercherContrat() {
	    	int N_contrat = Integer.parseInt(txt_search_contrat.getText());
	    	String requet = "select * from contrat "
	    			+ "where num_contrat = '"+N_contrat+"'";
	    	
	    	try {
				st1 = cnx.prepareStatement(requet);
				resultat1 = st1.executeQuery();
				if(resultat1.next()) {
					txt_num_contrat.setText(resultat1.getString("num_contrat"));
					java.sql.Date dateContrat = resultat1.getDate("date_contrat"); // to convert date.sql to Date in java
					picker_date_contrat.setValue(dateContrat.toLocalDate());
					java.sql.Date dateEcheance = resultat1.getDate("date_echeance");
					picker_date_echeance.setValue(dateEcheance.toLocalDate());
					box_num_client.setValue(resultat1.getInt("num_client"));
					box_num_immatriculation.setValue(resultat1.getString("num_immatriculation"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("chercherContrat");
			}
	    }
	
	    void AfficherContrats() {
	    	table_contrat.getItems().clear();
	    	String requet = "select * from contrat";
	    	try {
				st1 = cnx.prepareStatement(requet);
				resultat1 = st1.executeQuery();
				while(resultat1.next()) {
					listContrat.add(new Contrat(resultat1.getInt("num_contrat"), resultat1.getDate("date_contrat"),
							resultat1.getDate("date_echeance"),resultat1.getInt("num_client"),resultat1.getString("num_immatriculation")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	date_echeance_col.setCellValueFactory(new PropertyValueFactory<Contrat, Date>("date_echeance"));
	    	date_contrat_col.setCellValueFactory(new PropertyValueFactory<Contrat, Date>("date_contrat"));
	    	num_contrat_col.setCellValueFactory(new PropertyValueFactory<Contrat, Integer>("num_contrat"));
	    	num_client_col.setCellValueFactory(new PropertyValueFactory<Contrat, Integer>("num_client"));
	    	num_immatriculation_col.setCellValueFactory(new PropertyValueFactory<Contrat, String>("num_immatriculation"));
	    	table_contrat.setItems(listContrat);
	    }
	    
	    
	    
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherContrats();
		RemplirBoxNumImmatriculation();
		RemplirBoxNumClient();
	}
	
}
