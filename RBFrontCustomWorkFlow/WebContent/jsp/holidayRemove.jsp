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
		System.out.println("holiday Remove");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");
		
// 		System.out.println(request.getParameter("HRemove"));
		
		String temp = request.getParameter("PKey"+request.getParameter("HRemove"));
// 		System.out.println("temp"+temp);
		String branchday = temp.substring(0, temp.indexOf("$"));
		String holiday   = temp.substring(temp.indexOf("$")+1,temp.length());
		
		String sql = "DELETE FROM tblmt_holiday WHERE BranchDay= ? and Holiday= ? ";
		
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, branchday);
	    preparedStatement.setString(2, holiday);
		
//  	    System.out.println(preparedStatement);
	    try{
	    	if(preparedStatement.executeUpdate()>0){
	    		success="ลบรายการเรียบร้อย";
	    	}
	    }catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	success="ลบรายการมีปัญหา ลองทำรายการใหม่อีกครั้ง";
	    }
	    connect.close();
		System.out.println("remove holiday finish");
		
	    session.setAttribute("success", success);
		response.sendRedirect("holiday.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>

