package fi.metatavu.mecm.reader.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Wt {

  @JacksonXmlProperty(isAttribute = true)
  private String id;

  @JacksonXmlProperty(namespace = "mx", localName = "device")
  private String device;

  @JacksonXmlProperty(namespace = "mx", localName = "card")
  private String card;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getCard() {
    return card;
  }

  public void setCard(String card) {
    this.card = card;
  }

}
