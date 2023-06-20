package pt.ul.fc.css.example.demo.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.services.Democracia2ServiceImpl;

@Controller
public class WebController {

  Logger logger = LoggerFactory.getLogger(WebController.class);
  @Autowired Democracia2ServiceImpl democracia2Service;

  public WebController() {
    super();
  }

  String voterCC = "";

  @GetMapping({"/", "/login"})
  public String index() {
    populateBD();
    return "login";
  }

  @GetMapping("/logout")
  public String logout() {

    if (voterCC.length() > 1) {
      voterCC = "";
    }
    return "redirect:/login";
  }

  @PostMapping("/menu")
  public String login(final Model model, @RequestParam("voterCC") String voterCC) {
    populateBD();
    try {
      democracia2Service.login(voterCC);
    } catch (VoterFoundException e) {
      this.voterCC = voterCC;
      return "first_page";
    }

    model.addAttribute("error", "Error: O CC introduzido não existe no sistema");
    return "redirect:/login";
  }

  private void populateBD() {
    democracia2Service.populate();
  }

  @GetMapping("/menu")
  public String loginSucess() {
    if (voterCC.length() < 1) {
      return "redirect:/login";
    }
    return "first_page";
  }

  @GetMapping("/polls")
  public String getPollsInCourse(final Model model) {
    model.addAttribute("polls", democracia2Service.listPollsInCourse());
    return "list_polls";
  }

  @GetMapping("/create-project")
  public String createProjectPage(final Model model) {
    model.addAttribute("themes", democracia2Service.getThemes());
    return "create_project";
  }

  @PostMapping("/create-project")
  public String createProject(
      final Model model,
      @RequestParam("title") String title,
      @RequestParam("selectedTheme") Long themeID,
      @RequestParam("description") String description,
      @RequestParam("projectDate") String projectDate,
      @RequestParam("pdf") MultipartFile file)
      throws IOException {

    byte[] fileBytes = file.getBytes();

    LocalDateTime localDateTime = LocalDateTime.parse(projectDate);

    try {
      democracia2Service.presentLawProject(
          themeID, title, description, fileBytes, localDateTime, voterCC);
    } catch (VoterNotFoundException e) {
      model.addAttribute("error", "Erro: Não é um Delegado para criar um Projeto");
      model.addAttribute("themes", democracia2Service.getThemes());
      return "create_project";
    } catch (ThemeNotFoundException e) {
      model.addAttribute("error", "Erro: Tema Inválido");
      model.addAttribute("themes", democracia2Service.getThemes());
      return "create_project";
    } catch (ParameterException e) {
      model.addAttribute("themes", democracia2Service.getThemes());
      model.addAttribute("error", "Erro: Verifica que tem todos os campos preenchidos");
      return "create_project";
    }

    return "redirect:/menu";
  }

  @GetMapping("/projects")
  public String getProjects(final Model model) {
    model.addAttribute("projects", democracia2Service.getNotExpiredProjects());
    return "list_projects";
  }

  // tratar das excecoes
  @GetMapping("/project/{id}")
  public String openProject(final Model model, @PathVariable Long id) {
    try {
      model.addAttribute("project", democracia2Service.getProject(id));
    } catch (ProjectNotFoundException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjects());
      model.addAttribute("error", "Erro: Projeto não existe");
      return "redirect:/projects";
    }
    return "project_details";
  }

  @GetMapping("/support-project")
  public String supportProjectPage(final Model model) {
    model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
    return "support_project";
  }

  // tratar das exceçoes
  @PostMapping("/support-project")
  public String supportProject(final Model model, @RequestParam("selectedProject") Long projectId) {
    try {
      democracia2Service.supportLawProject(voterCC, projectId);
    } catch (ProjectNotFoundException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
      model.addAttribute("error", "Erro: Projeto não existe");
      return "support_project";
    } catch (VoterNotFoundException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
      model.addAttribute("error", "Erro: O seu CC não existe no sistema");
      return "support_project";
    } catch (PollAlreadyExistsException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
      model.addAttribute("error", "Erro: Este Projeto já foi aprovado");
      return "support_project";
    } catch (ParameterException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
      model.addAttribute("error", "Erro: Verifique que tem todos os campos preenchidos");
      return "support_project";
    } catch (VoterAlreadySupportsException e) {
      model.addAttribute("projects", democracia2Service.getNotExpiredProjectsToSupp());
      model.addAttribute("error", "Erro: Já apoia este projeto");
      return "support_project";
    }
    return "redirect:/menu";
  }

  @GetMapping("/choose-delegate")
  public String chooseDelegatePage(final Model model) {
    model.addAttribute("delegates", democracia2Service.listDelegates());
    model.addAttribute("themes", democracia2Service.listThemes());
    return "choose_delegate";
  }

  // tratar das excecoes
  @PostMapping("/choose-delegate")
  public String chooseDelegate(
      final Model model,
      @RequestParam("selectedTheme") Long themeID,
      @RequestParam("selectedDelegate") Long delegateID) {
    try {
      democracia2Service.chooseDelegate(delegateID, voterCC, themeID);
    } catch (VoterNotFoundException e) {
      model.addAttribute("delegates", democracia2Service.listDelegates());
      model.addAttribute("themes", democracia2Service.listThemes());
      model.addAttribute("error", "Erro: O seu CC não existe no sistema");
      return "choose_delegate";

    } catch (ThemeNotFoundException e) {
      model.addAttribute("delegates", democracia2Service.listDelegates());
      model.addAttribute("themes", democracia2Service.listThemes());
      model.addAttribute("error", "Erro: O tema escolhido não existe no sistema");

      return "choose_delegate";
    }
    return "redirect:/menu";
  }

  @GetMapping("/polls-to-vote")
  public String pollsToVotePage(final Model model) {
    model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
    return "polls_to_vote";
  }

  @GetMapping("/vote")
  public String votePage(final Model model, @RequestParam("selectedPoll") Long pollId) {
    String[] opcoes = {"Aprovado", "Reprovado"};
    String[] opcoesWithDefault = {"Voto do Delegado", "Aprovado", "Reprovado"};
    try {
      boolean vote = democracia2Service.getDefaultVote(voterCC, pollId).isContent();
      if (vote == true) {
        model.addAttribute("delegateVote", "O seu Delegado votou: Aprovado");
      } else model.addAttribute("delegateVote", "O seu Delegado votou: Reprovado");

      model.addAttribute("options", opcoesWithDefault);

    } catch (VoterNotFoundException e) {
      if (e.getMessage().equals("Citizen not found")) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: O seu CC não existe no sistema");
        return "redirect:/polls-to-vote";
      }
      model.addAttribute("delegateVote", "Não possui um voto de um delegado");
      model.addAttribute("poll", pollId);
      model.addAttribute("options", opcoes);
      return "vote";
    } catch (VoteNotFoundException e) {
      model.addAttribute("delegateVote", "Não possui um voto de um delegado");
      model.addAttribute("poll", pollId);
      model.addAttribute("options", opcoes);
      return "vote";
    } catch (PollNotFoundException e) {
      model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
      model.addAttribute("error", "Erro: Esta proposta não existe no sistema");
      return "redirect:/polls-to-vote";
    } catch (DelegateNotFoundException e) {
      throw new RuntimeException(e);
    }

    model.addAttribute("poll", pollId);

    return "vote";
  }

  @PostMapping("/vote/{id}")
  public String vote(final Model model, @PathVariable Long id, @RequestParam("vote") String vote) {

    String[] opcoes = {"Aprovado", "Reprovado"};

    if (vote.equals("Aprovado")) {
      try {
        democracia2Service.vote(true, voterCC, id);
      } catch (VoterNotFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: O seu CC não existe no sistema");
        return "redirect:/polls-to-vote";
      } catch (VoteFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: Já votou nesta proposta");
        return "redirect:/polls-to-vote";
      }
    } else if (vote.equals("Reprovado")) {
      try {
        democracia2Service.vote(false, voterCC, id);
      } catch (VoterNotFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: O seu CC não existe no sistema");
        return "redirect:/polls-to-vote";
      } catch (VoteFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: Já votou nesta proposta");
        return "redirect:/polls-to-vote";
      }
    } else {
      try {
        democracia2Service.confirmDefaultVote(
            voterCC, id, democracia2Service.getDefaultVote(voterCC, id).isContent());
      } catch (VoteFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: Já votou nesta proposta");
        return "redirect:/polls-to-vote";
      } catch (VoterNotFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: O seu CC não existe no sistema");
        return "redirect:/polls-to-vote";
      } catch (VoteNotFoundException e) {
        model.addAttribute("delegateVote", "Não possui um voto de um delegado");
        model.addAttribute("poll", id);
        model.addAttribute("options", opcoes);
        return "redirect:/polls-to-vote";
      } catch (PollNotFoundException e) {
        model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
        model.addAttribute("error", "Erro: Esta proposta não existe no sistema");
        return "redirect:/polls-to-vote";
      } catch (DelegateNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    model.addAttribute("polls", democracia2Service.listPollsInCourseToVote());
    return "redirect:/polls-to-vote";
  }

  @GetMapping("/create")
  public String createUserPage() {
    return "create_user";
  }

  @PostMapping("/create")
  public String createUser(
      final Model model,
      @RequestParam("voterCC") String voterCC,
      @RequestParam("type") String type,
      @RequestParam("name") String name) {
    try {
      democracia2Service.createUser(voterCC, type, name);
    } catch (VoterFoundException e) {
      model.addAttribute("error", "Erro: Este CC já se encontra registado no sistema");
      return "create_user";
    }
    model.addAttribute("error", "Conta criada com sucesso");
    return "redirect:/login";
  }
}
