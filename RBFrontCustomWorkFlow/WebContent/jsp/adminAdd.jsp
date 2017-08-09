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
		String empID,type,pkey;
		empID=type=pkey=null;
		
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start authen add ");

		request.setCharacterEncoding("UTF-8");
		
		
		String isEmpID = request.getParameter("isEmpID");
// 		System.out.println("isEmpID");
 		String temp = "";
// 		if("Y".equals(isEmpID))
// 			temp = "s";
			
		empID = temp+request.getParameter("empID");
		type = request.getParameter("RadioRole"); //request.getParameter("SelectType");		
		pkey = ControlSequenceTable.getSeqAuthen();
 		System.out.println("admin add:"+empID+":"+type+":"+pkey);
		
			connect = DatbaseConnection.getConnectionMySQL();
			String seq = ControlSequenceTable.getSeqValidate();			
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_authen VALUES (?,?,?,?) ");
		    preparedStatement.setString(1, pkey);
		    preparedStatement.setString(2, type);
		    preparedStatement.setString(3, empID);
		    preparedStatement.setString(4, "");
		    
		    //String returnAdd="a";
		    try{
//		    	System.out.println(preparedStatement);
		    	if(preparedStatement.executeUpdate()>0){
		    		success = success + "เพิม user เรียบร้อย";
		    	}
		    	
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success = success + "เพิม user ล้มเหลว";
		    }			
		    connect.close();

	    System.out.println("Add new user finish");
	    
 	    session.setAttribute("success", success);
 		response.sendRedirect("admin.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("logic.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
	
%>
</body>
</html>
  <%  connect.close(); %>