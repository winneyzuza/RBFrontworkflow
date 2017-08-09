<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="mt.ReqRepository" %>
<%@ page import="mt.NormalFunction" %>
<%@ page import="mt.EmployeeInfo" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Request Table</title>
</head>
<body>
<%
		boolean debugging = true;
		System.out.println("chekcFile2.jsp - start");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";
		
		
		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String reqID,UserID,CurBr,Curpos,Limit,TermID,Action,NewBr,NewPos,NewLimit,NewTermID,Mode,Complete,Checked;
		reqID=UserID=CurBr=Curpos=Limit=TermID=Action=NewBr=NewPos=NewLimit=NewTermID=Mode=Complete=Checked=null;
		if (debugging) System.out.println("num = "+num);
		
		String userIDs = "";
		for(int i=1;i<=num;i++){
			reqID = request.getParameter("reqID"+i).trim();
			if (reqID.endsWith("-1")) reqID = reqID.substring(0, reqID.length()-2);
			if (reqID.endsWith("-2")) reqID = reqID.substring(0, reqID.length()-2);
			if (reqID.indexOf("-") > -1) reqID = reqID.substring(0, reqID.length()-2); // suffix found on request ID
			if (debugging) { 
				System.out.println("Request ID= -" + reqID + "-");
				System.out.println("Request ID end with -1 =" + reqID.endsWith("-1"));
				System.out.println("Request ID end with -2 =" + reqID.endsWith("-2"));
			}
			UserID = request.getParameter("UserID"+i);
			NewTermID = request.getParameter("NewTermID"+i);
						
			if(request.getParameter("CheckBoxComplete"+i)==null){Complete="N";}else{Complete="C";};	
			
			if("C".equals(Complete)){
				
				String sqlfwd = "UPDATE tbldt_reqrepository "+
						"SET Status=?, LastChange= now(), CompleteF=?, remark=? "+
						"WHERE EmpID=? AND RequestID=? ";
				String sqlbwd = "UPDATE tbldt_reqrepository "+
						"SET Status=?, LastChange= now(), CompleteR=?, remark=concat(remark,?) "+
						"WHERE EmpID=? AND RequestID=? ";
				// add 2017-06-30 Avirut - to update recent terminal ID after "complete job" button pressed
				String sql2 = "UPDATE tblmt_employeeinfo SET TerminalID=? WHERE EmpID=?";
				//
				String through = "";
				String sql = "";
				String prefix = "";
				if (request.getParameter("Mode"+i).equalsIgnoreCase("FORWARD")) {
					sql = sqlfwd;
					Complete = "F";
					prefix = NewTermID.trim();
				} else {
					sql = sqlbwd;
					Complete = "C";
					prefix = "-" + NewTermID.trim();
				}
				if (debugging) System.out.println("checkFile2.jsp: INFO : SQL query=" + sql);
				try{
					
					preparedStatement = connect.prepareStatement(sql);
				    preparedStatement.setString(1, Complete);
				    preparedStatement.setString(4, UserID);
				    //preparedStatement.setString(3, reqID);
				    preparedStatement.setString(2, "Y");
				    // new
				    preparedStatement.setString(3, prefix );
				    // new - end
				    preparedStatement.setString(5, reqID);
				    through = "Will call update tbldt_reqrepository";
				    preparedStatement.executeUpdate();
				    through = "";
				    if (debugging) { 
				    	System.out.println("sql :: " + sql);
				    	System.out.println("Complete :: " + Complete);
						System.out.println("UserID :: " + UserID);
				    	System.out.println("reqID :: " + reqID);
				    }
				    if (userIDs.indexOf(UserID) < 0) userIDs = userIDs + " " + UserID;
				    if(through.equals("")) {
				    	preparedStatement = connect.prepareStatement(sql2);
				    	preparedStatement.setString(1, NewTermID);
					    preparedStatement.setString(2, UserID);
					    through = "Will call update tblmt_employeeinfo";
					    preparedStatement.executeUpdate();
					    through = "Pass!";
				    }
				    
				    returnRs="cjs";
				}
				catch(Exception e){
					System.out.println("checkFile2.jsp (Update tbldt_reqrespository)- " + through + " - exception:"+e.getMessage());
			    	returnRs="cje";
			    }
			}
		}
		
		System.out.println("Close JOb output1 finish");
		
	    connect.close();
		
	    session.setAttribute("module", returnRs);
	    session.setAttribute("userIDs", userIDs);
		response.sendRedirect("approveRBFrontRequest.jsp");
		
		
%>


</body>
</html>