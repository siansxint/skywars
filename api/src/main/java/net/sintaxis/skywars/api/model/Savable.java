package net.sintaxis.skywars.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A useful interface to identify classes can be stored in the database
 */

public interface Savable {

 /**
  * The id of the object to save in the database
  *
  * @return The id of the object
  */

 @JsonProperty("_id")
 String getId();
}