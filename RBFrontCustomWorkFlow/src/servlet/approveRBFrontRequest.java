package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class approveRBFrontRequest extends HttpServlet { 
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected void doPost(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException 
  { System.out.println("BBBB approveRBFrontRequest");

  /*response.setContentType("text/xml;charset=UTF-8");
  
  String resultXml = "<test>Hello World</test>";
  PrintWriter out = response.getWriter();
  out.append(resultXml);*/
  
  
  
  }  
}