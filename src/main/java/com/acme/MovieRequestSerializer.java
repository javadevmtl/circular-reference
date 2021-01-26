package com.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MovieRequestSerializer implements Serializer<Movie>
{
  private static final Logger LOG = LoggerFactory.getLogger(MovieRequestSerializer.class);

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void configure(Map<String, ?> configs, boolean isKey)
  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Override
  public byte[] serialize(String topic, Movie data)
  {
    byte[] retVal = null;

    try {
      retVal = objectMapper.writeValueAsBytes(data);
    } catch (Exception ex) {
      LOG.error("Failed to serialize Movie request to Json.", ex);
    }

    return retVal;
  }

  @Override
  public void close()
  {
    // nothing
  }
}