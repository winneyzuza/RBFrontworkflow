<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Check, Adjust & Approve RBFront</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>


<link rel="stylesheet" href="Datepicker_files/css/jquery.ui.all.css">
<script src="Datepicker_files/jquery-1.8.3.js"></script>
<script src="Datepicker_files/jquery.ui.core.js"></script>
<script src="Datepicker_files/jquery.ui.widget.js"></script>
<script src="Datepicker_files/jquery.ui.datepicker.js"></script>
<script>
$(function(){
window.prettyPrint && prettyPrint();
	$( "#start_d" ).datepicker({ minDate: -90, maxDate: 0, dateFormat: "yy-mm-dd"}).attr( 'readOnly' , 'readonly');	
	$( "#stop_d" ).datepicker({ minDate: -90, maxDate: 0, dateFormat: "yy-mm-dd"}).attr( 'readOnly' , 'readonly');	
	
	
	$("#start_d,#stop_d").on("contextmenu",function(e){
        return false;
    });
	
	var rowCount = $('#tableRet tr').length;
	
	if(rowCount == 2){
		$("#start_d").attr("value", getYesterdaysDate(1)); // get today date (0 will get yesterday)
		$("#stop_d").attr("value", getYesterdaysDate(1)); // get today date (0 will get yesterday)
		$("#BranchID").val("");
		$("#EmpID").val("");
	}

});

function getYesterdaysDate(d) {
    var date = new Date();
    if (d == 0) {
    	date.setDate(date.getDate()-1);
    }else{
    	date.setDate(date.getDate());
    }
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
	
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    var yesterday = year + "-" + month + "-" + day;  
    
    return yesterday;
}

	
	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		
		if(cmd == 'search'){
			mainForm.setAttribute("action","inquiry.jsp");
			mainForm.submit();	
		}else {
			if(cmd == 'reset'){
				$("#start_d").attr("value", getYesterdaysDate(1));
	   			$("#stop_d").attr("value", getYesterdaysDate(1));
    			$("#BranchID").val("");
    			$("#EmpID").val("");
			}
		}
			
	}
	
	function isNumber(input){

		var flage = (input >= 48 && input <= 57);
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}
		return flage;
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

	public String checkRed2(String term){
		
		if("".equals(term.trim()) ){
			return "style=\"background-color:#FF2F2F\"";
		}
		return "";
	}
	
	public String getCheckBox(String Y){
		if("Y".equals(Y))
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
		
		String sDate = request.getParameter("EffStartDateS");
		String eDate = request.getParameter("EffStartDateE");
		
		String branchId = request.getParameter("BranchID");
		String empId = request.getParameter("EmpID");
		Connection connect = null;
		
		ResultSet resultSet = null;
		
		System.out.println("sDate " +sDate );
		System.out.println("eDate " +eDate );
		System.out.println("BranchId  " +"%"+isNull(request.getParameter("BranchID"))+"%" );
		System.out.println("EmpId  " +"%"+isNull(request.getParameter("EmpID"))+"%" );
		
		if(sDate != null || eDate != null || branchId != null || empId != null ){
		
			PreparedStatement preparedStatement = null;
			  
			
			connect = DatbaseConnection.getConnectionMySQL();
			String sql = "(select t.EmpID,t.EffStartDate AS EffStartDate,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,(CASE WHEN e.BranchID = t.FwdBranch THEN 'Change' ELSE  'Move' END) AS ActionF,t.FwdBranch,t.FwdPosition,FwdLimit,'newTerminl','Forward' AS Mode ,("
				   		 + "CASE " 
			       		 	+ "WHEN t.Status = 'N' THEN 'new request' "
			       		 	+ "WHEN t.Status = 'A'  THEN 'approved request but not forward process yet' "
			       		 	+ "WHEN t.Status = 'C' THEN 'completed request on both forward & reverse' "
			       		 	+ "WHEN t.Status = 'F' THEN 'forward request processed' "
			       		 	+ "WHEN t.Status = 'R' THEN 'rejected request (not approved)' "
			       		 	+ "ELSE 'NA' "
			    		 + "END) AS Status " 
						 +" from tbldt_reqrepository t"
						 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
						// +" where t.Status like 'A' "
						 +" where t.EffStartDate between ? and ? "
						 +" and e.BranchID like ? and e.EmpID like ?) "
						 +" UNION"
						 +" (select t.EmpID,t.EffEndDate AS EffStartDate,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,(CASE WHEN e.BranchID = t.FwdBranch THEN 'Change' ELSE  'Move' END) AS ActionR,t.RevBranch,t.RevPosition,RevLimit,'newTerminl','Backward' AS Mode,("
						 + "CASE " 
			       		 	+ "WHEN t.Status = 'N' THEN 'new request' "
			       		 	+ "WHEN t.Status = 'A'  THEN 'approved request but not forward process yet' "
			       		 	+ "WHEN t.Status = 'C' THEN 'completed request on both forward & reverse' "
			       		 	+ "WHEN t.Status = 'F' THEN 'forward request processed' "
			       		 	+ "WHEN t.Status = 'R' THEN 'rejected request (not approved)' "
			       		 	+ "ELSE 'NA' "
			    		 + "END) AS Status " 
						  +" from tbldt_reqrepository t"
						 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
						 //+" where t.Status like 'A' "
						 +" where t.EffEndDate between ? and ? "
						 +" and e.BranchID like ? and e.EmpID like ?) ";
				 
			
				String ReqS,ReqE;
				ReqS="2000-01-01";
				ReqE="2099-01-01";
				if(!isNull((String)request.getParameter("EffStartDateS")).equals("")){
					ReqS = (String)request.getParameter("EffStartDateS");
				}
				if(!isNull((String)request.getParameter("EffStartDateE")).equals("")){
					ReqE = (String)request.getParameter("EffStartDateE");
				}	
				
				preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setString(1, ReqS);
				preparedStatement.setString(2, ReqE);
				preparedStatement.setString(3, "%"+isNull(request.getParameter("BranchID"))+"%");
				preparedStatement.setString(4, "%"+isNull(request.getParameter("EmpID"))+"%");
				preparedStatement.setString(5, ReqS);
				preparedStatement.setString(6, ReqE);
				preparedStatement.setString(7, "%"+isNull(request.getParameter("BranchID"))+"%");
				preparedStatement.setString(8, "%"+isNull(request.getParameter("EmpID"))+"%");
	
				resultSet = preparedStatement.executeQuery();
				System.out.println("sql3 " + sql);
		
		
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
    	<ul id=menu>
        </ul>
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   
    <!-- Main Content -->
    <div id="main_content">          
		<form id="mainForm" method="post" action=""> 
			<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_search">
            	
            <tr > 
            	
                <td width="5">&nbsp;</td>
                <td align="right">วันเริ่ม</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="50" id="start_d" name="EffStartDateS" value="<%=isNull(request.getParameter("EffStartDateS"))%>" /></td>
				<td align="right">วันสิ้นสุด</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="50" id="stop_d" name="EffStartDateE" value="<%=isNull(request.getParameter("EffStartDateE"))%>" /></td>
                
                <td width="5">&nbsp;</td>
                <td align="right">รหัสสาขา</td>
                <td width="5">&nbsp;</td>
		        <td align="left"><input type="text" class="txt1" maxlength="5" id="BranchID" name="BranchID" value="<%=isNull(request.getParameter("BranchID"))%>" onkeypress='return isNumber(event.charCode)' /></td>
		        
		        <td width="5">&nbsp;</td>
                <td align="right">รหัสพนักงาน</td>
                <td width="5">&nbsp;</td>
		        <td align="left"><input type="text" class="txt1" maxlength="5" id="EmpID" name="EmpID" value="<%=isNull(request.getParameter("EmpID"))%>" onkeypress='return isNumber(event.charCode)' /></td>
		        
		        <td align="left">
		        <input type="button" id="btSearch" value="Search" class="btn1" onClick="doSubmit('search')"/>
		        <input type="button" id="btReset" value="Reset" class="btn1" onClick="doSubmit('reset')"/>
		        </td>
		                              
            </tr>
            <tr>
            	<td width="20"><img src="imgs/spacer.gif" style="border:0;" /></td>
            	
            </tr>
        </table>
        <br>
			<table id="tableRet" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
			  <tr>
			    <td bgcolor="#f3defc" width="80%"><div align="left">
				
					
					<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
					  <tr>
						<td class="header_tbl_content"><strong>Emp ID</strong></td>
						<td class="header_tbl_content"><strong>Eff Date</strong></td>
						<td class="header_tbl_content"><strong>Current Branch</strong></td>
						<td class="header_tbl_content"><strong>Current Position</strong></td>
						<td class="header_tbl_content"><strong>Current Limit</strong></td>
						<td class="header_tbl_content"><strong>TerminalID</strong></td>
						<td class="header_tbl_content"><strong>Action</strong></td>
						<td class="header_tbl_content"><strong>New Branch</strong></td>
						<td class="header_tbl_content"><strong>New Position</strong></td>
						<td class="header_tbl_content"><strong>New Limit</strong></td>
						<td class="header_tbl_content"><strong>New Terminal</strong></td>
						<td class="header_tbl_content"><strong>Status</strong></td>
					 </tr>
					  		<%  int i =0;
					  			if(null != resultSet){
								  while(resultSet.next() )
									  { i++;
									    %><%
									    out.println("<tr><td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("EmpID")+"</td>");		
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("EffStartDate")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("BranchID")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("TechnicalRole")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("CurLimit")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("TerminalID")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("Mode")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdBranch")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdPosition")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdLimit")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("newTerminl")+"</td>");
									    out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("Status")+"</td>");
									    out.println("</tr>");
									  								  	
									  }
					  			
					  		    connect.close();
								  }%>
								  
					 
					 
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
	var ac = document.getElementById("inquiry");
	ac.setAttribute("class", "active");
</script>
