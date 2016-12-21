package fi.metatavu.mecm.reader.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Ref {

  @JacksonXmlProperty (isAttribute = true)
  private String target;
  
  @JacksonXmlText
  private Integer number;

  public String getTarget() {
    return target;
  }
  
  public void setTarget(String target) {
    this.target = target;
  }
  
  public Integer getNumber() {
    return number;
  }
  
  public void setNumber(Integer number) {
    this.number = number;
  }
}
