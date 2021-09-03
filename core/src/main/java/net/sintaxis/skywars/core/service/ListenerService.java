package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.listener.*;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@InjectAll
public class ListenerService
        implements Service {

 private SkyWars skyWars;
 private PlayerJoinListener playerJoinListener;
 private PlayerQuitListener playerQuitListener;
 private AsyncPlayerChatListener asyncPlayerChatListener;
 private BlockBreakListener blockBreakListener;
 private BlockPlaceListener blockPlaceListener;
 private EntityDamageByEntityListener entityDamageByEntityListener;
 private EntityDamageListener entityDamageListener;
 private FoodLevelChangeListener foodLevelChangeListener;
 private MatchEndListener matchEndListener;
 private MatchJoinListener matchJoinListener;
 private MatchLeaveListener matchLeaveListener;
 private PlayerDeathListener playerDeathListener;
 private PlayerDropItemListener playerDropItemListener;
 private PlayerMoveListener playerMoveListener;

 @Override
 public void start() {
  registerListeners(
          playerJoinListener,
          playerQuitListener,
          asyncPlayerChatListener,
          blockBreakListener,
          blockPlaceListener,
          entityDamageListener,
          entityDamageByEntityListener,
          matchEndListener,
          foodLevelChangeListener,
          matchJoinListener,
          matchLeaveListener,
          playerDeathListener,
          playerDropItemListener,
          playerMoveListener
  );
 }

 private void registerListeners(Listener... listeners) {
  for (Listener listener : listeners) {
   Bukkit.getPluginManager().registerEvents(listener, skyWars);
  }
 }
}