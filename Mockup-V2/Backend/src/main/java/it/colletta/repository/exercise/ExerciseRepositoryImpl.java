package it.colletta.repository.exercise;

import java.util.List;

import com.mongodb.client.result.UpdateResult;
import it.colletta.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import it.colletta.model.ExerciseModel;
import org.springframework.data.mongodb.core.query.Update;

import static com.mongodb.client.model.Updates.set;

public class ExerciseRepositoryImpl implements ExerciseCustomQueryInterface {
    @Autowired private MongoTemplate mongoTemplate;


    @Override
    public void modifyAuthorExercise(UserModel newUserData, String teacherId){
       String newTeacherName = newUserData.getFirstName() + " " + newUserData.getLastName();
       Query exerciseToUpdate = new Query(Criteria.where("authorId").is(teacherId));
       Update modify = new Update();
       modify.set("teacherName", newTeacherName);
       mongoTemplate.updateMulti(exerciseToUpdate, modify, ExerciseModel.class);
    }

  public List<ExerciseModel> findAllPublicExercises(List<ExerciseModel> exerciseToExclude) {
    //Query query = new Query(Criteria.nin(exerciseToExclude).and("visibility").is(true));
    return null;
  }
}
