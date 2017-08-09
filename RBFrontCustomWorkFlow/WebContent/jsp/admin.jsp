<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="mt.IAM" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
var request;
function sendInfo()
{
	var v=document.getElementById("empID").value //document.vinform.t1.value;
	var url="getName.jsp?val="+v;
	
	if(window.XMLHttpRequest){
	request=new XMLHttpRequest();
	}
	else if(window.ActiveXObject){
	request=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	try
	{
	request.onreadystatechange=getInfo;
	request.open("GET",url,true);
	request.send();
	}
	catch(e)
	{
	alert("Unable to connect to server");
	}
}

function getInfo(){
if(request.readyState==4){
var val=request.responseText;

document.getElementById('empName').innerHTML=val;
}
}

</script>

<title>Administrator</title>

<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<script type="text/javascript">

function doSubmit(cmd){
		if(confirm("ยืนยันทำรายการ")){
			var mainForm = document.getElementById("mainForm")
			if(cmd == 'add'){
				var empID = document.getElementById("empID").value;
				if(empID.trim()==''){
					alert('โปรดใส่ข้อมูล EmployeeID ก่อน Add');
				}else{
					mainForm.setAttribute("action","adminAdd.jsp");
					mainForm.submit();
				}
				
			}else if(cmd.substring(0,6)=='remove'){
// 				alert(cmd.substring(0,6));
				var i = cmd.substring(6)
				document.getElementById("aRemove").value = document.getElementById("PKey"+i).value;
				mainForm.setAttribute("action","adminRemove.jsp");
				mainForm.submit();
			}
		}
}

function resetTextBox(){	
	
	document.getElementById("empID").value = '';
	document.getElementById('empName').innerHTML='';	
	document.getElementById("RadioRole1").checked = true;

	
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
		String sql = "select *  from tblmt_authen a  ";		
		PreparedStatement preparedStatement = connect.prepareStatement(sql);		
		ResultSet rs = preparedStatement.executeQuery();
		
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
	        	<li><a href="..\jsp\admin.jsp" class="active" >Authentication</a></li>
	            <li><a href="..\jsp\configDatabase.jsp" >Database</a></li>
	            <li><a href="..\jsp\pathFile.jsp" >Path File</a></li>
	        </ul>
    <!-- Main Content -->
    <div id="main_content">
             
         <form id="mainForm" method="post" action="" >     
	         <table border="0" cellpadding="0" cellspacing="0"  class="tbl_search">
	         	<tr><td width="20">&nbsp;</td></tr>           	
	            <tr > 
	            	
	                <td width="10">&nbsp;</td>
	                <td align="right">User ID:</td>
	                <td width="5">&nbsp;</td>
	                <td align="left"><input id="empID" name="empID" type="text" class="txt1" size="5" value="" onchange="sendInfo()" /></td>
					<td width="5">&nbsp;(s12345)&nbsp;</td>
					<td width="5">&nbsp;</td>
					<td >ตรวจสอบข้อมูล HR:</td>
					<td width="5">&nbsp;</td>
					<td align="right" id="empName"></td>
					</tr>
					 <tr><td>&nbsp;</td></tr>
					<tr>	 				
	                <td width="5">&nbsp;</td>
	                <td align="right">Role:</td>
	                <td width="5">&nbsp;</td>
	                <td colspan=15 >
	                <input type="radio" id="RadioRole1" name="RadioRole" value="User" checked> User
	                <input type="radio" id="RadioRole2" name="RadioRole" value="Audit" > Audit
	                <input type="radio" id="RadioRole3" name="RadioRole" value="Administrator" > Administrator
	                </td>
<!-- 			        <td align="left"> -->
<!-- 			             <select class="txt1" name="SelectType" id="SelectType"> -->
<!-- 			              <option value="User" >User</option> -->
<!-- 				          <option value="Audit" >Audit</option> -->
<!-- 				          <option value="Administrator" >Administrator</option>	 -->
<!-- 			            </select> -->
<!-- 			        </td> -->
					</tr><tr><td>&nbsp;</td></tr>
					<tr>
			        <td width="20">&nbsp;</td>
			        <td colspan=7 align="center">
			        	<input type="button" id="btSearch" value="บันทึก" class="btn2" onClick="doSubmit('add')"/>
			        	<input type="button" class="btn2" value="clear" onClick="resetTextBox()"/>
			        </td>
			        </tr>		        
	        </table>
        <br>
        <table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">						  					  
						<%  int i =0;
						    int j =0;
							String tempJ = "";
				
							  	  while(rs.next() )
								  { i++;j++;
								  	String Module = rs.getString("Module");								  	
								  	String samAccountName 	= rs.getString("EmpID");
								  	String pkey 	= rs.getString("PKey");
								  	
								  	IAM e = new IAM(samAccountName,"samAccountName");
								  	
								  	
								  	if(!tempJ.equals(Module)){
								  		%>
								  		<tr><td colspan=25>
								  			<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
								  				<tr><td class="header_tbl_content2" >&nbsp;&nbsp;<%=Module %></td>
								  				</tr>
								  			</table>
								  		</td></tr>
								  		<tr><td></td><td></td><td></td><td align="center">User ID</td>
								  		<td></td><td align="center" >ชื่อ</td>
								  		<td></td><td align="center" >หน้าที่งาน</td></tr>
								  		<% 
								  		tempJ=Module;
								  		j=1;
								  	}
								  %>
								  <tr><td width="100" >&nbsp;</td><td><%=j+". " %></td>
								  <td width="15" >&nbsp;</td>
								  <td ><%=e.getSamAccountName() %></td>
								  <td width="15" >&nbsp;</td>
 								  <td ><%=e.getFull_Name_TH() %></td>
 								  <td width="25" >&nbsp;</td>
 								  <td ><%=e.getJob_Title_TH() %></td>
 								  <td width="20" >&nbsp;</td>
 								  
								  <td width="50" class="tbl_row1"  ><img src="imgs/delete_icon.png" style="border:0;" height="24" title="Delete" onClick="doSubmit('remove<%=i%>')" /></td>
									<input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>">									
								  <%
								  }
							connect.close();
						%>
							<input type="hidden"  name="numRow" value="<%=i%>">
							<input type="hidden"  id="aRemove" name="aRemove" value="">
						</table>
	        </form>
	        <br><br><br> 					
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

