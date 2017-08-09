
<%@page import="com.mysql.jdbc.StringUtils"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Request</title>


<link rel="stylesheet" href="Datepicker_files/css/jquery.ui.all.css">
<script src="Datepicker_files/jquery-1.8.3.js"></script>
<script src="Datepicker_files/jquery.ui.core.js"></script>
<script src="Datepicker_files/jquery.ui.widget.js"></script>
<script src="Datepicker_files/jquery.ui.datepicker.js"></script>

<script>
$(function(){
	window.prettyPrint && prettyPrint();
	$( "#start_d" ).datepicker({ minDate: -90, maxDate: +60, dateFormat: "yy-mm-dd" }).attr( 'readOnly' , 'readonly');	
	$( "#stop_d" ).datepicker({ minDate: -90, maxDate: +60, dateFormat: "yy-mm-dd" }).attr( 'readOnly' , 'readonly');	
	
	$( "#Rstart_d" ).datepicker({ minDate: -90, maxDate: 0, dateFormat: "yy-mm-dd" }).attr( 'readOnly' , 'readonly');	
	$( "#Rstop_d" ).datepicker({ minDate: "-90d", maxDate: 0, dateFormat: "yy-mm-dd" }).attr( 'readOnly' , 'readonly');	
	
	$("#start_d,#stop_d,#Rstart_d,#Rstop_d").on("contextmenu",function(e){
        return false;
    });
	
});


window.onload=function(){
	var s = sessionStorage.getItem("click");
	//var rowCount = $('#tableRet tr').length;
	
	if(null != s && s == "Y"){
		sessionStorage.setItem("click","");
	}else{
		initValue();
	}

};

</script>

<style type="text/css">
<!--
.style3 {color: #FF0000}
-->
</style>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<script type="text/javascript">
	function initValue(){
//		$("#Rstart_d").attr("value", getYesterdaysDate());
		$("#Rstart_d").attr("value", getTodayDate());
		$("#Rstop_d").attr("value", getTodayDate());
		
//		$("#start_d").attr("value", getYesterdaysDate());
//		$("#stop_d").attr("value", getYesterdaysDate());
		$("#start_d").attr("value", getTodayDate());
		$("#stop_d").attr("value", getTodayDate());
	}

	function doSubmit(cmd){
		var mainForm = document.getElementById("mainForm")
		if(cmd == 'search'){
			if(checkkDateDiff()){
				mainForm.setAttribute("action","request.jsp");
				mainForm.submit();
				sessionStorage.setItem("click","Y");
			}
		}	
	}
	
	function getTodayDate() {
	    var date = new Date();
	    date.setDate(date.getDate());
	    var day = date.getDate();
	    var month = date.getMonth() + 1;
	    var year = date.getFullYear();
		
	    if (month < 10) month = "0" + month;
	    if (day < 10) day = "0" + day;
	    var today = year + "-" + month + "-" + day;  
	    
	    return today;
	}
	
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
	
	function checkkDateDiff(){
		var dateR1 = new Date($("#Rstart_d").val());
		var dateR2 =  new Date($("#Rstop_d").val());
		
		var dateE1 = new Date($("#start_d").val());
		var dateE2 =  new Date($("#stop_d").val());
	
		var one_day = 1000*60*60*24;
		
		// check date sequence - swap if start date is greater than end date 
		if (dateE1 > dateE2) {
			var dateEtemp = dateE1;
			dateE2 = dateE1;
			dateE1 = dateEtemp;
		}
		if (dateR1 > dateR2) {
			var dateRtemp = dateR1;
			dateR2 = dateR1;
			dateR1 = dateRtemp;
		}
		var diffRDate = (dateR2.getTime() - dateR1.getTime()) / one_day;
		var diffEDate = (dateE2.getTime() - dateE1.getTime()) / one_day;
		
		//alert(" diffRDate " + diffRDate + " diffEDate " + diffEDate);
		if (diffEDate > 90){
			alert("สามารถค้นหาได้ไม่เกิน 90 วัน");
			return false;
		}
		else if(diffEDate < 0){
			alert("กรุณาเลือกวันสิ้นสุดของรายการที่เกิดผลให้มากกว่าวันเริ่ม");
			return false;
		}
		return true;
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
		var flage = (input >= 3585 && input <= 3662)||input == 32||(input >= 48 && input <= 57);
		if(!flage){
			alert('กรุณาใส่่ภาษาไทย')
		}
		return flage;
	}
	
	function isENG(input){
		//alert(input)
		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||input == 32||(input >= 48 && input <= 57);
		if(!flage){
			alert('กรุณาใส่่ภาษาอังกฤษ')
		}
		return flage;
	}
	
	function resetTextBox(){
		document.getElementById ( "msg2" ).innerText = '';
		
		$("#EmpID,#Rquestor,#SelectType").val("");
		$("#Status").val("All");
		$("#start_d,#stop_d,#Rstart_d,#Rstop_d").attr("value", getTodayDate());
    	
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
	//System.out.println("value: "+value + " AP " + AP);
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

public String AddTime(String date, int ampm){
	String revert="";
	
	if (!(date == null)) {
		try{ 
			if (ampm==0) {
				revert=date.trim()+" 00:00:00";
			}else{
				if (ampm==1){ 
					revert=date.trim()+" 23:59:59";
				}else{
					System.out.println("Exception: request.jsp:AddTime - ampm parameter out of range");
					revert=date;
				}
			}
		}catch(Exception e){
			System.out.println("Exception: request.jsp:AddTime - Code compilation error");
		}
//		switch(ampm){
//			case 0: {
//				System.out.println("FormValidate:AddTime - [Start] Return: "+revert);
//				break;
//			}
//			case 1: {
//				System.out.println("FormValidate:AddTime - [End] Return: "+revert);
//				break;
//			}
//			default: {
//				System.out.println("FormValidate:AddTime - [Default] Return: "+revert);
//				break;
//			}
//		}
	} else {
		revert="";
	}
	return revert; 
}
%>

<body>
<%
		
		//System.out.println(session.getAttribute( "resultLogin"));
		//response.sendRedirect(request.getContextPath()+"/jsp/main.jsp");
		String module = "";
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
		}else{
			module = session.getAttribute("authen").toString();
			//module = "Administrator";
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
			//System.out.println("module " + module);
		}
		String type = (String)request.getParameter("SelectType");
		String S = (String)request.getParameter("EffStartDateS");
		String E = (String)request.getParameter("EffStartDateE");
		String rqtor = (String)request.getParameter("Rquestor");
		String empID = (String)request.getParameter("EmpID");
		String reqS = (String)request.getParameter("REffStartDateS");
		String reqE = (String)request.getParameter("REffStartDateE");
		
		
		/*
		System.out.println("type " + type );
		System.out.println("S " + S );
		System.out.println("E " + E );
		System.out.println("rqtor " + rqtor );
		System.out.println("empID " + empID );
		System.out.println("reqS " + reqS );
		System.out.println("reqE " + reqE );
		*/
		
		ResultSet resultSet = null;
		ResultSet rsStatus =  null;
		Connection connect = null;
		Connection connectMy = null;
		if(type==null || type.isEmpty()) {
			type="All";
		}
		if(type != null || S != null || E != null || rqtor != null || empID != null || reqS != null || reqE != null ){
			
			PreparedStatement preparedStatement = null;
			  
			connect = DatbaseConnection.getConnectionMySQL();
			String sql = "select * "
					+" from tbldt_reqrepository r "
					+" where 1=1 "// r.EffStartDate > NOW() 
					+" and  r.Requestor like ? and  r.EmpID like ? ";
			if(type.equals("B")) {
				sql = sql + " and  (r.RevBranch like ? and r.EffEndDate between ? and ?)";

			}else{
				if(type.equals("F")){
					sql = sql + " and (r.FwdBranch like ? and r.EffStartDate between ? and ?)";
				}else{
					if(type.equals("All")){
						sql = sql + "and  ((r.FwdBranch like ? and r.EffStartDate between ? and ?) "
								+" or  (r.RevBranch like ? and r.EffEndDate between ? and ?))";
					} else {
						System.out.println("Error: Type Mismatch - Not All,B,F");
					}
				}
			}
			sql = sql +" and (r.ReqSubmitDate between ? and ?) and  r.Status like ? order by r.ReqSubmitDate desc ";
			preparedStatement = connect.prepareStatement(sql);
			
			Locale lc = new Locale("en","US");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd",lc);
			Date curDate = new Date();
			String today = sdf2.format(curDate);
			
			String ESS,ESE,EES,EEE,ReqS,ReqE;
			ReqS=ESS="2000-01-01";
			ReqE=ESE=EEE="2099-12-31";
			EES= today;
					
			if("All".equals(type)){
				ESS=EES=S;
				ESE=EEE=E;
			}else if("F".equals(type)){
				ESS=S;
				ESE=E;
			}else if("B".equals(type)){
				EES=S;
				EEE=E;
			}
			
			if(ESS=="" ){ESS="2000-01-01";}
			if(ESE=="" ){ESE="2099-12-31";}
			if(EES=="" ){EES=today;}
			if(EEE=="" ){EEE="2099-12-31";}		
			
			if(!isNull((String)request.getParameter("REffStartDateS")).equals("")){
				ReqS = (String)request.getParameter("REffStartDateS");
			}
			if(!isNull((String)request.getParameter("REffStartDateE")).equals("")){
				ReqE = (String)request.getParameter("REffStartDateE");
			}			
			
			switch(type){
			case "B":
				preparedStatement.setString(1, "%"+isNull(request.getParameter("Rquestor"))+"%");
				preparedStatement.setString(2, "%"+isNull(request.getParameter("EmpID"))+"%");
				//preparedStatement.setString(3, "%"+isNull(request.getParameter("FwdBranch"))+"%");
				//preparedStatement.setString(4, ESS);
				//preparedStatement.setString(5, ESE);
				preparedStatement.setString(3, "%"+isNull(request.getParameter("RevBranch"))+"%");
				preparedStatement.setString(4, AddTime(S,0));
				preparedStatement.setString(5, AddTime(E,1));
				preparedStatement.setString(8, "%"+isNull(request.getParameter("Status"))+"%");
				preparedStatement.setString(6, AddTime(ReqS,0));
				preparedStatement.setString(7, AddTime(ReqE,1));
				break;
			case "F":
				preparedStatement.setString(1, "%"+isNull(request.getParameter("Rquestor"))+"%");
				preparedStatement.setString(2, "%"+isNull(request.getParameter("EmpID"))+"%");
				preparedStatement.setString(3, "%"+isNull(request.getParameter("FwdBranch"))+"%");
				preparedStatement.setString(4, AddTime(ESS,0));
				preparedStatement.setString(5, AddTime(ESE,1));
				//preparedStatement.setString(6, "%"+isNull(request.getParameter("RevBranch"))+"%");
				//preparedStatement.setString(7, S);
				//preparedStatement.setString(8, E);
				preparedStatement.setString(8, "%"+isNull(request.getParameter("Status"))+"%");
				preparedStatement.setString(6, AddTime(ReqS,0));
				preparedStatement.setString(7, AddTime(ReqE,1));
				break;
			case "All":
				preparedStatement.setString(1, "%"+isNull(request.getParameter("Rquestor"))+"%");
				preparedStatement.setString(2, "%"+isNull(request.getParameter("EmpID"))+"%");
				preparedStatement.setString(3, "%"+isNull(request.getParameter("FwdBranch"))+"%");
				preparedStatement.setString(4, AddTime(ESS,0));
				preparedStatement.setString(5, AddTime(ESE,1));
				preparedStatement.setString(6, "%"+isNull(request.getParameter("RevBranch"))+"%");
				preparedStatement.setString(7, AddTime(S,0));
				preparedStatement.setString(8, AddTime(E,1));
				preparedStatement.setString(11, "%"+isNull(request.getParameter("Status"))+"%");
				preparedStatement.setString(9, AddTime(ReqS,0));
				preparedStatement.setString(10, AddTime(ReqE,1));
				break;
			default:
				System.out.println("Exception: request page - Default in switch command");
				break;
			}

//			System.out.println("Type = "+type);
//			System.out.println("SQL = " + sql);
//			System.out.println("sql " + "%"+isNull(request.getParameter("Rquestor"))+"%" );
//			System.out.println("1 " + "%"+isNull(request.getParameter("Rquestor"))+"%" );
//			System.out.println("2 " + "%"+isNull(request.getParameter("EmpID"))+"%" );
//			System.out.println("3 " + "%"+isNull(request.getParameter("FwdBranch"))+"%" );
//			System.out.println("4 " + AddTime(ESS,0));
//			System.out.println("5 " + AddTime(ESE,1));
//			System.out.println("6 " + "%"+isNull(request.getParameter("RevBranch"))+"%" );
//			System.out.println("7 " + AddTime(EES,0));
//			System.out.println("8 " + AddTime(EEE,1));
//			System.out.println("9 " + "%"+isNull(request.getParameter("Status"))+"%" );
//			System.out.println("10 " + AddTime(ReqS,0));
//			System.out.println("11 " + AddTime(ReqE,1));
			System.out.println("request.jsp:Main - Executing SQL - "+preparedStatement);
			
			
//	 		System.out.println(preparedStatement);
//		    resultSet = preparedStatement.executeQuery();
			try {
				resultSet = preparedStatement.executeQuery();
			} catch(Exception e) {
				System.out.println("SQL Error - query for request : request page");
				//out.println("<br>SQL Error - query for request:request page<br>");
			}
			out.flush();
			
////////////////////////////////////////////////////////////////////////////////////////////////////////			
		} 
		connectMy = DatbaseConnection.getConnectionMySQL();
		String sqlStatus = "select DISTINCT r.Status from tbldt_reqrepository r order by r.Status";
		PreparedStatement preparedStatementMy = connectMy.prepareStatement(sqlStatus);
		rsStatus = preparedStatementMy.executeQuery();
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
    
		<form id="mainForm" method="post" action="" >
		    <input type="hidden" id="RequestID" name="RequestID" value="">
		    <input type="hidden" id="TypeAdd" name="TypeAdd" value="">
		    <input type="hidden" id="hid" name="hid" value="">
		    <table border="0" cellpadding="0" cellspacing="0" class="tbl_search">
        	<tr>
            	<td width="30">&nbsp;</td>
                <td align="right">ผู้ขอ</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="Rquestor" name="Rquestor" value="<%=isNull(request.getParameter("Rquestor"))%>" onkeypress='return isNumber(event.charCode)' /></td>
                
                <td align="right">รหัสพนักงาน</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="EmpID" name="EmpID" value="<%=isNull(request.getParameter("EmpID"))%>" onkeypress='return isNumber(event.charCode)' /></td>
            	
            	<td width="30">&nbsp;</td>
                <td align="right">สถานะ</td>
                <td width="5">&nbsp;</td>
                <td align="left">
                       
                	<select class="txt1" id="Status"  name="Status" size="1" >
					<option value="">All</option>
					<% 
						if(null != rsStatus){
							while(rsStatus.next()){
								String rsS = rsStatus.getString("Status");
								String rsName = "";
								switch(rsS){
								case "A": 
									rsName = "Approved";
									break;
								case "N": 
									rsName = "New";
									break;
								case "R":
									rsName = "Rejected";
									break;
								default:
									rsName = rsS;
									break;	
								}
								out.println("<option value=\""+rsS+"\""+getSelect(isNull(request.getParameter("Status")),rsS)+" >"+rsName+"</option>");											
							}	
						}
						
					%>
					</select>
                </td>
  			
  			</tr><tr><td>&nbsp;</td></tr><tr > 
            	</tr><tr><td width="10">&nbsp;</td><td colspan="3" class="tbl_row3" ><b>วันที่ขอรายการ</b></td></tr><tr > 
                <td width="10">&nbsp;</td>
                <td align="right">วันเริ่ม</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="Rstart_d" name="REffStartDateS" value="<%=isNull(request.getParameter("REffStartDateS"))%>" /></td>
				<td align="right">วันสิ้นสุด</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="Rstop_d" name="REffStartDateE" value="<%=isNull(request.getParameter("REffStartDateE"))%>" /></td>
                <td width="5">&nbsp;</td>
            </tr>
  			
            </tr><tr><td>&nbsp;</td></tr><tr > 
            	</tr><tr><td width="10">&nbsp;</td><td colspan="3" class="tbl_row3" ><b>วันที่รายการเกิดผล</b></td></tr><tr > 
                <td width="10">&nbsp;</td>
                <td align="right">วันเริ่ม</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="start_d" name="EffStartDateS" value="<%=isNull(request.getParameter("EffStartDateS"))%>" /></td>
				<td align="right">วันสิ้นสุด</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="20" id="stop_d" name="EffStartDateE" value="<%=isNull(request.getParameter("EffStartDateE"))%>" /></td>
                <td width="5">&nbsp;</td>
                <td align="right">ชนิด</td>
                <td width="5">&nbsp;</td>
		        <td align="left" valign="middle" >
		             <select  name="SelectType" id="SelectType" size="1" class="txt1" >
			          <option value="F" <%=getSelect(isNull(request.getParameter("SelectType")),"F") %>>Forward</option>
			          <option value="B" <%=getSelect(isNull(request.getParameter("SelectType")),"B") %>>Backward</option>
			          <option value="" <%=getSelect(isNull(request.getParameter("SelectType")),"") %> >All</option>
		            </select>
		        </td>
		        <td width="20">&nbsp;</td>
		        <td align="left">
			        <input type="button" id="btSearch" value="Search" class="btn1" onClick="doSubmit('search')"/>
			        <input type="button" id="btReset" value="Reset" class="btn1" onClick="resetTextBox()"/>
		        </td>
		                              
            </tr>
            <tr>
            	<td width="20">&nbsp;</td>
            	<td colspan="15" align="center" height="35">
<!--             		<input type="button" id="btSearch" value="Search" class="btn1" onClick="doSubmit('search')"/> -->
<!--             		<input type="button" id="btReset" value="Reset" class="btn1" onClick="resetTextBox()"/> -->
            	</td>
            </tr>
        </table>
        <span id="msg2" class="style3"><% 	
		
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
		
							session.removeAttribute("success");
						%></span>
      </form>
      
            <table border="0" id="tableRet" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
        	<!-- หัวตาราง -->
        	<tr>
            	<td class="header_tbl_content">วันที่ขอ</td>
                <td class="header_tbl_content">ผู้ขอ</td>
                <td class="header_tbl_content">รหัสพนักงาน</td>
                <td class="header_tbl_content">ชื่อ</td>
                <td class="header_tbl_content">Corp.Title</td>
                <td class="header_tbl_content">ตำแหน่งตาม HR</td>
                <td class="header_tbl_content">ตำแหน่งRBFront</td>
                <td class="header_tbl_content">Limit</td>
                <td class="header_tbl_content">สาขาปัจจุบัน</td>
                <td class="header_tbl_content">สถานะ</td>
            </tr>
           
            <!-- หัวตาราง -->
            
            	<%
            	if(resultSet != null){
            	//if (resultSet.getFetchSize() > 0){
            		  int i =0;
				  	  while(resultSet.next() ) {
					  		i++;
						  	String curPos = resultSet.getString("CurPosition");
						  	String curLimit = resultSet.getString("CurLimit");
						  	if(curPos==null){ 
						  		curPos="-";
						  		}
						  	if(curLimit==null){ 
						  		curLimit="-";
						  		}

						    out.println("<input type=\"hidden\" id=\"refNo"+i+"\" value=\""+resultSet.getString("RequestID")+"\">");		
						    out.println("<tr><td class=\"tbl_row9\" >"+resultSet.getString("ReqSubmitDate").substring(0,19)+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("Requestor")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("EmpID")+"</td>");
						  	out.println("<td class=\"tbl_row9\" style=\"text-align:left;\" id=\""+i+"\" >"+resultSet.getString("EmpName")+"</td>");
						  	out.println("<td class=\"tbl_row9\" style=\"text-align:left;\" id=\""+i+"\" >"+resultSet.getString("CorpTitleName")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("JobTitleName")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+curPos+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+curLimit+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("CurBranch")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("Status")+"</td>");
						  	
						  	out.println("</tr><tr><td></td><td></td><td></td><td></td><td style=\"text-align:right;\" >forward</td>");
						  	out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("EffStartDate")+"</td>");							  	
						  	out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdPosition")+"</td>");
						  	out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdLimit")+"</td>");
						  	out.println("<td class=\"tbl_row1\" id=\""+i+"\" >"+resultSet.getString("FwdBranch")+"</td>");
	//					  	out.println("<td class=\"tbl_row1\" ><img src=\"imgs/add_icon.png\" style=\"border:0;\" height=\"24\" title=\"Add\" onClick=\"doSubmit('addF"+i+"')\" /></td>");
						  	
						  	out.println("</tr><tr><td></td><td></td><td></td><td></td><td style=\"text-align:right;\" >backward</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("EffEndDate")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("RevPosition")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("RevLimit")+"</td>");
						  	out.println("<td class=\"tbl_row9\" id=\""+i+"\" >"+resultSet.getString("RevBranch")+"</td>");
						  	
						  	
						  	out.println("<tr class='border_bottom'><td></td><td></td><td></td><td></td><td></td><td></td>");
						  	out.println("<td ></td>");
						  	out.println("<td ></td>");
						  	out.println("<td ></td>");
						  	out.println("<td ></td></tr>");
						  	
	//					  	out.println("<td class=\"tbl_row1\" >"+
	//					  	"<img src=\"imgs/add_icon.png\" style=\"border:0;\" height=\"24\" title=\"Add\" onClick=\"doSubmit('addR"+i+"')\" />"
	//					  	+"</td></tr>");
						  }
					connect.close();
					connectMy.close();
					} 
					
            				  
				%>
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
	var ac = document.getElementById("request");
	ac.setAttribute("class", "active");
</script>