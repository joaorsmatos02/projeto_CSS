package com.example.desktopapp.menu;

import com.example.desktopapp.consult_projects.ConsultProjectsController;
import com.example.desktopapp.login.LoginController;
import com.example.desktopapp.support_projects.SupportProjectsController;
import com.example.desktopapp.vote_poll.VotePollController;
import com.example.desktopapp.menu.Menu2Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.example.desktopapp.list_polls.ListPollsController;
import com.example.desktopapp.Main;
import com.example.desktopapp.main.RestAPIClientService;

public class MenuController {

	private double height;
	private double width;
	private Stage primaryStage;

	@FXML
	private StackPane pane;

	@FXML
	private HBox topHbox;

	@FXML
	private Button logOut;

	@FXML
	private Button moreOptions;

	@FXML
	private Label title;

	@FXML
	private GridPane menuGrid;

	@FXML
	private Button listPolls;

	@FXML
	private Button consultProjects;

	@FXML
	private Button supportProjects;

	@FXML
	private Button votePoll;

	public void setUp(Stage primaryStage) {
		this.primaryStage = primaryStage;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpGrid();
		setUpButtons();
		setUpTopHbox();
	}

	private void setUpTopHbox() {
		StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
		HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));
		HBox.setMargin(moreOptions, new Insets(0, 0, 0, width * 0.02));
	}

	private void setUpGrid() {
		StackPane.setMargin(menuGrid, new Insets(height * 0.11, 0, 0, 0));
		menuGrid.setPadding(new Insets(height / 8, width * 0.15, height / 5, width * 0.15));
		menuGrid.setHgap(width * 0.005);
		menuGrid.setVgap(width * 0.005);
	}

	private void setUpButtons() {
		logOut.setPrefSize(width / 20, height / 20);
		moreOptions.setPrefSize(width / 20, height / 20);
		listPolls.setPrefSize(width / 4, height / 5);
		consultProjects.setPrefSize(width / 4, height / 5);
		supportProjects.setPrefSize(width / 4, height / 5);
		votePoll.setPrefSize(width / 4, height / 5);
	}

	@FXML
	public void logOut() throws Exception {
		RestAPIClientService.getInstance().logOut();

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/login.fxml"));
		StackPane logIn = loader.load();
		LoginController controller = loader.<LoginController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(logIn);
	}

	@FXML
	public void moreOptions() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu2.fxml"));
		StackPane logIn = loader.load();
		Menu2Controller controller = loader.<Menu2Controller>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(logIn);
	}

	@FXML
	public void listPolls() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/list_polls.fxml"));
		StackPane listPolls = loader.load();
		ListPollsController controller = loader.<ListPollsController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(listPolls);
	}

	@FXML
	public void consultProjects() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/consult_projects.fxml"));
		StackPane consultProjects = loader.load();
		ConsultProjectsController controller = loader.<ConsultProjectsController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(consultProjects);
	}

	@FXML
	public void supportProjects() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/support_projects.fxml"));
		StackPane supportProjects = loader.load();
		SupportProjectsController controller = loader.<SupportProjectsController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(supportProjects);
	}

	@FXML
	public void votePoll() throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vote_poll.fxml"));
		StackPane votePoll = loader.load();
		VotePollController controller = loader.<VotePollController>getController();
		controller.setUp(primaryStage);
		primaryStage.getScene().setRoot(votePoll);
	}

}
