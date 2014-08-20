package com.instructure.bukkitlti;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "tool_id", "consumer_id"})})
public class User {
  @Id
  private int id;
  
  @Length(max=100)
  @NotEmpty
  private String userId;
  
  @Length(max=100)
  @NotEmpty
  private String toolId;
  
  @NotNull
  private int consumerId;
  
  @Nullable
  @Column(unique=true)
  private UUID uuid;
  
  @Length(max=32)
  @NotEmpty
  @Column(unique=true)
  private String token;
  
  @Length(max=200)
  @Nullable
  private String sourcedid;
  
  @Length(max=1023)
  @Nullable
  private String serviceUrl;
  
  @Length(max=1023)
  @Nullable
  private String xapiUrl;
  
  @Nullable
  private Date startDate;
  
  @Nullable
  private Boolean instructor;
  
  @Nullable
  private Integer assignmentId;
  
  public User() {}
  
  public User(String userId, String toolId, LTIConsumer consumer, String token) {
    this.userId = userId;
    this.toolId = toolId;
    setConsumer(consumer);
    this.token = token;
  }
  
  private static ExpressionList<User> where() {
    return BukkitLTI.getDb().find(User.class).where();
  }
  
  public static User byPlayer(Player player) {
    return User.where().eq("uuid", player.getUniqueId()).findUnique();
  }
  
  public static User byToken(String token) {
    return User.where().eq("token", token).findUnique();
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getToolId() {
    return toolId;
  }
  
  public void setToolId(String toolId) {
    this.toolId = toolId;
  }
  
  public int getConsumerId() {
    return consumerId;
  }
  
  public void setConsumerId(int consumerId) {
    this.consumerId = consumerId;
  }
  
  public LTIConsumer getConsumer() {
    return BukkitLTI.getDb().find(LTIConsumer.class).where().eq("id", getConsumerId()).findUnique();
  }
  
  public void setConsumer(LTIConsumer consumer) {
    setConsumerId(consumer == null ? null : consumer.getId());
  }
  
  public UUID getUuid() {
    return uuid;
  }
  
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }
  
  public Player getPlayer() {
    return Bukkit.getServer().getPlayer(getUuid());
  }
  
  public void setPlayer(Player player) {
    setUuid(player == null ? null : player.getUniqueId());
  }
  
  public String getToken() {
    return token;
  }
  
  public void setToken(String token) {
    this.token = token;
  }
  
  public String getSourcedid() {
    return sourcedid;
  }
  
  public void setSourcedid(String sourcedid) {
    this.sourcedid = sourcedid;
  }
  
  public Boolean getInstructor() {
    return instructor;
  }
  
  public void setInstructor(Boolean instructor) {
    this.instructor = instructor;
  }
  
  public String getServiceUrl() {
    return serviceUrl;
  }
  
  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }
  
  public String getXapiUrl() {
    return xapiUrl;
  }
  
  public void setXapiUrl(String xapiUrl) {
    this.xapiUrl = xapiUrl;
  }
  
  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public void setStartDate() {
    setStartDate(new Date());
  }
  
  public Integer getAssignmentId() {
    return assignmentId;
  }
  
  public Assignment getAssignment() {
    if (getAssignmentId() == null) {return null;}
    return BukkitLTI.getDb().find(Assignment.class).where().eq("id", getAssignmentId()).findUnique();
  }
  
  public void setAssignmentId(Integer assignmentId) {
    this.assignmentId = assignmentId;
  }
  
  public void setAssignment(Assignment assignment) {
    setAssignmentId(assignment == null ? null : assignment.getId());
  }
  
  public void assignmentAction(String effect) {
    Player player = getPlayer();
    if (player == null) {throw new CommandException("Cannot find player.");}
    Assignment assignment = getAssignment();
    if (assignment == null) {throw new CommandException("No current assignment.");}
    
    if (effect.equals("begin")) {
      if (assignment.getWorldName() == null) {throw new CommandException("This assignment has no location.");}
      player.teleport(assignment.getLocation());
      player.sendMessage(ChatColor.GREEN + "Began assignment.");
      return;
    }
    if (effect.equals("submit")) {
      Location l = player.getLocation();
      String warp = String.format("/tp %.0f %.0f %.0f", l.getX(), l.getY(), l.getZ());
      submitResult(warp);
      player.sendMessage(ChatColor.GREEN + "Submitted location.");
      return;
    }
    if (effect.equals("set")) {
      if (!getInstructor()) {throw new CommandException("Only instructors may use this command.");}
      assignment.setLocation(player.getLocation());
      BukkitLTI.getDb().save(assignment);
      player.sendMessage(ChatColor.GREEN + "Set assignment location.");
      return;
    }
    throw new CommandException("Accepted actions: begin, submit, set");
  }
  
  public Boolean submitResult(String text) {
    return submitResult(null, text);    
  }
  
  public Boolean submitResult(double grade) {
    return submitResult(grade, null);
  }
  
  public Boolean submitResult(Double grade, String text) {
    if (getSourcedid() == null) {return false;}
    ResultRunner runner = new ResultRunner(this, grade, text);
    runner.runTaskLater(BukkitLTI.getPlugin(BukkitLTI.class), 0);
    return true;
  }
  
  public Boolean handleQuit() {
    if (getSourcedid() == null || getXapiUrl() == null) {return false;}
    long duration = ((new Date()).getTime() - getStartDate().getTime())/1000;
    DurationRunner runner = new DurationRunner(this, duration);
    runner.runTaskLater(BukkitLTI.getPlugin(BukkitLTI.class), 0);
    return true;
  }
}
