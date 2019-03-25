package it.colletta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "users")
public class UserModel {
  @Id
  private String id;
  @JsonProperty("username")
  private String email;
  private String firstName; 
  private String lastName; 
  private String password;
  private String role;
  private String language;
  private Date dateOfBirth;
  private ArrayList<String> phrases;
  private ArrayList<String> execiseToDo;
  private Boolean actived;

}
