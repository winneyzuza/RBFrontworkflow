<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="mt.LvJob" %>

<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Job Title</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<style type="text/css">
.style3 {color: #FF0000}
</style>
<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		
		if(confirm("ยืนยันทำรายการ")){			
			if(cmd == 'update'){
				mainForm.setAttribute("action","logicJobLvUpdate.jsp");
				mainForm.submit();
			}else if(cmd.substring(0,6)=='remove'){
				var i = cmd.substring(6)
				document.getElementById("vRemove").value = document.getElementById("jobName"+i).value;
 				alert(document.getElementById("vRemove").value);
				mainForm.setAttribute("action","logicJobLvRemove.jsp");
				mainForm.submit();
			}
		}
		
	}
</script>

<%!
public String getSelect(String value,String AP){
	//System.out.println(value);
	if(value.equals(AP))
		return "selected";
	return "";
} 

public String getCheckBox(String Y){
	//System.out.println(Y);
	if(Y.equals("Y"))
		return "checked=\"checked\"";
	return "";
}

%>
<body>
<%
		boolean read = false;
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
			
		}else{
			String module = session.getAttribute("authen").toString();
			//module = "Administrator";
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
			if(module.indexOf("Audit")>-1){
				read= true;
			}
		}

		Connection connect = null;
		PreparedStatement preparedStatement = null;
  
		connect = DatbaseConnection.getConnectionMySQL();

			 String sql = "select * from tblmt_lvjob order by CONVERT(lv,UNSIGNED INTEGER) ";
			preparedStatement = connect.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////

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
	        	<li><a href="..\jsp\logicValidate.jsp" >Validation</a></li>
	            <li><a href="..\jsp\logicApprover.jsp" >Approver</a></li>
	            <li><a href="..\jsp\logicJobLv.jsp" class="active">Job Title</a></li>
	            <li><a href="..\jsp\logicCorpLv.jsp" >Corporate Staff</a></li>
	            <li><a href="..\jsp\holiday.jsp" >Holiday</a></li>
	        </ul>
	    <!-- Main Content -->
	    <div id="main_content">              
						<form id="mainForm"  method="post" action="" ><span class="style3">
						<% 								
							if( session.getAttribute("success")!=null )
								out.println(session.getAttribute("success"));
				
						session.removeAttribute("success");
						%></span>
						<br>
						
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">
							<tr>
								<td class="header_tbl_content" >ลำดับ Job Title   </td>
								<td width="10"><img src="imgs/spacer.gif" style="border:0;" /></td>
						    	<td id="imgAdd" align="left" ><img src="imgs/add_icon.png" style="border:0;" height="24" title="Add" onclick="window.open('..\\jsp\\logicJobLvAdd.jsp', '_blank','width=720,height=400')" /></td>
						    	<td align="right"><span id="msg1" width="200">
								<%
									
									if( "as".equals(session.getAttribute("module")) )
										out.println(" update success.");
									if( "ae".equals(session.getAttribute("module")) )
										out.println(" update error !!!");
									if( "rs".equals(session.getAttribute("module")) )
										out.println(" remove success.");
									if( "re".equals(session.getAttribute("module")) )
										out.println(" remove error !!!");
									session.removeAttribute("module");
								%>		
								</span>						</td>
							</tr>
						</table>
						
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">
						  <tr>
							<td width="100" class="header_tbl_content" >Old level</td>
							<td width="100" class="header_tbl_content" >New level</td>
							<td class="header_tbl_content" >Old Job Title</td>
							<td class="header_tbl_content" >New Job Title</td>
						  </tr>
						  
						<%  int i =0;
								  
							if(!read){
							  	  while(rs.next() )
								  { i++;
// 								  	String pkey = rs.getString("PKey");
// 								  	String statement = getStatement(rs.getString("Validate"),rs.getString("DayBranch"));
								  %>
<%-- 								  <input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>"> --%>
								  <tr>
								  	<td align="center" ><input size="8" align="right" class="txt1" style="background-color:#f3defc" 						type="text" value="<%=rs.getString("lv") %>" readonly /></td>
								  	<td align="center" ><input size="8" align="right" class="txt1" 			maxlength="3"						name="lvJob<%=i%>"  	type="text" value="<%=rs.getString("lv") %>" /></td>
								  	<td align="center" ><input size="40" align="center" class="txt1" style="background-color:#f3defc" name="OldjobName<%=i %>"	type="text" value="<%=rs.getString("JobName") %>" readonly /></td>
								  	<td align="center" ><input size="40" align="center" class="txt1" 	maxlength="255"			id="jobName<%=i %>" name="jobName<%=i %>" 	type="text" value="<%=rs.getString("JobName") %>" /></td>
								  	<td class="tbl_row1" ><img src="imgs/delete_icon.png" style="border:0;" height="24" title="Delete" onClick="doSubmit('remove<%=i%>')" /></td>
								  </tr>
						<%
								  }
							}else{ ///////  read only
								  
							  	  while(rs.next() )
								  { i++;
								  %>
								  <tr>
								  	<td align="center" ><input align="center" class="txt1" style="background-color:#f3defc"   type="text" value="<%=rs.getString("lv") %>" readonly /></td>
								  	<td align="center" ></td>
								  	<td align="center" ><input align="center" class="txt1" style="background-color:#f3defc"   type="text" value="<%=rs.getString("JobName") %>" readonly /></td>
								  	<td align="center" ></td>
								  </tr>
						<%
								  }
								
							}
							connect.close();
						%>
							<input type="hidden"  name="numRow" value="<%=i%>">
						 	<input type="hidden"  id="vRemove" name="vRemove" value="">
						</table>
						
						<div align="center">
						<input class="btn1" type="submit" id="btUpdate" value="Update" onClick="doSubmit('update')" />
						</div>
						</form>
						<br><br><br>
				    </div>
					 
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
	var ac = document.getElementById("BusinessChange");
	ac.setAttribute("class", "active");
</script>
