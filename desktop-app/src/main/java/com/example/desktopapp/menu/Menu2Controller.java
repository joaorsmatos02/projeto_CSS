package com.example.desktopapp.menu;

import com.example.desktopapp.consult_projects.ConsultProjectsController;
import com.example.desktopapp.login.LoginController;
import com.example.desktopapp.mocks.PresentProjectMockController;
import com.example.desktopapp.support_projects.SupportProjectsController;
import com.example.desktopapp.vote_poll.VotePollController;
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

public class Menu2Controller {

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
    private Button presentProject;
    @FXML
    private Button chooseDelegate;

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
    }

    private void setUpGrid() {
        StackPane.setMargin(menuGrid, new Insets(height * 0.11, 0, 0, 0));
        menuGrid.setPadding(new Insets(height / 8, width * 0.15, height / 5, width * 0.15));
        menuGrid.setHgap(width * 0.005);
        menuGrid.setVgap(width * 0.005);
    }

    private void setUpButtons() {
        logOut.setPrefSize(width / 20, height / 20);
        presentProject.setPrefSize(width / 4, height / 5);
        chooseDelegate.setPrefSize(width / 4, height / 5);
    }

    @FXML
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu.fxml"));
        StackPane logIn = loader.load();
        MenuController controller = loader.<MenuController>getController();
        controller.setUp(primaryStage);
        primaryStage.getScene().setRoot(logIn);
    }

    @FXML
    public void presentProject() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/present_project.fxml"));
        StackPane root = loader.load();
        PresentProjectMockController controller = loader.<PresentProjectMockController>getController();
        controller.setUp(primaryStage);
        primaryStage.getScene().setRoot(root);
    }

    @FXML
    public void chooseDelegate() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/support_projects.fxml"));
        StackPane supportProjects = loader.load();
        SupportProjectsController controller = loader.<SupportProjectsController>getController();
        controller.setUp(primaryStage);
        primaryStage.getScene().setRoot(supportProjects);
    }

}
