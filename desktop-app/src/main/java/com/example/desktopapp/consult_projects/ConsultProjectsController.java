package com.example.desktopapp.consult_projects;

import com.example.desktopapp.TableRow;
import com.example.desktopapp.dtos.ProjectDTO;
import com.example.desktopapp.Main;
import com.example.desktopapp.main.RestAPIClientService;
import com.example.desktopapp.menu.MenuController;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ConsultProjectsController {

  private double height;
  private double width;
  private Stage primaryStage;

  @FXML private StackPane pane;

  @FXML private HBox topHbox;

  @FXML private Button goBack;

  @FXML private Label title;

  @FXML private TableView<TableRow> table; // Talvez seja preciso a classe TableRow

  public void setUp(Stage primaryStage) {
    this.primaryStage = primaryStage;

    height = Screen.getPrimary().getBounds().getHeight();
    width = Screen.getPrimary().getBounds().getWidth();

    setUpTopHbox();
    setUpGoBackButton();
    setUpTable();
  }

  private void setUpTopHbox() {
    StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
    HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));
  }

  private void setUpGoBackButton() {
    goBack.setPrefSize(width / 20, height / 20);
  }

  private void setUpTable() {
    StackPane.setMargin(
        table, new Insets(height * 0.16, width * 0.08, height * 0.07, width * 0.08));
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn<TableRow, String> tc1 = new TableColumn<>("id");
    TableColumn<TableRow, String> tc2 = new TableColumn<>("theme");
    TableColumn<TableRow, String> tc3 = new TableColumn<>("title");
    TableColumn<TableRow, String> tc4 = new TableColumn<>("description");
    TableColumn<TableRow, String> tc5 = new TableColumn<>("expirationDate");
    TableColumn<TableRow, String> tc6 = new TableColumn<>("nrSupporters");
    TableColumn<TableRow, String> tc7 = new TableColumn<>("delegateName");
    tc1.setCellValueFactory(new PropertyValueFactory<>("col1"));
    tc2.setCellValueFactory(new PropertyValueFactory<>("col2"));
    tc3.setCellValueFactory(new PropertyValueFactory<>("col3"));
    tc4.setCellValueFactory(new PropertyValueFactory<>("col4"));
    tc5.setCellValueFactory(new PropertyValueFactory<>("col5"));
    tc6.setCellValueFactory(new PropertyValueFactory<>("col6"));
    tc7.setCellValueFactory(new PropertyValueFactory<>("col7"));

    List<ProjectDTO> projects = RestAPIClientService.getInstance().consultProjects();

    table.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7);

    for(ProjectDTO project : projects) {
      TableRow t = new TableRow();
      t.setCol1(String.valueOf(project.getId()));
      t.setCol2(project.getTheme());
      t.setCol3(project.getTitle());
      t.setCol4(project.getDescription());
      t.setCol5(project.getExpirationDate().toString());
      t.setCol6(String.valueOf(project.getNrSupporters()));
      t.setCol7(project.getDelegateName());

      table.getItems().add(t);
    }

    table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
              try {
                FXMLLoader loader =
                    new FXMLLoader(
                        Main.class.getResource("/project_details.fxml"));
                StackPane root = loader.load();
                ProjectDetailsController controller =
                    loader.<ProjectDetailsController>getController();
                controller.setUp(primaryStage, table.getSelectionModel().getSelectedItem());
                primaryStage.getScene().setRoot(root);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
  }

  @FXML
  public void goBack() throws Exception {
    FXMLLoader loader =
        new FXMLLoader(Main.class.getResource("/menu.fxml"));
    StackPane root = loader.load();
    MenuController controller = loader.<MenuController>getController();
    controller.setUp(primaryStage);
    primaryStage.getScene().setRoot(root);
  }
}
