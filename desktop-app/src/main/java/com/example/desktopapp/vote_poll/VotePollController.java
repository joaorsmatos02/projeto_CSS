package com.example.desktopapp.vote_poll;

import java.util.List;


import com.example.desktopapp.TableRow;
import com.example.desktopapp.dtos.PollDTO;
import com.example.desktopapp.dtos.VoteDTO;
import com.example.desktopapp.Main;
import com.example.desktopapp.main.RestAPIClientService;
import com.example.desktopapp.menu.MenuController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class VotePollController {

	private double height;
	private double width;
	private Stage primaryStage;
	private TableRow currentPoll;
	private VoteDTO defaultVote;
	private ToggleGroup toggleGroup;

	@FXML
	private StackPane pane;

	@FXML
	private HBox topHbox;

	@FXML
	private Button goBack;

	@FXML
	private Label title;

	@FXML
	private GridPane mainGrid;

	@FXML
	private TableView<TableRow> table;

	@FXML
	private VBox mainVbox;

	@FXML
	private Label voteLabel;

	@FXML
	private RadioButton omissionButton;

	@FXML
	private RadioButton approveButton;

	@FXML
	private RadioButton disapproveButton;

	@FXML
	private Button confirm;

	public void setUp(Stage primaryStage) {

		this.primaryStage = primaryStage;

		height = Screen.getPrimary().getBounds().getHeight();
		width = Screen.getPrimary().getBounds().getWidth();

		setUpTopHbox();
		setUpButtons();
		setUpMainVbox();
		setUpMainGrid();
		setUpTable();
		setUpRadioButtons();
	}

	private void setUpTopHbox() {
		StackPane.setMargin(topHbox, new Insets(height * 0.06, 0, 0, width * 0.04));
		HBox.setMargin(title, new Insets(0, 0, 0, width * 0.04));

	}

	private void setUpButtons() {
		goBack.setPrefSize(width / 20, height / 20);
		confirm.setPrefSize(width / 10, height / 20);
	}

	private void setUpMainVbox() {
		mainVbox.setMaxWidth(width / 3);
		mainVbox.setSpacing(height * 0.03);
	}

	private void setUpMainGrid() {
		StackPane.setMargin(mainGrid, new Insets(height * 0.16, width * 0.04, height * 0.07, width * 0.04));
		ColumnConstraints t = new ColumnConstraints();
		t.setPercentWidth(60);
		ColumnConstraints i = new ColumnConstraints();
		i.setPercentWidth(40);
		mainGrid.getColumnConstraints().addAll(t, i);
	}

	private void setUpTable() {
		table.setPrefHeight(height);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<TableRow, String> tc1 = new TableColumn<>("id");
		TableColumn<TableRow, String> tc2 = new TableColumn<>("theme");
		TableColumn<TableRow, String> tc3 = new TableColumn<>("title");
		TableColumn<TableRow, String> tc4 = new TableColumn<>("description");
		TableColumn<TableRow, String> tc5 = new TableColumn<>("expirationDate");
		tc1.setCellValueFactory(new PropertyValueFactory<>("col1"));
		tc2.setCellValueFactory(new PropertyValueFactory<>("col2"));
		tc3.setCellValueFactory(new PropertyValueFactory<>("col3"));
		tc4.setCellValueFactory(new PropertyValueFactory<>("col4"));
		tc5.setCellValueFactory(new PropertyValueFactory<>("col5"));

		table.getColumns().addAll(tc1, tc2, tc3, tc4, tc5);

		List<PollDTO> polls = RestAPIClientService.getInstance().listPolls();

		for (PollDTO poll : polls) {
			TableRow t = new TableRow();
			t.setCol1(String.valueOf(poll.getId()));
			t.setCol2(poll.getTheme());
			t.setCol3(poll.getTitle());
			t.setCol4(poll.getDescription());
			t.setCol5(poll.getExpirationDate().toString());

			table.getItems().add(t);
		}

		table.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				currentPoll = table.getSelectionModel().getSelectedItem();

				TableRow item = table.getSelectionModel().getSelectedItem();

				if(item != null)
					defaultVote = RestAPIClientService.getInstance()
							.getDefaultVote(Long.parseLong(item.getCol1()));
				if (defaultVote != null && defaultVote.isContent())
					voteLabel.setText("Voto por omissão: Aprovar");
				else if (defaultVote != null && !defaultVote.isContent())
					voteLabel.setText("Voto por omissão: Reprovar");
			}
		});
	}

	private void setUpRadioButtons() {
		toggleGroup = new ToggleGroup();
		omissionButton.setToggleGroup(toggleGroup);
		approveButton.setToggleGroup(toggleGroup);
		disapproveButton.setToggleGroup(toggleGroup);
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
	public void confirm() {
		if (omissionButton.isSelected() && defaultVote != null)
			RestAPIClientService.getInstance().confirmDefaultVote(defaultVote.isContent(), Long.parseLong(currentPoll.getCol1()));
		else if (approveButton.isSelected() && currentPoll != null)
			RestAPIClientService.getInstance().vote(true, Long.parseLong(currentPoll.getCol1()));
		else if (disapproveButton.isSelected() && currentPoll != null)
			RestAPIClientService.getInstance().vote(false, Long.parseLong(currentPoll.getCol1()));
		toggleGroup.selectToggle(null);
		currentPoll = null;
		defaultVote = null;
		voteLabel.setText("Voto por omissão: ---");
	}

}
