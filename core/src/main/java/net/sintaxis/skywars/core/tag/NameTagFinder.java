package net.sintaxis.skywars.core.tag;

import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.user.state.UserState;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class NameTagFinder {

 private final Map<UserState, NameTagFormat> formats;

 // TODO: 12/07/2021 de-hardcode this lol
 @Inject
 public NameTagFinder(YamlFile config) {
  this.formats = new HashMap<>();

  formats.put(
          UserState.ALIVE,
          new NameTagFormat(
                  config.getString("tag-formats.alive.prefix"),
                  config.getString("tag-formats.alive.suffix")
          )
  );

  formats.put(
          UserState.DEAD,
          new NameTagFormat(
                  config.getString("tag-formats.dead.prefix"),
                  config.getString("tag-formats.dead.suffix")
          )
  );
 }

 public NameTagFormat getTagFormatByType(UserState type) {
  return formats.getOrDefault(type, NameTagFormat.EMPTY_TAG_FORMAT);
 }
}