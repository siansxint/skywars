package net.sintaxis.skywars.core.scoreboard;

import net.sintaxis.skywars.core.scoreboard.entry.ScoreboardEntry;
import net.sintaxis.skywars.core.scoreboard.objective.BufferedObjective;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {

 private final ScoreboardEntry title;
 private final Map<String, ScoreboardEntry> entries
         = new ConcurrentHashMap<>();

 private final BufferedObjective bufferedObjective;

 public Scoreboard(ConfigurationSection scoreboardSection,
                   String ownerId
 ) {
  bufferedObjective = new BufferedObjective(Bukkit.getScoreboardManager().getNewScoreboard(), ownerId);

  title = new ScoreboardEntry(scoreboardSection.getConfigurationSection("title").getValues(true), bufferedObjective);

  Set<String> lines = scoreboardSection.getConfigurationSection("content").getKeys(false);
  int[] size = {lines.size()};
  lines.forEach(s -> entries.put(s, new ScoreboardEntry(bufferedObjective.getOrCreateTeam(size[0]--), scoreboardSection.getConfigurationSection("content." + s).getValues(true))));
 }

 public void show(Player player) {
  title.prepare();
  entries.values().forEach(ScoreboardEntry::prepare);
  run();
  player.setScoreboard(bufferedObjective.getScoreboard());
 }

 public Map<String, ScoreboardEntry> getEntries() {
  return entries;
 }

 public void run() {
  title.run();
  entries.values().forEach(ScoreboardEntry::run);
  bufferedObjective.update();
 }
}