package net.sintaxis.skywars.core.scoreboard.entry;

import net.sintaxis.skywars.core.placeholder.applier.PlaceholderApplier;
import net.sintaxis.skywars.core.scoreboard.objective.BufferedObjective;
import net.sintaxis.skywars.core.scoreboard.team.ScoreboardTeam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScoreboardEntry {

 private final BufferedObjective bufferedObjective;

 private final List<String> text = new ArrayList<>();
 private final List<String> parsedText;
 private final int interval;
 private ScoreboardTeam scoreboardTeam;

 private int ticks = 0;
 private int index = 1;

 public ScoreboardEntry(ScoreboardTeam scoreboardTeam,
                        Map<String, Object> args
 ) {
  this(args, null);
  this.scoreboardTeam = scoreboardTeam;
 }

 @SuppressWarnings("unchecked")
 public ScoreboardEntry(Map<String, Object> args,
                        BufferedObjective bufferedObjective
 ) {
  this.bufferedObjective = bufferedObjective;
  text.addAll((Collection<? extends String>) args.get("text"));
  parsedText = new ArrayList<>();
  interval = (int) args.get("interval");
 }

 public void prepare() {
  parsedText.clear();
  parsedText.addAll(text);

  if (parsedText.isEmpty()) {
   return;
  }

  setText(parsedText.get(0));
 }

 public void run() {
  if (parsedText.isEmpty()) {
   return;
  }

  ticks++;

  if (ticks >= interval) {
   ticks = 0;

   setText(parsedText.get(index++ % parsedText.size()));
  }
 }

 private void setText(String text) {
  if (bufferedObjective == null) {
   scoreboardTeam.setText(PlaceholderApplier.replace(scoreboardTeam.getBufferedObjective().getOwner(), text));
   scoreboardTeam.getBufferedObjective().setUpdateState(true);
   return;
  }

  bufferedObjective.setTitle(PlaceholderApplier.replace(bufferedObjective.getOwner(), text));
 }

 public ScoreboardTeam getScoreboardTeam() {
  return scoreboardTeam;
 }

 public BufferedObjective getBufferedObjective() {
  return bufferedObjective;
 }
}