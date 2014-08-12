package com.instructure.bukkitlti;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
  public QuitListener(BukkitLTI plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void handle(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    User user = User.byPlayer(player);
    if (user == null) {return;}
    user.handleQuit();
  }
}