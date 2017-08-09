<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*"%>
<%@ page import="mt.IAM" %>
<style type="text/css">
.style3 {color: #FF0000}
</style>
<%
//int n=Integer.parseInt(request.getParameter("val"));
String empID = request.getParameter("val");
// System.out.println(request.getParameter("val"));
IAM emp = new IAM(empID,"samAccountName");

	if(!emp.getErr().equals("Y")){
// 		out.print("<input class=\"txt1\" style=\"background-color:#f3defc\" type=\"text\" value=\""+emp.getCn()+"\" readonly />");	
		out.print(emp.getCn());	
		out.println("<input type=\"hidden\" name=\"isEmpID\" value=\"Y\">");
	}else{
		out.print("<span class=\"style3\"> ไม่พบข้อมูลใน HRMS. </span>");
		out.println("<input type=\"hidden\" name=\"isEmpID\" value=\"N\">");
	}


%>