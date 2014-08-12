package com.instructure.bukkitlti;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AssignmentCommand implements CommandExecutor {
  public AssignmentCommand(BukkitLTI plugin) {}
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    if (args.length < 1) {
      sender.sendMessage(ChatColor.RED + "Format: /assignment [begin|submit|set]");
      return true;
    }
    String effect = args[0];
    
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "Only players may use this command.");
      return true;
    }
    Player player = (Player)sender;
    User user = User.byPlayer(player);
    if (user == null) {
      sender.sendMessage(ChatColor.RED + "You are not associated with an LTI user.");
      return true;
    }
    
    try {
      user.assignmentAction(effect);
    } catch (CommandException e) {
      sender.sendMessage(ChatColor.RED + e.getMessage());
    }
    return true;
  }
}
