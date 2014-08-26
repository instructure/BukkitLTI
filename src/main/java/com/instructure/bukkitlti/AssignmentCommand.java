package com.instructure.bukkitlti;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.instructure.minecraftlti.User;

public class AssignmentCommand implements CommandExecutor {
  private final BukkitLTI plugin;
  
  public AssignmentCommand(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    if (args.length < 1) {
      plugin.sendPlayerError(sender, "Format: /assignment [begin|submit|set]");
      return true;
    }
    
    if (!(sender instanceof Player)) {
      plugin.sendPlayerError(sender, "Only players may use this command.");
      return true;
    }
    
    Player player = (Player)sender;
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {
      plugin.sendPlayerError(sender, "You are not associated with an LTI user.");
      return true;
    }
    
    try {
      user.assignmentAction(args[0]);
    } catch (IllegalStateException e) {
      plugin.sendPlayerError(sender, e.getMessage());
    }
    return true;
  }
}
