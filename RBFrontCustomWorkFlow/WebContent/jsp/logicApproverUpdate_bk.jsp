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
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String Job,Role,Approv,Active;
		Job=Role=Approv=Active=null;
		
		int i=1;
		for(i =1;i<=num;i++){
			Job = request.getParameter("job"+i);
			Role = request.getParameter("role"+i);
			Approv = request.getParameter("SelectApprover"+i);
			Active = request.getParameter("CheckBoxActive"+i);
			if(Active==null)
				Active="N";

		System.out.println("Job:"+Job+":"+Role+":"+Approv+":"+Active);
		
		
			String sql = "UPDATE tblmt_approver "+
					"SET Approver = ?,Active = ?"+
					"WHERE Job like ? and Role like ?";
			preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setString(1, Approv);
		    preparedStatement.setString(2, Active);
		    preparedStatement.setString(3, Job);
		    preparedStatement.setString(4, Role);
	    
		    try{
		    	if(preparedStatement.executeUpdate()>0){
		    		success="แก้ไขข้อมูลเรียบร้อย ";
		    	}else{
		    		success="ไม่มีการแก้ไขข้อมูล ";
		    	}
		    	
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success="รายการแก้ไขข้อมูลมีปํญหา ";
		    }
			
		}
		System.out.println("num="+num);
		
		System.out.println("update job role finish");
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start add job role "+i);

		request.setCharacterEncoding("UTF-8");
		
		Job = request.getParameter("job"+i);
		Role = request.getParameter("role"+i);
		Approv = request.getParameter("SelectApprover"+i);
		Active = request.getParameter("CheckBoxActive"+i);
		if(Active==null)
			Active="N";
		System.out.println("Add Job:"+Job+":"+Role+":"+Approv+":"+Active);
		if(!Job.trim().equals("") && !Role.trim().equals("")){
			connect = DatbaseConnection.getConnectionMySQL();
			
			System.out.println("add:"+Job+Role+Approv+Active);  
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_approver VALUES (?,?,?,?) ");
		    preparedStatement.setString(1, Job);
		    preparedStatement.setString(2, Role);
		    preparedStatement.setString(3, Approv);
		    preparedStatement.setString(4, Active);
		    
		    //String returnAdd="a";
		    try{
		    	if(preparedStatement.executeUpdate()>0){
		    		success = success + "เพิม ข้อมูลเรียบร้อย";
		    	}else{
		    		success = success + "ไม่มีรายการเพิม ข้อมูล";
		    	}
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success = success + "รายการเพิม ข้อมูลมีปัญหา";
		    }			
		}

	    System.out.println("Add job role finish");
		
	    preparedStatement.close();
	    connect.close();
		
	    session.setAttribute("success", success);
		response.sendRedirect("logicValidate.jsp");
	    
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("logic.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
	
%>


</body>
</html>