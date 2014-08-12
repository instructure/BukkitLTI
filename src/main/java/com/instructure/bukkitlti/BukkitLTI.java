package com.instructure.bukkitlti;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.jasper.servlet.JspServlet;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.avaje.ebean.EbeanServer;

public class BukkitLTI extends JavaPlugin {
	private Server webserver = null;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    setupDatabase();
    getCommand("register").setExecutor(new RegisterCommand(this));
    getCommand("assignment").setExecutor(new AssignmentCommand(this));
    getCommand("grade").setExecutor(new GradeCommand(this));
    new BookListener(this);
    new JoinListener(this);
    new QuitListener(this);
    startWebserver();
  }
 
  @Override
  public void onDisable() {
    stopWebserver();
  }
  
  @Override
  public List<Class<?>> getDatabaseClasses() {
      List<Class<?>> list = new ArrayList<Class<?>>();
      list.add(User.class);
      list.add(Assignment.class);
      list.add(LTIConsumer.class);
      return list;
  }
  
  private void setupDatabase() {
    try {
        getDatabase().find(User.class).findRowCount();
    } catch (PersistenceException ex) {
        System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
      installDDL();
    }
  }
  
  private void startWebserver() {
    FileConfiguration config = getConfig();

    webserver = new Server(config.getInt("port"));
    webserver.setSessionIdManager(new HashSessionIdManager());
    
    WebAppContext dynamicHandler = new WebAppContext();
    String webDir = this.getClass().getClassLoader().getResource("web").toExternalForm();
    dynamicHandler.setResourceBase(webDir);
    dynamicHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
    ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
    dynamicHandler.setClassLoader(jspClassLoader);
    
    dynamicHandler.addServlet(new ServletHolder(new LTIServlet(this)),"/lti");
    dynamicHandler.addServlet(new ServletHolder(new TokenServlet(this)),"/token");
    dynamicHandler.addServlet(new ServletHolder(new AssignmentServlet(this)),"/assignment");
    dynamicHandler.addServlet(new ServletHolder(new ConsumerServlet(this)),"/consumer");
    dynamicHandler.addServlet(new ServletHolder(new LTIConfigServlet()),"/config.xml");
    
    //Ensure the jsp engine is initialized correctly
    JettyJasperInitializer sci = new JettyJasperInitializer();
    ServletContainerInitializersStarter sciStarter = new ServletContainerInitializersStarter(dynamicHandler);
    List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
    initializers.add(new ContainerInitializer(sci, null));
    dynamicHandler.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
    dynamicHandler.addBean(sciStarter, true);

    ServletHolder holderJsp = new ServletHolder("jsp",JspServlet.class);
    holderJsp.setInitOrder(0);
    holderJsp.setInitParameter("fork","false");
    holderJsp.setInitParameter("keepgenerated","true");
    dynamicHandler.addServlet(holderJsp,"*.jsp");
    
    ResourceHandler staticHandler = new ResourceHandler();
    String staticDir = this.getClass().getClassLoader().getResource("static").toExternalForm();
    staticHandler.setResourceBase(staticDir);
    
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { staticHandler, dynamicHandler, new DefaultHandler() });
    webserver.setHandler(handlers);
    try {
      webserver.start();
    } catch (Exception e) {
      getLogger().severe("Failed to start server.");
    }
  }
 
   private void stopWebserver() {
     try {
       webserver.stop();
       for(int i = 0; i < 100; i++) {  /* Limit wait to 10 seconds */
         if(webserver.isStopping())
           Thread.sleep(100);
       }
       if(webserver.isStopping()) {
         getLogger().warning("Graceful shutdown timed out - continuing to terminate");
       }
     } catch (Exception e) {
       getLogger().severe("Failed to stop server.");
     }
     webserver = null;
   }
   
   public static EbeanServer getDb() {
     return BukkitLTI.getPlugin(BukkitLTI.class).getDatabase();
   }
}
