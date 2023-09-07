package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignAdminController implements Initializable{


    @FXML
    private VBox vbox;
	
    @FXML
    private TextField text_adminEmail;

    @FXML
    private PasswordField text_adminPassword;
    private Parent fxml;

    @FXML
    void openHomeAdmin(ActionEvent event) {
    	
       	String email = text_adminEmail.getText();
       	
    	String password = text_adminPassword.getText();
    	
    	/*if(email.equals("kamal") && password.equals("kamal")) {*/
    		
    		System.out.println("bien");
    		
    		vbox.getScene().getWindow().hide();
    		
    		Stage home_admin = new Stage();
    			try {
					fxml = FXMLLoader.load(getClass().getResource("/interfaces/HomeAdmin.fxml"));
					
					Scene scene = new Scene(fxml);
					scene.setFill(Color.TRANSPARENT);
					
					home_admin.setScene(scene);
					home_admin.initStyle(StageStyle.TRANSPARENT);
					
					home_admin.show();
					
				} catch (IOException e) {
					
					e.printStackTrace();
					System.out.println("ereuuur connection admin");
				}
    		
    	/*}
    	else {
    		System.out.println("error");
    	}*/
    	
    	
    }


	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
