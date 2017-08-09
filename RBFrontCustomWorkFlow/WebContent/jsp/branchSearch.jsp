<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.IOException" %>
<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%
	if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
		response.sendRedirect("main.jsp");
	}
%>
<body>
<%
	  request.setCharacterEncoding("UTF-8");
	  response.setCharacterEncoding("UTF-8");
	  
	  String returnrs = null;

	  out.println(request.getParameter("BranchSearch.jsp"));
	  
	  String orgCode = request.getParameter("OrgCode");
	  String OcCode = request.getParameter("OcCode");
	  String BNameTH = request.getParameter("BNameTH");
	  String BNameEN = request.getParameter("BNameEN");
	  String OperationDay = request.getParameter("OperationDay");
	  String CounterLimit1 = request.getParameter("CounterLimit1");
	  String CounterLimit2 = request.getParameter("CounterLimit2");
	  String Metropolitan = request.getParameter("SelectMetropolitan");
	  String ABM = request.getParameter("SelectABM");
	  String group = request.getParameter("SelectGroup");
	  String area = request.getParameter("Area");
	  String network = request.getParameter("Network");			  
	  
	  Connection connect = null;
	  PreparedStatement preparedStatement = null;
	  //System.out.println("orgCode:"+orgCode);
	  connect = DatbaseConnection.getConnectionMySQL();
	  
	  String sql = "select * from tblmt_branchinfo b where b.OrgCode like ? "+
 			   "and OcCode like ? and BNameTH like ? and BNameEN like ? "+
 			   "and OperationDay like ? and CounterLimit1 like ? and CounterLimit2 like ? "+
		       "and Metropolitan like ? and ABM like ? "+
		       "and AreaNo like ? and Network like ? and GroupB like ?  "+ 	   
		       "order by OcCode ";
	  
	  preparedStatement = connect.prepareStatement(sql);
      preparedStatement.setString(1, "%"+orgCode+"%");
      preparedStatement.setString(2, "%"+OcCode+"%");
      preparedStatement.setString(3, "%"+BNameTH+"%");
      preparedStatement.setString(4, "%"+BNameEN+"%");
      preparedStatement.setString(5, "%"+OperationDay+"%");
      preparedStatement.setString(6, "%"+CounterLimit1+"%");
      preparedStatement.setString(7, "%"+CounterLimit2+"%");
      preparedStatement.setString(8, "%"+Metropolitan+"%");
      preparedStatement.setString(9, "%"+ABM+"%");
      preparedStatement.setString(10, "%"+area+"%");
      preparedStatement.setString(11, "%"+network+"%");
      preparedStatement.setString(12, "%"+group+"%");
      System.out.println(preparedStatement);
      ResultSet resultSet = preparedStatement.executeQuery();
      
      if(resultSet.next()){
  		request.setAttribute("OrgCode",resultSet.getString("OrgCode"));
  		request.setAttribute("OcCode",resultSet.getString("OcCode"));
  		request.setAttribute("BNameTH",resultSet.getString("BNameTH"));
  		request.setAttribute("BNameEN",resultSet.getString("BNameEN"));
  		request.setAttribute("OperationDay",resultSet.getString("OperationDay"));
  		request.setAttribute("CounterLimit1",resultSet.getString("CounterLimit1"));
  		request.setAttribute("CounterLimit2",resultSet.getString("CounterLimit2"));
  		request.setAttribute("Metropolitan",resultSet.getString("Metropolitan"));
  		request.setAttribute("ABM",resultSet.getString("ABM"));
  	
  		System.out.println("selcet finish");
  		returnrs = "ss";
      }else{
    	returnrs = "se";  
      }
	
    connect.close();
	
    session.setAttribute("success2", returnrs);
//	response.sendRedirect("branch.jsp");
    
 	RequestDispatcher requestDispatcher; 
 	requestDispatcher = request.getRequestDispatcher("branch.jsp");
 	requestDispatcher.forward(request, response);
	


%>


</body>
</html>