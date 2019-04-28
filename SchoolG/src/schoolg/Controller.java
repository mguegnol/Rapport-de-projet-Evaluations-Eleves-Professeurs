package schoolg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Controller {

	private static ArrayList<Promotion> all_promotions = new ArrayList<>();
	
	private static final AtomicInteger eleve_uuid = new AtomicInteger(1);

	@FXML
	private Label welcome_label;

	@FXML
	private ChoiceBox<Promotion> promotion_choice_box;

	@FXML
	private Button plus_student_button;

	@FXML
	private Button plus_chart_button;

	@FXML
	private Button plus_promotion_button;

	@FXML
	private TableView<Eleve> student_tableview;

	@FXML
	private TableColumn<Eleve, String> last_name;

	@FXML
	private TableColumn<Eleve, String> first_name;

	@FXML
	private TableColumn<Eleve, Float> median_grade;

	@FXML
	private TableColumn<Eleve, Float> average_grade;

	@FXML
	private TableColumn<Eleve, String> see_student_button;

	@FXML
	private TableColumn<Eleve, String> plus_grade_button;

	@FXML
	void initialize() {
		loadChoiceBoxItems();
		promotion_choice_box.getSelectionModel().selectedItemProperty().addListener( (v, old_value, new_value) -> choiceBoxValueChanged(old_value, new_value) );

		setCellValueFactory();
		//loadTableViewItems(promotion_choice_box.getValue()); // TODO Create a loader from file
	}

	/**
     * Fonction lié au bouton de création d'un graphique
     */
	@FXML
	void plusChartAction(ActionEvent event) {
		//Create chart for the selected promotion
		
		ObservableList<Eleve> eleves = student_tableview.getItems();
		Map<Number, Number> grade_stats_moyenne = new HashMap<Number, Number>();
		Map<Number, Number> grade_stats_medianne = new HashMap<Number, Number>();
		//Set up for the value
		for (int i = 0; i <= 20; i++) {
			grade_stats_moyenne.put(i, 0);
			grade_stats_medianne.put(i, 0);
		}
		//Count mediane & moyenne by promotion
		eleves.forEach((eleve) -> {
			int count = (int)grade_stats_moyenne.get(Math.round(eleve.getMoyenne()));
			count++;
			grade_stats_moyenne.put(Math.round(eleve.getMoyenne()), count);
			
			int countmed = (int)grade_stats_medianne.get(Math.round(eleve.getMediane()));
			countmed++;
			grade_stats_medianne.put(Math.round(eleve.getMediane()), countmed);
		});
		//Create chart
		Stage stage = new Stage();
		stage.setTitle("Moyenne & Medianne");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis(0, 20, 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value");
        yAxis.setLabel("Number of student");
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Moyenne & Medianne for each student");
        //defining a series
        Series<Number, Number> series = new XYChart.Series<Number,Number>();
        series.setName("Moyenne");
        
        Series<Number, Number> series2 = new XYChart.Series<Number,Number>();
        series2.setName("Medianne");
        //populating the series with data
        for (int i = 0; i <= 20; i++) {
        	series.getData().add(new XYChart.Data<Number,Number>(i, grade_stats_moyenne.get(i)));
        	series2.getData().add(new XYChart.Data<Number,Number>(i, grade_stats_medianne.get(i)));
		}
        
        Scene scene  = new Scene(lineChart,600,400);
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
       
        stage.setScene(scene);
        stage.show();
	}
	
	/**
     * Fonction lié au bouton de création d'une promotion
     */
	@FXML
	void plusPromotionAction(ActionEvent event) {
		//Create new promotion

		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("New Promotion");
		dialog.setHeaderText(null);
		dialog.setContentText("Promotion name:");

		Optional<String> result = dialog.showAndWait();
		//Récupere le nom de la promotion, la crée, la sauvegarde, l'ajoute à la liste déroulante, et charge la promo
		result.ifPresent(name -> {
			if (!name.isEmpty()) {
				Promotion new_promotion = new Promotion(name);
				all_promotions.add(new_promotion);
				promotion_choice_box.getItems().add(new_promotion);
				promotion_choice_box.setValue(new_promotion);
				// loadTableViewItems(name); //Done by the listener 
			}
		});

	}
	
	/**
     * Fonction lié à l'ajout d'un eleve
     */
	@FXML
	void plusStudentAction(ActionEvent event) { 
		//Verifie qu'il existe bien au moins une promotion sinon message erreur
		if (all_promotions.size() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Promotion Warning");
			alert.setHeaderText(null);
			alert.setContentText("You need at least one promotion to add a student. Please create a promotion first.");

			alert.showAndWait();
		}

		else {
			//Fenetre de création d'un eleve
			Dialog<Eleve> dialog = new Dialog<>();
			dialog.setTitle("New Student");
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			
			TextField first_name = new TextField("First Name");
			TextField last_name = new TextField("Last Name");
			
			//Création de la liste déroulante des promotions existantes
			ObservableList<Promotion> promotions_dialog_choicebox = FXCollections.observableArrayList(promotion_choice_box.getItems());
			ChoiceBox<Promotion> dialog_choicebox = new ChoiceBox<>();

			dialog_choicebox.setConverter(new StringConverter<Promotion>() {
				@Override
				public String toString(Promotion promotion) {
					return promotion.getNom();
				}

				@Override
				// not used but forced
				public Promotion fromString(String s) {
					return null ;
				}
			});

			dialog_choicebox.setItems(promotions_dialog_choicebox);

			dialog_choicebox.setValue(promotion_choice_box.getValue());
			
			//Interface graphique
			dialogPane.setContent(new VBox(8, last_name, first_name, dialog_choicebox));
			Platform.runLater(last_name::requestFocus);
			//Création eleve
			dialog.setResultConverter((ButtonType button) -> {
				if (button == ButtonType.OK) {
					return new Eleve(first_name.getText(), last_name.getText(), eleve_uuid.getAndIncrement(), dialog_choicebox.getValue());
				}
				return null;
			});
			Optional<Eleve> optionalResult = dialog.showAndWait();
			//Ajout de l'eleve
			optionalResult.ifPresent((Eleve new_eleve) -> {
				//dialog_choicebox.getValue().setEleves(new_eleve); // Automatically done by the converter return
				promotion_choice_box.setValue(dialog_choicebox.getValue());
				loadTableViewFromPromotion(promotion_choice_box.getValue());
			});
		}
	}
	
	/**
     * Load promotions in the choice box
     */
	private void loadChoiceBoxItems() {
		promotion_choice_box.setConverter(new StringConverter<Promotion>() {
			@Override
			public String toString(Promotion promotion) {
				return promotion.getNom();
			}

			@Override
			// not used but forced
			public Promotion fromString(String s) {
				return null ;
			}
		});

		ObservableList<Promotion> promotions = FXCollections.observableArrayList(all_promotions);

		promotion_choice_box.setItems(promotions);
		promotion_choice_box.getSelectionModel().selectFirst();
	}

	/**
     * Load promotion when choice box value changed
     * @param oldValue Previous selected item
     * @param newValue New selected item
     */
	private void choiceBoxValueChanged(Promotion oldValue, Promotion newValue) {
		loadTableViewFromPromotion(newValue);
	}
	
	/**
     * Set column of TableView
     */
	private void setCellValueFactory() {
		//Set cellValueFactory
		last_name.setCellValueFactory(new PropertyValueFactory<Eleve, String>("nom"));
		first_name.setCellValueFactory(new PropertyValueFactory<Eleve, String>("prenom"));
		median_grade.setCellValueFactory(new PropertyValueFactory<Eleve, Float>("mediane"));
		average_grade.setCellValueFactory(new PropertyValueFactory<Eleve, Float>("moyenne"));

		see_student_button.setCellValueFactory(new PropertyValueFactory<Eleve, String>(null));
		seeStudentButtonCellFactory();
		plus_grade_button.setCellValueFactory(new PropertyValueFactory<Eleve, String>(null));
		plusGradeButtonCellFactory();
	}
	
	/**
     * Create button in column to see student
     */
	private void seeStudentButtonCellFactory() {
		Callback<TableColumn<Eleve, String>, TableCell<Eleve, String>> cellFactory = new Callback<TableColumn<Eleve, String>, TableCell<Eleve, String>>() {
			@Override
			public TableCell<Eleve, String> call(final TableColumn<Eleve, String> param) {
				final TableCell<Eleve, String> cell = new TableCell<Eleve, String>() {
					
					//Create button
					Image see_image = new Image("/img/view.png", 16, 16, true, false);
					final Button btn = new Button("See", new ImageView(see_image));

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							//Set action when button click
							btn.setOnAction(event -> {
								Eleve eleve = getTableView().getItems().get(getIndex()); //Eleve we clicked on
								
								//Load graphic interface
								Stage primaryStage = new Stage();
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeeGrades.fxml"));
						        Parent root;
								try {
									root = loader.load();
									
									EvaluationsController evaluationsController = loader.getController();
									evaluationsController.setup(eleve); //Setup the controller
							        primaryStage.setTitle("Evaluations");
							        primaryStage.setScene(new Scene(root, 500, 300));
							        primaryStage.setResizable(false);
							        //Close event to calculate Mediane & Moyenne
							        primaryStage.setOnCloseRequest(closeEvent -> {
							            System.out.println("Stage is closing");
							            try {
							            	 Evaluation.calculMediane(eleve);
							            	 Evaluation.calculMoyenne(eleve);
										} catch (IllegalStateException e) {
											eleve.setMediane(0);
											eleve.setMoyenne(0);
										}
							           
							            student_tableview.refresh();
							        });
							        primaryStage.show();
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		see_student_button.setCellFactory(cellFactory);
	}
	
	/**
     * Create button in column to add student garde
     */
	private void plusGradeButtonCellFactory() {

		Callback<TableColumn<Eleve, String>, TableCell<Eleve, String>> cellFactory = new Callback<TableColumn<Eleve, String>, TableCell<Eleve, String>>() {
			@Override
			public TableCell<Eleve, String> call(final TableColumn<Eleve, String> param) {
				final TableCell<Eleve, String> cell = new TableCell<Eleve, String>() {

					//Setup butotn
					Image plus_image = new Image("/img/add-plus-button-3.png", 10, 10, true, false);
					final Button btn = new Button("Grade", new ImageView(plus_image));

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							//Event on button click
							btn.setOnAction(event -> {
								Eleve currentEleve = getTableView().getItems().get(getIndex()); //Clicked ELeve
								//Setup dialog box
								Dialog<List<String>> dialog = new Dialog<>();
								dialog.setTitle("New Grade");
								dialog.setHeaderText(null);
								DialogPane dialogPane = dialog.getDialogPane();
								dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
								
								
								TextField matiere = new TextField("Matiere");
								TextField grade = new TextField("Grade");
								//Bind isValidGrade to OK button to ensure good grade format
								BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> !isValideGrade(grade.getText()), grade.textProperty());
								dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(isInvalid);
								
								dialogPane.setContent(new VBox(8, matiere, grade));

								Platform.runLater(matiere::requestFocus);
								dialog.setResultConverter(dialogButton -> {
								    List<String> result = new ArrayList<>();
								    result.add(matiere.getText());
								    result.add(grade.getText());
								    return result ;
								});
								Optional<List<String>> optionalResult = dialog.showAndWait();
								//Set new grade
								optionalResult.ifPresent((List<String> result) -> {
									LoginController.connectedProfesseur.setNote(result.get(0), Float.parseFloat(result.get(1)), currentEleve.getUid(), promotion_choice_box.getValue());
								});
								student_tableview.refresh();
							});
	
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		plus_grade_button.setCellFactory(cellFactory);
	}

	/**
     * Load TableView for a given Promotion
     * @param promotion The promotion we want to load
     */
	private void loadTableViewFromPromotion(Promotion promotion) {
		//Get student, load them and refresh
		ObservableList<Eleve> eleves = FXCollections.observableArrayList(promotion.getEleves());
		student_tableview.setItems(eleves);
		student_tableview.refresh();
	}
	
	/**
     * Check if an input grade in the dialog box is valid
     * @param str The string format grade we want to check
     */
	private Boolean isValideGrade(String str) {
		//Try to convert into Int or Float and check if the grade is between 0 and 20
		try {
	        int grade = Integer.parseInt(str);
	        if (grade <= 20.0 && grade >= 0) {
	        	return true;
	        } else {
				return false;
			}
	    } catch(NumberFormatException e) {
	    	try {
		        Float grade = Float.parseFloat(str);
		        if (grade <= 20.0 && grade >= 0.0) {
		        	return true;
		        } else {
					return false;
				}
		    } catch(NumberFormatException ee) {
		        return false;
		    }	
	    }
	}
}
