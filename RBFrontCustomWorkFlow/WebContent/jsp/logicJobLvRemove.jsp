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
		System.out.println("logicJobTitleRemove");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");

		String PKey="";
		PKey = request.getParameter("vRemove");
		
		String sql = "DELETE FROM tblmt_lvjob WHERE jobName= ? ";
		
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, PKey);
		
// 	    System.out.println(preparedStatement);
	    try{
	    	if(preparedStatement.executeUpdate()>0){
	    		success="ลบ  job title  สำเร็จ ";
	    	}else{
	    		success="ลบ  job title ล้มเหลว ";
	    	}
	    }catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	success="ลบ  job title ล้มเหลว ";
	    }
		
		System.out.println("remove jobLv finish");
		
	    connect.close();
		
	    session.setAttribute("success", success);
		response.sendRedirect("logicJobLv.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>