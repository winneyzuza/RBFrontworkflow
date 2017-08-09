<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>


<title>Import Export</title>
<style type="text/css">
<!--
.style3 {color: #FF0000}
-->
</style>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<script type="text/javascript">
	function forwardSearch(){
		window.location.href = "branchSearch.jsp?OrgCode="+document.getElementById("OrgCode").value;
	}	
	function forwardAddEdit(){
		window.location.href = "branchAddEdit.jsp?status=a";
	}
function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
			
			if(confirm("ยืนยันทำรายการ")){
						
				if(cmd == 'addBranch'){
					document.getElementById("fileTerminal").disabled = true;
					document.getElementById("fileUserlist3").disabled = true;
					document.getElementById("fileExam").disabled = true;
					document.getElementById("fileArea").disabled = true;
					document.getElementById("fileNetwork").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","branchAddFile.jsp");
					mainForm.submit();
					
				}else if(cmd == 'addUserlist3'){
					document.getElementById("fileBranch").disabled = true;
					document.getElementById("fileTerminal").disabled = true;
					document.getElementById("fileExam").disabled = true;
					document.getElementById("fileArea").disabled = true;
					document.getElementById("fileNetwork").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","terminalAddUserlist3.jsp");
					mainForm.submit();
					
				}else if(cmd == 'addTerminal'){
					document.getElementById("fileBranch").disabled = true;
					document.getElementById("fileUserlist3").disabled = true;
					document.getElementById("fileExam").disabled = true;
					document.getElementById("fileArea").disabled = true;
					document.getElementById("fileNetwork").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","terminalAddMaster.jsp");
					mainForm.submit();
					
				}else if(cmd == 'exam'){
					document.getElementById("fileBranch").disabled = true;
					document.getElementById("fileUserlist3").disabled = true;
					document.getElementById("fileTerminal").disabled = true;
					document.getElementById("fileArea").disabled = true;
					document.getElementById("fileNetwork").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","examFileUpload.jsp");
					mainForm.submit();
				}else if(cmd == 'addArea'){
					document.getElementById("fileBranch").disabled = true;
					document.getElementById("fileUserlist3").disabled = true;
					document.getElementById("fileTerminal").disabled = true;
					document.getElementById("fileArea").disabled = false;
					document.getElementById("fileNetwork").disabled = true;
					document.getElementById("fileExam").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","AreaUpload.jsp");
					mainForm.submit();
				}else if(cmd == 'addNetwork'){
					document.getElementById("fileBranch").disabled = true;
					document.getElementById("fileUserlist3").disabled = true;
					document.getElementById("fileTerminal").disabled = true;
					document.getElementById("fileArea").disabled = true;
					document.getElementById("fileNetwork").disabled = false;
					document.getElementById("fileExam").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","NetworkUpload.jsp");
					mainForm.submit();
				}
			}

	}
	
	
</script>


<body >
<%
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
		}else{
			String module = session.getAttribute("authen").toString();
			//module = "Administrator";
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
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
   
    <!-- Main Content -->
    <div id="main_content">
    <form id="mainForm" method="post" action="">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr><td align="center">
	    <table border="0" cellpadding="0" cellspacing="0" width="40%" class="tbl_login">
	    	<tr>
	    		
	    		<td align="right" >
	    		<input class="txt1" id="fileBranch" name="fileBranch" type="file" size="20" />
	    		</td><td>
				<input class="btn1" type="submit" id="uploadBranch" value="Upload Branch" size="20" onClick="doSubmit('addBranch')" />	
	        	</td>
	  		</tr><tr>
	  			
	  			<td align="right" >
	    		<input class="txt1" id="fileUserlist3" name="file1" type="file" size="20" />
	    		</td><td>
	        	<input class="btn1" type="submit" id="uploadUserlist3" value="Upload Userlist3"  onClick="doSubmit('addUserlist3')" />  
	        	</td>
	  		</tr><tr>
	  			
	  			<td align="right" >
	  			<input class="txt1" id="fileTerminal" name="file2" type="file" size="20" />
	  			</td><td>
	        	<input class="btn1"  type="submit" id="uploadTerminal" value="Upload Terminal list" onClick="doSubmit('addTerminal')"  />  
	        	</td>
	  		</tr><tr>
	  			
	  			<td align="right" >
	  			<input class="txt1" id="fileArea" name="file3" type="file" size="20" />
	  			</td><td>
	        	<input class="btn1"  type="submit" id="uploadArea" value="Upload Area list" onClick="doSubmit('addArea')"  />  
	        	</td>
	  		</tr><tr>
	  			
	  			<td align="right" >
	  			<input class="txt1" id="fileNetwork" name="file4" type="file" size="20" />
	  			</td><td>
	        	<input class="btn1"  type="submit" id="uploadNetwork" value="Upload Network list" onClick="doSubmit('addNetwork')"  />  
	        	</td>
	  		</tr>
	  		<tr>
	  			
	  			<td align="right" >
	  			<input class="txt1" id="fileExam" name="file5" type="file" size="20" />
	  			</td><td>
	        	<input class="btn1"  type="submit" id="uploadExam" value="Upload Exam" onClick="doSubmit('exam')"  />  
	        	</td>
	  		</tr>
	  		<tr><td colspan="15" >
	  		        <span id="msg2" class="style3"><% 	
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
		
							session.removeAttribute("success");
						%></span>
	  		</td></tr>
	    </table>
	    
	    <table border="0" cellpadding="0" cellspacing="0" width="40%" >
	    <tr><td align="right">
	    	<img src="imgs/help-icon-30.png" style="border:0;" height="24" title="Help" onclick="window.open('..\\jsp\\help.html', '_blank','width=945,height=510')" /> 
	    </td></tr>
	    
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
	var ac = document.getElementById("importExport");
	ac.setAttribute("class", "active");
</script>
