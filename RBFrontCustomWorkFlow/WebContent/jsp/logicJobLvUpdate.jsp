<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="mt.IAM" %>

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
		System.out.println("logic Job title Update");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";

		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String lvJob,oldJobName,newJobName;
		lvJob=oldJobName=newJobName=null;
		
		for(int i =1;i<=num;i++){
			lvJob = request.getParameter("lvJob"+i);
			oldJobName = request.getParameter("OldjobName"+i);
			newJobName = request.getParameter("jobName"+i);
			
			try{
				String sql = "UPDATE tblmt_lvjob "+
						"SET lv=?,jobName=? "+
						"WHERE jobName=? ";
				preparedStatement = connect.prepareStatement(sql);
				
				
				if(lvJob.length() > 1){
					preparedStatement.setString(1, lvJob);
				    preparedStatement.setString(2, newJobName);
				    preparedStatement.setString(3, oldJobName);	    			    
			    
			    	if(preparedStatement.executeUpdate()>0){
			    		returnRs="อัพเดด level job title สำเร็จ ";
			    	}else{
			    		returnRs="อัพเดด level job title ล้มเหลว";
			    	}
				}else{
					returnRs="กรุณาระบุค่า Level";
					break;
				}
		    	
			}catch(Exception ex){
				//System.out.println("ex 1 :"+ex.getMessage());
				returnRs =  "เพิม jobTitle ล้มเหลว : " + ex.getMessage();	
				break;
			}
			try{
		    	
		    	String sql2 = "UPDATE tblmt_job "+
						"SET NameTH=? "+
						"WHERE NameEN=? ";
		    	preparedStatement = connect.prepareStatement(sql2);
			    preparedStatement.setString(1, IAM.getJobNameTH(oldJobName));
			    preparedStatement.setString(2, oldJobName);
		    	if(preparedStatement.executeUpdate()>0){
		    		returnRs="อัพเดด level job title สำเร็จ ";
		    	}
		    	
		    	//else{
		    	//	returnRs="อัพเดด level job title ล้มเหลว tblmt_job";
		    	//}
			    
		    }catch(Exception e){
		    	System.out.println("ex:"+e.getMessage());
		    	returnRs="อัพเดด level job title ล้มเหลว";
		    }
			
		}
		System.out.println("num="+num);
		
		System.out.println("update JobLv Update finish");
		
	    connect.close();
		
	    session.setAttribute("success", returnRs);
		response.sendRedirect("logicJobLv.jsp");
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>


</body>
</html>