package com.instructure.bukkitlti;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.imsglobal.pox.IMSPOXRequest;

import com.google.common.base.Throwables;

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
      String url = user.getServiceUrl();
      String key = consumer.getKey().toString();
      String secret = consumer.getSecret().toString();
      String sourcedid = user.getSourcedid();
      String gradeStr = grade == null ? "" : grade.toString();
      IMSPOXRequest.sendReplaceResult(url, key, secret, sourcedid, gradeStr, text);
    } catch (Exception e) {
      Bukkit.getLogger().warning("Failed to send submission ("+e.getClass().getSimpleName()+"): "
        +Throwables.getStackTraceAsString(e));
    }
  }
}
