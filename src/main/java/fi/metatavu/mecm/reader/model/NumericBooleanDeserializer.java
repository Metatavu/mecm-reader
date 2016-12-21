package fi.metatavu.mecm.reader.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {

  @Override
  public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContetx) throws IOException {
    JsonToken currentToken = jsonParser.getCurrentToken();

    if (currentToken.equals(JsonToken.VALUE_STRING)) {
      String text = jsonParser.getText().trim();
      return !"0".equals(text);
    } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
      return getNullValue(deserializationContetx);
    }

    return (Boolean) deserializationContetx.handleUnexpectedToken(Boolean.class, jsonParser);
  }

}
