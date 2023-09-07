package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable{

    @FXML
    private Button Admin_btn;

    @FXML
    private Button User_btn;

    @FXML
    private VBox vbox;
    
    @FXML
    private Button close_btn;
    @FXML
    private AnchorPane scenePane;
    
    private Parent fxml;
    
	Stage stage;
    

    @FXML
    void close(ActionEvent event) {
    	stage = (Stage) scenePane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void openSignAdmin() {
    	TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),vbox);
    	t.setToX(5);
    	t.play();
    	t.setOnFinished(e ->{
    		try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/SignAdmin.fxml"));
				vbox.getChildren().removeAll();
				vbox.getChildren().setAll(fxml);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	});
    }

    @FXML
    void openSignUser() {
		TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),vbox);
		t.setToX(vbox.getLayoutX() * 21);
		t.play();
		t.setOnFinished(e ->{
    		try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/SignUser.fxml"));
				vbox.getChildren().removeAll();
				vbox.getChildren().setAll(fxml);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	});

    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),vbox);
		t.setToX(vbox.getLayoutX() * 21);
		t.play();
		openSignUser();
		
	}

}
