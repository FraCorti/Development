package it.colletta.model.validator;

import it.colletta.model.helper.StudentClassHelper;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class StudentClassHelperValidator implements Validator {

  /**
   * This Validator validates *just* ClassHelper instances.
   */
  public boolean supports(Class clazz) {
    return StudentClassHelper.class.equals(clazz);
  }


  /**
   * Validate.
   */
  public void validate(Object obj, Errors err) {
    ValidationUtils.rejectIfEmpty(err, "classId", "classId.empty");
    StudentClassHelper par = (StudentClassHelper) obj;
  }
}
