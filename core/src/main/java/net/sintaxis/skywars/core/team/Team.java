package net.sintaxis.skywars.core.team;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.user.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class Team
        implements Iterable<User> {

 private final String name;
 private final Set<User> members;
 private AbbreviatedCoordinates coordinates;

 public Team(String name) {
  this.name = name;
  this.members = new HashSet<>();
 }

 public String getName() {
  return name;
 }

 public Set<User> getMembers() {
  return members;
 }

 public Optional<AbbreviatedCoordinates> getCoordinates() {
  return Optional.ofNullable(coordinates);
 }

 public void setCoordinates(AbbreviatedCoordinates coordinates) {
  this.coordinates = coordinates;
 }

 public @NotNull Iterator<User> iterator() {
  return members.iterator();
 }
}