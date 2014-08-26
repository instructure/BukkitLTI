package com.instructure.bukkitlti;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.base.Joiner;
import com.instructure.minecraftlti.User;

public class BukkitLTIListener implements Listener {
  private BukkitLTI plugin;
  
  public BukkitLTIListener(BukkitLTI plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    this.plugin = plugin;
  }
  
  @EventHandler
  public void handle(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {return;}
    user.setStartDate();
    user.save();
  }
    
  @EventHandler
  public void handle(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {return;}
    user.handleQuit();
  }  
  
  @EventHandler
  public void handle(PlayerEditBookEvent event) {
    Player player = event.getPlayer();
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {return;}
    String contents = Joiner.on("\n\n").join(event.getNewBookMeta().getPages());
    if (user.submitResult(contents)) {
      plugin.sendPlayerMessage(player, "Submitted book contents.");
    }
  }
}