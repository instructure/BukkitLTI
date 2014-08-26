package com.instructure.bukkitlti;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.instructure.minecraftlti.MinecraftLTI;

public class BukkitLTI extends JavaPlugin {
	public MinecraftLTI lti = null;
	private BukkitLTIAdapter adapter = null;

  @Override
  public void onEnable() {
    adapter = new BukkitLTIAdapter(this);
    lti = new MinecraftLTI(adapter);
    getCommand("register").setExecutor(new RegisterCommand(this));
    getCommand("assignment").setExecutor(new AssignmentCommand(this));
    getCommand("grade").setExecutor(new GradeCommand(this));
    new BukkitLTIListener(this);
  }
 
  @Override
  public void onDisable() {
    lti.close();
  }
  
  public void sendPlayerMessage(CommandSender player, String msg) {
    player.sendMessage(ChatColor.GREEN + msg);
  }
  
  public void sendPlayerError(CommandSender player, String msg) {
    player.sendMessage(ChatColor.RED + msg);
  }
}
