package fi.metatavu.mecm.reader.vcard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import fi.metatavu.mecm.reader.Main;

public class MainTest {
 
  private static final String URI_TEMPLATE = "http://example.com/cards/%s";
  private static final String ORGANIZATION_ID = "test";
  
  @Test
  public void testBasic() throws ParseException {
    String inputFile = getClass().getClassLoader().getResource("persons.xml").getPath();
    String outputFile = String.format("%s/%s.vcard", System.getProperty("java.io.tmpdir"), System.currentTimeMillis());
    
    assertFalse(new File(outputFile).exists());
    
    Main.main(new String[] { "-f", "VCARD", "-i",  inputFile, "-o", outputFile, "-O", ORGANIZATION_ID, "-t", URI_TEMPLATE } );

    assertTrue(new File(outputFile).exists());
  }

  @Test
  public void testMissingOptions() throws ParseException, UnsupportedEncodingException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    Main.main(new String[] {});
    String stdOut = outputStream.toString("UTF-8");
    assertTrue(StringUtils.startsWith(stdOut, "usage:"));
  }

  @Test
  public void testHelp() throws ParseException, UnsupportedEncodingException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    Main.main(new String[] {"-h"});
    String stdOut = outputStream.toString("UTF-8");
    assertTrue(StringUtils.startsWith(stdOut, "usage:"));
  }

}
