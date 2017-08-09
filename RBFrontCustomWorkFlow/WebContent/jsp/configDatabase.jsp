<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.SEADatabase" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Administrator</title>

<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<style type="text/css">
.style3 {color: #FF0000}
</style>

<script type="text/javascript">

function doSubmit(cmd){
		if(confirm("ยืนยันทำรายการ")){
			var mainForm = document.getElementById("mainForm")

			if(cmd == 'update'){
				mainForm.setAttribute("action","configDatabaseUpdate.jsp");
				mainForm.submit();
			}
		}
}

</script>

<body>

<%
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
		}else{
			String module = session.getAttribute("authen").toString();
			//module = "Administrator"; "Administrator";
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
		}

		SEADatabase cf = new SEADatabase();
		cf.setUserPass();
		String userRB = cf.getUserRB();
		String userHR = cf.getUserHR();
		
		String psRB = cf.getPassRB();
		String psHR = cf.getPassHR();
		
%>

<div id="container">
	<div id="header">
    	<table border="0" cellpadding="0" cellspacing="0" class="header_banner" width="100%" height="90">
        <tr>
        	<td class="header_logo" width="229"><img src="imgs/spacer.gif" style="border:0;" /></td>
            <td><span class="header_caption">RBFront Custom Workflow</span></td>
        </tr>
        </table>
        
        <!-- top menu -->
    	<ul id=menu style="width: 1250px;">
        </ul>
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   	   		<ul>
	        	<li><a href="..\jsp\admin.jsp" >Authentication</a></li>
	            <li><a href="..\jsp\configDatabase.jsp" class="active" >Database</a></li>
	            <li><a href="..\jsp\pathFile.jsp" >Path File</a></li>
	        </ul>
    <!-- Main Content -->
    <div id="main_content" >
             
         <form id="mainForm" method="post" action="" >     
	         <table border="0" cellpadding="0" cellspacing="0"  class="tbl_search">
	         	<tr><td width="20">&nbsp;</td></tr>
	         	<tr><td colspan="20" >      
	         			<span id="msg2" class="style3"><% 	
		
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
		
							session.removeAttribute("success");
						%></span>
						
				</td></tr>
	         	<tr><td colspan="20" class="header_tbl_content2" >RBFront Custom Application </td></tr>     	
	            <tr> 	            	
	                <td width="10">&nbsp;</td>
	                <td align="right">Username</td>
	                <td width="5">&nbsp;</td>
	                <td align="left"><input name="userRB" type="text" class="txt1"  value="<%=userRB %>"/></td>
					<td width="5">&nbsp;</td>
	 				<td align="right" ></td>
	                <td width="5">&nbsp;</td>	                
	                <td width="10">&nbsp;</td>
	                <td align="right">Password</td>
	                <td width="5">&nbsp;</td>
			        <td align="left"><input name="passRB" type="password" class="txt1"  value="<%=psRB %>>"/></td>		        
			        <td width="20">&nbsp;</td>
			                              
	            </tr>
	            <tr><td width="20">&nbsp;</td></tr>
	         	<tr><td colspan="20" class="header_tbl_content2" >HRMS </td></tr>  
	            <tr> 	            	
	                <td width="10">&nbsp;</td>
	                <td align="right">Username</td>
	                <td width="5">&nbsp;</td>
	                <td align="left"><input name="userHR" type="text" class="txt1"  value="<%=userHR %>"/></td>
					<td width="5">&nbsp;</td>
	 				<td align="right" name="PassRB"></td>
	                <td width="5">&nbsp;</td>	                
	                <td width="10">&nbsp;</td>
	                <td align="right">Password</td>
	                <td width="5">&nbsp;</td>
			        <td align="left"><input name="passHR" type="password" class="txt1"  value="<%=psHR %>"/></td>		        
			        <td width="20">&nbsp;</td>
			                              
	            </tr>
	            <tr><td width="5">&nbsp;</td></tr>
	            <tr> 
	            	<td colspan="20" align="center">
			        <input type="button" id="btSearch" value="Update" class="btn1" onClick="doSubmit('update')"/>
			        </td>
	            
	            </tr>
	        </table>
        
	        </form>					
    </div>
    <!-- Main Content -->
    
    </div>
    
    <div id="footer">
    	<span class="footer_text">&copy; สงวนลิขสิทธิ์ 2559 ธนาคารไทยพาณิชย์ จำกัด (มหาชน)</span>
    </div>
</div>

</body>
</html>

<script src="js/menu.js"></script>
<script type="text/javascript">
	var ac = document.getElementById("admin");
	ac.setAttribute("class", "active");
</script>

