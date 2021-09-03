package net.sintaxis.skywars.core.logic.announcer;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.packet.actionbar.ActionbarMessenger;
import net.sintaxis.skywars.core.packet.title.TitleMessenger;
import net.sintaxis.skywars.core.user.finder.UserFinder;

@InjectAll
public class CountdownAnnouncer {

 private MessageHandler messageHandler;
 private UserFinder userFinder;
 private TitleMessenger titleMessenger;
 private ActionbarMessenger actionbarMessenger;

 public void announce(CountdownType countdownType, Match match, int second) {
  userFinder.getMatchPlayers(match)
          .forEach(player -> {

           player.setLevel(second);

           if (!isImportantSecond(second)) {
            return;
           }

           messageHandler.sendReplacing(player,
                   "countdown." + countdownType.toString().toLowerCase() + ".message",
                   "%seconds-remaining%", second);
           titleMessenger.sendBoth(
                   player,
                   messageHandler.replacing(player, "countdown." + countdownType.toString().toLowerCase() + ".title", "%seconds-remaining%", second),
                   messageHandler.replacing(player, "countdown." + countdownType.toString().toLowerCase() + ".sub-title", "%seconds-remaining%", second),
                   20,
                   20,
                   20
           );
           actionbarMessenger.send(
                   player,
                   messageHandler.replacing(player, "countdown." + countdownType.toString().toLowerCase() + ".action-bar", "%seconds-remaining%", second)
           );
          });
 }

 private boolean isImportantSecond(int second) {
  return second <= 5 || second % 15 == 0;
 }

 public enum CountdownType {
  LOBBY,
  CAGES
 }
}