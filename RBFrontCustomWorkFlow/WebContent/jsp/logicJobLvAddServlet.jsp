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
		String lvJob,jobTitle;
		lvJob=jobTitle=null;
		
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start add jobLv ");

		request.setCharacterEncoding("UTF-8");
		
		lvJob = request.getParameter("lvJob");
		jobTitle = request.getParameter("jobTitle");		
		
			String seq = ControlSequenceTable.getSeqValidate();			
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_lvjob VALUES (?,?) ");
		    preparedStatement.setString(1, lvJob);
		    preparedStatement.setString(2, jobTitle);
		    
		    //String returnAdd="a";
		    try{
//		    	System.out.println(preparedStatement);
		    	if(preparedStatement.executeUpdate()>0){
		    		success = success + "เพิมเ  jobTitle เรียบร้อย";
		    	}else{
		    		success = success + "เพิม jobTitle ล้มเหลว";
		    	}
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success = success + "เพิม jobTitle ล้มเหลว";
		    }			
		

	    System.out.println("Add job title finish");
		
	    connect.close();
		%>
		<script type="text/javascript">
			alert("<%=success%>");
			window.close();
		</script> 
		<%
	    
// 	    session.setAttribute("success", success);
// 		response.sendRedirect("logicApprover.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("logic.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
	
%>
</body>
</html>