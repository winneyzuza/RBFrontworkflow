<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="servlet.MakeOutput1" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Check, Adjust and Approve RBFront</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>

<link rel="stylesheet" href="Datepicker_files/css/jquery.ui.all.css" />
<script src="Datepicker_files/jquery-1.8.3.js"></script>
<script src="Datepicker_files/jquery.ui.core.js"></script>
<script src="Datepicker_files/jquery.ui.widget.js"></script>
<script src="Datepicker_files/jquery.ui.datepicker.js"></script>

<script>
$(function(){
	window.prettyPrint && prettyPrint();
	$( "#start_d" ).datepicker({ minDate: 0, maxDate: "30", dateFormat: "yy-mm-dd"}).attr( 'readOnly' , 'readonly');	
	$( "#stop_d" ).datepicker({ minDate: 0, maxDate: "30", dateFormat: "yy-mm-dd"}).attr( 'readOnly' , 'readonly');

	$("#start_d,#stop_d").on("contextmenu",function(e){
        return false;
    });	


});

//window.onload=function(){
$(document).ready(function(){
	var s = sessionStorage.getItem("click");
	//var rowCount = $('#tableRet tr').length;
	//var t = $("#stop_d").attr("value").toString();
	var t = document.getElementById("stop_d").value;
	t = t.trim();
	if(null != s && s == "Y"){
		sessionStorage.setItem("click","");
//		$("#start_d").attr("value", $("#hstart_d")); 
//		$("#stop_d").attr("value", $("#hstop_d")); 
	}else{
		if(t.length == 0) {
			initValue();	
		}
	}

});

</script>
<script>
function getYesterdaysDate() {
    var date = new Date();
    date.setDate(date.getDate()-1);
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
	
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    var yesterday = year + "-" + month + "-" + day;  
    
    return yesterday;
}

function getNextDate(){
	var date = new Date();
	date.setDate(date.getDate()+1);
	var day = date.getDate();
    var month = date.getMonth()+1;
    var year = date.getFullYear();
	day = date.getDate();
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    var tomorrow = year + "-" + month + "-" + day;  
    
    return tomorrow;
	
}

function toggleCheckbox(element)
{
  if("N" == element.value){
	  element.checked = "checked";
	  element.value = "Y";
  }else{
	  element.checked = "";
	  element.value = "N";
  }
  //alert(element.value);
}

//$(document).ready(function() {
   // $("#start_d").attr("value", getYesterdaysDate());
   //$("#stop_d").attr("value", getYesterdaysDate());
	
//});

function initValue(){
	$("#start_d").attr("value", getNextDate()); 
	$("#stop_d").attr("value", getNextDate()); 
}
</script>
<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		if(cmd == 'search'){
			mainForm.setAttribute("action","approveRBFrontRequest.jsp");
			mainForm.submit();
			sessionStorage.setItem("click","Y");
		}else if(cmd == 'reset'){
			mainForm.setAttribute("action","approveRBFrontRequest.jsp");
			initValue();
			document.setElementById("stop_d").value = "";
			// mainForm.submit();
			window.location("approveRBFrontRequest.jsp");
			sessionStorage.setItem("click","");
		}else if(cmd == 'closejob'){
			mainForm.setAttribute("action","checkFile2.jsp");
			mainForm.submit();
		}else{
			if(confirm("ยืนยันทำรายการ")){
				if(cmd == 'modify'){
					mainForm.setAttribute("action","approveModify.jsp");
					mainForm.submit();
				}else if(cmd == 'exportFile'){
					mainForm.setAttribute("action","approveExportFile.jsp");
					var start_d = $("#start_d").val();
					var stop_d = $("#stop_d").val();
					var selectType = $("#SelectType").val();
					var selectComplete = $("#SelectComplete").val();
					
					$("#hstart_d").attr("value", start_d);
					$("#hstop_d").attr("value", stop_d);
					$("#hselectType").attr("value", selectType);
					$("#hselectComplete").attr("value", selectComplete);
					
					mainForm.submit();
				}
			}
		}
			
	}
	
	function checkAll(){
		var numRow = document.getElementById("numRow").value;
		
		if(document.getElementById("CheckBoxCompleteAll").checked){
			for(var i=1;i<=numRow;i++){
				document.getElementById("CheckBoxComplete"+i).checked = true;
			}
		}else{
			for(var i=1;i<=numRow;i++){
				document.getElementById("CheckBoxComplete"+i).checked = false;
			}
		}
		
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
	public String checkRed(String term){
	
		if("".equals(term.trim()) ){
			return "bgcolor=\"#FF2F2F\"";//f3defc
		}
		return "";
	}

	public String checkRed2(String term){
		
		if("".equals(term.trim()) ){
			return "style=\"background-color:#FF2F2F\"";
		}
		return "";
	}
	
	public String getCheckBox(String Y){
		if("Y".equals(Y) || "C".equals(Y))
			return "checked=\"checked\"";
		return "";
	}

	public String getSelect(String value,String AP){
		//System.out.println("value:"+value);
		if(value!=null){
			if(value.equals(AP))
				return "selected";	
		}
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
		/*
		Connection connect = null;
		PreparedStatement preparedStatement = null;
  
		connect = DatbaseConnection.getConnectionMySQL();
			 String sql = "select * from tbldt_output1 t order by t.CurBr,t.UserID,t.NewTermID";
			preparedStatement = connect.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
		*/
		
		String sDate = request.getParameter("EffStartDateS");
		String eDate = request.getParameter("EffStartDateE");
		String type = request.getParameter("SelectType");
		String complete = request.getParameter("SelectComplete");
		System.out.println("approveRBFrontRequest: Data check sDate=" + sDate + "- eDate=" + eDate + "- type="+ type + "- complete=" + complete);
		//System.out.println(sDate+eDate+type);
		Connection connect = null;
		ResultSet resultSet = null;
		Date oldDate = new Date();
		Date str_Date = new Date (oldDate.getTime()+(24*60*60*1000));
		SimpleDateFormat objFormatter = new SimpleDateFormat("dd-MM-yyyy");
		if (sDate==null) sDate=objFormatter.format(str_Date);
		if (eDate==null) eDate=objFormatter.format(str_Date);
		if (type==null) type="A";
		if (complete==null) complete="%";
				
		if(sDate != null || eDate != null || type != null || complete != null ){
			MakeOutput1 op1 = null;
			if (request.getParameter("attrib") != null) {
				if (request.getParameter("attrib").equals("update")) {
					System.out.println("approveRBFrontRequest.jsp: Back from UPDATE function");
					System.out.println("approveRBFrontRequest.jsp: sDate=" + request.getParameter("EffStartDateS"));
					System.out.println("approveRBFrontRequest.jsp: eDate=" + request.getParameter("EffStartDateE"));
				} else {
					System.out.println("approveRBFrontRequest.jsp: Found attrib parameter but it is not UPDATE");
				}
			} else {
				// attrib parameter not found - process new search result;
				op1 = new MakeOutput1();
				op1.startProcessOutput2(sDate,eDate,type,complete);
			}
			
			PreparedStatement preparedStatement = null;
	  
			connect = DatbaseConnection.getConnectionMySQL();
			String sql = "select * from tbldt_output2 t order by t.CurBr,t.UserID,t.NewTermID";
			preparedStatement = connect.prepareStatement(sql);
	
			resultSet = preparedStatement.executeQuery();
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
    	<ul id=menu >
        </ul>
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   
    <!-- Main Content -->
    <div id="main_content">          
		<form id="mainForm" method="post" action=""> 
			<table id="tableRet" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_search">
            	
            <tr> 
            	
                <td width="10">&nbsp;</td>
                <td align="right">วันเริ่ม</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="start_d" name="EffStartDateS" value="<%=isNull(request.getParameter("EffStartDateS")) %>" /></td>
				<td align="right">วันสิ้นสุด</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="stop_d" name="EffStartDateE" value="<%=isNull(request.getParameter("EffStartDateE")) %>" /></td>
                
                <td width="10">&nbsp;</td>
                <td align="right">ชนิด</td>
                <td width="5">&nbsp;</td>
		        <td align="left">
		             <select class="txt1" name="SelectType" id="SelectType">
		              <option value="A" <%=getSelect(isNull(request.getParameter("SelectType")),"A") %>>All</option>
			          <option value="F" <%=getSelect(isNull(request.getParameter("SelectType")),"F") %>>Forward</option>
			          <option value="B" <%=getSelect(isNull(request.getParameter("SelectType")),"B") %>>Backward</option>	
		            </select>
		        </td>
		        
		        <td width="10">&nbsp;</td>
                <td align="right">เสร็จสมบูรณ์:</td>
                <td width="5">&nbsp;</td>
		        <td align="left">
		        	<select class="txt1" name="SelectComplete" id="SelectComplete">
		              <option value="%" <%=getSelect(isNull(request.getParameter("SelectComplete")),"%") %>>All</option>
			          <option value="Y" <%=getSelect(isNull(request.getParameter("SelectComplete")),"Y") %>>Yes</option>
			          <option value="N" <%=getSelect(isNull(request.getParameter("SelectComplete")),"N") %>>No</option>	
		            </select>
		        </td>
		        
		        <td align="left">
		        <input type="button" id="btSearch" value="Search" class="btn1" onClick="doSubmit('search')"/>
		        <input type="button" id="btReset" value="Reset" class="btn1" onClick="doSubmit('reset')"/>
		        </td>
		                              
            </tr>
            <tr>
            	<td width="20"><img src="imgs/spacer.gif" style="border:0;" /></td>
            	<td colspan="15" align="center" height="35">
            		
<!--             		<input type="button" id="btReset" value="Reset" class="btn1" onClick="resetTextBox()"/> -->
            	</td>
            </tr>
        </table>
        <br>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
			  <tr>
			    <td bgcolor="#f3defc" width="80%"><div align="left">
				
					
					<%
					if( "as".equals(session.getAttribute("module")) )
						out.println("แก้ไขรายการเรียบร้อย<br>");
					if( "ae".equals(session.getAttribute("module")) )
						out.println("แก้ไขรายการมีปัญหา<br>");
					if( "cjs".equals(session.getAttribute("module")) )
						out.println("Closed Job with users " + session.getAttribute("userIDs")+"<br>");
					if( "cje".equals(session.getAttribute("module")) )
						out.println("Closed Job error<br>");
					
					session.removeAttribute("module");
					%>
					<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
					  <tr>
						<td class="header_tbl_content"><strong>Emp ID</strong></td>
						<td class="header_tbl_content"><strong>Current Branch</strong></td>
						<td class="header_tbl_content"><strong>Current Position</strong></td>
						<td class="header_tbl_content"><strong>Limit</strong></td>
						<td class="header_tbl_content"><strong>Terminal ID</strong></td>
						<td class="header_tbl_content"><strong>Action</strong></td>
						<td class="header_tbl_content"><strong>New Branch</strong></td>
						<td class="header_tbl_content"><strong>New Position</strong></td>
						<td class="header_tbl_content"><strong>New Limit</strong></td>
						<td class="header_tbl_content"><strong>New Terminal</strong></td>
						<td class="header_tbl_content"><strong>Mode</strong></td>
						<td class="header_tbl_content_last"><strong>Complete</strong>
						<input class="txt1" id="CheckBoxCompleteAll" type="checkbox" onclick="checkAll()" />
						</td>
						<!-- <td class="header_tbl_content_last"><strong>Checked</strong></td> -->
					  </tr>
					  		<%  int i =0;
					  		if(null != resultSet){
					  			if(!read){

								  	  while(resultSet.next() )
									  { i++;
									    %><input type="hidden"  name="reqID<%=i %>" value="<%=resultSet.getString("RequestID")%>"><%
									    //style=\"background-color:#f3defc\" 		
									    //out.println("<tr "+checkRed(resultSet.getString("NewTermID"))+" ><td class=\"tbl_row1\" ><input class=\"txt1\" name=\"UserID"+i+"\" type=\"text\" value=\""+resultSet.getString("UserID")+"\" size=\"10\" readonly /></td>");
									    out.println("<tr><td class=\"tbl_row1\" ><input class=\"txt1\" name=\"UserID"+i+"\" type=\"text\" value=\""+resultSet.getString("UserID")+"\" size=\"10\" readonly /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"CurBr"+i+"\" type=\"text\" value=\""+resultSet.getString("CurBr")+"\" size=\"10\" readonly /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Curpos"+i+"\" type=\"text\" value=\""+resultSet.getString("Curpos")+"\" size=\"10\" readonly /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Limit"+i+"\" type=\"text\" value=\""+resultSet.getString("CurLimit")+"\" size=\"10\" readonly /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"TermID"+i+"\" type=\"text\" value=\""+resultSet.getString("TermID")+"\" size=\"10\" readonly /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" name=\"Action"+i+"\" type=\"text\" value=\""+resultSet.getString("Action")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" name=\"NewBr"+i+"\" type=\"text\" value=\""+resultSet.getString("NewBr")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" name=\"NewPos"+i+"\" type=\"text\" value=\""+resultSet.getString("NewPos")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" name=\"NewLimit"+i+"\" type=\"text\" value=\""+resultSet.getString("NewLimit")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input "+checkRed2(resultSet.getString("NewTermID"))+" class=\"txt1\" name=\"NewTermID"+i+"\" type=\"text\" value=\""+resultSet.getString("NewTermID")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" name=\"Mode"+i+"\" type=\"text\" value=\""+resultSet.getString("ModeOutput")+"\" size=\"10\"  /></td>");
									  	out.println("<td class=\"tbl_row1\" align=\"center\" ><input class=\"txt1\" id=\"CheckBoxComplete"+i+"\" name=\"CheckBoxComplete"+i+"\" type=\"checkbox\" value=\"Y\" "+getCheckBox(resultSet.getString("Complete"))+" /></td>");
									  	
									  	/* out.println("<td class='tbl_row1'" + ">"+ 
									  			"<input class='txt1'" + "style='background-color:#f3defc'"+ "name=Checked"+i+ " "+ "value="+resultSet.getString("Checked") + " type=checkbox"  + " "+ ("Y".equals(resultSet.getString("Checked"))?"checked": "") + " " + "onclick=toggleCheckbox(this)" +  ">" 
									  			+"</td></tr>");	 */
									  	
									  }
					  				
					  			}else{
									  while(resultSet.next() )
										  { i++;
										    %><input type="hidden"  name="reqID<%=i %>" value="<%=resultSet.getString("RequestID")%>"><%
										    // tbldt_output1 use reqID not RequestID column
										    //out.println("<tr "+checkRed(resultSet.getString("NewTermID"))+" ><td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"UserID"+i+"\" type=\"text\" value=\""+resultSet.getString("UserID")+"\" size=\"10\" readonly /></td>");
										    out.println("<tr><td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"UserID"+i+"\" type=\"text\" value=\""+resultSet.getString("UserID")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"CurBr"+i+"\" type=\"text\" value=\""+resultSet.getString("CurBr")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Curpos"+i+"\" type=\"text\" value=\""+resultSet.getString("Curpos")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Limit"+i+"\" type=\"text\" value=\""+resultSet.getString("CurLimit")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"TermID"+i+"\" type=\"text\" value=\""+resultSet.getString("TermID")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Action"+i+"\" type=\"text\" value=\""+resultSet.getString("Action")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"NewBr"+i+"\" type=\"text\" value=\""+resultSet.getString("NewBr")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"NewPos"+i+"\" type=\"text\" value=\""+resultSet.getString("NewPos")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"NewLimit"+i+"\" type=\"text\" value=\""+resultSet.getString("NewLimit")+"\" size=\"10\" readonly /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input "+checkRed2(resultSet.getString("NewTermID"))+" class=\"txt1\" name=\"NewTermID"+i+"\" type=\"text\" value=\""+resultSet.getString("NewTermID")+"\" size=\"10\"  /></td>");
										  	out.println("<td class=\"tbl_row1\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Mode"+i+"\" type=\"text\" value=\""+resultSet.getString("ModeOutput")+"\" size=\"10\" readonly  /></td>");
										//  	out.println("<td class=\"tbl_row1\" align=\"center\" ><input class=\"txt1\" style=\"background-color:#f3defc\" name=\"Mode"+i+"\" type=\"text\" value=\""+resultSet.getString("Complete")+"\" size=\"10\" readonly  /></td>");
											out.println("<td class=\"tbl_row1\" align=\"center\" ><input class=\"txt1\" id=\"CheckBoxComplete"+i+"\" name=\"CheckBoxComplete"+i+"\" type=\"checkbox\" value=\"Y\" "+getCheckBox(resultSet.getString("Complete"))+" /></td>");
										  	/* out.println("<td class='tbl_row1'" + ">"+ 
										  			"<input class='txt1'" + "style='background-color:#f3defc'"+ "name=Checked"+i+ " "+ "value="+resultSet.getString("Checked") + " type=checkbox"  + " "+ ("Y".equals(resultSet.getString("Checked"))?"checked": "") + " " + "onclick=toggleCheckbox(this)" +  ">" 
										  			+"</td></tr>");		 */							  	
										  }
					  			}
					  		    connect.close();
					  		    }
								  %>
								  <input type="hidden" id="numRow" name="numRow" value="<%=i%>">
								  <input type="hidden" id="hstart_d" name="hstart_d" value="">
								  <input type="hidden" id="hstop_d" name="hstop_d" value="">
								  <input type="hidden" id="hselectType" name="hselectType" value="">
								  <input type="hidden" id="hselectComplete" name="hselectComplete" value="">
					  </input>
					  <tr>
						<td colspan="12">
						  
							<div align="center">
							  <input class="btn1" type="submit" id="modify" name="Submit2" value="Update" onClick="doSubmit('modify')" />							   				  
							</div></td>
						</tr>
					  <tr><td>&nbsp;</td></tr>
					  <tr><td colspan="12">
					  		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_search">
					  		<tr><td><img src="imgs/spacer.gif" style="border:0;" /></td></tr>
					     	<tr><td>
					     	<div align="center">
							  	<input class="btn1" type="submit" id="exportF" name="Submit" value="Generate File" onClick="doSubmit('exportFile')" />   				  
					     	</div>
					     	</td></tr>
					     	<tr><td><img src="imgs/spacer.gif" style="border:0;" /></td>
					  </tr>
					  <tr><td>&nbsp;</td></tr>
					   <tr>
						<td colspan="12">
						  
							<div align="center">
							  <input class="btn1" type="submit" id="closejob" name="Submit3" value="Close Job" onClick="doSubmit('closejob')" />							   				  
							</div>
						</td>
					   </tr>
					     	</table>
					  </td>
					  </tr>
					  <tr><td colspan="12">&nbsp;</td></tr>
					  <tr><td colspan="12">
					  	<div align="center">
					  	<font color="FF0000"><u>คำเตือน</u> การเปลี่ยนเงื่อนไขค้นหา หรือเปลี่ยนเมนู จะส่งผลให้ข้อมูลที่ทำการแก้ไขแล้วสูญหาย</font>
					  	</div>
					  	</td>
					  </tr>
					</table>
					
					
				  </div>
				</td>
			  </tr>
			</table>
			</form>
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
	var ac = document.getElementById("approveRBFrontRequest");
	ac.setAttribute("class", "active");
</script>
