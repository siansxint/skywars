package net.sintaxis.skywars.core.command.internal.part.user;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.user.User;

import javax.inject.Inject;

import java.lang.annotation.Annotation;

import java.util.List;

public class UserSenderPartFactory
        implements PartFactory {

 @Inject
 private ObjectCache<User> userObjectCache;

 @Override
 public CommandPart createPart(String name,
                               List<? extends Annotation> modifiers
 ) {
  return new UserSenderPart(name, userObjectCache);
 }
}