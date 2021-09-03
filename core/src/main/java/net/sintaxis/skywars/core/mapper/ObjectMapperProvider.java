package net.sintaxis.skywars.core.mapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.sintaxis.skywars.core.mapper.deserializer.MatchEventDeserializer;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.match.event.provider.MatchEventProvider;

import javax.inject.Provider;

public class ObjectMapperProvider
        implements Provider<ObjectMapper> {

 private final MatchEventProvider matchEventProvider;

 public ObjectMapperProvider(MatchEventProvider matchEventProvider) {
  this.matchEventProvider = matchEventProvider;
 }

 @Override
 public ObjectMapper get() {
  ObjectMapper mapper = new ObjectMapper();
  mapper.setVisibility(mapper.getSerializationConfig()
          .getDefaultVisibilityChecker()
          .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
          .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
          .withIsGetterVisibility(JsonAutoDetect.Visibility.ANY)
          .withSetterVisibility(JsonAutoDetect.Visibility.ANY)
          .withCreatorVisibility(JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC));
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  SimpleModule module = new SimpleModule();
  module.addDeserializer(MatchEvent.class, new MatchEventDeserializer(matchEventProvider));
  mapper.registerModule(module);
  return mapper;
 }
}