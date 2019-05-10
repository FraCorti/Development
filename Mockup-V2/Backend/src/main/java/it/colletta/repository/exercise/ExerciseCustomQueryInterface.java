package it.colletta.repository.exercise;

import com.mongodb.client.result.UpdateResult;

import it.colletta.model.ExerciseModel;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExerciseCustomQueryInterface {

  UpdateResult modifyAuthorName(final String newAuthorName, String authorId);

  Page<ExerciseModel> findByIdPaged(Pageable page, List<ObjectId> ids);

  Page<ExerciseModel> findPublicByIdPaged(Pageable page, List<ObjectId> ids);

  Page<ExerciseModel> findByAuthorIdPaged(Pageable page, String id);

  void pullStudentToDoList(String exerciseId, String studentId);

  void pushStudentDoneList(String exerciseId, String studentId);

  Page<ExerciseModel> findAllPublicExercises(Pageable page, String studentId);

  Page<ExerciseModel> findAllFavoriteExercises(Pageable page, List<String> teacherFavoriteIds,
      String studentId);
}
