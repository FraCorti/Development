package it.colletta.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import it.colletta.model.ExerciseModel;
import it.colletta.model.PhraseModel;
import it.colletta.model.SolutionModel;
import it.colletta.model.StudentModel;
import it.colletta.model.UserModel;
import it.colletta.model.helper.CorrectionHelper;
import it.colletta.model.helper.ExerciseHelper;
import it.colletta.repository.exercise.ExerciseRepository;
import it.colletta.security.Role;
import it.colletta.service.student.StudentService;
import it.colletta.service.user.UserService;
import it.colletta.strategy.DecimalCorrectionStrategyImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;


@RunWith(MockitoJUnitRunner.class)
public class ExerciseServiceTest {

  @Mock
  private ExerciseRepository exerciseRepository;

  @Mock
  private PhraseService phraseService;

  @Mock
  private UserService userService;

  @Mock
  private StudentService studentService;

  @InjectMocks
  private ExerciseService exerciseService;

  private ExerciseModel exerciseModel;

  private ExerciseHelper exercise;

  private CorrectionHelper correctionHelper;

  private UserModel userModel;

  private StudentModel studentModel;

  private PhraseModel phrase;

  private SolutionModel mainSolution;


  @Before
  public void setUp() {

    exerciseModel = ExerciseModel.builder()
        .id("1")
        .phraseId("10")
        .phraseText("questa è una prova")
        .dateExercise(378136781L)
        .mainSolutionId("22")
        .alternativeSolutionId("22")
        .authorName("Insegnante Insegnante")
        .authorId("100")
        .visibility(true)
        .build();

    List<String> assignedUsersIds = new ArrayList<>();
    assignedUsersIds.add("104");
    Date date = new Date(2323223232L);

    exercise = ExerciseHelper.builder()
        .id("123")
        .assignedUsersIds(assignedUsersIds)
        .phraseText("questa è una prova")
        .mainSolution("[\"AP0MN3S\",\"NPNNG0D\",\"RG\",\"DE2FSS\"]")
        .alternativeSolution("[\"AP0MN3S\",\"NPNNG0D\",\"RG\",\"DE2FSS\"]")
        .visibility(true)
        .author("100")
        .date(378136781L)
        .language("it")
        .build();

    correctionHelper = CorrectionHelper.builder()
        .exerciseId(exercise.getId())
        .solutionFromStudent("[\"AP0MN3S\",\"NPNNG0D\",\"RG\",\"DE2FSS\"]")
        .build();

    phrase = PhraseModel.builder()
        .id("321")
        .language(exercise.getLanguage())
        .datePhrase(exercise.getDate())
        .phraseText(exercise.getPhraseText())
        .build();

    mainSolution = SolutionModel.builder()
        .id("1246")
        .reliability(0)
        .authorId(exercise.getAuthor())
        .solutionText(exercise.getMainSolution())
        .build();

    studentModel = StudentModel.studentBuilder().id("101").firstName("Studente")
        .lastName("Studente").build();

    userModel = UserModel.builder()
        .id("123")
        .firstName("firstname")
        .email("email@email.com")
        .enabled(true)
        .role(Role.TEACHER)
        .lastName("lastname")
        .dateOfBirth(date)
        .language("it")
        .build();

  }

  /**
   * Test insertExercise method.
   */

  @Test
  public void insertExercise() {
    Mockito.when(phraseService.createPhrase(exercise.getPhraseText(), exercise.getLanguage()))
        .thenReturn(phrase);

    Mockito.when(phraseService.insertPhrase(any(PhraseModel.class))).thenAnswer(returnsFirstArg());

    Mockito.when(userService.findById(anyString())).thenReturn(Optional.of(userModel));

    Mockito.when(exerciseRepository.save(any(ExerciseModel.class))).thenReturn(exerciseModel);

    ExerciseModel myAddedExercise = exerciseService.insertExercise(exercise);

    assertEquals(myAddedExercise.getAuthorName(), "Insegnante Insegnante");
    assertEquals(myAddedExercise.getPhraseText(), "questa è una prova");

  }

  /**
   * Test insertExercise method.
   */

  @Test
  public void insertFreeExercise() {
    Mockito.when(phraseService.insertPhrase(any(PhraseModel.class))).thenAnswer(returnsFirstArg());

    Mockito.when(userService.findById(anyString())).thenReturn(Optional.of(userModel));

    ExerciseModel myAddedExercise = exerciseService
        .insertFreeExercise(exercise, exercise.getAuthor());

    assertEquals(myAddedExercise.getAuthorName(), "firstname lastname");
    assertEquals(myAddedExercise.getPhraseText(), "questa è una prova");

  }


  /**
   * Test doExercise method.
   */
  @Test
  public void doExercise() {

    Mockito.when(exerciseRepository.findById(anyString())).thenReturn(Optional.of(exerciseModel));
    Mockito.when(phraseService.getSolutionInPhrase(anyString(), anyString(), anyString()))
        .thenReturn(mainSolution);
    Mockito.when(phraseService.getPhraseById(anyString())).thenReturn(Optional.of(phrase));
    try {
      SolutionModel mySolution = exerciseService.doExercise(correctionHelper, anyString());

      assertEquals(mySolution.getSolutionText(), "[\"AP0MN3S\",\"NPNNG0D\",\"RG\",\"DE2FSS\"]");

    } catch (Exception error) {
      error.printStackTrace();
    }
  }


  /**
   * Test findById method.
   */

  @Test
  public void findById() {
    Mockito.when(exerciseRepository.findById(anyString())).thenReturn(Optional.of(exerciseModel));
    ExerciseModel myidfound = exerciseService.findById(exerciseModel.getId());

    Assert.assertNotNull(myidfound);
    Mockito.verify(exerciseRepository, Mockito.times(1)).findById(anyString());
    Mockito.verifyNoMoreInteractions(exerciseRepository);

  }

  @Test
  public void correct() {
    ArrayList<String> studentSolutionMap = new ArrayList<>();
    ArrayList<String> systemSolution = new ArrayList<>();
    studentSolutionMap.add(exercise.getMainSolution());
    systemSolution.add(exercise.getMainSolution());

    String myMark = ReflectionTestUtils.
        invokeMethod(new DecimalCorrectionStrategyImpl<>(), "correction", studentSolutionMap,
            systemSolution).toString();
    assertEquals(myMark, "10.0");
  }
}