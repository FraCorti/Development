package it.colletta.repository.classes;

import it.colletta.model.ClassModel;
import it.colletta.model.StudentModel;

import java.util.List;

public interface ClassCustomQueryInterface {


  List<ClassModel> getAllTeacherClasses(String teacherId);

  void updateStudentList(String classId, List<String> studentId, String className);
}