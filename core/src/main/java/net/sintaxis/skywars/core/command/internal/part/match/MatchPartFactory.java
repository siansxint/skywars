package net.sintaxis.skywars.core.command.internal.part.match;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;

import javax.inject.Inject;

import java.lang.annotation.Annotation;
import java.util.List;

public class MatchPartFactory
        implements PartFactory {

 @Inject
 private ObjectCache<Match> matchObjectCache;

 @Override
 public CommandPart createPart(String name,
                               List<? extends Annotation> modifiers
 ) {
  return new MatchPart(name, matchObjectCache);
 }
}