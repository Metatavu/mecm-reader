package fi.metatavu.mecm.reader.mecm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.metatavu.mecm.reader.model.Merex;

public class MecmReader {

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
      return xmlMapper.readValue(inputStream, Merex.class);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    
    return null;
  }
  
}
