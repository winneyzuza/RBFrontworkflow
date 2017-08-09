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
		String success = "Update ";
		SEADatabase cf = new SEADatabase();
		request.setCharacterEncoding("UTF-8");
		//FileNameUserlist3,PathUserlist3,FileNameOutput1,PathOutput1;
		
		String FileNameUserlist3 = request.getParameter("FileNameUserlist3");
		String PathUserlist3 = request.getParameter("PathUserlist3");
		String FileNameOutput1 = request.getParameter("FileNameOutput1");
		String PathOutput1 = request.getParameter("PathOutput1");
		
		System.out.println(FileNameUserlist3+PathUserlist3+FileNameOutput1+PathOutput1);
		
		if("".equals(FileNameUserlist3.trim()) || "".equals(PathUserlist3.trim()) 
				|| "".equals(FileNameOutput1.trim()) || "".equals(PathOutput1.trim()) ){
			
			success = success + "กรอกข้อมูลไม่ครบ ";
		}else
		{
			System.out.println("Path File Update");

			Connection connect = DatbaseConnection.getConnectionMySQL();
			
		    try{			
				String sql = "UPDATE tblmt_config SET Value=? WHERE Name like 'FileNameUserlist3' ";			
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
			    preparedStatement.setString(1, FileNameUserlist3);
				preparedStatement.executeUpdate();
				
				sql = "UPDATE tblmt_config SET Value=? WHERE Name like 'PathUserlist3' ";			
				preparedStatement = connect.prepareStatement(sql);
			    preparedStatement.setString(1, PathUserlist3);
				preparedStatement.executeUpdate();
				
				sql = "UPDATE tblmt_config SET Value=? WHERE Name like 'FileNameOutput1' ";			
				preparedStatement = connect.prepareStatement(sql);
			    preparedStatement.setString(1, FileNameOutput1);
				preparedStatement.executeUpdate();
				
				sql = "UPDATE tblmt_config SET Value=? WHERE Name like 'PathOutput1' ";			
				preparedStatement = connect.prepareStatement(sql);
			    preparedStatement.setString(1, PathOutput1);
				preparedStatement.executeUpdate();
			
		    	success = "Update path File finish";
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    }
		    
			System.out.println("edit branch finish");
		    connect.close();	
		}
			
	    session.setAttribute("success", success);
		response.sendRedirect("pathFile.jsp");
		
%>
</body>
</html>

