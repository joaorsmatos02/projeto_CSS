package com.example.desktopapp.support_projects;

import com.example.desktopapp.Main;
import com.example.desktopapp.main.RestAPIClientService;
import com.example.desktopapp.menu.MenuController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SupportProjectsController {

	private double height;
	private double width;
	private Stage primaryStage;

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
	private Label projectLabel;

	@FXML
	private TextField project;

	@FXML
	private Button send;

	public void setUp(Stage primaryStage) {
		this.primaryStage = primaryStage;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpTopHbox();
		setUpButtons();
		setUpMainVbox();
		setUpProject();
	}

	private void setUpTopHbox() {
		StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
		HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));
	}

	private void setUpButtons() {
		goBack.setPrefSize(width / 20, height / 20);
		send.setPrefSize(width / 20, height / 20);
	}

	private void setUpMainVbox() {
		mainVbox.setMaxHeight(height / 5); // para nao interferir com botao de voltar
		mainVbox.setSpacing(height * 0.02);
	}

	private void setUpProject() {
		project.setMaxWidth(width * 0.2);
	}

	@FXML
	public void goBack() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu.fxml"));
		StackPane root = loader.load();
		MenuController controller = loader.<MenuController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(root);
	}

	@FXML
	public void sendHandler() {
		RestAPIClientService.getInstance().supportProject(project.getText());
		project.clear();
	}

}
