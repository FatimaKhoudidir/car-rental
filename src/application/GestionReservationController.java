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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Reservation;

public class GestionReservationController implements Initializable{
	
	 @FXML
	    private TextField txt_search_reservation;

	    @FXML
	    private TextField txt_num_client;

	    @FXML
	    private TextField txt_num_reservation;

	    @FXML
	    private DatePicker picker_date_reservation;

	    @FXML
	    private DatePicker picker_date_depart;

	    @FXML
	    private DatePicker picker_date_retour;

	    @FXML
	    private TextField txt_marque;

	    @FXML
	    private TextField txt_nom_complet;
	    
	    @FXML
	    private Label comment;

	    @FXML
	    private TableView<Reservation> table_reservation;

	    @FXML
	    private TableColumn<Reservation, Integer> num_reservation_col;

	    @FXML
	    private TableColumn<Reservation, Integer> num_client_col;

	    @FXML
	    private TableColumn<Reservation, Date> date_reservation_col;

	    @FXML
	    private TableColumn<Reservation, Date> date_depart_col;

	    @FXML
	    private TableColumn<Reservation, Date> date_retour_col;

	    @FXML
	    private TableColumn<Reservation, Boolean> statu_col;
	    @FXML
	    private TableColumn<Reservation, String> num_immatriculation_col;

	    @FXML
	    private TableColumn<Reservation, Boolean> activated_col;
	    
	    @FXML
	    private ComboBox<Boolean> box_activated;
	    
	    @FXML
	    private ComboBox<String> box_num_immatriculation;
	    
	    @FXML
	    private ComboBox<Integer> box_num_client;


	    Connection cnx = null;
	    PreparedStatement st,st2;
	    ResultSet resultat,resultat2;
	    
	    ObservableList<Reservation> listReservation = FXCollections.observableArrayList();
	    
	    
	    void RemplirBoxNumImmatriculation() {
	    	box_num_immatriculation.getItems().clear();
	    	String requet = "select num_immatriculation from vehicule where available = 1 ";
	    			//+ "and num_immatriculation not in (select num_immatriculation from reservation where activated=1)";
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
	    
	    
	    void RemplirBoxNumClient() {
	    	String requet = "select num_client from client";
	    	ObservableList<Integer> listNumClient = FXCollections.observableArrayList();
	    	try {
				st = cnx.prepareStatement(requet);
				resultat = st.executeQuery();
				while(resultat.next()) {
					int NumClient = resultat.getInt("num_client");
					listNumClient.add(NumClient);
				}
				box_num_client.getItems().addAll(listNumClient);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    

   
	    @FXML
	    void AddReservation() {
	    	String num_immatriculation = box_num_immatriculation.getValue();
	    	int num_client = box_num_client.getValue();
	    	java.util.Date dateReservation = java.util.Date.from((picker_date_reservation.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_reservation = new java.sql.Date(dateReservation.getTime());
	    	
	    	java.util.Date dateRetour = java.util.Date.from((picker_date_retour.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_retour = new java.sql.Date(dateRetour.getTime());
	    	
	    	java.util.Date dateDepart = java.util.Date.from((picker_date_depart.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_depart = new java.sql.Date(dateDepart.getTime());
	    	String requet = "insert into reservation (num_client,num_immatriculation,date_reservation,date_depart,date_retour) values(?,?,?,?,? )";
	    	String requet2 = "update vehicule set available = ? "
	    					+ "where num_immatriculation = '"+num_immatriculation+"'";
	    	try {
		    	st = cnx.prepareStatement(requet);	
		    	
		    	st.setInt(1, num_client);
		    	st.setString(2, num_immatriculation);
		    	st.setDate(3, date_reservation);
		    	st.setDate(4, date_depart);
		    	st.setDate(5, date_retour);
		    	st.execute();
		    	txt_nom_complet.setText("");
				box_num_client.setValue(null);
				txt_num_reservation.setText("");
				picker_date_reservation.setValue(null);
				picker_date_depart.setValue(null);
				picker_date_retour.setValue(null);
				txt_search_reservation.setText("");
				box_activated.setValue(null);
				box_num_immatriculation.setValue(null);
				txt_marque.setText("");
				st = cnx.prepareStatement(requet2);
				st.setBoolean(1, false);
				st.execute();	
				
	    	}catch (SQLException e) {
				e.printStackTrace();
			}
	    	RemplirBoxNumImmatriculation();
	    	AfficherReservations();
	    }
	    


	    @FXML
	    void ChercherReservation() {
	    	int numReservation = Integer.parseInt(txt_search_reservation.getText());
	    	String requet = "select * from reservation where num_reservation = '"+numReservation+"'";
	    	
	    	try {
				st = cnx.prepareStatement(requet);
				resultat = st.executeQuery();
				if(resultat.next()) {
					box_num_client.setValue(resultat.getInt("num_client"));
					txt_num_reservation.setText(resultat.getString("num_reservation"));
					java.sql.Date date_reservation = resultat.getDate("date_reservation");
					picker_date_reservation.setValue(date_reservation.toLocalDate());
					java.sql.Date date_depart = resultat.getDate("date_depart");
					picker_date_depart.setValue(date_depart.toLocalDate());
					java.sql.Date date_retour = resultat.getDate("date_retour");
					picker_date_retour.setValue(date_retour.toLocalDate());
					txt_search_reservation.setText(resultat.getString("num_reservation"));
					box_activated.setValue(resultat.getBoolean("activated"));
					box_num_immatriculation.setValue(resultat.getString("num_immatriculation"));
					AfficherReservations();
				}
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void DeleteReservation() {
	    	String num_immatriculation = box_num_immatriculation.getValue();
	    	int numReservation = Integer.parseInt(txt_search_reservation.getText());
	    	String requet = "delete from reservation where num_reservation = '"+numReservation+"'";
	    	String requet2 = "update vehicule set available = ? "         // apres la suppression d'une reservation, la vehicule reservee sera available
					+ "where num_immatriculation = '"+num_immatriculation+"'";
	    	try {
				st = cnx.prepareStatement(requet);
				st.executeUpdate();
				
				txt_nom_complet.setText("");
				box_num_client.setValue(null);
				txt_num_reservation.setText("");
				picker_date_reservation.setValue(null);
				picker_date_depart.setValue(null);
				picker_date_retour.setValue(null);
				txt_search_reservation.setText("");
				box_activated.setValue(null);
				box_num_immatriculation.setValue(null);
				txt_marque.setText("");
				st = cnx.prepareStatement(requet2);
				st.setBoolean(1, true);
				st.execute();	
				AfficherReservations();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	RemplirBoxNumImmatriculation();
	    }

	    
	    @FXML
	    void EditReservation() {
	    	String num_immatriculation = box_num_immatriculation.getValue();
	    	int num_client = box_num_client.getValue();
	    	boolean activated = box_activated.getValue();
	    	java.util.Date dateReservation = java.util.Date.from((picker_date_reservation.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_reservation = new java.sql.Date(dateReservation.getTime());
	    	
	    	java.util.Date dateRetour = java.util.Date.from((picker_date_retour.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_retour = new java.sql.Date(dateRetour.getTime());
	    	
	    	java.util.Date dateDepart = java.util.Date.from((picker_date_depart.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	java.sql.Date date_depart = new java.sql.Date(dateDepart.getTime());
	    	
			String requet = "update reservation set num_immatriculation=?, num_client=?, date_depart=?, date_retour=?,date_reservation=?,activated=?"
					+ " where num_reservation = '"+txt_search_reservation.getText()+"'";
	    	try {
				st = cnx.prepareStatement(requet);
		    	st.setString(1, num_immatriculation);
		    	st.setInt(2, num_client);
		    	st.setDate(3, date_depart);
		    	st.setDate(4, date_retour);
		    	st.setDate(5, date_reservation);
		    	st.setBoolean(6, activated);
		    	st.executeUpdate();
		    	// pour vider les champ apres chaque modification
		    	txt_nom_complet.setText("");
				box_num_client.setValue(null);
				txt_num_reservation.setText("");
				picker_date_reservation.setValue(null);
				picker_date_depart.setValue(null);
				picker_date_retour.setValue(null);
				txt_search_reservation.setText("");
				box_activated.setValue(null);
				box_num_immatriculation.setValue(null);
				txt_marque.setText("");
		    	AfficherReservations(); // mise l'affichage de taleau
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	RemplirBoxNumImmatriculation();
	    }
	    
	    @FXML
	    void AnnulerReservation() {
	    	String requet = "update reservation set activated=? where num_reservation = '"+txt_search_reservation.getText()+"'";
	    	try {
	    		st = cnx.prepareStatement(requet);
					st.setBoolean(1, false);
					st.executeUpdate();
					AfficherReservations();
					String requet1 = "update vehicule set available=? "
							+ "where num_immatriculation = '"+box_num_immatriculation.getValue()+"'";
				st = cnx.prepareStatement(requet1);
				st.setBoolean(1, true);
				st.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	RemplirBoxNumImmatriculation();
	    }

	    @FXML
	    void ValiderReservation() {
	    	String requet = "update reservation set activated=? where num_reservation = '"+txt_search_reservation.getText()+"'";
	    	try {
	    		st = cnx.prepareStatement(requet);
					st.setBoolean(1, true);
					st.executeUpdate();
					AfficherReservations();
					String requet1 = "update vehicule set available=? "
							+ "where num_immatriculation = '"+box_num_immatriculation.getValue()+"'";
				st = cnx.prepareStatement(requet1);
				st.setBoolean(1, false);
				st.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("vous n'avez pas selectione aucun reservation");
			}
	    	RemplirBoxNumImmatriculation();
	    }
	    
	    @FXML
	    void AfficherLigne() {
	    	Reservation reservation = table_reservation.getSelectionModel().getSelectedItem();
	    	if(reservation != null) {
	    		String requet = "select * from reservation where num_reservation = ? ";
//		    	"select reservation.*, nom_complet from reservation, client,vehicule"
//				+ " where num_reservation = ? "
//				+ "and reservation.num_client = client.num_client "
//				+ "and reservation.num_immatriculation = vehicule.num_immatriculation";
		    	try {
					st = cnx.prepareStatement(requet);
					st.setInt(1, reservation.getNum_reservation());
					resultat = st.executeQuery();
					if(resultat.next()) {
						box_num_client.setValue(resultat.getInt("num_client"));
						txt_num_reservation.setText(resultat.getString("num_reservation"));
						java.sql.Date date_reservation = resultat.getDate("date_reservation");
						picker_date_reservation.setValue(date_reservation.toLocalDate());
						java.sql.Date date_depart = resultat.getDate("date_depart");
						picker_date_depart.setValue(date_depart.toLocalDate());
						java.sql.Date date_retour = resultat.getDate("date_retour");
						picker_date_retour.setValue(date_retour.toLocalDate());
						txt_search_reservation.setText(resultat.getString("num_reservation"));
						box_activated.setValue(resultat.getBoolean("activated"));
						box_num_immatriculation.setValue(resultat.getString("num_immatriculation"));
					} 
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("ereeeeeru en affichage Ligne");
				}
	    	}
	    }
	    
	    void AfficherReservations(){
	    	table_reservation.getItems().clear();
	    	String requet = "select * from Reservation";
	    	try {
				st = cnx.prepareStatement(requet);
				resultat = st.executeQuery();
				while(resultat.next()) {
					listReservation.add(new Reservation(resultat.getInt("num_reservation"),
							resultat.getDate("date_reservation"), resultat.getDate("date_depart"), resultat.getDate("date_retour"),
							resultat.getString("num_immatriculation"),resultat.getInt("num_client"),resultat.getBoolean("activated")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	num_reservation_col.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("num_reservation"));
			date_reservation_col.setCellValueFactory(new PropertyValueFactory<Reservation, Date>("date_reservation"));
			date_depart_col.setCellValueFactory(new PropertyValueFactory<Reservation, Date>("date_depart"));
			date_retour_col.setCellValueFactory(new PropertyValueFactory<Reservation, Date>("date_retour"));
			num_client_col.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("num_client"));
			num_immatriculation_col.setCellValueFactory(new PropertyValueFactory<Reservation, String>("num_immatriculation"));
			activated_col.setCellValueFactory(new PropertyValueFactory<Reservation, Boolean>("activated"));
			table_reservation.setItems(listReservation);
	    }
	    
	    @FXML
	    void GetNomComplet() {
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
					System.out.println("erreeuuur get nom complet");
				}
	    	}
	    }
	    

	    @FXML
	    void aide() {
	    		comment.setText("sera la date d'aujourdui par defaut");
	    }

	    @FXML
	    void vide() {
	    	comment.setText("");
	    }

	@Override
public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
		AfficherReservations();
		RemplirBoxNumImmatriculation();
		RemplirBoxNumClient();
	}

}
