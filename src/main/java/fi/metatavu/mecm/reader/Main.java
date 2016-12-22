package fi.metatavu.mecm.reader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.metatavu.mecm.reader.model.Merex;
import fi.metatavu.mecm.reader.vcard.VCardConverter;

public class Main {
  
  private static final String OPTION_URI_TEMPLATE = "uri-template";
  private static final String OPTION_ORGANIZATION = "organization";
  private static final String OPTION_OUTPUT = "output";
  private static final String OPTION_OUTPUT_FORMAT = "output-format";
  private static final String OPTION_INPUT = "input";
  private static final String OPTION_HELP = "help";
  
  private Main() {
  }
  
  @SuppressWarnings ("squid:S1301")
  public static void main(String[] args) throws ParseException {
    CommandLine commandLine = handleOptions(args);
    if (commandLine == null) {
      return;
    }
    
    OutputFormat outputFormat = EnumUtils.getEnum(OutputFormat.class, commandLine.getOptionValue(OPTION_OUTPUT_FORMAT));
    File inputFile = new File(commandLine.getOptionValue(OPTION_INPUT));
    File outputFile = new File(commandLine.getOptionValue(OPTION_OUTPUT));
    String organizationId = commandLine.getOptionValue(OPTION_ORGANIZATION);
    String uriTemplate = commandLine.getOptionValue(OPTION_URI_TEMPLATE);
    
    Merex merex = readMerex(inputFile);
    if (merex == null) {
      System.exit(-1);
    }
    
    switch (outputFormat) {
      case VCARD:
      if (!convertToVCard(outputFile, organizationId, uriTemplate, merex)) {
        System.exit(-1);
      }
      break;
      default:
    }
  }

  @SuppressWarnings ({"squid:S1166"})
  private static CommandLine handleOptions(String[] args) throws ParseException {
    Options options = new Options();
    
    options.addOption(createOption(true, "i", OPTION_INPUT, true, "Input file name (MECM)"));
    options.addOption(createOption(true, "f", OPTION_OUTPUT_FORMAT, true, String.format("Format the input file will be converted. Poosible values: %s", StringUtils.join(OutputFormat.values(), ","))));
    options.addOption(createOption(true, "o", OPTION_OUTPUT, true, "Output file name)"));
    options.addOption(createOption(true, "O", OPTION_ORGANIZATION, true, "Organization Id"));
    options.addOption(createOption(false, "t", OPTION_URI_TEMPLATE, true, "URI Template. Use %s as placeholder for id"));
    options.addOption(createOption(false, "h", OPTION_HELP, false, "Prints help"));
    
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption(OPTION_HELP)) {
        printHelp(options);
        return null;
      }
      
      return commandLine;
    } catch (MissingOptionException e) {
      printHelp(options);
    }
    
    return null;
  }

  private static void printHelp(Options options) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("java -jar mecm-reader.jar", options);
  }

  @SuppressWarnings ({"squid:S106", "squid:S1166"})
  private static Merex readMerex(File inputFile) {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.registerModule(new JavaTimeModule());
    try {
      return xmlMapper.readValue(inputFile, Merex.class);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    
    return null;
  }
  
  @SuppressWarnings ({"squid:S106", "squid:S1166"})
  private static boolean convertToVCard(File outputFile, String organizationId, String uriTemplate, Merex merex) {
    try {
      VCardConverter vCardConverter = new VCardConverter();
      vCardConverter.toVCardFile(organizationId, uriTemplate, merex, outputFile);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return false;
    }
    
    return true;
  }
  
  private static Option createOption(boolean required, String opt, String longOpt, boolean hasArg, String description) {
    Option option = new Option(opt, longOpt, hasArg, description);
    option.setRequired(required);
    return option;
  }
  
  private enum OutputFormat {
    
    VCARD
    
  }

}
