package net.sintaxis.skywars.core.mapper.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.match.event.provider.MatchEventProvider;

import java.io.IOException;

public class MatchEventDeserializer
        extends StdDeserializer<MatchEvent> {

 private final MatchEventProvider matchEventProvider;

 public MatchEventDeserializer(MatchEventProvider matchEventProvider) {
  super(MatchEvent.class);
  this.matchEventProvider = matchEventProvider;
 }

 @Override
 public MatchEvent deserialize(JsonParser parser, DeserializationContext context)
         throws IOException {

  JsonNode node = parser.getCodec().readTree(parser);

  MatchEvent.EventType eventType = MatchEvent.EventType.valueOf(node.get("type").asText());
  int timeUntilNextEvent = node.get("timeUntilNextEvent").asInt();

  return matchEventProvider.provide(eventType, timeUntilNextEvent);
 }
}