<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
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
		request.setCharacterEncoding("UTF-8");

		String orgCode = request.getParameter("OrgCode");
		String EmpID = request.getParameter("EmpID");
		String Name = request.getParameter("Name");
		String TechnicalRole = request.getParameter("TechnicalRole");
		String BusinessRole = request.getParameter("BusinessRole");
		String TerminalID = request.getParameter("TerminalID");
		String Reserved = request.getParameter("SelectReserved");
		if(Reserved==null)
			Reserved="N";
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		
		String sql = "UPDATE tblmt_terminallist "+
				"SET EmpID=?,Name=?,TechnicalRole=?,BusinessRole=?,Reserved=? "+
				"WHERE OrgCode=? and TerminalID=? ";
		preparedStatement = connect.prepareStatement(sql);
			    
	    preparedStatement.setString(1, EmpID);
	    preparedStatement.setString(2, Name);
	    preparedStatement.setString(3, TechnicalRole);
	    preparedStatement.setString(4, BusinessRole);
	    preparedStatement.setString(5, Reserved);
	    preparedStatement.setString(6, orgCode);
	    preparedStatement.setString(7, TerminalID);
	    
	    String returnrs="a";
	    try{
			preparedStatement.executeUpdate();
			returnrs="Branch "+orgCode+ " Terminal "+TerminalID+" แก้ไขเรียบร้อย";    		
	    	
	    }catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	returnrs="ee";
	    }
	    
		System.out.println("edit termianl finish");
		
	    connect.close();
		
	    session.setAttribute("success2", returnrs);
	    response.sendRedirect("terminal.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("terminal.jsp?module="+returnrs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>