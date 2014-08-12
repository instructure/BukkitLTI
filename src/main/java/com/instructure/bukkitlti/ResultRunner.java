package com.instructure.bukkitlti;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.imsglobal.pox.IMSPOXRequest;

public class ResultRunner extends BukkitRunnable {
  private User user;
  private Double grade;
  private String text;
  
  public ResultRunner(User user, Double grade, String text) {
    this.user = user;
    this.grade = grade;
    this.text = text;
  }
  
  @Override
  public void run() {
    LTIConsumer consumer = user.getConsumer();
    
    try {
      IMSPOXRequest.sendReplaceResult(
          user.getServiceUrl(),
          consumer.getKey().toString(),
          consumer.getSecret().toString(),
          user.getSourcedid(),
          grade.toString(),
          text
      );
    } catch (Exception e) {
      Bukkit.getLogger().warning("Failed to send submission: "+e.getMessage());
    }
  }
}
