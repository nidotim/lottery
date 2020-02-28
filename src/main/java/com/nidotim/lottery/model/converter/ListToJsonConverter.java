package com.nidotim.lottery.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
@Slf4j
public class ListToJsonConverter implements AttributeConverter<List<Integer>, String> {

  @Autowired
  private ObjectMapper mapper;

  @Override
  public String convertToDatabaseColumn(List<Integer> attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return mapper.writeValueAsString(attribute);
    } catch (IOException e) {
      throw new RuntimeException("Convert error while trying to convert list to json string.", e);
    }
  }

  @Override
  public List<Integer> convertToEntityAttribute(String dbData) {
    if (null == dbData || dbData.length() == 0) {
      return new ArrayList<>();
    }
    try {
      return mapper.readValue(dbData, new TypeReference<List<Integer>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Could not convert json string to list.", e);
    }
  }
}
