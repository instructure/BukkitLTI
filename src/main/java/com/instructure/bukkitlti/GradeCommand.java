package com.instructure.bukkitlti;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GradeCommand implements CommandExecutor {
  public GradeCommand(BukkitLTI plugin) {}
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    
    if (args.length < 2) {
      Bukkit.getLogger().warning("Format: /grade @p 0.9");
      return true;
    }
    if (!(sender instanceof BlockCommandSender)) {
      Bukkit.getLogger().warning("This command may only be sent by command blocks.");
      return true;
    }
    
    Player player = getPlayer(args[0]);
    if (player == null) {
      Bukkit.getLogger().warning("Unrecognized player.");
      return true;
    }
    
    double grade = Double.parseDouble(args[1]);
    if (grade < 0 || grade > 1) {
      Bukkit.getLogger().warning("Grade must be between 0 and 1.");
      return true;
    }

    User user = User.byPlayer(player);
    if (user == null) {
      return true;
    }
    Assignment assignment = user.getAssignment();
    if (assignment == null) {
      return true;      
    }
    
    if (user.submitResult(grade)) {
      player.sendMessage(ChatColor.GREEN+String.format("Submitted grade: %.0f%%", grade*100));
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  private Player getPlayer(String name) {
    return Bukkit.getPlayer(name);
  }
}
