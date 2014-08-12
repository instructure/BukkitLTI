package com.instructure.bukkitlti;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
  private BukkitLTI plugin;
  
  public JoinListener(BukkitLTI plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    this.plugin = plugin;
  }
  
  @EventHandler
  public void handle(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    User user = User.byPlayer(player);
    if (user == null) {return;}
    user.setStartDate();
    plugin.getDatabase().save(user);
  }
}