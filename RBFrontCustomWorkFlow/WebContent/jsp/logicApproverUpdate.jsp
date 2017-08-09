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
// public String converOpertion(String o){
// 	String cover = "";
// 	if("A")
// }
%>

<%
		System.out.println("logicApproverupdate");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";

		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String PKey,Job,SelectRequester,SelectApprover,DefaultApp,Active,SelectCorp;
		PKey=Job=SelectRequester=SelectApprover=DefaultApp=Active=SelectCorp=null;
		
		for(int i =1;i<=num;i++){
			Job = request.getParameter("Job"+i);
			DefaultApp = request.getParameter("DefaultApp"+i);
// 			System.out.println(Job+":"+DefaultApp);
			String sql1 = "UPDATE tblmt_approver "+
					"SET DefaultApp=? "+
					"WHERE Job=? ";			
			if(Job!=null){
				preparedStatement = connect.prepareStatement(sql1);
			    	preparedStatement.setString(1, DefaultApp);
			    	preparedStatement.setString(2, Job);
// 			    	System.out.println(preparedStatement);
			    preparedStatement.executeUpdate();
			}
			
			
			PKey = request.getParameter("PKey"+i);
			SelectRequester = request.getParameter("SelectRequester"+i).trim();
			SelectApprover = request.getParameter("SelectApprover"+i);
			SelectCorp	= request.getParameter("SelectCorp"+i);
// 			System.out.println("SelectCorp:"+SelectCorp);
			if(request.getParameter("CheckBoxActive"+i)==null){Active="N";}else{Active="Y";};						
			
			String sql2 = "UPDATE tblmt_approver "+
					"SET Requester=?,Approver=?,Active=?,CorpStaff=? "+
					"WHERE PKey=? ";
			preparedStatement = connect.prepareStatement(sql2);
		    preparedStatement.setString(1, SelectRequester);
		    preparedStatement.setString(2, SelectApprover);
		    preparedStatement.setString(3, Active);
		    preparedStatement.setString(4, SelectCorp);
		    preparedStatement.setString(5, PKey);
	    
		    try{
		    	if(preparedStatement.executeUpdate()>0){
		    		returnRs="as";
		    	}
		    }catch(Exception e){
		    	e.printStackTrace();
		    	returnRs="ae";
		    }
			
		}
		System.out.println("num="+num);
		
		System.out.println("update ApproverUpdate finish");

	    connect.close();
		
	    session.setAttribute("module", returnRs);
		response.sendRedirect("logicApprover.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>