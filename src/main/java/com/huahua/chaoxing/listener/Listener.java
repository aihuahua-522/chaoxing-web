package com.huahua.chaoxing.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.*;
import java.util.HashMap;

@WebListener()
public class Listener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {
    public static HashMap<String, HashMap<String, String>> USER_MAP = new HashMap<>();


    // Public constructor is required by servlet spec
    public Listener() {

    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            USER_MAP = (HashMap<String, HashMap<String, String>>) new ObjectInputStream(new FileInputStream("cookies" + File.pathSeparator + "USER_MAP")).readObject();
            System.out.println("初始化数据中");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("初始化失败");
            e.printStackTrace();
        }
        /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            new ObjectOutputStream(new FileOutputStream("cookies" + File.pathSeparator + "USER_MAP")).writeObject(USER_MAP);
            System.out.println("保存数据中");
        } catch (IOException e) {
            System.out.println("保存失败");
            e.printStackTrace();
        }
      /* This method is invoked when the Servlet Context
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }
}
