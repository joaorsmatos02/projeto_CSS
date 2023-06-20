package com.example.desktopapp.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.desktopapp.dtos.PollDTO;
import com.example.desktopapp.dtos.ProjectDTO;
import com.example.desktopapp.dtos.VoteDTO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RestAPIClientService {

	private String voterCC;

	private static RestAPIClientService instance;

	private RestAPIClientService() {

	}

	public static RestAPIClientService getInstance() {
		if (instance == null)
			instance = new RestAPIClientService();
		return instance;
	}

	public boolean logIn(String voterCC) {
		try {
			URL url = new URL("http://localhost:8080/api/checkuser");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			String requestBody = "{\"voterCC\": \"" + voterCC + "\"}";
			OutputStream outputStream = con.getOutputStream();
			outputStream.write(requestBody.getBytes("UTF-8"));
			outputStream.close();

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				this.voterCC = voterCC;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Sucesso");
				alert.setHeaderText("Login efetuado com sucesso");
				alert.show();
				return true;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Utilizador nao existe");
				alert.show();
				return false;
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao com o servidor");
			alert.show();
			return false;
		}
	}

	public void logOut() {
		this.voterCC = null;
	}

	public boolean isLoggedIn() {
		return voterCC != null;
	}

	public List<PollDTO> listPolls() {
		try {
			URL url = new URL("http://localhost:8080/api/polls");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				List<PollDTO> pollDTOs = objectMapper.readValue(response.toString(), new TypeReference<List<PollDTO>>() {});
				return pollDTOs;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao obter lista de votacoes");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}

		return new ArrayList<>();
	}

	public List<ProjectDTO> consultProjects() {
		try {
			URL url = new URL("http://localhost:8080/api/projects");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());

				List<ProjectDTO> projectDTOs = objectMapper.readValue(response.toString(), new TypeReference<List<ProjectDTO>>() {});
				return projectDTOs;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao obter lista de projetos");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}

		return new ArrayList<>();
	}

	public void supportProject(String id) {
		try {
			URL url = new URL("http://localhost:8080/api/support/" + id);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			String requestBody = "{\"voterCC\": \"" + voterCC + "\"}";
			OutputStream outputStream = con.getOutputStream();
			outputStream.write(requestBody.getBytes("UTF-8"));
			outputStream.close();

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao suportar projeto");
				alert.show();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Sucesso");
				alert.setHeaderText("Projeto apoiado com sucesso");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}
	}

	public VoteDTO getDefaultVote(long pollID) {
		try {
			URL url = new URL("http://localhost:8080/api/defaultvote/" + pollID);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			String requestBody = "{\"voterCC\": \"" + voterCC + "\"}";
			OutputStream outputStream = con.getOutputStream();
			outputStream.write(requestBody.getBytes("UTF-8"));
			outputStream.close();

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ObjectMapper objectMapper = new ObjectMapper();
				return objectMapper.readValue(response.toString(), VoteDTO.class);
			}
			if(responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				return null;
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao obter voto default");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}

		return null;
	}

	public void confirmDefaultVote(boolean vote, long pollID) {
		try {
			URL url = new URL("http://localhost:8080/api/confirm-default-vote/" + pollID);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + vote + "}";
			OutputStream outputStream = con.getOutputStream();
			outputStream.write(requestBody.getBytes("UTF-8"));
			outputStream.close();

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao submeter voto");
				alert.show();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Sucesso");
				alert.setHeaderText("Voto submetido com sucesso");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}
	}

	public void vote(boolean vote, long pollID) {
		try {
			URL url = new URL("http://localhost:8080/api/vote/" + pollID);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + vote + "}";

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(requestBody.getBytes("UTF-8"));
			outputStream.close();

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao submeter voto");

				alert.show();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.getDialogPane().getStylesheets().add("/JavaFX.css");
				alert.setTitle("Sucesso");
				alert.setHeaderText("Voto submetido com sucesso");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().getStylesheets().add("/JavaFX.css");
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao estabelecer conexao ao servidor");
			alert.show();
		}
	}

}
