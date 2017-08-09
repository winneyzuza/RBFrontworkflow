<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="db.LDAPConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="mt.Authen" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
		
	  String user = request.getParameter("userID");
	  String pass = request.getParameter("passUser");
	  
	  System.out.println("u/p:"+user+":"+"hidden");
	  
		if("".equals(Configuration.TYPE)){
			try{
				
				Configuration.TYPE = Configuration.getTYPE();
				Configuration.setConfigFromFile();
				Configuration.getSuperUser();
				Configuration.setConfig();
			}catch(Exception ex){
				
			}
// 			System.out.println("hostAD"+Configuration.hostAD);
// 			System.out.println("ldapURI"+Configuration.ldapURI);
// 			System.out.println("hostnameDB"+Configuration.hostnameDB);
		}
	  
	  
	  Boolean result=false;
	  Boolean administator = false;
	  String module="";
	  LDAPConnection login = new LDAPConnection();
	  
	  if(!Configuration.TYPE.equals("DEV")){		  
		  try{
			  System.out.println("usr/pass:"+user+":"+"hidden");
			  result = login.getLDAP(user, pass);//login.connectLAP(user, pass);
			  System.out.println("SuperUser:"+result);
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  if(result){
// 			  System.out.println("user:"+user+":"+Configuration.superUser1);
			  session.setAttribute( "userModify", user );
			  if(user.trim().equals(Configuration.superUser1.trim())){
					administator = true;
					module = "Administrator";
			  }else{
				  module = Authen.getAuthen(user);
			  }
			  
			  if("".equals(module)){
				  result = false;
				  session.setAttribute("loginFail","user เนเธกเนเธกเธตเธชเธดเธ—เธเธดเนเนเธเธฃเธฐเธเธ");
			  }
				   
		  }else{
			  session.setAttribute("loginFail","login เนเธกเนเธชเธณเน€เธฃเนเธ");
		  }
		  
	  }else{
		  result = true;
		  module = Authen.getAuthen(user);
		  session.setAttribute( "userModify", user );
	  }
	  System.out.println("module:"+module);
	  
	
		System.out.println("login finish :"+result.toString());
		System.out.println("login superUser :"+administator);
		
		if(administator){
			//Configuration.setConfig();
			session.setAttribute( "resultLogin", "true" );
			session.setAttribute( "authen",module);
			response.sendRedirect("configDatabase.jsp");
		}else if(result){		
			Configuration.setConfig();
			session.setAttribute( "resultLogin", "true" );
			session.setAttribute( "authen",module);
			response.sendRedirect("systemStatus.jsp");
		}else{
			session.setAttribute( "resultLogin", "false" );
			response.sendRedirect("main.jsp");
		}
//  	RequestDispatcher requestDispatcher; 
//  	requestDispatcher = request.getRequestDispatcher("main.jsp");
//  	requestDispatcher.forward(request, response);

%>

</body>
</html>