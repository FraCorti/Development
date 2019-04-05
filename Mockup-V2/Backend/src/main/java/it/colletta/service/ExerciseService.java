package it.colletta.service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.colletta.model.ExerciseModel;
import it.colletta.model.PhraseModel;
import it.colletta.model.SolutionModel;
import it.colletta.model.UserModel;
import it.colletta.model.helper.ExerciseHelper;
import it.colletta.repository.exercise.ExerciseRepository;
import it.colletta.service.user.UserService;

@Service
public class ExerciseService {

  @Autowired
  private ExerciseRepository exerciseRepository;

  @Autowired
  private PhraseService phraseService;

  @Autowired
  private UserService userService;

  public Optional<ExerciseModel> findById(String id) {
    return exerciseRepository.findById(id);
    // controllo su id, in caso exception
  }

  public void modifyExerciseAuthorName(UserModel newUserData, String token) {
    UserModel oldUser = userService.findByEmail(newUserData.getUsername());
    Optional<String> firstName = Optional.ofNullable(newUserData.getFirstName());
    Optional<String> lastName = Optional.ofNullable(newUserData.getLastName());
    if (firstName.isPresent() && !firstName.get().equals(oldUser.getFirstName())
        || lastName.isPresent() && !lastName.get().equals(oldUser.getLastName())) {
      String authorName = newUserData.getFirstName() + " " + newUserData.getLastName();
      exerciseRepository.modifyAuthorName(authorName, oldUser.getId());
    }
  }

  public ExerciseModel insertExercise(ExerciseHelper exercise) {

    SolutionModel mainSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
        .solutionText(exercise.getMainSolution()).build();

    SolutionModel alternativeSolution = null;
    if (exercise != null && !exercise.getAlternativeSolution().isEmpty()) {
      alternativeSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
          .solutionText(exercise.getAlternativeSolution()).build();
    }

    PhraseModel phrase = PhraseModel.builder().language(exercise.getLanguage()).datePhrase(System.currentTimeMillis())
        .phraseText(exercise.getPhraseText()).build();

    phrase.addSolution(mainSolution);
    if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
        phrase.addSolution(alternativeSolution);
      }
    phrase = phraseService.insertPhrase(phrase);

    Optional<UserModel> user = userService.findById(exercise.getAuthor());
    String authorName = user.isPresent() ? user.get().getFirstName() + " " + user.get().getLastName() : null;
    ExerciseModel exerciseModel = ExerciseModel.builder().id((new ObjectId().toHexString()))
        .dateExercise(System.currentTimeMillis()).mainSolutionReference(mainSolution)
        .alternativeSolutionReference(alternativeSolution).phraseId(phrase.getId()).phraseText(exercise.getPhraseText())
        .visibility(exercise.getVisibility()).authorId(exercise.getAuthor()).authorName(authorName).build();
    exerciseRepository.save(exerciseModel);
    phraseService.increaseReliability(mainSolution);
    if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
      phraseService.increaseReliability(alternativeSolution);
    }
    return exerciseModel;
  }

  /**
   * 
   * Add a phrase solution or free exercise
   * 
   * @author Gionata Legrottaglie
   * @param exercise Exercise/Solution
   * @param userId   Author Id
   * @return This Exercise
   */
  public ExerciseModel insertFreeExercise(ExerciseHelper exercise, String userId) {

    SolutionModel mainSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
        .solutionText(exercise.getMainSolution()).build();

    SolutionModel alternativeSolution = null;
    if (exercise != null && !exercise.getAlternativeSolution().isEmpty()) {
      alternativeSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
          .solutionText(exercise.getAlternativeSolution()).build();
    }

    PhraseModel phrase = PhraseModel.builder().language(exercise.getLanguage()).datePhrase(System.currentTimeMillis())
        .phraseText(exercise.getPhraseText()).build();

    phrase.addSolution(mainSolution);
    if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
      phrase.addSolution(alternativeSolution);
    }

    phrase = phraseService.insertPhrase(phrase);

    Optional<UserModel> user = userService.findById(userId);
    String authorName = user.isPresent() ? user.get().getFirstName() + " " + user.get().getLastName() : null;
    ExerciseModel exerciseModel = ExerciseModel.builder().id((new ObjectId().toHexString()))
        .dateExercise(System.currentTimeMillis()).mainSolutionReference(mainSolution)
        .alternativeSolutionReference(alternativeSolution).phraseId(phrase.getId()).phraseText(exercise.getPhraseText())
        .visibility(exercise.getVisibility()).authorId(userId).authorName(authorName).build();
    phraseService.increaseReliability(mainSolution);
    if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
      phraseService.increaseReliability(alternativeSolution);
    }
    return exerciseModel;
  }

  /**
   * 
   * Add a solution in phrase
   * 
   * @author Gionata Legrottaglie
   * @param exercise Exercise/Solution
   * @param userId   Author Id
   * @return This Exercise
   */
  public ExerciseModel insertSolution(ExerciseHelper exercise, String userId) {

    Optional<UserModel> optionalUser = userService.findById(userId);
    Optional<ExerciseModel> teacherExercise = exerciseRepository.findById(exercise.getId());
    if (optionalUser.isPresent() && teacherExercise.isPresent()) {

      UserModel user = optionalUser.get();
      ExerciseModel exerciseModel = teacherExercise.get();

      user.addExerciseDone(exerciseModel);
      SolutionModel alternativeSolution = null;
      if (exercise != null && !exercise.getAlternativeSolution().isEmpty()) {
        alternativeSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
            .solutionText(exercise.getAlternativeSolution()).build();
      }
        SolutionModel mainSolution = SolutionModel.builder().reliability(0).authorId(exercise.getAuthor())
            .solutionText(exercise.getMainSolution()).build();
        PhraseModel phrase = PhraseModel.builder().language(exercise.getLanguage())
            .datePhrase(System.currentTimeMillis()).phraseText(exercise.getPhraseText()).build();

        phrase.addSolution(mainSolution);
        if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
          phrase.addSolution(alternativeSolution);
        }

        phrase = phraseService.insertPhrase(phrase);
        phraseService.increaseReliability(mainSolution);
        if (alternativeSolution != null && !alternativeSolution.getSolutionText().isEmpty()) {
          phraseService.increaseReliability(alternativeSolution);
        }
      
      return exerciseModel;
    } else {
      return null;
    }

  }

  public void deleteExercise(String exerciseId) {
    Optional<ExerciseModel> exerciseModelOptional = exerciseRepository.findById(exerciseId);
    if (exerciseModelOptional.isPresent()) {
      ExerciseModel exerciseModel = exerciseModelOptional.get();
      exerciseRepository.delete(exerciseModel);
    }
    // else exception
  }
}
