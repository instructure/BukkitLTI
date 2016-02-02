package com.instructure.bukkitlti;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.instructure.minecraftlti.User;

public class InstructorCommand implements CommandExecutor {
  private final BukkitLTI plugin;
  
  public InstructorCommand(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label,
      String[] args) {
	if (!sender.isOp()) {
      plugin.sendPlayerError(sender, "This command may only used by ops.");
      return true;
    }
    
    Player player;
    if (args.length > 0) {
      player = getPlayer(args[0]);
    } else if (sender instanceof Player) {
      player = (Player)sender;
    } else {
      plugin.getLogger().warning("Format: /instructor [username]");
      return true;
    }

    if (player == null) {
      plugin.getLogger().warning("Unrecognized player.");
      return true;
    }
    
    User user = User.byUuid(player.getUniqueId());
    if (user == null) {
      plugin.getLogger().warning("Player not associated with an LTI user.");
      return true;
    }
    
    boolean instructor = user.getInstructor();
    user.setInstructor(!instructor);
    user.save();
    if (instructor) {
      plugin.sendPlayerMessage(player, "You are no longer an instructor.");
    } else {
      plugin.sendPlayerMessage(player, "You are now an instructor.");
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  private Player getPlayer(String name) {
    return Bukkit.getPlayer(name);
  }
}
