package net.sintaxis.skywars.core.logic.worker;

import net.sintaxis.skywars.core.match.Match;

public interface Worker {

 /**
  * A simple class in charge of work a unique state of a match
  *
  * @param match The match to work
  */

 void work(Match match);
}