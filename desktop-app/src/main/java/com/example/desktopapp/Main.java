package com.example.desktopapp;

import com.example.desktopapp.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Democracia 2");

			double height = Screen.getPrimary().getBounds().getHeight();
			double width = Screen.getPrimary().getBounds().getWidth();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
			StackPane root = loader.load();
			LoginController controller = loader.<LoginController>getController();
			Scene scene = new Scene(root, width * (2.0 / 3), height * (2.0 / 3));
			scene.getStylesheets().add("JavaFX.css");
			controller.setUp(primaryStage);

			primaryStage.setMinHeight(height * (7.0 / 12));
			primaryStage.setMinWidth(width * (7.0 / 12));
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
