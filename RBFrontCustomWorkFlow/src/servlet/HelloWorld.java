package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DatbaseConnection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Properties; // ใช้สำหรับอ่าน properties file ที่เขียนไว้แล้ว
import org.apache.log4j.Logger; // ใช้เขียน log
import org.apache.log4j.PropertyConfigurator; // ใช้สำหรับตั้งค่าจาก properties file ให้ log

public class HelloWorld extends HttpServlet { 
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException 
  { System.out.println("AAAA");

//  Properties log4jProperties = new Properties();
//  Logger logger;
// 
//      try {
//          log4jProperties = new Properties();
//
//          
//          PropertyConfigurator.configure(log4jProperties);
//          logger = Logger.getLogger(HelloWorld.class);
//          logger.debug(">>>>>>>>>>>> LOAD LOG4J SUCCESS <<<<<<<<<<<<<<< ");   
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
  
  }

  public void test1(){
	    // reading the user input
//    String color= request.getParameter("color");    
//    PrintWriter out = response.getWriter();
//    out.println (
//      "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
//      "<html> \n" +
//        "<head> \n" +
//          "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" +
//          "<title> My first jsp  </title> \n" +
//        "</head> \n" +
//        "<body> \n" +
//          "<font size=\"12px\" color=\"" + color + "\">" +
//            "Hello World" +
//          "</font> \n" +
//        "</body> \n" +
//      "</html>" 
//    ); 
//    
//	RequestDispatcher requestDispatcher; 
//	requestDispatcher = request.getRequestDispatcher("jsp/terminal.jsp");
//	requestDispatcher.forward(request, response);
  }

  public void test2(){
	//  Connection connectMy = DatbaseConnection.getConnectionMySQL();
//	  try{					
//			PreparedStatement preparedStatementMy = connectMy
//					.prepareStatement("delete from tblmt_employeeinfo ");
//
//			int rs = preparedStatementMy.executeUpdate();
//			System.out.print("delete finish:"+rs);
//			
//			String sqlD = "DROP TABLE IF EXISTS `tblmt_employeeinfo`";
//			preparedStatementMy = connectMy.prepareStatement(sqlD);
//			rs = preparedStatementMy.executeUpdate();
//			
//			System.out.print("drop finish:"+rs);
//			
//			String sqlC = "CREATE TABLE `tblmt_employeeinfo` ( "+
//			  "`EmpID` varchar(10) NOT NULL, "+
//			  "`BranchID` varchar(10) DEFAULT NULL, "+
//			  "`Name` varchar(50) DEFAULT NULL, "+
//			  "`TechnicalRole` varchar(50) DEFAULT NULL, "+
//			  "`CurLimit` varchar(50) DEFAULT NULL, "+
//			  "`TerminalID` varchar(10) DEFAULT NULL, "+
//			  "`LastUpdate` datetime DEFAULT NULL, "+
//			  "`LimitStatus` varchar(5) DEFAULT NULL, "+
//			  "`BusinessRole` varchar(50) DEFAULT NULL, "+
//			  "`Exam` varchar(2) DEFAULT NULL, "+
//			  "PRIMARY KEY (`EmpID`), "+
//			  "KEY `EmployeeID_Index` (`EmpID`) USING BTREE "+
//			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
//			
//			preparedStatementMy = connectMy.prepareStatement(sqlC);
//			rs = preparedStatementMy.executeUpdate();
//			
//			System.out.print("create finish:"+rs);
//			
//		} catch (SQLException e) {
//			System.out.print("employee dup skip--->importHR");
//		
//		}finally{
//			
//		}
  }
  
	public static void main(String[] args){
		  Properties log4jProperties = new Properties();
		  Logger logger;
		 
		      try {
		          log4jProperties = new Properties();
		          System.out.println("AA:"+log4jProperties.getProperty("log4j.appender.connectorLog.File"));
		          PropertyConfigurator.configure(log4jProperties);
		          
		          logger = Logger.getLogger(HelloWorld.class);
		          logger.debug(">>>>>>>>>>>> LOAD LOG4J SUCCESS <<<<<<<<<<<<<<< ");   
		      } catch (Exception e) {
		          e.printStackTrace();
		      }

	}
  
}