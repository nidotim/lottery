package com.nidotim.lottery.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
@Slf4j
public class SetToJsonConverter implements AttributeConverter<Set<Integer>, String> {

  @Autowired
  private ObjectMapper mapper;

  @Override
  public String convertToDatabaseColumn(Set<Integer> attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return mapper.writeValueAsString(attribute);
    } catch (IOException e) {
      throw new RuntimeException("Convert error while trying to convert set to json string.", e);
    }
  }

  @Override
  public Set<Integer> convertToEntityAttribute(String dbData) {
    if (null == dbData || dbData.length() == 0) {
      return Sets.newHashSet();
    }
    try {
      return mapper.readValue(dbData, new TypeReference<Set<Integer>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Could not convert json string to set.", e);
    }
  }
}
