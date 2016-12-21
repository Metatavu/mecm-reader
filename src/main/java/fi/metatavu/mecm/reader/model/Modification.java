package fi.metatavu.mecm.reader.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Modification {
  
  @JacksonXmlProperty (isAttribute = true)
  private OffsetDateTime time;
  
  @JacksonXmlProperty (isAttribute = true)
  private String actor;

  public OffsetDateTime getTime() {
    return time;
  }
  
  public void setTime(OffsetDateTime time) {
    this.time = time;
  }
  
  public String getActor() {
    return actor;
  }
  
  public void setActor(String actor) {
    this.actor = actor;
  }

}
