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
		System.out.println("logic Validate Update");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";

		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String PKey,Operation,Value,Action,Active;
		PKey=Operation=Value=Action=Active=null;
		
		for(int i =1;i<=num;i++){
			PKey = request.getParameter("PKey"+i);
			Operation = request.getParameter("SelectOper"+i).trim();
			Value = request.getParameter("Value"+i).trim();
			Action = request.getParameter("SelectAction"+i);
			if(request.getParameter("CheckBoxActive"+i)==null){Active="N";}else{Active="Y";};			
			
			if(request.getParameter("Validate"+i).indexOf("Group")>-1){
				Operation = "=";
			}
// 			System.out.println("PKey:"+PKey+":"+Operation+":"+Value+":"+Action+":"+Active);
		
			// "select PKey,Validate,DayBranch,Operation,Value,Action,Active from tblmt_validation ";
			
			String sql = "UPDATE tblmt_validation "+
					"SET Operation=?,Value=?,Action=?,Active=? "+
					"WHERE PKey=? ";
			preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setString(1, Operation);
		    preparedStatement.setString(2, Value);
		    preparedStatement.setString(3, Action);
		    preparedStatement.setString(4, Active);
		    preparedStatement.setString(5, PKey);
	    	
		    System.out.println("sqlUpdateValidate:"+preparedStatement);
		    try{
		    	if(preparedStatement.executeUpdate()>0){
		    		returnRs="as";
		    	}else{
		    		returnRs="ae";
		    	}
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	returnRs="ae";
		    }
			
		}
		System.out.println("num="+num);
		
		System.out.println("update ValidateUpdate finish");
		
	    connect.close();
		
	    session.setAttribute("module", returnRs);
		response.sendRedirect("logicValidate.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>