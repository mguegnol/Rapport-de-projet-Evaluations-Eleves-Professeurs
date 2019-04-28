package schoolg;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	public boolean is_student_mode = false;
	
	public static Professeur connectedProfesseur;
	public static Eleve connectedEleve;

	@FXML
	private TextField last_name_textfield;

	@FXML
	private TextField first_name_textfield;

	@FXML
	private Label last_name_label;

	@FXML
	private Label first_name_label;

	@FXML
	private RadioButton student_mode_radiobutton;

	@FXML
	private Label uuid_label;

	@FXML
	private TextField uuid_textfield;

	@FXML
	private Button enter_button;
	
	void enterSchoolG(Node source) throws IOException {
		Stage loginStage = (Stage)source.getScene().getWindow();
		loginStage.close();
		    
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/JAVA2.fxml"));
        primaryStage.setTitle("SchoolG");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	/**
     * Clicked enter button
     */
	@FXML
	void enterButtonAction(ActionEvent event) throws Exception {
		//Check for student mode
		if(!is_student_mode) {
			//Check for professeur format
			if (!first_name_textfield.getText().isEmpty() &&  !last_name_textfield.getText().isEmpty()) {
				connectedProfesseur = new Professeur(first_name_textfield.getText(), last_name_textfield.getText());
				enterSchoolG((Node)event.getSource());
			}
			else {
				System.out.println("Empty");
			}
		}
		else {
			//Student Mode
			//Check for format
			if(!uuid_textfield.getText().isEmpty()) {
				//Load see grade view
				Stage primaryStage = new Stage();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeeGrades.fxml"));
		        Parent root;
				try {
					root = loader.load();
					EvaluationsController evaluationsController = loader.getController();
					evaluationsController.setup(Integer.parseInt(uuid_textfield.getText()));
			        primaryStage.setTitle("Evaluations");
			        primaryStage.setScene(new Scene(root, 500, 300));
			        primaryStage.setResizable(false);
			        primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

	}
	
	/**
     * On ration button click
     * Hide or show appropriate text field
     */
	@FXML
	void studentModeAction(ActionEvent event) {
		if (!is_student_mode) {
			last_name_label.setVisible(false);
			last_name_textfield.setVisible(false);

			first_name_label.setVisible(false);
			first_name_textfield.setVisible(false);

			uuid_label.setVisible(true);
			uuid_textfield.setVisible(true);
			
			is_student_mode = true;
		}
		else {
			uuid_label.setVisible(false);
			uuid_textfield.setVisible(false);
			
			last_name_label.setVisible(true);
			last_name_textfield.setVisible(true);

			first_name_label.setVisible(true);
			first_name_textfield.setVisible(true);
			
			is_student_mode = false;
		}

	}

	@FXML
	void initialize() {
		enter_button.setDefaultButton(true);
	}

}
