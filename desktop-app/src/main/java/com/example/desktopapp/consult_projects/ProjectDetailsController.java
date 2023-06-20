package com.example.desktopapp.consult_projects;

import com.example.desktopapp.TableRow;
import com.example.desktopapp.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ProjectDetailsController {

	private double height;
	private double width;
	private Stage primaryStage;
	private TableRow project;

	@FXML
	private StackPane pane;

	@FXML
	private HBox topHbox;

	@FXML
	private Button goBack;

	@FXML
	private Label title;

	@FXML
	private VBox mainVbox;

	@FXML
	private Label id;

	@FXML
	private Label theme;

	@FXML
	private Label projectTitle;

	@FXML
	private Label description;

	@FXML
	private Label expiration;

	@FXML
	private Label nrSupporters;

	@FXML
	private Label delegate;

	public void setUp(Stage primaryStage, TableRow project) {
		this.primaryStage = primaryStage;
		this.project = project;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpTopHbox();
		setUpMainVbox();
		setUpGoBackButton();
		setUpLabels();
	}

	private void setUpTopHbox() {
		StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
		HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));
	}

	private void setUpMainVbox() {
		StackPane.setMargin(mainVbox, new Insets(height * 0.15, 0, height * 0.15, 0));
		mainVbox.setSpacing(height * 0.02);
	}

	private void setUpGoBackButton() {
		goBack.setPrefSize(width / 20, height / 20);
	}

	private void setUpLabels() {
		id.setText("Id: " + project.getCol1());
		theme.setText("Tema: " + project.getCol2());
		projectTitle.setText("Titulo: " + project.getCol3());
		description.setText("Descricao: " + project.getCol4());
		expiration.setText("Data de validade: " + project.getCol5());
		nrSupporters.setText("Nr apoiantes: " + project.getCol6());
		delegate.setText("Delegado: " + project.getCol7());
	}

	@FXML
	public void goBack() throws Exception {
		FXMLLoader loader = new FXMLLoader(
				Main.class.getResource("/consult_projects.fxml"));
		StackPane root = loader.load();
		ConsultProjectsController controller = loader.<ConsultProjectsController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(root);
	}
}
