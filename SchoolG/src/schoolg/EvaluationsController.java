package schoolg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class EvaluationsController {
	
	Boolean isStudentMode = false;

    @FXML
    private Label evaluation_student_label;

    @FXML
    private TableView<Evaluation> evaluations_tableview;

    @FXML
    private TableColumn<Evaluation, String> matiere;
    
    @FXML
    private TableColumn<Evaluation, Float> grade;

    @FXML
    private TableColumn<Evaluation, Professeur> corrector;

    @FXML
    private TableColumn<Evaluation, String> edit_grade_button;
    
    @FXML
	void initialize() {
		
	}
    
    /**
     * Load column, set header for student name and load the tableview
     * @param eleve The student we want to see
     */
    public void setup(Eleve eleve) {
    	setCellValueFactory();
    	evaluation_student_label.setText("Evaluations for " + eleve.getNom() + " " + eleve.getPrenom());
    	loadTableViewFromStudent(eleve);
	}
    /**
     * Used for the Student Mode
     * Load column, set header for student name and load the tableview
     * @param uid The student uid we want to see
     */
    public void setup(int uid) {
    	isStudentMode = true;
    	setCellValueFactory();
    	evaluation_student_label.setText("Evaluations for Jeanne Doe");
    	loadTableViewFromUid(uid);
	}
    
    /**
     * Set tableview column
     */
    private void setCellValueFactory() {
		//Set cellValueFactory
    	matiere.setCellValueFactory(new PropertyValueFactory<Evaluation, String>("matiere"));
    	grade.setCellValueFactory(new PropertyValueFactory<Evaluation, Float>("note"));
    	corrector.setCellValueFactory(new PropertyValueFactory<Evaluation, Professeur>("professeur_correcteur"));

    	//Don't show edit button if Student Mode
    	if (!isStudentMode) {
    		edit_grade_button.setCellValueFactory(new PropertyValueFactory<Evaluation, String>(null));
    		editGradeButtonCellFactory();
    	}
	}
    
    /**
     * Create button in column
     */
	private void editGradeButtonCellFactory() {
		
		Callback<TableColumn<Evaluation, String>, TableCell<Evaluation, String>> cellFactory = new Callback<TableColumn<Evaluation, String>, TableCell<Evaluation, String>>() {
			@Override
			public TableCell<Evaluation, String> call(final TableColumn<Evaluation, String> param) {
				final TableCell<Evaluation, String> cell = new TableCell<Evaluation, String>() {
					
					//Button setup
					Image plus_image = new Image("/img/pencil-edit-button.png", 10, 10, true, false);
					final Button btn = new Button("Edit", new ImageView(plus_image));

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							//Action on button click
							btn.setOnAction(event -> {
								Evaluation currentEvaluation = getTableView().getItems().get(getIndex()); //Evaluation we clicked on
								//Setup dialog box
								Dialog<List<String>> dialog = new Dialog<>();
								dialog.setTitle("Edit Grade");
								dialog.setHeaderText(null);
								DialogPane dialogPane = dialog.getDialogPane();
								dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
								
								//Set up interface
								Label matiere = new Label(currentEvaluation.getMatiere());
								TextField grade = new TextField(Float.toString(currentEvaluation.getNote()));
								Image plus_image = new Image("/img/delete.png", 10, 10, true, false);
								final Button delete_button = new Button("Delete", new ImageView(plus_image));
								
								//Delete grade button setup
								delete_button.setOnAction(delete_event -> {
									//Delete Evaluation
									Evaluation.delete_evaluation(currentEvaluation);
									evaluations_tableview.getItems().remove(currentEvaluation);
									dialog.setResultConverter(quit_event -> {
										return null;
									});
									dialog.close();
								});
								
								dialogPane.setContent(new VBox(8, matiere, grade, delete_button));

								Platform.runLater(matiere::requestFocus);
								dialog.setResultConverter(dialogButton -> {
								    List<String> result = new ArrayList<>();
								    result.add(matiere.getText());
								    result.add(grade.getText());
								    return result ;
								});
								Optional<List<String>> optionalResult = dialog.showAndWait();
								optionalResult.ifPresent((List<String> result) -> {
									//Set edited garde
									currentEvaluation.setNote(Float.parseFloat(result.get(1)));
								});
								evaluations_tableview.refresh();
							});
	
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		edit_grade_button.setCellFactory(cellFactory);
		
	}
	/**
     * Load tableview from given student
     * @param eleve Given student
     */
	private void loadTableViewFromStudent(Eleve eleve) {

		ObservableList<Evaluation> evaluations = FXCollections.observableArrayList(Evaluation.getEvaluations(eleve.getUid()));
		evaluations_tableview.setItems(evaluations);
		evaluations_tableview.refresh();
	}
	
	/**
	 * Used for Student Mode
     * Load tableview from given uid
     * @param uid Given student uid
     */
	private void loadTableViewFromUid(int uid) {
		//Create static student and professeur and load them in tableview
		ArrayList<Evaluation> evaluations_array = new ArrayList<Evaluation>();
		Eleve eleve = new Eleve("Jeanne", "Doe", uid);
		Professeur professeur = new Professeur("Prof of", "Math");
		
		evaluations_array.add(new Evaluation("Math", 10, eleve, professeur));
		evaluations_array.add(new Evaluation("Math", 12, eleve, professeur));
		evaluations_array.add(new Evaluation("Math", 17, eleve, professeur));
		evaluations_array.add(new Evaluation("Math", 4, eleve, professeur));
		evaluations_array.add(new Evaluation("Math", 15, eleve, professeur));

		ObservableList<Evaluation> evaluations = FXCollections.observableArrayList(Evaluation.getEvaluations(eleve.getUid()));
		evaluations_tableview.setItems(evaluations);
		evaluations_tableview.refresh();
	}

}
