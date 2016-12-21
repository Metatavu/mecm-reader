package fi.metatavu.mecm.reader.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Status {

  @JacksonXmlProperty(isAttribute = true)
  private Long id;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "creator")
  private String creator;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "reason")
  private String reason;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "comment")
  private String comment;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "departure")
  private OffsetDateTime departure;

  @JacksonXmlElementWrapper(namespace = "mx", localName = "arrival")
  private OffsetDateTime arrival;

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

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public OffsetDateTime getDeparture() {
    return departure;
  }

  public void setDeparture(OffsetDateTime departure) {
    this.departure = departure;
  }

  public OffsetDateTime getArrival() {
    return arrival;
  }

  public void setArrival(OffsetDateTime arrival) {
    this.arrival = arrival;
  }

}
