package com.instructure.bukkitlti;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import com.google.common.base.Joiner;

public class BookListener implements Listener {
  public BookListener(BukkitLTI plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void handle(PlayerEditBookEvent event) {
    Player player = event.getPlayer();
    User user = User.byPlayer(player);
    if (user == null) {return;}
    String contents = Joiner.on("\n\n").join(event.getNewBookMeta().getPages());
    if (user.submitResult(contents)) {
      player.sendMessage(ChatColor.GREEN + "Submitted book contents.");
    }
  }
}
