<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.DatbaseConnectionMsSQL" %>
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
		System.out.println("logic CorpLv Update");
		String success ="";
		Connection connect = DatbaseConnection.getConnectionMySQL();

		Connection connectMs = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatementMs = connectMs.prepareStatement("select distinct Corporate_Title_Code,Corporate_Title_EN from iam where Corporate_Title_EN is not null");
		ResultSet resultSetIAM = preparedStatementMs.executeQuery();
		
		if(resultSetIAM != null){
			PreparedStatement preparedStatement = connect.prepareStatement("delete from tblmt_lvcorpstaff ");
			preparedStatement.executeUpdate();
		}
		
		while(resultSetIAM.next() ){
			String corpCode = resultSetIAM.getString("Corporate_Title_Code");
			String corpEN 	= resultSetIAM.getString("Corporate_Title_EN");
			
			String sql = "INSERT INTO tblmt_lvcorpstaff VALUES (?,?,?)";			
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    	preparedStatement.setString(1, corpCode);
		    	preparedStatement.setString(2, corpEN);
		    	preparedStatement.setString(3, corpCode);
		    	
		    try{
		    	preparedStatement.executeUpdate();
		    	success="อัพเดด level Corporate สำเร็จ";
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success="อัพเดด level Corporate ล้มเหลว";
		    }
		}
		PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO tblmt_lvcorpstaff VALUES ('999','Lowest Corp.','999')");
		preparedStatement.executeUpdate();
		
		connect.close();
		connectMs.close();	
		
		System.out.println("update Corporate Update finish");
		
		
	    session.setAttribute("success", success);
		response.sendRedirect("logicCorpLv.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>