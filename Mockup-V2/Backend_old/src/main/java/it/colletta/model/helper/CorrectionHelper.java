package it.colletta.model.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CorrectionHelper {

  private String solutionFromStudent;
  private String exerciseId;

  /**
   *
   */
  public String getSolutionFromStudent() {
    return solutionFromStudent;
  }

  /**
   *
   */
  public String getExerciseId() {
    return exerciseId;
  }
}