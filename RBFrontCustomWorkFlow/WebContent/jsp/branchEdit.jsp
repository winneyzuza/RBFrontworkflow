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
		String ocCode = request.getParameter("OcCode");
		String bNameTH = request.getParameter("BNameTH");
		String bNameEN = request.getParameter("BNameEN");
		String operationDay = request.getParameter("OperationDay");
		String counterLimit1 = request.getParameter("CounterLimit1");
		String counterLimit2 = request.getParameter("CounterLimit2");
		String metropolitan = request.getParameter("SelectMetropolitan");
		String ABM = request.getParameter("SelectABM");
		String group = request.getParameter("SelectGroup");
		String area = request.getParameter("Area");
		String network = request.getParameter("Network");
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		
		String sql = "UPDATE tblmt_branchinfo "+
				"SET OcCode=?,BNameTH=?,BNameEN=?,OperationDay=?,CounterLimit1=?,CounterLimit2=?,Metropolitan=?,ABM=?,AreaNo=?,Network=?,GroupB=? "+
				"WHERE OrgCode=? ";
		preparedStatement = connect.prepareStatement(sql);
	    preparedStatement.setString(1, ocCode);
	    preparedStatement.setString(2, bNameTH);
	    preparedStatement.setString(3, bNameEN);
	    preparedStatement.setString(4, operationDay);
	    preparedStatement.setString(5, counterLimit1);
	    preparedStatement.setString(6, counterLimit2);
	    preparedStatement.setString(7, metropolitan);
	    preparedStatement.setString(8, ABM);
	    preparedStatement.setString(9, area);
	    preparedStatement.setString(10, network);
	    preparedStatement.setString(11, group);
	    preparedStatement.setString(12, orgCode);
	    
	    String returnrs="a";
	    try{
	    	preparedStatement.executeUpdate();
	    	returnrs="branch "+orgCode+" แก้ไขเรียบร้อย";
	    }catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	returnrs="ee";
	    }
	    
		System.out.println("edit branch finish");
	    connect.close();
		
	    session.setAttribute("success2", returnrs);
		response.sendRedirect("branch.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("branch.jsp?module="+returnrs);
// 		requestDispatcher.forward(request, response);
%>


</body>
</html>