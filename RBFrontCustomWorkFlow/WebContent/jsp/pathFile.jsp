<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.SEADatabase" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>PahtFile</title>

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
				mainForm.setAttribute("action","pathFileUpdate.jsp");
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
			//module = "Administrator";
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
		}
		  
		Connection connect = DatbaseConnection.getConnectionMySQL();
		String sql = "select * from tblmt_config ";
		PreparedStatement preparedStatement = connect.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		String FileNameUserlist3,PathUserlist3,FileNameOutput1,PathOutput1;
		FileNameUserlist3=PathUserlist3=FileNameOutput1=PathOutput1="";
		
		while(resultSet.next()){
			String name = resultSet.getString("Name");
			String value = resultSet.getString("Value");
			if("FileNameUserlist3".equals(name)){
				FileNameUserlist3=value;
			}
			if("PathUserlist3".equals(name)){
				PathUserlist3=value;
			}
			if("FileNameOutput1".equals(name)){
				FileNameOutput1=value;
			}
			if("PathOutput1".equals(name)){
				PathOutput1=value;
			}
		}
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
	            <li><a href="..\jsp\configDatabase.jsp" >Database</a></li>
	            <li><a href="..\jsp\pathFile.jsp" class="active" >Path File</a></li>
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
	         	<tr><td colspan="20" class="header_tbl_content2" >File UserList3</td></tr>     	
	            <tr> 	            	
	                <td width="10">&nbsp;</td>
	                <td align="right">PathUserlist3</td>
	                <td width="5">&nbsp;</td>
	                <td align="left"><input name="FileNameUserlist3" type="text" class="txt1"  value="<%=FileNameUserlist3 %>"/></td>
					<td width="5">&nbsp;</td>
	 				<td align="right" ></td>
	                <td width="5">&nbsp;</td>	                
	                <td width="10">&nbsp;</td>
	                <td align="right">FileNameUserlist3</td>
	                <td width="5">&nbsp;</td>
			        <td align="left"><input name="PathUserlist3" type="text" class="txt1"  value="<%=PathUserlist3 %>"/></td>		        
			        <td width="20">&nbsp;</td>
			                              
	            </tr>
	            <tr><td width="20">&nbsp;</td></tr>
	         	<tr><td colspan="20" class="header_tbl_content2" >File Output1 </td></tr>  
	            <tr> 	            	
	                <td width="10">&nbsp;</td>
	                <td align="right">PathOutput1</td>
	                <td width="5">&nbsp;</td>
	                <td align="left"><input name="FileNameOutput1" type="text" class="txt1"  value="<%=FileNameOutput1 %>"/></td>
					<td width="5">&nbsp;</td>
	 				<td align="right" name="PassRB"></td>
	                <td width="5">&nbsp;</td>	                
	                <td width="10">&nbsp;</td>
	                <td align="right">FileNameOutput1</td>
	                <td width="5">&nbsp;</td>
			        <td align="left"><input name="PathOutput1" type="text" class="txt1"  value="<%=PathOutput1 %>"/></td>		        
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

