package fi.metatavu.mecm.reader.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Memo {

  @JacksonXmlProperty(isAttribute = true)
  private Long id;

  @JacksonXmlProperty(isAttribute = true)
  private Integer priority;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "creator")
  private String creator;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "header")
  private String header;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "body")
  private String body;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getCreator() {
    return creator;
  }
  
  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

}
