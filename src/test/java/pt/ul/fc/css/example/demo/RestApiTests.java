package pt.ul.fc.css.example.demo;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.dtos.VoteDTO;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.handlers.*;
import pt.ul.fc.css.example.demo.repositories.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import pt.ul.fc.css.example.demo.services.Democracia2ServiceImpl;

@WebMvcTest
public class RestApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private Democracia2ServiceImpl democracia2Service;

    @MockBean private ListPollsInCourseHandler listPollsInCourseHandler;
    @MockBean private PresentLawProjectHandler presentLawProjectHandler;
    @MockBean private ConsultLawProjectsHandler consultLawProjectsHandler;
    @MockBean private SupportLawProjectHandler supportLawProjectHandler;
    @MockBean private ChooseDelegateHandler chooseDelegateHandler;
    @MockBean private VotePollHandler votePollHandler;

    @MockBean private PollRepository pollRepository;
    @MockBean private ProjectRepository projectRepository;
    @MockBean private ThemeRepository themeRepository;
    @MockBean private VoterRepository voterRepository;
    @MockBean private VoterVoteRepository voterVoteRepository;
    @MockBean private VoterDelegationRepository voterDelegationRepository;

    @Test
    public void testGetPollsInCourse() throws Exception {

        List<LocalDateTime> expirationDates2 = new ArrayList<>();
        expirationDates2.add(LocalDateTime.of(2023, 10, 15, 20, 15));
        expirationDates2.add(LocalDateTime.of(2023, 9, 17, 12, 30));
        expirationDates2.add(LocalDateTime.of(2023, 6, 30, 9, 45));

        List<PollDTO> pollDTOs = new ArrayList<>();
        pollDTOs.add(new PollDTO(1, "General", "Project1", "test", expirationDates2.get(0)));
        pollDTOs.add(new PollDTO(2, "Health", "Project2", "test", expirationDates2.get(1)));

        when(democracia2Service.listPollsInCourse()).thenReturn(pollDTOs);

        ResultActions resultActions = mockMvc.perform(get("/api/polls")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();

            List<PollDTO> polls = objectMapper.readValue(responseBody, new TypeReference<List<PollDTO>>() {});

            assertThat(polls).isNotEmpty();
            assertThat(polls.size()).isEqualTo(2);

            for (int i = 0; i < pollDTOs.size(); i++) {

                PollDTO poll = polls.get(i);
                assertThat(poll.getId()).isEqualTo(pollDTOs.get(i).getId());
                assertThat(poll.getTitle()).isEqualTo(pollDTOs.get(i).getTitle());
                assertThat(poll.getTheme()).isEqualTo(pollDTOs.get(i).getTheme());
                assertThat(poll.getDescription()).isEqualTo(pollDTOs.get(i).getDescription());
                assertThat(poll.getExpirationDate().isEqual(pollDTOs.get(i).getExpirationDate()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetNotExpiredProjects() throws Exception {

        List<LocalDateTime> expirationDates2 = new ArrayList<>();
        expirationDates2.add(LocalDateTime.of(2023, 10, 15, 20, 15));
        expirationDates2.add(LocalDateTime.of(2023, 9, 17, 12, 30));
        expirationDates2.add(LocalDateTime.of(2023, 6, 30, 9, 45));

        List<ProjectDTO> projectDTOS = new ArrayList<>();
        projectDTOS.add(new ProjectDTO(1,"General", "proj1", "test1", false, expirationDates2.get(0),1,null,"delegate1"));
        projectDTOS.add(new ProjectDTO(2,"General", "proj2", "test2", false, expirationDates2.get(1),1,null,"delegate2"));

        when(democracia2Service.getNotExpiredProjects()).thenReturn(projectDTOS);

        ResultActions resultActions = mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();

            List<ProjectDTO> projects = objectMapper.readValue(responseBody, new TypeReference<List<ProjectDTO>>() {});

            assertThat(projects).isNotEmpty();
            assertThat(projects.size()).isEqualTo(2);

            for (int i = 0; i < projectDTOS.size(); i++) {
                ProjectDTO project = projects.get(i);
                assertThat(project.getId()).isEqualTo(projectDTOS.get(i).getId());
                assertThat(project.getTitle()).isEqualTo(projectDTOS.get(i).getTitle());
                assertThat(project.getTheme()).isEqualTo(projectDTOS.get(i).getTheme());
                assertThat(project.getDescription()).isEqualTo(projectDTOS.get(i).getDescription());
                assertThat(project.getExpirationDate().isEqual(projectDTOS.get(i).getExpirationDate()));
                assertThat(project.getNrSupporters()).isEqualTo(projectDTOS.get(i).getNrSupporters());
                assertThat(project.getPdf()).isEqualTo(projectDTOS.get(i).getPdf());
                assertThat(project.getDelegateName()).isEqualTo(projectDTOS.get(i).getDelegateName());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSupportLawProject() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "123456789";

        doNothing().when(democracia2Service).supportLawProject(anyString(), anyLong());

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody));


            resultActions.andExpect(status().isOk());
            verify(democracia2Service).supportLawProject(voterCC, projectId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (VoterAlreadySupportsException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testSupportLawProjectProjectNotFound() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "123456789";

        doThrow(new ProjectNotFoundException("Project not found")).when(democracia2Service)
                .supportLawProject(eq("123456789"), eq(projectId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isNotFound());

            verify(democracia2Service).supportLawProject("123456789", projectId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSupportLawProjectVoterNotFound() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "123456789";

        doThrow(new VoterNotFoundException("Voter not found")).when(democracia2Service)
                .supportLawProject(eq("123456789"), eq(projectId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));
            resultActions.andExpect(status().isNotFound());
            verify(democracia2Service).supportLawProject("123456789", projectId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSupportLawProjectPollAlreadyExists() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "123456789";

        doThrow(new PollAlreadyExistsException("Poll already exists")).when(democracia2Service)
                .supportLawProject(eq("123456789"), eq(projectId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isBadRequest());

            verify(democracia2Service).supportLawProject("123456789", projectId);

        } catch (Exception | VoterAlreadySupportsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSupportLawProjectParameterException() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "";

        doThrow(new ParameterException("")).when(democracia2Service)
                .supportLawProject(eq(""), eq(projectId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isBadRequest());

            verify(democracia2Service).supportLawProject("", projectId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSupportLawProjectVoterAlreadySupportsException() throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException, ParameterException, VoterAlreadySupportsException {
        long projectId = 1;
        String voterCC = "123456789";


        doThrow(new VoterAlreadySupportsException("Voter already supports project")).when(democracia2Service)
                .supportLawProject(eq("123456789"), eq(projectId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/support/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));


            resultActions.andExpect(status().isBadRequest());

            verify(democracia2Service).supportLawProject("123456789", projectId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetDefaultVote() throws Exception, VoteNotFoundException {
        long pollId = 1;
        String voterCC = "123456789";

        VoteDTO vote = new VoteDTO(1, "delegate1" , true);

        when(democracia2Service.getDefaultVote(eq("123456789"), eq(pollId)))
                .thenReturn(vote);

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/defaultvote/{id}", pollId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));


            resultActions.andExpect(status().isOk());

            ObjectMapper objectMapper = new ObjectMapper();

            String responseBody = resultActions.andReturn().getResponse().getContentAsString();

            VoteDTO voteDTOResult = objectMapper.readValue(responseBody, new TypeReference<VoteDTO>() {});

            assertThat(voteDTOResult.getDelegateId()).isEqualTo(vote.getDelegateId());
            assertThat(voteDTOResult.getDelegateName()).isEqualTo(vote.getDelegateName());
            assertThat(voteDTOResult.isContent()).isEqualTo(vote.isContent());

            verify(democracia2Service).getDefaultVote("123456789", pollId);

        } catch (Exception | VoteNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetDefaultVoteVoterNotFound() throws Exception, VoteNotFoundException {
        long pollId = 1;
        String voterCC = "123456789";

        doThrow(new VoterNotFoundException("Voter not found"))
                .when(democracia2Service)
                .getDefaultVote(eq("123456789"), eq(pollId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/defaultvote/{id}", pollId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isNotFound());

            verify(democracia2Service).getDefaultVote("123456789", pollId);

        } catch (Exception | VoteNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetDefaultVoteNotFound() throws Exception, VoteNotFoundException {
        long pollId = 1;
        String voterCC = "123456789";

        doThrow(new VoteNotFoundException("Vote not found"))
                .when(democracia2Service)
                .getDefaultVote(eq("123456789"), eq(pollId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/defaultvote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isNotFound());

            verify(democracia2Service).getDefaultVote("123456789", 1);

        } catch (Exception | VoteNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetDefaultPollNotFound() throws Exception, VoteNotFoundException {
        long pollId = 1;
        String voterCC = "123456789";

        doThrow(new VoteNotFoundException("Poll not found"))
                .when(democracia2Service)
                .getDefaultVote(eq("123456789"), eq(pollId));

        try {
            String requestBody = "{\"voterCC\":\"" + voterCC + "\"}";
            ResultActions resultActions = mockMvc.perform(post("/api/defaultvote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isNotFound());

            verify(democracia2Service).getDefaultVote("123456789", 1);

        } catch (Exception | VoteNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConfirmDefaultVote() throws VoteFoundException {
        long pollId = 1;
        String voterCC = "123456789";
        boolean vote = true;

        doNothing().when(democracia2Service).confirmDefaultVote(anyString(), anyLong(), anyBoolean());


        try {
            String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + vote + "}";
            ResultActions resultActions = mockMvc.perform(post("/api/confirm-default-vote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isOk());

            verify(democracia2Service).confirmDefaultVote(voterCC, pollId, vote);

        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConfirmDefaultVoteVoteFound() throws VoteFoundException {
        long pollId = 1;
        String voterCC = "123456789";
        boolean vote = true;

        doThrow(new VoteFoundException("Vote found"))
                .when(democracia2Service)
                .confirmDefaultVote(eq(voterCC), eq(pollId), anyBoolean());


        try {
            String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + vote + "}";
            ResultActions resultActions = mockMvc.perform(post("/api/confirm-default-vote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isBadRequest());

            verify(democracia2Service).confirmDefaultVote(voterCC, pollId, vote);

        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testVote() throws VoterNotFoundException, VoteFoundException {
        long pollId = 1;
        String voterCC = "123456789";
        boolean vote = true;

        doNothing().when(democracia2Service).vote(anyBoolean(), anyString(), anyLong());


        try {
            String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + String.valueOf(vote) + "}";
            ResultActions resultActions = mockMvc.perform(post("/api/vote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isOk());

            verify(democracia2Service).vote(vote, voterCC, pollId);

        } catch (Exception  e) {
            throw new RuntimeException(e);
        } catch (VoteFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testVoteVoteFound() throws VoterNotFoundException, VoteFoundException {
        long pollId = 1;
        String voterCC = "123456789";
        boolean vote = true;

        doNothing().when(democracia2Service).vote(anyBoolean(), anyString(), anyLong());
        doThrow(new VoteFoundException("Vote found"))
                .when(democracia2Service)
                .vote(anyBoolean(), anyString(), anyLong());

        try {
            String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + String.valueOf(vote) + "}";
            ResultActions resultActions = mockMvc.perform(post("/api/vote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isBadRequest());

            verify(democracia2Service).vote(vote, voterCC, pollId);

        } catch (Exception  e) {
            throw new RuntimeException(e);
        } catch (VoteFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testVoteVoterNotFound() throws VoterNotFoundException, VoteFoundException {
        long pollId = 1;
        String voterCC = "123456789";
        boolean vote = true;

        doNothing().when(democracia2Service).vote(anyBoolean(), anyString(), anyLong());
        doThrow(new VoterNotFoundException("Voter not found"))
                .when(democracia2Service)
                .vote(anyBoolean(), anyString(), anyLong());

        try {
            String requestBody = "{\"voterCC\": \"" + voterCC + "\", \"vote\": " + String.valueOf(vote) + "}";
            ResultActions resultActions = mockMvc.perform(post("/api/vote/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isNotFound());

            verify(democracia2Service).vote(vote, voterCC, pollId);

        } catch (Exception  e) {
            throw new RuntimeException(e);
        } catch (VoteFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
}
