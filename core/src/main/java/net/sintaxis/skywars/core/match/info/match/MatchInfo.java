package net.sintaxis.skywars.core.match.info.match;

import java.beans.ConstructorProperties;

public class MatchInfo {

 private final int minTeamsCapacity;
 private final int maxTeamsCapacity;
 private final int membersPerTeamSize;

 @ConstructorProperties({
         "minTeamsCapacity",
         "maxTeamsCapacity",
         "membersPerTeamSize"
 })
 public MatchInfo(int minTeamsCapacity,
                  int maxTeamsCapacity,
                  int membersPerTeamSize
 ) {
  this.minTeamsCapacity = minTeamsCapacity;
  this.maxTeamsCapacity = maxTeamsCapacity;
  this.membersPerTeamSize = membersPerTeamSize;
 }

 public int getMinTeamsCapacity() {
  return minTeamsCapacity;
 }

 public int getMaxTeamsCapacity() {
  return maxTeamsCapacity;
 }

 public int getMembersPerTeamSize() {
  return membersPerTeamSize;
 }
}