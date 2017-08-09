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
<title>Business Logic - Add</title>
</head>
<body>
<%
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String success="";

		request.setCharacterEncoding("UTF-8");

		String DefaultApp,Job,Role,Approv,Active,Requester,Corp;
		DefaultApp=Requester=Job=Role=Approv=Active=Corp=null;
		
////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("start add job role ");

		request.setCharacterEncoding("UTF-8");
		
		Job = request.getParameter("job");
		Role = request.getParameter("role");
		Requester = request.getParameter("SelectRequester");
		Corp = request.getParameter("SelectCorp");
		Approv = request.getParameter("SelectApprover");
		DefaultApp = request.getParameter("SelectDefaultApp");
		Active = request.getParameter("CheckBoxActive");
		if(Active==null)
			Active="N";
		if(Job.indexOf("Choose")>-1 || Role.indexOf("Choose")>-1 ){
			success = "ยังกรอกข้อมูลไม่ครบ";
			Job="";
		}
		
// 		System.out.println("Add Job:"+Job+":"+Role+":"+Approv+":"+Active);
		if(!Job.trim().equals("") && !Role.trim().equals("")){
			connect = DatbaseConnection.getConnectionMySQL();
			
// 			System.out.println("add:"+Job+Role+Approv+Active);
			
			String seq = ControlSequenceTable.getSeqApprover();	
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_approver(Job,Role,Requester,Approver,DefaultApp,Active,PKey,CorpStaff) VALUES (?,?,?,?,?,?,?,?) ");
		    preparedStatement.setString(1, Job);
		    preparedStatement.setString(2, Role);
		    preparedStatement.setString(3, Requester);
		    preparedStatement.setString(4, Approv);
		    preparedStatement.setString(5, DefaultApp);		    
		    preparedStatement.setString(6, Active);
		    preparedStatement.setString(7, seq);
		    preparedStatement.setString(8, Corp);
		    
// 		    System.out.println(preparedStatement);
		    try{
		    	if(preparedStatement.executeUpdate()>0){
		    		success = success + "เพิม ข้อมูลเรียบร้อย";
		    	}
		    	preparedStatement.close();
			    connect.close();
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	success = success + "รายการเพิม ข้อมูลมีปัญหา";
		    }			
		}
		
		connect.close();
	    System.out.println("Add job role finish");
		
	    %>
		<script type="text/javascript">
			alert("<%=success%>");
			window.close();
		</script> 

</body>
</html>