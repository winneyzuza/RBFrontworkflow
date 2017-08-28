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
<title>Validation Logic Add</title>
</head>
<body>
<%
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");
		String topic,statement,operation,value,action,active;
		//topic=statement=operation=value=action=active=null;
		topic=statement=operation=value=action=active="";
		
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start add validate ");
		
		request.setCharacterEncoding("UTF-8");
		
		topic = request.getParameter("topic");
		statement = request.getParameter("statement");
		operation = request.getParameter("SelectOperation");
		value = request.getParameter("Value").trim();
		action = request.getParameter("SelectAction");
		active = request.getParameter("CheckBoxActive");
		if(null == active ||active.isEmpty() || active.trim()==null) { 
			active="N";
		}
		
		System.out.println("data add validate:"+topic+":"+statement+":"+operation+":"+value+":"+action+":"+active);
		//out.println("data add validate:"+topic+":"+statement+":"+operation+":"+value+":"+action+":"+active);
		out.flush();
		connect = DatbaseConnection.getConnectionMySQL();
		String seq = ControlSequenceTable.getSeqValidate();
		preparedStatement = connect.prepareStatement("INSERT INTO tblmt_validation VALUES (?,?,?,?,?,?,?)");
		preparedStatement.setString(1, seq);
		preparedStatement.setString(2, topic);
		preparedStatement.setString(3, statement);
		preparedStatement.setString(4, operation);
		preparedStatement.setString(5, value);
		preparedStatement.setString(6, action);
		preparedStatement.setString(7, active);
			
		    //String returnAdd="a";

		try{
		    //System.out.println(preparedStatement);
		    if(preparedStatement.executeUpdate()>0){
		    	success = success + "เพิมเงือ่นไข validate เรียบร้อย";
		    }else{
		    	success = success + "เพิมเงือ่นไข validate ล้มเหลว - SQL execution fail";
		    }
		}catch(Exception e){
	    	System.out.println("ex:"+e.getMessage());
	    	success = success + "เพิมเงือ่นไข validate ล้มเหลว - SQL validation fail";
		}			
		

	    //System.out.println("Add validate finish");
		
	    preparedStatement.close();
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
<script type="text/javascript">
	alert ("STOP");
</script>
</body>
</html>