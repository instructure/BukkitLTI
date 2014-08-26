package com.instructure.bukkitlti;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.instructure.minecraftlti.User;

public class GradeCommand implements CommandExecutor {
  private final BukkitLTI plugin;
  
  public GradeCommand(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
    if (!(sender instanceof BlockCommandSender)) {
      plugin.sendPlayerError(sender, "This command may only be sent by command blocks.");
      return true;
    }
    
    if (args.length < 2) {
      plugin.getLogger().warning("Format: /grade @p 0.9");
      return true;
    }
    
    Player player = getPlayer(args[0]);
    if (player == null) {
      plugin.getLogger().warning("Unrecognized player.");
      return true;
    }
    
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {
      plugin.getLogger().warning("Graded player not associated with an LTI user.");
      return true;
    }
    
    user.grade(args[1]);
    return true;
  }

  @SuppressWarnings("deprecation")
  private Player getPlayer(String name) {
    return Bukkit.getPlayer(name);
  }
}
