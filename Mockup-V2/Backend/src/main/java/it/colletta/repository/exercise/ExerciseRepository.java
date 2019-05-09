package it.colletta.repository.exercise;

import com.mongodb.client.result.UpdateResult;

import it.colletta.model.ExerciseModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository
    extends MongoRepository<ExerciseModel, String>, ExerciseCustomQueryInterface {

  UpdateResult modifyAuthorName(final String newAuthorName, String teacherId);

  Page<ExerciseModel> findExerciseModelsByStudentIdDoneIsIn(Pageable page, String id);

  Page<ExerciseModel> findExerciseModelsByStudentIdToDoIsIn(Pageable page, String id);
}
