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
<%!

%>

<%
		System.out.println("authen Remove");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";

		request.setCharacterEncoding("UTF-8");

		String PKey="";
		PKey = request.getParameter("aRemove");
		
		String sql = "DELETE FROM tblmt_authen WHERE PKey= ? ";
		
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, PKey);
		
// 	    System.out.println(preparedStatement);
	    try{
	    	if(preparedStatement.executeUpdate()>0){
	    		returnRs="rs";
	    	}
	    }catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	returnRs="re";
	    }
	    connect.close();
		System.out.println("remove Authen finish");
		
	    session.setAttribute("module", returnRs);
		response.sendRedirect("admin.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>

