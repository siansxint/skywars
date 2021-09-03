package net.sintaxis.skywars.core.scoreboard.team;

import com.google.common.base.Splitter;

import net.sintaxis.skywars.core.scoreboard.objective.BufferedObjective;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScoreboardTeam {

 private final Score score;
 private final int position;

 private final Team team;
 private final BufferedObjective bufferedObjective;
 private String text;
 private String lastText;

 public ScoreboardTeam(Team team,
                       int position,
                       Score score,
                       BufferedObjective bufferedObjective
 ) {
  this.team = team;
  this.position = position;
  this.score = score;
  this.bufferedObjective = bufferedObjective;
 }

 public void update() {

  if (text == null) {
   team.setPrefix("ERROR");
   return;
  }

  if (text.equalsIgnoreCase(lastText)) {
   return;
  }

  lastText = text;

  if (text.contains("#N/A")) {
   team.setPrefix("");
   team.setSuffix("");
   return;
  }

  List<String> strings = format(ChatColor.translateAlternateColorCodes('&', text));
  team.setPrefix(strings.get(0));

  if (strings.size() > 1) {
   team.setSuffix(strings.get(1));
  } else {
   team.setSuffix("");
  }
 }

 public String getText() {
  return text;
 }

 public void setText(String text) {
  this.text = text;
 }

 public Score getScore() {
  return score;
 }

 public int getPosition() {
  return position;
 }

 public Team getTeam() {
  return team;
 }

 public BufferedObjective getBufferedObjective() {
  return bufferedObjective;
 }

 private List<String> format(String text) {
  List<String> list = new ArrayList<>();
  if (text.length() <= 16) {
   list.add(text);
   return list;
  }

  Iterator<String> iterator = Splitter.fixedLength(16)
          .split(text)
          .iterator();

  list.add(iterator.next());
  String line = iterator.next();
  char lastChar = list.get(0).charAt(15);
  char firstChar = line.charAt(0);

  if ((lastChar == '&' || lastChar == ChatColor.COLOR_CHAR) && ChatColor.getByChar(firstChar) != null) {
   list.set(0, list.get(0).substring(0, 15));
   line = lastChar + line;
  }

  line = ChatColor.getLastColors(list.get(0)) + line;
  if (line.length() > 16) {
   line = line.substring(0, 15);
  }

  list.add(line);
  return list;
 }
}