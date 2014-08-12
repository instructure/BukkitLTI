package com.instructure.bukkitlti;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
  private final BukkitLTI plugin;
  
  public RegisterCommand(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "Only players may register.");
      return true;
    }
    if (args.length < 1) {
      sender.sendMessage(ChatColor.RED + "Format: /register token");
      return true;
    }
    User user = User.byToken(args[0]);
    if (user == null) {
      sender.sendMessage(ChatColor.RED + "Invalid token.");
      return true;
    }
    
    Player player = (Player)sender;
    User existing = User.byPlayer(player);
    if (existing != null) {
      existing.setPlayer(null);
      plugin.getDatabase().save(existing);
    }
    user.setPlayer(player);
    user.setStartDate();
    plugin.getDatabase().save(user);
    sender.sendMessage(ChatColor.GREEN + "Registered.");
    return true; 
  }

}
