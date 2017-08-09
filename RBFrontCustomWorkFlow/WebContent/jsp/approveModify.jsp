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
<title>Update Output2</title>
</head>
<body>

<%
		System.out.println("approveModify.jsp");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String returnRs="";

		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("numRow"));
		String reqID,UserID,CurBr,Curpos,Limit,TermID,Action,NewBr,NewPos,NewLimit,NewTermID,Mode,Complete,Checked;
		reqID=UserID=CurBr=Curpos=Limit=TermID=Action=NewBr=NewPos=NewLimit=NewTermID=Mode=Complete=Checked=null;
		String lastTermID = "";
		
		for(int i =1;i<=num;i++){
			reqID = request.getParameter("reqID"+i);
			
			UserID = request.getParameter("UserID"+i);
			CurBr = request.getParameter("CurBr"+i);
			Curpos = request.getParameter("Curpos"+i);
			Limit = request.getParameter("Limit"+i);
			TermID = request.getParameter("TermID"+i);
			Action = request.getParameter("Action"+i);
			NewBr = request.getParameter("NewBr"+i);
			NewPos = request.getParameter("NewPos"+i);
			NewLimit = request.getParameter("NewLimit"+i);
			NewTermID = request.getParameter("NewTermID"+i);
			Mode = request.getParameter("Mode"+i);
			Checked = request.getParameter("Checked"+i);
			
//			if (reqID.endsWith("-1") || Mode.equals("Forward")) { // forward request
				lastTermID = NewTermID;
			//} else {
			//	if (reqID.endsWith("-2") || Mode.equals("Backward")) { // backward request
			//		TermID = lastTermID;
			//	} else {
			//		lastTermID = NewTermID;
			//	}
			//}
			
			if ((TermID == null || TermID.isEmpty()) && (reqID.indexOf("-")>=0))	{
				System.out.println("EXCEPTION! approveModifyRequest: Found EMPTY current terminal ID");
			}
						
			if(Checked==null || !"Y".equals(Checked)){
				Checked = "N";
			}
			
			//Complete = request.getParameter("CheckBoxComplete"+i);
			if(request.getParameter("CheckBoxComplete"+i)==null){Complete="N";}else{Complete="Y";};	
		
			
			//String sql = "UPDATE tbldt_output2 "+
			//		"SET Action=?,NewBr=?,NewPos=?,NewLimit=?,NewTermID=?,ModeOutput=?,Complete=?,Checked=? "+
			//		"WHERE UserID=? "; // issue will happen when update on multiple entries for same user 
			String sql = "UPDATE tbldt_output2 "+
					"SET Action=?,NewBr=?,NewPos=?,NewLimit=?,NewTermID=?,ModeOutput=?,Complete=?,Checked=?, TermID=? "+
					"WHERE RequestID=? ";
			preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setString(1, Action);
		    preparedStatement.setString(2, NewBr);
		    preparedStatement.setString(3, NewPos);
		    preparedStatement.setString(4, NewLimit);
		    preparedStatement.setString(5, NewTermID);
		    preparedStatement.setString(6, Mode);
		    preparedStatement.setString(7, Complete);
		    preparedStatement.setString(8, Checked);
		    preparedStatement.setString(9, TermID);
		    preparedStatement.setString(10, reqID);
	    	
		    try{
		    	//System.out.println(preparedStatement);
		    	preparedStatement.executeUpdate();		    	
		    	ReqRepository.setComplete(reqID,Mode,Complete);
		    	
		    	String busRole = NormalFunction.convertPositionTecnicalToUser(NewPos);
//				comment by Avirut - we do not need to update yet until the job has been closed and confirm the changes
//		    	EmployeeInfo.updateEmployee(UserID,NewBr,NewPos,NewLimit,NewTermID,busRole);
		    	
		    	returnRs="as";
		    }catch(Exception e){
		    	System.out.println("approveModify.jsp: Error found - Update table tbldt_output2");
		    	System.out.println("ex:"+e.getMessage());
		    	returnRs="ae";
		    }
			
		}
		System.out.println("num="+num);		
		System.out.println("edit output2 finish");
		
	    connect.close();
		String rqtype = request.getParameter("SelectType");
		String rqcomplete = request.getParameter("SelectComplete");		
	    session.setAttribute("module", returnRs);
		response.sendRedirect("approveRBFrontRequest.jsp?attrib=update&EffStartDateS="+request.getParameter("EffStartDateS").trim()+"&EffStartDateE="+request.getParameter("EffStartDateE").trim()+"&SelectType="+rqtype.trim()+"&SelectComplete="+rqcomplete.trim());
		
// 		RequestDispatcher requestDispatcher; 
// 		requestDispatcher = request.getRequestDispatcher("approveRBFrontRequest.jsp?module="+returnRs);
// 		requestDispatcher.forward(request, response);
		
%>

</body>
</html>