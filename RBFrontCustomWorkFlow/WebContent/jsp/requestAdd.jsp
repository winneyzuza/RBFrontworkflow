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
<% 	    // not complete business change
		request.setCharacterEncoding("UTF-8");

	  	String requsetID = request.getParameter("RequestID");
		System.out.println("requsetID:"+request.getParameter("RequestID"));
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		
		String sql = "UPDATE tbldt_reqrepository SET Status = 'C2' "+
				"WHERE RequestID = ? ";
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, requsetID);
// 	    //System.out.println("preparedStatement:"+preparedStatement);
// 	    String returnrs="a";
// 	    try{
// 	    	preparedStatement.executeUpdate();
// 	    	returnrs="es";
// 	    }catch(Exception e){
// 	    	e.getStackTrace();
// 	    	System.out.println("ex:"+e.getMessage());
// 	    	returnrs="ee";
// 	    }
	    
// 		System.out.println("remove branch finish");
		
// 	    preparedStatement.close();
// 	    connect.close();
		
// 	    session.setAttribute("module", returnrs);
// 		response.sendRedirect("request.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("branch.jsp?module="+returnrs);
// 		requestDispatcher.forward(request, response);
%>


</body>
</html>