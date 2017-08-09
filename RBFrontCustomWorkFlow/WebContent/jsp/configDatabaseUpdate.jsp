<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.SEADatabase" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>


<body>

<%
		System.out.println("update user pass DB");
		String success = "Update ";
		SEADatabase cf = new SEADatabase();
		request.setCharacterEncoding("UTF-8");
		
		String userRB = request.getParameter("userRB");
		String passRB = request.getParameter("passRB");
		String userHR = request.getParameter("userHR");
		String passHR = request.getParameter("passHR");
		
// 		System.out.println("UUU:"+userRB+passRB+userHR+passHR);
		
		if("".equals(passRB.trim())){
			passRB = SEADatabase.getPassRB();
			success = success + "username password databaseHRMS ";
		}
		if("".equals(passHR.trim())){
			passRB = SEADatabase.getPassHR();
			success = success + "username password databaseRBFront Custom ";
		}
		
		if( "".equals(passRB.trim()) && "".equals(passHR.trim()) ){
			success = "please input password ";
		}else{
			cf.writeFileDB(userRB.trim(),passRB.trim(),userHR.trim(),passHR.trim());
			success = success + " finish ";			
		}
			
	    session.setAttribute("success", success);
		response.sendRedirect("configDatabase.jsp");
		
%>
</body>
</html>

