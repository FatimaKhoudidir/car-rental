package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignUserController implements Initializable{

	Connection cnx;
	public PreparedStatement st; //retourne le nombre de ligne trouver d'un requette
	public ResultSet resultat;

    @FXML
    private VBox vbox;

    @FXML
    private TextField text_userEmail;

    @FXML
    private PasswordField text_userPassword;

    @FXML
    private Button valider_btn;
    
    private Parent fxml;
    
    @FXML
    private Label txt_erreur_connexion;
    
    private static int Connected_user;
    
    @FXML
    public void openHomeUser() {
    	String email = text_userEmail.getText();
    	String password = text_userPassword.getText();
    	String requet = "select gmail, password, num_utilisateur, suspend from user";
    	try {
			st = cnx.prepareStatement(requet);
			resultat = st.executeQuery();
			
			boolean valide = false;
			
				while(resultat.next()) {
					if(email.equals(resultat.getString("gmail")) && password.equals(resultat.getString("password")) && !resultat.getBoolean("suspend")) {
			    		System.out.println("login succesfully");
			    		Connected_user = resultat.getInt("num_utilisateur");
			    		System.out.println(Connected_user);
			    		vbox.getScene().getWindow().hide();
			    		Stage home_user = new Stage();
			    			try {
								fxml = FXMLLoader.load(getClass().getResource("/interfaces/HomeUser.fxml"));
								Scene scene = new Scene(fxml);
								scene.setFill(Color.TRANSPARENT); //to make the it transparent
								home_user.setScene(scene);
								home_user.initStyle(StageStyle.TRANSPARENT);
								home_user.show();
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("une erreur geting interface Home User");
							}
			    			valide = true;
			    		break;
			    	}
				}
				if(!valide) {
				    txt_erreur_connexion.setText("Un problème est survenu lors de la connexion. Vérifiez votre adresse e-mail et votre mot de passe");
				}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

    }
    
    public static int GetUser() {
		return Connected_user;
    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnectionMysql.connectToDB();
	}
		
	


}
