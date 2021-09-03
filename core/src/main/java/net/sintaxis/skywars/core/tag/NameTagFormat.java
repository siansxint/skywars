package net.sintaxis.skywars.core.tag;

public class NameTagFormat {

 public static final NameTagFormat EMPTY_TAG_FORMAT
         = new NameTagFormat("", "");

 private final String prefix;
 private final String suffix;

 public NameTagFormat(String prefix,
                      String suffix) {
  this.prefix = prefix;
  this.suffix = suffix;
 }

 public String getPrefix() {
  return prefix;
 }

 public String getSuffix() {
  return suffix;
 }
}