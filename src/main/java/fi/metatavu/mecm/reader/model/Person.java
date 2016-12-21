package fi.metatavu.mecm.reader.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Person {

  @JacksonXmlProperty(localName = "merexid", isAttribute = true)
  private Integer merexId;

  @JacksonXmlProperty(localName = "customerid", isAttribute = true)
  private String customerId;

  @JacksonXmlProperty(localName = "hidden", isAttribute = true)
  @JsonDeserialize(using = NumericBooleanDeserializer.class)
  private Boolean hidden;

  @JacksonXmlProperty(namespace = "mx", localName = "created")
  private Modification created;

  @JacksonXmlProperty(namespace = "mx", localName = "modified")
  private Modification modified;

  @JacksonXmlProperty(namespace = "mx", localName = "name")
  private String name;

  @JacksonXmlProperty(namespace = "mx", localName = "nick1")
  private String nick1;

  @JacksonXmlProperty(namespace = "mx", localName = "nick2")
  private String nick2;

  @JacksonXmlProperty(namespace = "mx", localName = "nick3")
  private String nick3;

  @JacksonXmlProperty(namespace = "mx", localName = "nick4")
  private String nick4;

  @JacksonXmlProperty(namespace = "mx", localName = "number")
  private String number;

  @JacksonXmlProperty(namespace = "mx", localName = "mobile_nbr")
  private String mobileNumber;

  @JacksonXmlProperty(namespace = "mx", localName = "other_nbr")
  private String otherNumber;

  @JacksonXmlProperty(namespace = "mx", localName = "substitute_nbr")
  private String substituteNumber;

  @JacksonXmlProperty(namespace = "mx", localName = "secretary_nbr")
  private String secretaryNumber;

  @JacksonXmlProperty(namespace = "mx", localName = "spare_nbr")
  private String spareNumber;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr1")
  private String additionalNumber1;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr2")
  private String additionalNumber2;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr3")
  private String additionalNumber3;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr4")
  private String additionalNumber4;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr5")
  private String additionalNumber5;

  @JacksonXmlProperty(namespace = "mx", localName = "add_nbr6")
  private String additionalNumber6;

  @JacksonXmlProperty(namespace = "mx", localName = "task1")
  private List<Task> task1;

  @JacksonXmlProperty(namespace = "mx", localName = "task2")
  private String task2;

  @JacksonXmlProperty(namespace = "mx", localName = "task3")
  private String task3;

  @JacksonXmlProperty(namespace = "mx", localName = "task4")
  private String task4;

  @JacksonXmlProperty(namespace = "mx", localName = "task5")
  private String task5;

  @JacksonXmlProperty(namespace = "mx", localName = "task6")
  private String task6;

  @JacksonXmlProperty(namespace = "mx", localName = "task7")
  private String task7;

  @JacksonXmlProperty(namespace = "mx", localName = "task8")
  private String task8;

  @JacksonXmlProperty(namespace = "mx", localName = "task9")
  private String task9;

  @JacksonXmlProperty(namespace = "mx", localName = "task10")
  private String task10;

  @JacksonXmlProperty(namespace = "mx", localName = "task11")
  private String task11;

  @JacksonXmlProperty(namespace = "mx", localName = "task12")
  private String task12;

  @JacksonXmlProperty(namespace = "mx", localName = "task13")
  private String task13;

  @JacksonXmlProperty(namespace = "mx", localName = "task14")
  private String task14;

  @JacksonXmlProperty(namespace = "mx", localName = "title1")
  private String title1;

  @JacksonXmlProperty(namespace = "mx", localName = "title2")
  private String title2;

  @JacksonXmlProperty(namespace = "mx", localName = "title3")
  private String title3;

  @JacksonXmlProperty(namespace = "mx", localName = "visit_address")
  private String visitAddress;

  @JacksonXmlProperty(namespace = "mx", localName = "mail_address")
  private String mailAddress;

  @JacksonXmlProperty(namespace = "mx", localName = "room")
  private String room;

  @JacksonXmlProperty(namespace = "mx", localName = "fax")
  private String fax;

  @JacksonXmlProperty(namespace = "mx", localName = "location")
  private String location;

  @JacksonXmlProperty(namespace = "mx", localName = "company")
  private String company;

  @JacksonXmlProperty(namespace = "mx", localName = "department")
  private List<DepartmentItem> department;

  @JacksonXmlProperty(namespace = "mx", localName = "group")
  private String group;

  @JacksonXmlProperty(namespace = "mx", localName = "refs")
  private Refs refs;

  @JacksonXmlProperty(namespace = "mx", localName = "person_id")
  private String personId;

  @JacksonXmlProperty(namespace = "mx", localName = "cc")
  private String cc;

  @JacksonXmlProperty(namespace = "mx", localName = "e-mail")
  private String email;

  @JacksonXmlProperty(namespace = "mx", localName = "pbx")
  private String pbx;

  @JacksonXmlProperty(namespace = "mx", localName = "wts")
  private List<Wt> wts;

  @JacksonXmlProperty(namespace = "mx", localName = "memos")
  private List<Memo> memos;

  @JacksonXmlProperty(namespace = "mx", localName = "statuses")
  private List<Status> statuses;

  public Integer getMerexId() {
    return merexId;
  }

  public void setMerexId(Integer merexId) {
    this.merexId = merexId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public Modification getCreated() {
    return created;
  }

  public void setCreated(Modification created) {
    this.created = created;
  }

  public Modification getModified() {
    return modified;
  }

  public void setModified(Modification modified) {
    this.modified = modified;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNick1() {
    return nick1;
  }

  public void setNick1(String nick1) {
    this.nick1 = nick1;
  }

  public String getNick2() {
    return nick2;
  }

  public void setNick2(String nick2) {
    this.nick2 = nick2;
  }

  public String getNick3() {
    return nick3;
  }

  public void setNick3(String nick3) {
    this.nick3 = nick3;
  }

  public String getNick4() {
    return nick4;
  }

  public void setNick4(String nick4) {
    this.nick4 = nick4;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getOtherNumber() {
    return otherNumber;
  }

  public void setOtherNumber(String otherNumber) {
    this.otherNumber = otherNumber;
  }

  public String getSubstituteNumber() {
    return substituteNumber;
  }

  public void setSubstituteNumber(String substituteNumber) {
    this.substituteNumber = substituteNumber;
  }

  public String getSecretaryNumber() {
    return secretaryNumber;
  }

  public void setSecretaryNumber(String secretaryNumber) {
    this.secretaryNumber = secretaryNumber;
  }

  public String getSpareNumber() {
    return spareNumber;
  }

  public void setSpareNumber(String spareNumber) {
    this.spareNumber = spareNumber;
  }

  public String getAdditionalNumber1() {
    return additionalNumber1;
  }

  public void setAdditionalNumber1(String additionalNumber1) {
    this.additionalNumber1 = additionalNumber1;
  }

  public String getAdditionalNumber2() {
    return additionalNumber2;
  }

  public void setAdditionalNumber2(String additionalNumber2) {
    this.additionalNumber2 = additionalNumber2;
  }

  public String getAdditionalNumber3() {
    return additionalNumber3;
  }

  public void setAdditionalNumber3(String additionalNumber3) {
    this.additionalNumber3 = additionalNumber3;
  }

  public String getAdditionalNumber4() {
    return additionalNumber4;
  }

  public void setAdditionalNumber4(String additionalNumber4) {
    this.additionalNumber4 = additionalNumber4;
  }

  public String getAdditionalNumber5() {
    return additionalNumber5;
  }

  public void setAdditionalNumber5(String additionalNumber5) {
    this.additionalNumber5 = additionalNumber5;
  }

  public String getAdditionalNumber6() {
    return additionalNumber6;
  }

  public void setAdditionalNumber6(String additionalNumber6) {
    this.additionalNumber6 = additionalNumber6;
  }

  public List<Task> getTask1() {
    return task1;
  }

  public void setTask1(List<Task> task1) {
    this.task1 = task1;
  }

  public String getTask2() {
    return task2;
  }

  public void setTask2(String task2) {
    this.task2 = task2;
  }

  public String getTask3() {
    return task3;
  }

  public void setTask3(String task3) {
    this.task3 = task3;
  }

  public String getTask4() {
    return task4;
  }

  public void setTask4(String task4) {
    this.task4 = task4;
  }

  public String getTask5() {
    return task5;
  }

  public void setTask5(String task5) {
    this.task5 = task5;
  }

  public String getTask6() {
    return task6;
  }

  public void setTask6(String task6) {
    this.task6 = task6;
  }

  public String getTask7() {
    return task7;
  }

  public void setTask7(String task7) {
    this.task7 = task7;
  }

  public String getTask8() {
    return task8;
  }

  public void setTask8(String task8) {
    this.task8 = task8;
  }

  public String getTask9() {
    return task9;
  }

  public void setTask9(String task9) {
    this.task9 = task9;
  }

  public String getTask10() {
    return task10;
  }

  public void setTask10(String task10) {
    this.task10 = task10;
  }

  public String getTask11() {
    return task11;
  }

  public void setTask11(String task11) {
    this.task11 = task11;
  }

  public String getTask12() {
    return task12;
  }

  public void setTask12(String task12) {
    this.task12 = task12;
  }

  public String getTask13() {
    return task13;
  }

  public void setTask13(String task13) {
    this.task13 = task13;
  }

  public String getTask14() {
    return task14;
  }

  public void setTask14(String task14) {
    this.task14 = task14;
  }

  public String getTitle1() {
    return title1;
  }

  public void setTitle1(String title1) {
    this.title1 = title1;
  }

  public String getTitle2() {
    return title2;
  }

  public void setTitle2(String title2) {
    this.title2 = title2;
  }

  public String getTitle3() {
    return title3;
  }

  public void setTitle3(String title3) {
    this.title3 = title3;
  }

  public String getVisitAddress() {
    return visitAddress;
  }

  public void setVisitAddress(String visitAddress) {
    this.visitAddress = visitAddress;
  }

  public String getMailAddress() {
    return mailAddress;
  }

  public void setMailAddress(String mailAddress) {
    this.mailAddress = mailAddress;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public List<DepartmentItem> getDepartment() {
    return department;
  }

  public void setDepartment(List<DepartmentItem> department) {
    this.department = department;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public Refs getRefs() {
    return refs;
  }

  public void setRefs(Refs refs) {
    this.refs = refs;
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPbx() {
    return pbx;
  }

  public void setPbx(String pbx) {
    this.pbx = pbx;
  }

  public List<Wt> getWts() {
    return wts;
  }

  public void setWts(List<Wt> wts) {
    this.wts = wts;
  }

  public List<Memo> getMemos() {
    return memos;
  }

  public void setMemos(List<Memo> memos) {
    this.memos = memos;
  }

  public List<Status> getStatuses() {
    return statuses;
  }

  public void setStatuses(List<Status> statuses) {
    this.statuses = statuses;
  }

  @XmlTransient
  public List<String> getNicks() {
    List<String> result = new ArrayList<>();
    
    if (StringUtils.isNotBlank(getNick1())) {
      result.add(getNick1());
    }
    
    if (StringUtils.isNotBlank(getNick2())) {
      result.add(getNick2());
    }
    
    if (StringUtils.isNotBlank(getNick3())) {
      result.add(getNick3());
    }
    
    if (StringUtils.isNotBlank(getNick4())) {
      result.add(getNick4());
    }
    
    return result;
  }

  @XmlTransient
  public List<String> getAdditionalNumbers() {
    List<String> result = new ArrayList<>();
    
    if (StringUtils.isNotBlank(getAdditionalNumber1())) {
      result.add(getAdditionalNumber1());
    }
    
    if (StringUtils.isNotBlank(getAdditionalNumber2())) {
      result.add(getAdditionalNumber2());
    }
    
    if (StringUtils.isNotBlank(getAdditionalNumber3())) {
      result.add(getAdditionalNumber3());
    }

    if (StringUtils.isNotBlank(getAdditionalNumber4())) {
      result.add(getAdditionalNumber4());
    }

    if (StringUtils.isNotBlank(getAdditionalNumber5())) {
      result.add(getAdditionalNumber5());
    }

    if (StringUtils.isNotBlank(getAdditionalNumber6())) {
      result.add(getAdditionalNumber6());
    }
    
    return result;
  }

  @XmlTransient
  public List<String> getTitles() {
    List<String> result = new ArrayList<>();
    
    if (StringUtils.isNotBlank(getTitle1())) {
      result.add(getTitle1());
    }
    
    if (StringUtils.isNotBlank(getTitle2())) {
      result.add(getTitle2());
    }
    
    if (StringUtils.isNotBlank(getTitle3())) {
      result.add(getTitle3());
    }

    return result;
  }

}
