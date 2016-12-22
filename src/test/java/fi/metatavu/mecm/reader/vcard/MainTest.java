package fi.metatavu.mecm.reader.vcard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import fi.metatavu.mecm.reader.Main;

public class MainTest {
 
  private static final String URI_TEMPLATE = "http://example.com/cards/%s";
  private static final String ORGANIZATION_ID = "test";
  
  @Test
  public void testBasic() {
    try {
      String inputFile = getClass().getClassLoader().getResource("persons.xml").getPath();
      String outputFile = String.format("%s/%s.vcard", System.getProperty("java.io.tmpdir"), System.currentTimeMillis());
      
      assertFalse(new File(outputFile).exists());
      
      Main.main(new String[] { "-f", "VCARD", "-i",  inputFile, "-o", outputFile, "-O", ORGANIZATION_ID, "-t", URI_TEMPLATE } );

      assertTrue(new File(outputFile).exists());
      
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
