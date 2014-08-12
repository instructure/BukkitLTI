package com.instructure.bukkitlti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConsumerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private final BukkitLTI plugin;

  public ConsumerServlet(BukkitLTI plugin) {
    this.plugin = plugin;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    LTIConsumer consumer = LTIConsumer.random();
    plugin.getDatabase().save(consumer);
    request.setAttribute("key", consumer.getKey());
    request.setAttribute("secret", consumer.getSecret());
    request.getRequestDispatcher("/consumer.jsp").forward(request, response);
  }
}
