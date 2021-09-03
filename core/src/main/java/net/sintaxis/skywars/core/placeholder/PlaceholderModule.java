package net.sintaxis.skywars.core.placeholder;

import me.yushust.inject.AbstractModule;

import net.sintaxis.skywars.core.placeholder.types.MatchPlaceholder;
import net.sintaxis.skywars.core.placeholder.types.UserPlaceholder;

public class PlaceholderModule
        extends AbstractModule {

 @Override
 protected void configure() {
  bind(Placeholder.class)
          .to(MatchPlaceholder.class)
          .singleton();
  bind(Placeholder.class)
          .named("user-statistics")
          .to(UserPlaceholder.class)
          .singleton();
 }
}