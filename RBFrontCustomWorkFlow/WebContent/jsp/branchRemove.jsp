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

	  	String orgCode = request.getParameter("BRemove");
		//System.out.println("BRemove:"+request.getParameter("BRemove"));
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		
		String sql = "DELETE FROM tblmt_branchinfo "+
				"WHERE OrgCode = ? ";
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, orgCode);
	    //System.out.println("preparedStatement:"+preparedStatement);
	    String returnrs="a";
	    try{
	    	preparedStatement.executeUpdate();
	    	returnrs="es";
	    }catch(Exception e){
	    	e.getStackTrace();
	    	System.out.println("ex:"+e.getMessage());
	    	returnrs="ee";
	    }
	    
		System.out.println("remove branch finish");
		
	    preparedStatement.close();
	    connect.close();
		
	    session.setAttribute("success2", returnrs);
		response.sendRedirect("branch.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("branch.jsp?module="+returnrs);
// 		requestDispatcher.forward(request, response);
%>


</body>
</html>