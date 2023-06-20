package com.example.desktopapp.login;

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
import com.example.desktopapp.Main;
import com.example.desktopapp.main.RestAPIClientService;
import com.example.desktopapp.menu.MenuController;

public class LoginController {
	private double height;
	private double width;
	private Stage primaryStage;

	@FXML
	private StackPane pane;

	@FXML
	private HBox topHbox;

	@FXML
	private Label title;

	@FXML
	private VBox mainVbox;

	@FXML
	private Label loginLabel;

	@FXML
	private TextField CC;

	@FXML
	private Button loginButton;

	public void setUp(Stage primaryStage) {
		this.primaryStage = primaryStage;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpTopHbox();
		setUpLoginButton();
		setUpMainVbox();
		setUpCC();
	}

	private void setUpTopHbox() {
		StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
		HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));
	}

	private void setUpLoginButton() {
		loginButton.setPrefSize(width / 20, height / 20);
	}

	private void setUpMainVbox() {
		mainVbox.setMaxHeight(height / 5); // para nao interferir com botao de voltar
		mainVbox.setSpacing(height * 0.02);
	}

	private void setUpCC() {
		CC.setMaxWidth(width * 0.2);
	}

	@FXML
	public void loginHandler() throws Exception {
		if(RestAPIClientService.getInstance().logIn(CC.getText())){
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu.fxml"));
			StackPane root = loader.load();
			MenuController controller = loader.<MenuController>getController();
			controller.setUp(primaryStage);
			primaryStage.getScene().setRoot(root);
		}
	}
}
