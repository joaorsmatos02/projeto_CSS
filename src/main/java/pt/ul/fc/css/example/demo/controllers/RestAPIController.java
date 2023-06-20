package pt.ul.fc.css.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.dtos.VoteDTO;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.services.Democracia2ServiceImpl;


@RestController()
@RequestMapping("api")
public class RestAPIController {

  @Autowired private Democracia2ServiceImpl democracia2Service;

  @PostMapping("/checkuser")
  boolean userExists(@RequestBody String json) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonNode = objectMapper.readTree(json);
      String voterCC = jsonNode.get("voterCC").asText();
      democracia2Service.login(voterCC);
    } catch (VoterFoundException v) {
      return true;
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  @GetMapping("/polls")
  List<PollDTO> getPollsInCourse() {
    return democracia2Service.listPollsInCourse();
  }

  @GetMapping("/projects")
  List<ProjectDTO> getNotExpiredProjects() {
    return democracia2Service.getNotExpiredProjects();
  }

  @PostMapping("/support/{id}")
  ResponseEntity<?> supportLawProject(@RequestBody String json, @PathVariable long id) {
    ObjectMapper objectMapper = new ObjectMapper();

    try{
      System.out.println(json + "...........................");
      JsonNode jsonNode = objectMapper.readTree(json);
      String voterCC = jsonNode.get("voterCC").asText();
      System.out.println(voterCC + "...........................");
      democracia2Service.supportLawProject(voterCC, id);
    } catch (ProjectNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (VoterNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PollAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ParameterException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (VoterAlreadySupportsException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonMappingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/defaultvote/{id}")
  ResponseEntity<?> getDefaultVote(@RequestBody String json, @PathVariable long id){
    VoteDTO vote;
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      System.out.println(json);
      JsonNode jsonNode = objectMapper.readTree(json);
      String voterCC = jsonNode.get("voterCC").asText();
      vote = democracia2Service.getDefaultVote(voterCC, id);
    } catch (VoterNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (VoteNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PollNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (DelegateNotFoundException e) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    } catch (JsonMappingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(vote, HttpStatus.OK);
  }

  @PostMapping("/confirm-default-vote/{id}")
  ResponseEntity<?> confirmDefault(@RequestBody String json, @PathVariable long id){
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode jsonNode = objectMapper.readTree(json);
      String voterCC = jsonNode.get("voterCC").asText();
      boolean vote = jsonNode.get("vote").asBoolean();
      democracia2Service.confirmDefaultVote(voterCC, id, vote);
    } catch (VoteFoundException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonMappingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/vote/{id}")
  ResponseEntity<?> vote(@RequestBody String json, @PathVariable long id){
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode jsonNode = objectMapper.readTree(json);
      String voterCC = jsonNode.get("voterCC").asText();
      boolean vote = jsonNode.get("vote").asBoolean();

      democracia2Service.vote(vote, voterCC, id);
    } catch (VoteFoundException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (VoterNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (JsonMappingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
