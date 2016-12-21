package fi.metatavu.mecm.reader.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Refs {

  @JacksonXmlProperty(namespace = "mx", localName = "substitute")
  private Ref substitute1;

  @JacksonXmlProperty(namespace = "mx", localName = "substitute_2")
  private Ref substitute2;

  @JacksonXmlProperty(namespace = "mx", localName = "secretary")
  private Ref secretary1;

  @JacksonXmlProperty(namespace = "mx", localName = "secretary_2")
  private Ref secretary2;

  @JacksonXmlProperty(namespace = "mx", localName = "manager")
  private Ref manager;

  public Ref getSubstitute1() {
    return substitute1;
  }

  public void setSubstitute1(Ref substitute1) {
    this.substitute1 = substitute1;
  }

  public Ref getSubstitute2() {
    return substitute2;
  }

  public void setSubstitute2(Ref substitute2) {
    this.substitute2 = substitute2;
  }

  public Ref getSecretary1() {
    return secretary1;
  }

  public void setSecretary1(Ref secretary1) {
    this.secretary1 = secretary1;
  }

  public Ref getSecretary2() {
    return secretary2;
  }

  public void setSecretary2(Ref secretary2) {
    this.secretary2 = secretary2;
  }

  public Ref getManager() {
    return manager;
  }

  public void setManager(Ref manager) {
    this.manager = manager;
  }

}