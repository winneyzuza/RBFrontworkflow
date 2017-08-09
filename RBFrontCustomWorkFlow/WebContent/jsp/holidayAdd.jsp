<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="db.ControlSequenceTable" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");
		
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start authen add ");

		request.setCharacterEncoding("UTF-8");
		
		String branch_day = request.getParameter("branch_day");
		String holiday    = request.getParameter("stop_d");		

// 		System.out.println("data add validate:"+topic+":"+statement+":"+approver+":"+value+":"+action+":"+active);
		
			connect = DatbaseConnection.getConnectionMySQL();
			String seq = ControlSequenceTable.getSeqValidate();			
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_holiday VALUES (?,?) ");
		    preparedStatement.setString(1, branch_day);
		    preparedStatement.setString(2, holiday);
		    
		    try{
		    	System.out.println(preparedStatement);
		    	if(preparedStatement.executeUpdate()>0){
		    		success = success + "เพิมวันหยุดพิเศษ เรียบร้อย";
		    	}
		    	
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success = success + "เพิมวันหยุด  ล้มเหลว";
		    }			
		    connect.close();

	    System.out.println("Add holiday finish");
	    
 	    session.setAttribute("success", success);
 		response.sendRedirect("holiday.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("logic.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
	
%>
</body>
</html>
  <%  connect.close(); %>