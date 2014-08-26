package com.instructure.bukkitlti;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.instructure.minecraftlti.Assignment;
import com.instructure.minecraftlti.MinecraftLTIAdapter;

public class BukkitLTIAdapter implements MinecraftLTIAdapter {
  private BukkitLTI plugin;
  
  public BukkitLTIAdapter(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public Path getStorageDirectory() {
    File privateDir = plugin.getDataFolder();
    privateDir.mkdir();
    return Paths.get(privateDir.toURI());
  }
  
  @Override
  public String getServerAddress() {
    Server server = plugin.getServer();
    String address = server.getIp();
    int port = server.getPort();
    if (port != 25565) {
      address += ":"+port;
    }
    return address;
  }
  
  private Player getPlayer(UUID uuid) {
    return Bukkit.getServer().getPlayer(uuid);
  }

  @Override
  public boolean isPlayerPresent(UUID uuid) {
    return getPlayer(uuid) != null;
  }

  @Override
  public String getPlayerTp(UUID uuid) {
    Location l = getPlayer(uuid).getLocation();
    return String.format("/tp %.0f %.0f %.0f", l.getX(), l.getY(), l.getZ());
  }

  @Override
  public void teleportPlayer(UUID uuid, Assignment a) {
    Player p = getPlayer(uuid);
    World world = Bukkit.getServer().getWorld(a.getWorldName());
    Location l = new Location(world, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch());
    p.teleport(l);
  }

  @Override
  public void setAssignmentLocation(UUID uuid, Assignment a) {
    Location l = getPlayer(uuid).getLocation();
    a.setLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
  }

  @Override
  public void sendPlayerMessage(UUID uuid, String msg) {
    plugin.sendPlayerMessage(getPlayer(uuid), msg);
  }

  @Override
  public void sendPlayerError(UUID uuid, String msg) {
    plugin.sendPlayerError(getPlayer(uuid), msg);
  }

  @Override
  public Logger getLogger() {
    return plugin.getLogger();
  }
}
