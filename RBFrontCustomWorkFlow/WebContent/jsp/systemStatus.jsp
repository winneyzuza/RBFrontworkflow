<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.Configuration" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>System Status</title>
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
		if(cmd == 'search'){
			mainForm.setAttribute("action","branchSearch.jsp");
			mainForm.submit();
		}else{		
			if(confirm("ยืนยันทำรายการ")){
				document.getElementById("Submit1").disabled = true;
				document.getElementById("Submit2").disabled = true;
				document.getElementById("Submit3").disabled = true;
				document.getElementById("Submit4").disabled = true;
				document.getElementById("reset").disabled = true;			
				
				
				if(cmd == 'addFile'){
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","branchAddFile.jsp");
					mainForm.submit();
				}else if(cmd == 'add'){
					mainForm.setAttribute("action","branchAdd.jsp");
					mainForm.submit();
				}else if(cmd == 'edit'){
					mainForm.setAttribute("action","branchEdit.jsp");
					mainForm.submit();
				}else if(cmd.substring(0,6)=='remove'){
					var i = cmd.substring(6)
					//alert(i)
					//alert(document.getElementById ( "OrgCode"+i ).innerText)
					document.getElementById("BRemove").value = document.getElementById ( "OrgCode"+i ).innerText;
					//alert(document.getElementById("BRemove").value);
					mainForm.setAttribute("action","branchRemove.jsp");
					mainForm.submit();
				}
			}
		}	
	}
	
	function isNumber(input){

		var flage = (input >= 48 && input <= 57);
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}else{
			document.getElementById("Submit2").disabled = false ;
		}
		return flage;
	}
	
	function isNumber2(input){
		//alert(input)
		var flage = (input >= 48 && input <= 57)||input == 46;
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}
		return flage;
	}
	
	function isTHAI(input){
		//alert(input)
		var flage = (input >= 3585 && input <= 3662)||input == 32;
		if(!flage){
			alert('กรุณาใส่่ภาษาไทย')
		}
		return flage;
	}
	
	function isENG(input){
		//alert(input)
		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||input == 32;
		if(!flage){
			alert('กรุณาใส่่ภาษาอังกฤษ')
		}
		return flage;
	}
	
	function inputField(i){

		//alert(document.getElementById ( "SelectMetropolitan"+i ).innerText);
		document.getElementById("OrgCode").value = document.getElementById ( "OrgCode"+i ).innerText;
		document.getElementById("OcCode").value = document.getElementById ( "OcCode"+i ).innerText;
		document.getElementById("BNameTH").value = document.getElementById ( "BNameTH"+i ).innerText;
		document.getElementById("BNameEN").value = document.getElementById ( "BNameEN"+i ).innerText;
		document.getElementById("OperationDay").value = document.getElementById ( "OperationDay"+i ).innerText;
		document.getElementById("CounterLimit1").value = document.getElementById ( "CounterLimit1"+i ).innerText;
		document.getElementById("CounterLimit2").value = document.getElementById ( "CounterLimit2"+i ).innerText;
		document.getElementById("SelectMetropolitan").value = document.getElementById ( "Metropolitan"+i ).innerText;
		document.getElementById("SelectABM").value = document.getElementById ( "ABM"+i ).innerText;
		
		document.getElementById("Submit4").disabled = false ;

	}
	
	function resetTextBox(){
		document.getElementById ( "msg1" ).innerText = '';
		document.getElementById ( "msg2" ).innerText = '';
		
		document.getElementById("OrgCode").value = '';
		document.getElementById("OcCode").value = '';
		document.getElementById("BNameTH").value = '';
		document.getElementById("BNameEN").value = '';
		document.getElementById("OperationDay").value = '';
		document.getElementById("CounterLimit1").value = '';
		document.getElementById("CounterLimit2").value = '';
		document.getElementById("SelectMetropolitan").value = '';
		document.getElementById("SelectABM").value = '';
	}
</script>

<%!
public String isNull(String s){
	//System.out.println("s:"+s);
	if(s==null){
		return "";
	}else{
		return s;
	}
		
}

public String getSelect(String value,String AP){
	//System.out.println("value:"+value);
	if(value!=null){
		if(value.equals(AP))
			return "selected";	
	}
	return "";
} 

public String getCheckBox(String Y){
// 	System.out.println(Y);
	if("Y".equals(Y))
		return "checked=\"checked\"";
	return "";
}

%>

<body>
<%
		
		//System.out.println(session.getAttribute( "resultLogin"));
		//response.sendRedirect(request.getContextPath()+"/jsp/main.jsp");
		
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
		}else{
			String module = session.getAttribute("authen").toString();

			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
		}
		Object userObject = session.getAttribute("userModify");
		//System.out.println("user " + userObject);
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		String sql = 
				"(select PKey,sysdate() DD,t.Module,t.Desc,t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('ManualUploadBranch') "+
				"order by t.DateTime desc "+
				"limit 1)"+
				"union "+		
				"(select PKey,sysdate() DD,t.Module,t.Desc,t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('ManualUploadUserlist3') "+
				"order by t.DateTime desc "+
				"limit 1)"+
				"union "+
				"(select PKey,sysdate() DD,t.Module,t.Desc,t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('ManualUploadExam') "+
				"order by t.DateTime desc "+
				"limit 1)"+
				"union "+
				"(select PKey,sysdate(),t.Module,CONCAT(t.Desc, ' by user: ', t.usermodified),t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('ManualMakeOutput1') "+
				"order by t.DateTime desc "+
				"limit 1) "+
				"union "+				
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('AutoUploadUserlist3') "+
				"order by t.DateTime desc "+
				"limit 1) "+
				"union "+
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime from tbldt_auditlog t "+
				"where t.Module in ('AutoMakeOutput1') "+
				"order by t.DateTime desc "+
				"limit 1) "+
				"union "+
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime ed from tbldt_auditlog t "+
				"where t.Status like 'E' and t.DateTime between CURDATE() and DATE_ADD(CURDATE(),INTERVAL 1 DAY) "+
				"order by t.DateTime desc) "+
				"union "+
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime ed from tbldt_auditlog t "+
				"where t.Status like 'EU' and t.DateTime between DATE_ADD(CURDATE(),INTERVAL -1 DAY) and DATE_ADD(CURDATE(),INTERVAL 1 DAY) "+
				"order by t.DateTime desc) "+
				"union "+
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime ed from tbldt_auditlog t "+
				"where t.Status like 'EE' and t.DateTime between DATE_ADD(CURDATE(),INTERVAL -1 DAY) and DATE_ADD(CURDATE(),INTERVAL 1 DAY) "+
				"order by t.DateTime desc) "+
				"union "+
				"(select PKey,sysdate(),t.Module,t.Desc,t.Status,t.DateTime ed from tbldt_auditlog t "+
				"where t.Status like 'ET' and t.DateTime between DATE_ADD(CURDATE(),INTERVAL -1 DAY) and DATE_ADD(CURDATE(),INTERVAL 1 DAY) "+
				"order by t.DateTime desc) ";
		
		preparedStatement = connect
		          .prepareStatement(sql);
//  		System.out.println(preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();

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
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        	<tr>
            	<td align="center">
                	<table border="0" cellpadding="0" cellspacing="0" width="80%" class="tbl_login">
                	<%  int i =0;             	
							  %><tr><td class="header_tbl_content" align="right">function &nbsp;  &nbsp;</td><td class="header_tbl_content" align="left" >รายละเอียด</td><td class="header_tbl_content" >เวลาครั้งล่าสุด</td></tr>
							    <tr><td align="right">@Version&nbsp;: &nbsp;</td><td>RBFront Custom Workflow 2.6l (ABM Update + Bug Fix - Approver replace)</td><td>2017-07-27</td></tr>
							  <%	
							  boolean first = true;
							  boolean err = true;
							  while(resultSet.next()){
								  String module = resultSet.getString("Module");
								  String desc   = resultSet.getString("Desc");
								  String datetime = resultSet.getString("DateTime");
								  String status   = resultSet.getString("Status");
								  
								if(first){
								   // out.println("<tr><td align=\"right\">เวลาในระบบ	&nbsp; : &nbsp; </td>");
								  //	out.println("<td  >"+resultSet.getString("DD")+"</td></tr>");
								  	first = false;
								}
								if((status.indexOf("E")>-1) && err){
									out.println("<tr><td></td><td><span class=\"style3\"> จำนวนรายการ Error ในวันนี้ </span></td></tr>");
									err = false;
								}
								
									out.println("<tr><td align=\"right\">"+resultSet.getString("Module")+"&nbsp; :	&nbsp;</td>");
								  	out.println("<td  >"+resultSet.getString("Desc")+"</td>");
								  	out.println("<td  >"+resultSet.getString("DateTime")+"</td></tr>");
								
							  	
							  }
						connect.close();
				%>
                    	
                    </table>
                </td>
            </tr>
        </table>
        <br><br>
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
	var ac = document.getElementById("systemStatus");
	ac.setAttribute("class", "active");
</script>
