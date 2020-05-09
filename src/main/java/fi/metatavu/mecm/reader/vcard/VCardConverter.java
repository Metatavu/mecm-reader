package fi.metatavu.mecm.reader.vcard;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.chain.ChainingTextWriter;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Categories;
import ezvcard.property.Email;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.RawProperty;
import ezvcard.property.Related;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import ezvcard.property.Uid;
import fi.metatavu.mecm.reader.model.DepartmentItem;
import fi.metatavu.mecm.reader.model.Memo;
import fi.metatavu.mecm.reader.model.Merex;
import fi.metatavu.mecm.reader.model.Person;
import fi.metatavu.mecm.reader.model.Ref;
import fi.metatavu.mecm.reader.model.Refs;
import fi.metatavu.mecm.reader.model.Task;
import fi.metatavu.mecm.reader.model.Wt;
import fi.metatavu.mecm.reader.model.Status;

public class VCardConverter {
  
  public static final String TIMEZONE = "Europe/Helsinki";
  public static final String MECM_TYPE_ORGANIZATION_UNIT_DEPARTMENT = "DEPARTMENT";
  public static final String MECM_TYPE = "mecm-type";
  public static final String MECM_TYPE_CATEGORY_TASK = "TASK";
  public static final String MECM_TYPE_RELATION_SUBSTITUTE = "SUBSTITUTE";
  public static final String MECM_TYPE_RELATION_SECRETARY = "SECRETARY";
  public static final String MECM_TYPE_RELATION_MANAGER = "MANAGER";
  public static final String MECM_TYPE_ORGANIZATION_UNIT_GROUP = "GROUP";

  public static final String MECM_TYPE_NUMBER_NUMBER = "NUMBER";
  public static final String MECM_TYPE_NUMBER_MOBILE = "MOBILE";
  public static final String MECM_TYPE_NUMBER_OTHER = "OTHER";
  public static final String MECM_TYPE_NUMBER_SUBSTITUTE = "SUBSTITUTE";
  public static final String MECM_TYPE_NUMBER_SECRETARY = "SECRETARY";
  public static final String MECM_TYPE_NUMBER_SPARE = "SPARE";
  public static final String MECM_TYPE_NUMBER_ADDITIONAL = "ADDITIONAL";
  public static final String MECM_TYPE_NUMBER_FAX = "FAX";

  public static final String MECM_NOTE_ID = "mecm-note-id";
  public static final String MECM_NOTE_PRIORITY = "mecm-note-priority";

  public static final String MECM_ADDITIONAL_PERSON_ID = "mecm-person-id";
  public static final String MECM_ADDITIONAL_CC = "mecm-cc";
  public static final String MECM_ADDITIONAL_PBX = "mecm-pbx";
  public static final String MECM_ADDITIONAL_WT_JSON = "mecm-wt-json";
  public static final String MECM_ADDITIONAL_STATUS_JSON = "mecm-status-json";
  public static final String MECM_ADDITIONAL_PRIVATE = "X-MECM-PRIVATE";
  public static final String MECM_ADDITIONAL_NO_CALLS = "X-MECM-NO-CALLS";

  private static final String MECM_NAME_PRIVATE = "EI JULKINEN";
  private static final String MECM_TASK1_NO_CALLS = "EI PUHELUITA";
  private static final String[] MECM_TASK1_PRIVATES = { "ei julkinen", "ei_julkinen" };
  
  public void toVCardFile(String organizationId, String uriTemplate, Merex merex, File outputFile) throws IOException {
    List<VCard> vCards = toVCards(organizationId, uriTemplate, merex);
    ChainingTextWriter writer = Ezvcard.write(vCards).version(VCardVersion.V4_0);
    writer.go(outputFile);
  }

  public List<VCard> toVCards(String organizationId, String uriTemplate, Merex merex) {
    List<VCard> vCards = new ArrayList<>(merex.getPersons().size());
    
    for (Person person : merex.getPersons()) {
      if (!person.getHidden()) {
        VCard vCard = new VCard();
        String id = createId(organizationId, person.getMerexId());
        
        vCard.setUid(new Uid(id));
        vCard.addSource(createCardUri(uriTemplate, id));
        vCard.setTimezone(new Timezone(TIMEZONE));
        vCard.setRevision(toDate(person.getModified().getTime()));
        vCard.setFormattedName(person.getName());
        
        if (StringUtils.isNotBlank(person.getEmail())) {
          Email email = new Email(person.getEmail());
          vCard.addEmail(email);
        }
        
        for (String nick : person.getNicks()) {
          Nickname nickname = new Nickname();
          nickname.getValues().add(nick);
          vCard.addNickname(nickname);
        }
        
        handleTasks(person, vCard);
        handleAddresses(person, vCard);
        handleMemos(person, vCard);
        handleOrganization(person, vCard);        
        handleTitles(person, vCard);
        handleRefs(organizationId, uriTemplate, person, vCard);
        handleNumbers(person, vCard);
        handleAdditional(person, vCard);

        if (StringUtils.containsIgnoreCase(person.getName(), MECM_NAME_PRIVATE)) {
          vCard.setExtendedProperty(MECM_ADDITIONAL_PRIVATE, "true");
        }
        
        if (!isPrivateCard(vCard)) {
          vCards.add(vCard);
        }
      }
      
    }
    
    return vCards;
  }
  
  private boolean isPrivateCard(VCard vCard) {
    RawProperty property = vCard.getExtendedProperty(MECM_ADDITIONAL_PRIVATE);
    return property != null && "true".equals(property.getValue());
  }

  @SuppressWarnings ({"squid:S106", "squid:S1166"})
  private void handleAdditional(Person person, VCard vCard) {
    if (StringUtils.isNotBlank(person.getPersonId())) {
      vCard.addExtendedProperty(MECM_ADDITIONAL_PERSON_ID, person.getPersonId());
    }
    
    if (StringUtils.isNotBlank(person.getCc())) {
      vCard.addExtendedProperty(MECM_ADDITIONAL_CC, person.getCc());
    }
    
    if (StringUtils.isNotBlank(person.getPbx())) {
      vCard.addExtendedProperty(MECM_ADDITIONAL_PBX, person.getPbx());
    }
    
    if (person.getWts() != null && !person.getWts().isEmpty()) {
      for (Wt wt : person.getWts()) {
        vCard.addExtendedProperty(MECM_ADDITIONAL_WT_JSON, toJSON(wt));
      }
    }
    
    if (person.getStatuses() != null) {
      for (Status status : person.getStatuses()) {
        vCard.addExtendedProperty(MECM_ADDITIONAL_STATUS_JSON, toJSON(status));
      }
    }
  }

  @SuppressWarnings ({"squid:S106", "squid:S1166"})
  private String toJSON(Object value) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.registerModule(new JavaTimeModule());
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      System.err.println(e.getMessage());
    }
    
    return null;
  }

  private void handleNumbers(Person person, VCard vCard) {
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_NUMBER, person.getNumber());
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_MOBILE, person.getMobileNumber());
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_OTHER, person.getOtherNumber());
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_SUBSTITUTE, person.getSubstituteNumber());
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_SECRETARY, person.getSecretaryNumber());
    addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_SPARE, person.getSpareNumber());
    
    for (String additionalNumber : person.getAdditionalNumbers()) {
      addNumber(vCard, TelephoneType.WORK, MECM_TYPE_NUMBER_ADDITIONAL, additionalNumber);
    }
    
    addNumber(vCard, TelephoneType.FAX, MECM_TYPE_NUMBER_FAX, person.getFax());
  }
  
  private void addNumber(VCard vCard, TelephoneType type, String mecmType, String number) {
    if (StringUtils.isNotBlank(number)) {
      Telephone telephone = new Telephone(number);
      telephone.setParameter(MECM_TYPE, mecmType);
      
      if (type != null) {
        telephone.getTypes().add(type);
      }
      
      vCard.addTelephoneNumber(telephone);
    }
  }

  private void handleTitles(Person person, VCard vCard) {
    for (String title : person.getTitles()) {
      vCard.addTitle(title);
    }
  }

  private void handleOrganization(Person person, VCard vCard) {
    if (StringUtils.isNotBlank(person.getCompany())) {
      Organization organization = new Organization();
      organization.getValues().add(person.getCompany());
      vCard.addOrganization(organization);
    }
    
    if (person.getDepartment() != null) {
      for (DepartmentItem departmentItem : person.getDepartment()) {
        Organization organization = new Organization();
        organization.getValues().add(departmentItem.getText());
        organization.addParameter(MECM_TYPE, MECM_TYPE_ORGANIZATION_UNIT_DEPARTMENT);
        vCard.addOrganization(organization);
      }
    }

    if (StringUtils.isNotBlank(person.getGroup())) {
      Organization organization = new Organization();
      organization.getValues().add(person.getGroup());
      organization.addParameter(MECM_TYPE, MECM_TYPE_ORGANIZATION_UNIT_GROUP);
      vCard.addOrganization(organization);
    }
  }

  private void handleMemos(Person person, VCard vCard) {
    if (person.getMemos() != null) {
      for (Memo memo : person.getMemos()) {
        handleMemo(vCard, memo);
      }
    }
  }

  private void handleMemo(VCard vCard, Memo memo) {
    StringBuilder noteBuilder = new StringBuilder();
    
    if (StringUtils.isNotBlank(memo.getHeader())) {
      noteBuilder.append(memo.getHeader());
    }
 
    if (StringUtils.isNotBlank(memo.getBody())) {
      if (noteBuilder.length() > 0) {
        noteBuilder.append('\n');
      }
      
      noteBuilder.append(memo.getBody());
    }
    
    if (noteBuilder.length() > 0 && memo.getId() != null) {
      Note note = new Note(noteBuilder.toString());
      
      if (memo.getId() != null) {
        note.addParameter(MECM_NOTE_ID, String.valueOf(memo.getId()));
      }

      if (memo.getPriority() != null) {
        note.addParameter(MECM_NOTE_PRIORITY, String.valueOf(memo.getPriority()));
      }
      
      vCard.addNote(note);
    }
  }

  private void handleAddresses(Person person, VCard vCard) {
    String mailAddress = person.getMailAddress();
    String room = person.getRoom();
    String visitAddress = person.getVisitAddress();
    String location = person.getLocation();
    
    boolean mailAddressBlank = StringUtils.isBlank(mailAddress);
    boolean roomBlank = StringUtils.isBlank(room);
    boolean visitAddressBlank = StringUtils.isBlank(visitAddress);
    boolean locationBlank = StringUtils.isBlank(location);
    
    if (mailAddressBlank && roomBlank && visitAddressBlank && locationBlank) {
      return;
    }
    
    Address address = new Address();
    
    if (!mailAddressBlank) {
      if (mailAddress.matches("^[0-9]{5}.*")) {
        String postalCode = StringUtils.trim(mailAddress.substring(0, 5));
        String locality = StringUtils.trim(mailAddress.substring(5));
        address.setPostalCode(postalCode);
        address.setLocality(locality);
      } else {
        address.setLocality(mailAddress); 
      }
    }
      
    if (!roomBlank) {
      address.setExtendedAddress(room);
    }
      
    if (!visitAddressBlank) {
      address.setStreetAddress(visitAddress);
    }
    
    vCard.addAddress(address);
  }

  @SuppressWarnings ("squid:S3776")
  private void handleTasks(Person person, VCard vCard) {
    boolean privateCard = false;
    boolean noCalls = false;
    
    if (person.getTask1() != null) {
      Categories categories = new Categories();
      categories.addParameter(MECM_TYPE, MECM_TYPE_CATEGORY_TASK);

      for (Task task : person.getTask1()) {
        if (StringUtils.isNotBlank(task.getText())) {
          String taskText = task.getText();
          if (ArrayUtils.contains(MECM_TASK1_PRIVATES, StringUtils.lowerCase(taskText))) {
            privateCard = true;
          } else if (MECM_TASK1_NO_CALLS.equals(taskText)) {
            noCalls = true;
          } else {
            categories.getValues().add(task.getText());
          }
        }
      }

      if (!categories.getValues().isEmpty()) {
        vCard.addCategories(categories);
      }
    }
    
    if (StringUtils.equals("1", StringUtils.trim(person.getTask7()))) {
      privateCard = true;
    }

    vCard.setExtendedProperty(MECM_ADDITIONAL_PRIVATE, privateCard ? "true" : "false");
    vCard.setExtendedProperty(MECM_ADDITIONAL_NO_CALLS, noCalls ? "true" : "false");
  }

  private Date toDate(OffsetDateTime dateTime) {
    return Date.from(dateTime.toInstant());
  }

  private void handleRefs(String organizationId, String uriTemplate, Person person, VCard vCard) {
    Refs refs = person.getRefs();
    if (refs != null) {
      handleRefs(organizationId, uriTemplate, vCard, refs);
    }
  }

  private void handleRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    handleManagerRefs(organizationId, cardUri, vcard, refs);
    handleSecretaryRefs(organizationId, cardUri, vcard, refs);
    handleSubstituteRefs(organizationId, cardUri, vcard, refs);
  }

  private void handleManagerRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    if (refs.getManager() != null && refs.getManager().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_RELATION_MANAGER, organizationId, refs.getManager()));
    }
  }

  private void handleSecretaryRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    if (refs.getSecretary1() != null && refs.getSecretary1().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_RELATION_SECRETARY, organizationId, refs.getSecretary1()));
    }
    
    if (refs.getSecretary2() != null && refs.getSecretary2().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_RELATION_SECRETARY, organizationId, refs.getSecretary2()));
    }
  }

  private void handleSubstituteRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    if (refs.getSubstitute1() != null && refs.getSubstitute1().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_RELATION_SUBSTITUTE, organizationId, refs.getSubstitute1()));
    }

    if (refs.getSubstitute2() != null && refs.getSubstitute2().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_RELATION_SUBSTITUTE, organizationId, refs.getSubstitute2()));
    }
  }
  
  private String createId(String organizationId, Integer merexId) {
    return String.format("urn:mecm:%s-%d", organizationId, merexId);
  }
  
  private Related createRelated(String uriTemplate, String refType, String organizationId, Ref ref) {
    Related result = new Related(createCardUri(uriTemplate, createId(organizationId, ref.getNumber())));
    result.addParameter(MECM_TYPE, refType);
    return result;
  }
  
  private String createCardUri(String uriTemplate, String id) {
    return String.format(uriTemplate, id);
  }
  
}
