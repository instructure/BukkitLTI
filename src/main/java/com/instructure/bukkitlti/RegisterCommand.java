package com.instructure.bukkitlti;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.instructure.minecraftlti.User;

public class RegisterCommand implements CommandExecutor {
  private final BukkitLTI plugin;
  
  public RegisterCommand(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    if (!(sender instanceof Player)) {
      plugin.sendPlayerError(sender, "Only players may register.");
      return true;
    }
    if (args.length < 1) {
      plugin.sendPlayerError(sender, "Format: /register token");
      return true;
    }
    
    User user = User.byToken(args[0]);
    if (user == null) {
      plugin.sendPlayerError(sender, "Invalid token.");
      return true;
    }
    
    Player player = (Player)sender;
    user.register(player.getUniqueId());
    plugin.sendPlayerMessage(sender, "Registered.");
    return true; 
  }

}
