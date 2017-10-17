package fi.metatavu.mecm.reader.vcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.RawProperty;
import ezvcard.property.Related;
import ezvcard.property.Telephone;
import ezvcard.property.Title;
import fi.metatavu.mecm.reader.mecm.MecmReader;
import fi.metatavu.mecm.reader.model.Merex;
import fi.metatavu.mecm.reader.model.Status;
import fi.metatavu.mecm.reader.model.Wt;

public class ConverterTest {
 
  private static final String TRUE = "true";
  private static final String FALSE = "false";
  private static final String URI_TEMPLATE = "http://example.com/cards/%s";
  private static final String ORGANIZATION_ID = "test";
  
  @Test
  public void testBasics() {
    String cardId = createId("1234");
    OffsetDateTime modified = OffsetDateTime.of(2017, 1, 2, 3, 4, 5, 0, ZoneOffset.ofHours(2));
    Date modifiedDate = Date.from(modified.toInstant());
    VCard vCard = loadTestCard(0);
    
    assertEquals(cardId, vCard.getUid().getValue());
    assertEquals(modifiedDate, vCard.getRevision().getValue());
    assertEquals("Test Person", vCard.getFormattedName().getValue());
    assertEquals(Arrays.asList("Nick Name 1"), vCard.getNickname().getValues());
    assertEquals(1, vCard.getEmails().size());
    assertEquals("test.person@example.com", vCard.getEmails().get(0).getValue());
    assertEquals("TEST", vCard.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_PBX).getValue());
  }

  @Test
  public void testNumbers() {
    VCard vCard = loadTestCard(0);

    List<Telephone> numbers = vCard.getTelephoneNumbers();
    assertEquals(9, numbers.size());
    assertNumber(numbers.get(0), VCardConverter.MECM_TYPE_NUMBER_NUMBER, "123456");
    assertNumber(numbers.get(1), VCardConverter.MECM_TYPE_NUMBER_MOBILE, "+358 50 123 4567");
    assertNumber(numbers.get(2), VCardConverter.MECM_TYPE_NUMBER_OTHER, "0987654321");
    assertNumber(numbers.get(3), VCardConverter.MECM_TYPE_NUMBER_SUBSTITUTE, "1357924680");
    assertNumber(numbers.get(4), VCardConverter.MECM_TYPE_NUMBER_SECRETARY, "123581321");
    assertNumber(numbers.get(5), VCardConverter.MECM_TYPE_NUMBER_SPARE, "248163264128");
    assertNumber(numbers.get(6), VCardConverter.MECM_TYPE_NUMBER_ADDITIONAL, "1231234");
    assertNumber(numbers.get(7), VCardConverter.MECM_TYPE_NUMBER_ADDITIONAL, "6543210");
    assertNumber(numbers.get(8), VCardConverter.MECM_TYPE_NUMBER_FAX, "987987666");
  }
  
  @Test
  public void testCategories() {    
    VCard vCard = loadTestCard(0);
    
    List<String> categories = vCard.getCategories().getValues();
    assertEquals(2, categories.size());
    assertEquals("testing", categories.get(0));
    assertEquals("integration testing", categories.get(1));
  }

  @Test
  public void testTitles() {    
    VCard vCard = loadTestCard(0);    
    
    List<Title> titles = vCard.getTitles();
    assertEquals(1, titles.size());
    assertEquals("tester", titles.get(0).getValue());
  }

  @Test
  public void testAddresses() {    
    VCard vCard = loadTestCard(0);
    
    List<Address> addresses = vCard.getAddresses();
    assertEquals(1, addresses.size());
    assertEquals("Test street 123", addresses.get(0).getStreetAddress());
    assertEquals("Testia", addresses.get(0).getLocality());
    assertEquals("12345", addresses.get(0).getPostalCode());
    assertEquals("Test room", addresses.get(0).getExtendedAddress());
    assertNull(addresses.get(0).getPoBox());
  }

  @Test
  public void testRelations() {    
    VCard vCard = loadTestCard(0);  
    
    assertEquals(3, vCard.getRelations().size());
    assertRelation(vCard.getRelations().get(0), "567", VCardConverter.MECM_TYPE_RELATION_MANAGER);
    assertRelation(vCard.getRelations().get(1), "456", VCardConverter.MECM_TYPE_RELATION_SECRETARY);
    assertRelation(vCard.getRelations().get(2), "123", VCardConverter.MECM_TYPE_RELATION_SUBSTITUTE);
  }

  @Test
  public void testNotes() {    
    VCard vCard = loadTestCard(0);    
    List<Note> notes = vCard.getNotes();
    assertEquals(1, notes.size());
    assertEquals("Test header\nTest text", notes.get(0).getValue());
    assertEquals("12345", notes.get(0).getParameter(VCardConverter.MECM_NOTE_ID));
    assertEquals("255", notes.get(0).getParameter(VCardConverter.MECM_NOTE_PRIORITY));
  }
  
  @Test
  public void testOrganizations() {    
    VCard vCard = loadTestCard(0);
    
    List<Organization> organizations = vCard.getOrganizations();
    assertEquals(5, organizations.size());
    assertOrganization(organizations.get(0), null, "Test company");
    assertOrganization(organizations.get(1), VCardConverter.MECM_TYPE_ORGANIZATION_UNIT_DEPARTMENT, "Test services");
    assertOrganization(organizations.get(2), VCardConverter.MECM_TYPE_ORGANIZATION_UNIT_DEPARTMENT, "Testers");
    assertOrganization(organizations.get(3), VCardConverter.MECM_TYPE_ORGANIZATION_UNIT_DEPARTMENT, "Integration testers");
    assertOrganization(organizations.get(4), VCardConverter.MECM_TYPE_ORGANIZATION_UNIT_GROUP, "Testers");
  }
  
  @Test
  public void testWts() throws IOException, JsonParseException, JsonMappingException {
    VCard vCard = loadTestCard(0);
    
    List<RawProperty> wtsProperties = vCard.getExtendedProperties(VCardConverter.MECM_ADDITIONAL_WT_JSON);
    assertEquals(1, wtsProperties.size());
    Wt wt = readJSON(Wt.class, wtsProperties.get(0).getValue());
    assertEquals("1111", wt.getId());
    assertEquals("Test device", wt.getDevice());
    assertEquals("2222", wt.getCard());
  }

  @Test
  public void testStatus() throws IOException, JsonParseException, JsonMappingException {
    VCard vCard = loadTestCard(0);
    
    List<RawProperty> statusProperties = vCard.getExtendedProperties(VCardConverter.MECM_ADDITIONAL_STATUS_JSON);
    Status status = readJSON(Status.class, statusProperties.get(0).getValue());
    assertEquals(1, statusProperties.size());
    assertEquals(98765, status.getId().intValue());
    assertEquals("Test Creator", status.getCreator());
    assertEquals("Testing", status.getReason());
    assertEquals("Testing away", status.getComment());
    assertEquals(OffsetDateTime.of(2017, 6, 5, 0, 0, 0, 0, ZoneOffset.ofHours(2)).toInstant(), status.getDeparture().toInstant());
    assertEquals(OffsetDateTime.of(2017, 6, 6, 0, 0, 0, 0, ZoneOffset.ofHours(2)).toInstant(), status.getArrival().toInstant());
  }
  
  @Test
  public void testPrivateCard() {
    VCard vCard1 = loadTestCard(0);
    VCard vCard2 = loadTestCard(1);
    VCard vCard3 = loadTestCard(2);
    VCard vCard4 = loadTestCard(3);

    assertEquals(FALSE, vCard1.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_PRIVATE).getValue());
    assertEquals(TRUE, vCard2.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_PRIVATE).getValue());
    assertEquals(TRUE, vCard3.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_PRIVATE).getValue());
    assertEquals(TRUE, vCard4.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_PRIVATE).getValue());
  }

  @Test
  public void testNoCalls() {
    VCard vCard1 = loadTestCard(0);
    VCard vCard2 = loadTestCard(1);
    VCard vCard3 = loadTestCard(2);

    assertEquals(FALSE, vCard1.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_NO_CALLS).getValue());
    assertEquals(TRUE, vCard2.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_NO_CALLS).getValue());
    assertEquals(FALSE, vCard3.getExtendedProperty(VCardConverter.MECM_ADDITIONAL_NO_CALLS).getValue());
  }
  
  private VCard loadTestCard(int index) {
    MecmReader mecmReader = new MecmReader();
    InputStream xmlStream = getClass().getClassLoader().getResourceAsStream("persons.xml");
    assertNotNull(xmlStream);
    
    Merex merex = mecmReader.readMecm(xmlStream);
    assertNotNull(merex);
    
    VCardConverter vCardConverter = new VCardConverter();
    List<VCard> vCards = vCardConverter.toVCards(ORGANIZATION_ID, URI_TEMPLATE, merex);
    assertEquals(4, vCards.size());
    VCard vCard = vCards.get(index);
    
    assertNotNull(vCard);
    
    return vCard;
  }
  
  private void assertRelation(Related related, String id, String type) {
    assertEquals(String.format(URI_TEMPLATE, createId(id)), related.getUri());
    assertEquals(related.getParameter("mecm-type"), type);
  }

  private String createId(String mecmId) {
    return String.format("urn:mecm:%s-%s", ORGANIZATION_ID, mecmId);
  }

  private void assertNumber(Telephone actual, String mecmType, String number) {
    assertEquals(Arrays.asList(mecmType), actual.getParameters("mecm-type"));
    assertEquals(number, actual.getText());
  }

  private void assertOrganization(Organization actual, String mecmType, String text) {
    if (mecmType != null) {
      assertEquals(Arrays.asList(mecmType), actual.getParameters("mecm-type"));
    }
    
    assertEquals(Arrays.asList(text), actual.getValues());
  }
  
  private <T> T readJSON(Class<T> entityClass, String value) throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper.readValue(value, entityClass);
  }
}
