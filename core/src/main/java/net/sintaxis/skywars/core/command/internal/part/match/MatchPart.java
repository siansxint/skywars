package net.sintaxis.skywars.core.command.internal.part.match;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.model.Savable;
import net.sintaxis.skywars.core.match.Match;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MatchPart
        implements ArgumentPart {

 private final String name;
 private final ObjectCache<Match> matchObjectCache;

 public MatchPart(String name,
                  ObjectCache<Match> matchObjectCache) {
  this.name = name;
  this.matchObjectCache = matchObjectCache;
 }

 @Override
 public List<Match> parseValue(CommandContext context,
                               ArgumentStack stack
 ) throws ArgumentParseException {
  String possibleMatchName =
          stack.next();

  Optional<Match> optionalMatch =
          matchObjectCache.find(possibleMatchName);

  if (!optionalMatch.isPresent()) {
   throw new ArgumentParseException(TranslatableComponent.of("match.not-found", TextComponent.of(possibleMatchName)));
  }

  return Collections.singletonList(optionalMatch.get());
 }

 @Override
 public List<String> getSuggestions(CommandContext commandContext,
                                    ArgumentStack stack
 ) {
  String possibleMatchName = stack.hasNext() ? stack.next() : "";

  if (matchObjectCache.find(possibleMatchName).isPresent()) {
   return Collections.emptyList();
  }

  return matchObjectCache
          .get()
          .values()
          .stream()
          .map(Savable::getId)
          .filter(s -> s.startsWith(possibleMatchName))
          .collect(Collectors.toList());
 }

 @Override
 public Type getType() {
  return Match.class;
 }

 @Override
 public String getName() {
  return name;
 }
}