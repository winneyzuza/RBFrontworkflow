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
		System.out.println("Add Branch");
		request.setCharacterEncoding("UTF-8");

	  	String orgCode = request.getParameter("OrgCode");
		String ocCode = request.getParameter("OcCode");
		String bNameTH = request.getParameter("BNameTH");
		String bNameEN = request.getParameter("BNameEN");
		String operationDay = request.getParameter("OperationDay");
		String counterLimit1 = request.getParameter("CounterLimit1");
		String counterLimit2 = request.getParameter("CounterLimit2");
		String metropolitan = request.getParameter("SelectMetropolitan");
		String ABM = request.getParameter("SelectABM");
		String group = request.getParameter("SelectGroup");
		String area = request.getParameter("Area");
		String network = request.getParameter("Network");		
		//System.out.println(metropolitan+":"+ABM);
		
		boolean check = true;
		String success="";
		if("".equals(orgCode)||"".equals(ocCode)||"".equals(bNameTH)||"".equals(bNameEN)||"".equals(operationDay)||
				"".equals(counterLimit1)||"".equals(counterLimit2)||"".equals(metropolitan)||"".equals(ABM)
				||"".equals(group)||"".equals(area)||"".equals(network)){
			check=false;
		}
		
		if(check){
			
			Connection connect = null;
			PreparedStatement preparedStatement = null;
			connect = DatbaseConnection.getConnectionMySQL();
			
//	 		preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo_log where OrgCode = '0001'");
//	 		  preparedStatement.executeUpdate();
//			  System.out.println("TESTTT:"+ocCode);
			  
			preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");
		    preparedStatement.setString(1, orgCode);
		    preparedStatement.setString(2, ocCode);
		    preparedStatement.setString(3, bNameTH);
		    preparedStatement.setString(4, bNameEN);
		    preparedStatement.setString(5, operationDay);
		    preparedStatement.setString(6, counterLimit1);
		    preparedStatement.setString(7, counterLimit2);
		    preparedStatement.setString(8, metropolitan);
		    preparedStatement.setString(9, ABM);
		    preparedStatement.setString(10, area);
		    preparedStatement.setString(11, network);
		    preparedStatement.setString(12, group);
		    
		    
		    try{
		    	System.out.println(preparedStatement);
		    	preparedStatement.executeUpdate();
		    	success="เพิม branch สำเร็จ ";
		    }catch(Exception e){
		    	check = false;
		    	e.printStackTrace();
		    	success="เพิ่ม branch มีปัญหา อาจเกิดจากรหัสสาขาหรือรหัสหน่วยงานซ้ำ";
		    }
		    
			System.out.println("add finish");
			
		    connect.close();
						
		}else{
			success="ยังกรอกข้อมูลไม่ครบถ้วน";
		}
	    //request.getSession().setAttribute("success1", "XXXX");
	    //response.sendRedirect("branch.jsp?module="+returnrs);
	    
	    %>
		<script type="text/javascript">
			alert("<%=success%>");
			<%
			if(check){
				out.println("window.close()");
			}else{
				session.setAttribute("success", success);
		 		RequestDispatcher requestDispatcher; 
		 		requestDispatcher = request.getRequestDispatcher("branchAdd.jsp");
		 		requestDispatcher.forward(request, response);
			}
			%>

		</script> 
		<%
	    
// 	    session.setAttribute("module", returnrs);
// 		response.sendRedirect("branch.jsp");
	    

		
%>


</body>
</html>