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
		System.out.println("add terminal");
		request.setCharacterEncoding("UTF-8");
		String success="";
		

	  	String orgCode = request.getParameter("OrgCode");
		String EmpID = request.getParameter("EmpID");
		String Name = request.getParameter("Name");
		String TechnicalRole = request.getParameter("TechnicalRole");
		String BusinessRole = request.getParameter("BusinessRole");
		String TerminalID = request.getParameter("TerminalID");
		String Reserved = request.getParameter("Reserved");
		
		if(Reserved==null)
			Reserved="N";
		
		boolean check = true;
// 		System.out.println(orgCode+EmpID+Name+TechnicalRole+BusinessRole+TerminalID+Reserved); 
		
		if(orgCode.trim().equals("") || EmpID.trim().equals("") || Name.trim().equals("") || TechnicalRole.trim().equals("") 
		   || BusinessRole.trim().equals("") || TerminalID.trim().equals("") || Reserved.trim().equals("")){
			check = false;
		}
		
		if(check){
			
			Connection connect = null;
			PreparedStatement preparedStatement = null;
			connect = DatbaseConnection.getConnectionMySQL();
						 
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_terminallist VALUES (LPAD(?,4,'0'),?,?,?,?,?,?) ");
		    preparedStatement.setString(1, orgCode);
		    preparedStatement.setString(2, EmpID);
		    preparedStatement.setString(3, Name);
		    preparedStatement.setString(4, TechnicalRole);
		    preparedStatement.setString(5, BusinessRole);
		    preparedStatement.setString(6, TerminalID);
		    preparedStatement.setString(7, Reserved);
		    
		    try{
		    	preparedStatement.executeUpdate();
		    	success="เพิ่ม Terminal สำเร็จ";
		    		
				preparedStatement.close();
				connect.close();
		    	
		    }catch(Exception e){
		    	check = false;
		    	System.out.println("ex:"+e.getMessage());
		    	success="เพิ่ม Terminal ไม่สำเร็จ  อาจเกิดจาก TerminalID มีแล้วในสาขาข้างต้น";
		    }
		    
			System.out.println("add terminal finish");
			connect.close();
		}else{
			success="ยังกรอกข้อมูลไม่ครบถ้วน";
		}
		
	    %>
		<script type="text/javascript">
			alert("<%=success%>");
			<%
			if(check){
				out.println("window.close()");
			}else{
				session.setAttribute("success", success);
		 		RequestDispatcher requestDispatcher; 
		 		requestDispatcher = request.getRequestDispatcher("terminalAdd.jsp");
		 		requestDispatcher.forward(request, response);
			}
			%>

		</script> 
		<%
		
// 	    session.setAttribute("module", returnrs);
// 	    response.sendRedirect("terminal.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("terminal.jsp?module="+returnrs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>