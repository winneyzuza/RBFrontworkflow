package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Configuration;
import db.DatbaseConnection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetConfiguration extends HttpServlet { 

	private static final long serialVersionUID = 1L;

	
	 public void init() throws ServletException
	    {
			System.out.println("set configuration");
			Configuration.TYPE = Configuration.getTYPE();
			Configuration.setConfigFromFile();
			Configuration.getSuperUser();
			Configuration.setConfig();
	    }
	 
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	  { 
		System.out.println("set configuration");
		Configuration.setConfig();
		Configuration.setConfigFromFile();
	  
	  }  
}