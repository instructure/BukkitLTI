package com.instructure.bukkitlti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bukkit.Server;

public class TokenServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private final BukkitLTI plugin;
  
  public TokenServlet(BukkitLTI plugin) {
    this.plugin = plugin;
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object id = session.getAttribute("id");
    if (id == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    User user = plugin.getDatabase().find(User.class).where().eq("id", id).findUnique();
    Server server = plugin.getServer();
    Boolean isInstructor = user.getInstructor();
    Boolean isAssignment = user.getAssignmentId() != null;
    request.setAttribute("unregistered", user.getUuid() == null);
    request.setAttribute("studentAssignment", isAssignment && !isInstructor);
    request.setAttribute("teacherAssignment", isAssignment && isInstructor);
    request.setAttribute("token", user.getToken());
    request.setAttribute("address", request.getServerName()+":"+server.getPort());
    request.getRequestDispatcher("/token.jsp").forward(request, response);
  }
}