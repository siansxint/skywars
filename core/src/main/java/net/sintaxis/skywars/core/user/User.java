package net.sintaxis.skywars.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.sintaxis.skywars.api.model.Savable;
import net.sintaxis.skywars.api.statistic.DoubleStatistic;
import net.sintaxis.skywars.api.statistic.IntegerStatistic;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.user.state.UserState;
import net.sintaxis.skywars.core.user.statistic.UserStatistic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@JsonSerialize(as = User.class)
public class User
        implements Savable {

 private final String id;

 private final DoubleStatistic money;

 private final UserStatistic soloStatistics;
 private final UserStatistic teamStatistics;

 private String language;

 private String selectedKit;
 private final Set<String> boughtKits
         = new HashSet<>();

 private String cageName;

 @JsonIgnore
 private final User.Temporally temporally;

 @ConstructorProperties("id")
 public User(String id) {
  this.id = id;
  this.language = "es";
  this.selectedKit = "default";
  this.cageName = "default";

  money = new DoubleStatistic();

  soloStatistics = new UserStatistic();
  teamStatistics = new UserStatistic();

  temporally = new Temporally(id, UserState.LOBBY, null);
 }

 @Override
 public String getId() {
  return id;
 }

 public String getLanguage() {
  return language;
 }

 public void setLanguage(String language) {
  this.language = language;
 }

 public String getSelectedKit() {
  return selectedKit;
 }

 public void setSelectedKit(String kitName) {
  this.selectedKit = kitName;
 }

 public Set<String> getBoughtKits() {
  return boughtKits;
 }

 public String getCageName() {
  return cageName;
 }

 public void setCageName(String name) {
  this.cageName = name;
 }

 public DoubleStatistic getMoney() {
  return money;
 }

 public UserStatistic getSoloStatistics() {
  return soloStatistics;
 }

 public UserStatistic getTeamStatistics() {
  return teamStatistics;
 }

 public UserStatistic getStatisticsByType(MatchType matchType) {
  return matchType == MatchType.SOLO ? soloStatistics : teamStatistics;
 }

 public User.Temporally getTemporally() {
  return temporally;
 }

 public static class Temporally {

  private final String id;
  private final IntegerStatistic kills;
  private UserState userState;
  private String matchName;
  private String teamName;

  public Temporally(String id, UserState userState, String matchName) {
   this.id = id;
   this.userState = userState;
   this.kills = new IntegerStatistic();
   this.matchName = matchName;
  }

  public Optional<Player> getRaw() {
   return Optional.ofNullable(Bukkit.getPlayer(UUID.fromString(id)));
  }

  public UserState getRole() {
   return userState;
  }

  public void setRole(UserState userState) {
   this.userState = userState;
  }

  public IntegerStatistic getMatchKills() {
   return kills;
  }

  public Optional<String> getMatchName() {
   return Optional.ofNullable(matchName);
  }

  public void setMatchName(String matchName) {
   this.matchName = matchName;
  }

  public Optional<String> getTeamName() {
   return Optional.ofNullable(teamName);
  }

  public void setTeamName(String teamName) {
   this.teamName = teamName;
  }
 }
}