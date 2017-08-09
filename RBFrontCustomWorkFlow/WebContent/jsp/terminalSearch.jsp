<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
		response.sendRedirect("main.jsp");
	}
%>
<%
System.out.println("TerminalSearch");
	  String returnrs=null;
	  
	  String orgCode = request.getParameter("OrgCode");
	  String EmpID = request.getParameter("EmpID");
	  String Name = request.getParameter("Name");
	  String TechnicalRole = request.getParameter("TechnicalRole");
	  String BusinessRole = request.getParameter("BusinessRole");
	  String TerminalID = request.getParameter("TerminalID");
	  String Reserved = request.getParameter("SelectReserved");
	  
	 if("".equals(Reserved))
		Reserved="";
		
	  System.out.println(orgCode+EmpID+Name+TechnicalRole+BusinessRole+TerminalID+Reserved);

	  System.out.println("orgCode:"+orgCode);
	  Connection connect = DatbaseConnection.getConnectionMySQL();
	  
	  String sql = "select * from tblmt_terminallist t where t.OrgCode like ? "+
	  			   "and EmpID like ? and Name like ? and TechnicalRole like ? "+
			       "and BusinessRole like ? and TerminalID like ? and Reserved like ?";
			  
	  PreparedStatement preparedStatement = connect.prepareStatement(sql);
      preparedStatement.setString(1, "%"+orgCode+"%");
      preparedStatement.setString(2, "%"+EmpID+"%");
      preparedStatement.setString(3, "%"+Name+"%");
      preparedStatement.setString(4, "%"+TechnicalRole+"%");
      preparedStatement.setString(5, "%"+BusinessRole+"%");
      preparedStatement.setString(6, "%"+TerminalID+"%");
      preparedStatement.setString(7, "%"+Reserved+"%");
      
      
      ResultSet resultSet = preparedStatement.executeQuery();

      if(resultSet.next())
      {			
// 		 request.setAttribute("OrgCode",resultSet.getString("OrgCode"));
// 		 request.setAttribute("EmpID",resultSet.getString("EmpID"));
// 		 request.setAttribute("Name",resultSet.getString("Name"));
// 		 request.setAttribute("TechnicalRole",resultSet.getString("TechnicalRole"));
// 		 request.setAttribute("BusinessRole",resultSet.getString("BusinessRole"));
// 		 request.setAttribute("TerminalID",resultSet.getString("TerminalID"));
// 		 request.setAttribute("Reserved",resultSet.getString("Reserved").substring(0, 1));
		 returnrs = "ss";
      }else{
    	 returnrs = "se"; 
      }
	
	System.out.println("selcet terminal finish");
	
    connect.close();
	
    session.setAttribute("success2", returnrs);
    
	RequestDispatcher requestDispatcher; 
	requestDispatcher = request.getRequestDispatcher("terminal.jsp");
	requestDispatcher.forward(request, response);

%>


</body>
</html>