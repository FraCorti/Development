/**
 * @path it.colletta.controller.ClassController
 * @author Francesco Magarotto, Enrico Muraro, Francesco Corti
 * @date 2019-03-23
 * @description Manage the HTTP teacher request regarding the classes
 */

package it.colletta.controller;

import it.colletta.model.ClassModel;
import it.colletta.security.ParseJwt;
import it.colletta.service.ExerciseService;
import it.colletta.service.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

  @Autowired
  private ClassService classService;


  /**
   * @param newClass the new class which needs to be inserted in the database
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the status of the operation.
   */
  @RequestMapping(value = "/create-class",
          method = RequestMethod.POST,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> createClass(@RequestHeader("Authorization") String token,
                                                @RequestBody ClassModel newClass) {
    try{
        String newClassName = classService.createNewClass(newClass, ParseJwt.getIdFromJwt(token));
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param studentId the unique Id of the student that is add in the class
   * @param classId the unique Id of the class
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the student that was added to the class.
   */
  @RequestMapping(value = "/add-student-class",
          method = RequestMethod.POST,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> addStudentClass(@RequestHeader("Authorization") String token,
                                                      @RequestParam("student-id") String studentId,
                                                      @RequestParam("class-id") String classId) {
    try{
        classService.addNewStudent(studentId, classId);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param renamedClassModel the class that is going to change the name
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the new name of the class
   */
  @RequestMapping(value = "/rename-class",
          method = RequestMethod.PUT,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> renameClass(@RequestHeader("Authorization") String token,
                                            @RequestBody ClassModel renamedClassModel) {
    try{
        String newClassName = classService.renameExistingClass(renamedClassModel);
        return new ResponseEntity<String>(renamedClassModel.getName(), HttpStatus.OK);
    } catch (Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param studentId the unique Id of the student
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the status of the operation
   */
  @RequestMapping(value = "/remove-student-class",
          method = RequestMethod.DELETE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> deleteStudentClass(@RequestHeader("Authorization") String token,
                                                       @RequestParam("student-id") String studentId,
                                                       @RequestParam("class-id") String classId) {
      try {
        classService.removeExistingStudent(studentId, classId);
        return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

  /**
   * @param classId the unique Id of the class
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the status of the operation
   */
  @RequestMapping(value = "/delete-class",
          method = RequestMethod.DELETE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> deleteClass(@RequestHeader("Authorization") String token,
                                                @RequestParam("class-id") String classId) {
    try{
      classService.deleteExistingClass(classId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e){
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param teacherId the unique Id of the teacher
   * @param token the authorization token of the teacher
   * @return A new ResponseEntity that contains the list of teacher classes
   */
  @RequestMapping(value = "/get-all-classes",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ClassModel>> getAllClasses(@RequestHeader("Authorization") String token,
                                                        @RequestParam("teacher-id") String teacherId) {
    try{
      return new ResponseEntity<>(classService.getAllClasses(teacherId), HttpStatus.OK);
    } catch (Exception e){
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}