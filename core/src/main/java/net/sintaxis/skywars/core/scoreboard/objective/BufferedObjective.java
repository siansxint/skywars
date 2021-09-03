package net.sintaxis.skywars.core.scoreboard.objective;

import net.sintaxis.skywars.core.scoreboard.team.ScoreboardTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BufferedObjective {

 private final static String[] scoreTeams = new String[16];

 static {
  for (int i = 0; i < 15; i++) {
   scoreTeams[i] = ChatColor.values()[i].toString() + ChatColor.RESET;
  }
 }

 private final Scoreboard scoreboard;
 private final Objective objective;
 private final Map<Integer, ScoreboardTeam> teams;

 private final String ownerId;
 private boolean needUpdate;

 public BufferedObjective(Scoreboard scoreboard,
                          String ownerId) {
  this.ownerId = ownerId;
  this.scoreboard = scoreboard;
  objective = scoreboard.registerNewObjective("SkyWars", "dummy");
  objective.setDisplaySlot(DisplaySlot.SIDEBAR);

  teams = new HashMap<>();
 }

 public void setTitle(String title) {
  this.objective.setDisplayName(title);
 }

 public Objective getObjective() {
  return objective;
 }

 public boolean needUpdate() {
  return needUpdate;
 }

 public void setUpdateState(boolean updateState) {
  this.needUpdate = updateState;
 }

 public void update() {
  if (needUpdate) {
   teams.values().forEach(ScoreboardTeam::update);
   setUpdateState(false);
  }
 }

 public ScoreboardTeam getOrCreateTeam(int position) {

  if (!teams.containsKey(position)) {

   Score score = objective.getScore(scoreTeams[position]);
   score.setScore(position);

   ScoreboardTeam scoreboardTeam = new ScoreboardTeam(
           scoreboard.registerNewTeam(scoreTeams[position]),
           position,
           score,
           this
   );

   scoreboardTeam.getTeam().addEntry(scoreTeams[position]);

   teams.put(position, scoreboardTeam);
   return scoreboardTeam;
  }

  return teams.get(position);
 }

 public Scoreboard getScoreboard() {
  return scoreboard;
 }

 public Map<Integer, ScoreboardTeam> getScoreboardTeams() {
  return teams;
 }

 public Player getOwner() {
  return Bukkit.getPlayer(UUID.fromString(ownerId));
 }
}