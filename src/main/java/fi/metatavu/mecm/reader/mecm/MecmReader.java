package fi.metatavu.mecm.reader.mecm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import fi.metatavu.mecm.reader.model.Merex;

public class MecmReader {
  
  private static final String INVALID_XML_CHARACTERS_PATTERN = "[^"
    + "\u0009\r\n"
    + "\u0020-\uD7FF"
    + "\uE000-\uFFFD"
    + "\ud800\udc00-\udbff\udfff"
    + "]";

  public Merex readMecm(File inputFile) throws IOException {
    try (FileInputStream inputStream = new FileInputStream(inputFile)) {
      return readMecm(inputStream);
    }
  }
  
  @SuppressWarnings ({"squid:S106", "squid:S1166"})
  public Merex readMecm(InputStream inputStream) {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.registerModule(new JavaTimeModule());
    try {
      String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      xml = xml.replaceAll(INVALID_XML_CHARACTERS_PATTERN, "");
      return xmlMapper.readValue(xml, Merex.class);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    
    return null;
  }
  
}