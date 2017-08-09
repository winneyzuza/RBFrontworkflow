<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add  Role</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css" />

<script type="text/javascript">

	function doSubmit(cmd){
		var lv = document.getElementById("lvJob").value;
		var jobTitle = document.getElementById("jobTitle").value;
		
		if(lv.trim() == "" || jobTitle.trim() == ""){
			alert("โปรดใส่ข้อมูล");
		}else{
			var mainForm = document.getElementById("mainForm")
			
			if(confirm("ยืนยันทำรายการ")){
				if(cmd == 'add'){
					mainForm.setAttribute("action","logicJobLvAddServlet.jsp");
					mainForm.submit();
				}
			}
		}

			
	}
	
	
</script>

<%
  
///////////////////////////////////////////////////////////////////////////////////////////

// 			Connection connect = DatbaseConnection.getConnectionMySQL();
// 			String sqlRole = "select CONCAT(lv,':',JobName) JName from tblmt_lvjob order by lv ";
// 			PreparedStatement preparedStatement = connect.prepareStatement(sqlRole);
// 			ResultSet rsJobName = preparedStatement.executeQuery();

///////////////////////////////////////////////////////////////////////////////////////////

%>

<body>

<div id="container">
	<div id="header">
    	<table border="0" cellpadding="0" cellspacing="0" class="header_banner" width="100%" height="90">
        <tr>
        	<td class="header_logo" width="229"><img src="imgs/spacer.gif" style="border:0;" /></td>
            <td><span class="header_caption">RBFront Custom Workflow</span></td>
        </tr>
        </table>
        
        <!-- top menu -->
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   
    <!-- Main Content -->
    <div id="main_content">  
         <form id="mainForm"  method="post" action="" >
				<% 								
					if( session.getAttribute("success")!=null )
						out.println(session.getAttribute("success"));
					
					session.removeAttribute("success");
				%>
				
				 <input type="hidden" id="countJName" value="">
							        
		         <table border="0" cellpadding="0" cellspacing="0" width="60%" class="tbl_search" >
		          <tr><td align="right">Level: </td>
		          	  <td width="10">&nbsp;</td>
					  <td align="left" ><input align="center" class="txt1" id="lvJob" name="lvJob"  type="text" value=""  /></td>
				  </tr><tr><td align="right">Job Title:</td>
				  	  <td width="10">&nbsp;</td>
					  <td align="left" ><input align="center" class="txt1" id="jobTitle" name="jobTitle"  type="text" value=""  /></td>
				  </tr>

				 <tr><td colspan="3" align="center" ><input class="btn1" type="submit" name="Submit" value="Add" onClick="doSubmit('add')" /></td></tr>
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
<%

// preparedStatement.close();
// connect.close();
%>