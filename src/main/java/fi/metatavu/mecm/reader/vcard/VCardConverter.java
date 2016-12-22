package fi.metatavu.mecm.reader.vcard;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.chain.ChainingTextWriter;
import ezvcard.property.Address;
import ezvcard.property.Categories;
import ezvcard.property.Email;
import ezvcard.property.Nickname;
import ezvcard.property.Organization;
import ezvcard.property.Related;
import ezvcard.property.Timezone;
import ezvcard.property.Uid;
import fi.metatavu.mecm.reader.model.DepartmentItem;
import fi.metatavu.mecm.reader.model.Memo;
import fi.metatavu.mecm.reader.model.Merex;
import fi.metatavu.mecm.reader.model.Person;
import fi.metatavu.mecm.reader.model.Ref;
import fi.metatavu.mecm.reader.model.Refs;
import fi.metatavu.mecm.reader.model.Task;

public class VCardConverter {
  
  private static final String TIMEZONE = "Europe/Helsinki";
  private static final String MECM_TYPE_DEPARTMENT = "DEPARTMENT";
  private static final String MECM_TYPE = "mecm-type";
  private static final String MECM_TYPE_TASK = "TASK";
  private static final String MECM_TYPE_SUBSTITUTE = "SUBSTITUTE";
  private static final String MECM_TYPE_SECRETARY = "SECRETARY";
  private static final String MECM_TYPE_MANAGER = "MANAGER";
  private static final String MECM_TYPE_GROUP = "GROUP";
  
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
        
        vCards.add(vCard);
      }
      
    }
    
    return vCards;
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
        organization.addParameter(MECM_TYPE, MECM_TYPE_DEPARTMENT);
        vCard.addOrganization(organization);
      }
    }

    if (StringUtils.isNotBlank(person.getGroup())) {
      Organization organization = new Organization();
      organization.getValues().add(person.getGroup());
      organization.addParameter(MECM_TYPE, MECM_TYPE_GROUP);
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
 
    vCard.addNote(noteBuilder.toString());
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
      address.setPoBox(mailAddress);
    }
      
    if (!roomBlank) {
      address.setExtendedAddress(room);
    }
      
    if (!visitAddressBlank) {
      address.setStreetAddress(visitAddress);
    }
    
    if (!locationBlank) {
      address.setLocality(location);
    }
    
    vCard.addAddress(address);
  }

  private void handleTasks(Person person, VCard vCard) {
    if (person.getTask1() != null) {
      Categories categories = new Categories();
      categories.addParameter(MECM_TYPE, MECM_TYPE_TASK);

      for (Task task : person.getTask1()) {
        if (StringUtils.isNotBlank(task.getText())) {
          categories.getValues().add(task.getText());
        }
      }

      if (!categories.getValues().isEmpty()) {
        vCard.addCategories(categories);
      }
    }
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
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_MANAGER, organizationId, refs.getManager()));
    }
  }

  private void handleSecretaryRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    if (refs.getSecretary1() != null && refs.getSecretary1().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_SECRETARY, organizationId, refs.getSecretary1()));
    }
    
    if (refs.getSecretary2() != null && refs.getSecretary2().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_SECRETARY, organizationId, refs.getSecretary2()));
    }
  }

  private void handleSubstituteRefs(String organizationId, String cardUri, VCard vcard, Refs refs) {
    if (refs.getSubstitute1() != null && refs.getSubstitute1().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_SUBSTITUTE, organizationId, refs.getSubstitute1()));
    }

    if (refs.getSubstitute2() != null && refs.getSubstitute2().getNumber() != null) {
      vcard.addRelated(createRelated(cardUri, MECM_TYPE_SUBSTITUTE, organizationId, refs.getSubstitute2()));
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