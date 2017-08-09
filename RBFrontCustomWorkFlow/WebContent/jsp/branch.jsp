<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Branch Information</title>
<style type="text/css">
.style3 {color: #FF0000}
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
				document.getElementById("btSearch").disabled = true;
				document.getElementById("btAdd").disabled = true;
				document.getElementById("btUpdate").disabled = true;
				document.getElementById("btReset").disabled = true;
				//document.getElementById("reset").disabled = true;			
				
				
				if(cmd == 'addFile'){
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","branchAddFile.jsp");
					mainForm.submit();
				}else if(cmd == 'add'){
					mainForm.setAttribute("action","branchAdd.jsp");
					mainForm.submit();
				}else if(cmd == 'edit'){
					if(chkInputNull()){
						alert("ข้อมูลไม่ครบ");
						document.getElementById("btSearch").disabled = false;
						document.getElementById("btAdd").disabled = false;
						document.getElementById("btUpdate").disabled = false;
						document.getElementById("btReset").disabled = false;
					}else{
						mainForm.setAttribute("action","branchEdit.jsp");
						mainForm.submit();						
					}

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

		var flage = (input >= 48 && input <= 57) || input==13;
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}else{
			document.getElementById("Submit2").disabled = false ;
		}
		return flage;
	}
	
	function isNumber2(input){
		//alert(input)
		var flage = (input >= 48 && input <= 57)||input == 46|| input==13;
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}
		return flage;
	}
	
	function isTHAI(input){
		//alert(input)
//		var flage = (input >= 3585 && input <= 3673)||input == 32||(input >= 48 && input <= 57);
// 		if(!flage){
// 			alert('กรุณาใส่่ภาษาไทย')
// 		}
		//return flage;
		return true;
	}
	
	function isENG(input){
		//alert(input)
// 		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||input == 32||(input >= 48 && input <= 57)||(input >= 40 && input <= 41);
// 		if(!flage){
// 			alert('กรุณาใส่่ภาษาอังกฤษ')
// 		}
// 		return flage;
		return true;
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
		document.getElementById("AreaNo").value = document.getElementById ( "AreaNo"+i ).innerText;
		document.getElementById("Network").value = document.getElementById ( "Network"+i ).innerText;
		document.getElementById("GroupB").value = document.getElementById ( "GroupB"+i ).innerText;
		
		document.getElementById("OrgCode").readOnly = true;
		document.getElementById("OrgCode").style.backgroundColor = "#f3defc";
// 		style=\"background-color:#f3defc\"

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
		document.getElementById("GroupB").value = '';
		document.getElementById("AreaNo").value = '';
		document.getElementById("Network").value = '';
		
		document.getElementById("OrgCode").readOnly = false;
		document.getElementById("OrgCode").style.backgroundColor = "";
		
	}
	
	function listenEnter(){
		if(chkKeyEnter()){
			doSubmit('search');
		}
	}
	function chkKeyEnter(){
		if(event.keyCode==13){
			return true;
		}else{
			return false;
		}
	}
	function chkInputNull(){
		if(document.getElementById("OrgCode").value == ''
		|| document.getElementById("OcCode").value == ''
		|| document.getElementById("BNameTH").value == ''
		|| document.getElementById("BNameEN").value == ''
		|| document.getElementById("OperationDay").value == ''
		|| document.getElementById("CounterLimit1").value == ''
		|| document.getElementById("CounterLimit2").value == ''
		|| document.getElementById("SelectMetropolitan").value == ''
		|| document.getElementById("SelectABM").value == ''
		|| document.getElementById("GroupB").value == ''
		|| document.getElementById("AreaNo").value == ''
		|| document.getElementById("Network").value == ''){
			return true;
		}else{
			return false;
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

public String getSelect(String value,String AP){
	//System.out.println("value:"+value);
	if(value!=null){
		if(value.equals(AP))
			return "selected";	
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
		
		//System.out.println(session.getAttribute( "resultLogin"));
		//response.sendRedirect(request.getContextPath()+"/jsp/main.jsp");
		boolean read = false;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
			
		}else{
			String module = session.getAttribute("authen").toString();
// 			module = "Administrator";
			System.out.println("authen:"+module);
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
			if(module.indexOf("Audit")>-1){
				read= true;
			}
		}

		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		String sql = "select OrgCode,IFNULL(OcCode,'-') OcCode,IFNULL(BNameTH,'-') BNameTH,IFNULL(BNameEN,'-') BNameEN"
				    +" ,IFNULL(OperationDay,'-') OperationDay,IFNULL(CounterLimit1,'-') CounterLimit1"
					+" ,IFNULL(CounterLimit2,'-') CounterLimit2,IFNULL(Metropolitan,'-') Metropolitan,IFNULL(ABM,'-') ABM"
					+" ,IFNULL(AreaNo,'-') AreaNo,IFNULL(Network,'-') Network,IFNULL(GroupB,'-') GroupB"
					+" from tblmt_branchinfo b "
					+" where OrgCode like ? and OcCode like ? and BNameTH like ? and BNameEN like ? "
					+" and OperationDay like ? and CounterLimit1 like ? "
					+" and CounterLimit2 like ? and Metropolitan like ? and ABM like ? "
					+" and AreaNo like ? and Network like ? and GroupB like ? " 	
					+" order by OcCode ";
		preparedStatement = connect
		          .prepareStatement(sql);
		preparedStatement.setString(1, "%"+isNull(request.getParameter("OrgCode"))+"%");
		preparedStatement.setString(2, "%"+isNull(request.getParameter("OcCode"))+"%");
		preparedStatement.setString(3, "%"+isNull(request.getParameter("BNameTH"))+"%");
// 		preparedStatement.setString(3, "%"+"ถนน"+"%");
		preparedStatement.setString(4, "%"+isNull(request.getParameter("BNameEN"))+"%");
		preparedStatement.setString(5, "%"+isNull(request.getParameter("OperationDay"))+"%");
		preparedStatement.setString(6, "%"+isNull(request.getParameter("CounterLimit1"))+"%");
		preparedStatement.setString(7, "%"+isNull(request.getParameter("CounterLimit2"))+"%");
		preparedStatement.setString(8, "%"+isNull(request.getParameter("SelectMetropolitan"))+"%");
		preparedStatement.setString(9, "%"+isNull(request.getParameter("SelectABM"))+"%");
		preparedStatement.setString(10, "%"+isNull(request.getParameter("Area"))+"%");
		preparedStatement.setString(11, "%"+isNull(request.getParameter("Network"))+"%");
		preparedStatement.setString(12, "%"+isNull(request.getParameter("SelectGroup"))+"%");
		
// 		System.out.println("X"+preparedStatement);
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
    
		<form id="mainForm" method="post" action="" onkeypress='return listenEnter()' >
		 <input type="hidden" id="BRemove" name="BRemove" value="">
		    <table border="0" cellpadding="0" cellspacing="0" width="60%" class="tbl_search">
        	<tr>
            	<td width="30">&nbsp;</td>
                <td align="right">รหัสสาขา</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="OrgCode" name="OrgCode" value="<%=isNull(request.getParameter("OrgCode"))%>" onkeypress='return isNumber(event.charCode)' onkeyup='listenEnter()' /></td>
                <td width="10">&nbsp;</td>
                <td align="right">รหัสหน่วยงาน</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="OcCode" name="OcCode" value="<%=isNull(request.getParameter("OcCode"))%>" onkeypress='return isNumber(event.charCode)' onkeyup='listenEnter()' /></td>
            </tr><tr>    
                <td width="10">&nbsp;</td>
                <td align="right">ชื่อสาขา(ไทย)</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="50" id="BNameTH" name="BNameTH" value="<%=isNull(request.getParameter("BNameTH"))%>" onkeypress='return isTHAI(event.charCode)' onkeyup='listenEnter()' /></td>
                <td width="10">&nbsp;</td>
                <td align="right">ชื่อสาขา(อังกฤษ)</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="50" id="BNameEN" name="BNameEN" value="<%=isNull(request.getParameter("BNameEN"))%>" onkeypress='return isENG(event.charCode)' onkeyup='listenEnter()' /></td>
                <td width="20">&nbsp;</td>
            </tr>
            <tr>
                <td width="10">&nbsp;</td>
                <td align="right">Low&nbsp;Counter</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="CounterLimit1" name="CounterLimit1" value="<%=isNull(request.getParameter("CounterLimit1"))%>" onkeypress='return isNumber2(event.charCode)' onkeyup='listenEnter()' /></td>
                <td width="10">&nbsp;</td>
                <td align="right">High&nbsp;Counter</td>
                <td width="5">&nbsp;</td>                
                <td align="left"><input type="text" class="txt1" maxlength="5" id="CounterLimit2" name="CounterLimit2" value="<%=isNull(request.getParameter("CounterLimit2"))%>" onkeypress='return isNumber2(event.charCode)' onkeyup='listenEnter()' /></td>
            </tr><tr>
                <td width="30">&nbsp;</td>
                <td align="right">วันทำการ </td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="5" id="OperationDay" name="OperationDay" value="<%=isNull(request.getParameter("OperationDay"))%>" onkeypress='return isNumber(event.charCode)' onkeyup='listenEnter()' /></td>           
                <td width="10">&nbsp;</td>
                <td align="right">สาขาปริมณฑล </td>
                <td width="5">&nbsp;</td>
                <td align="left">
                		<select name="SelectMetropolitan" size="1" id="SelectMetropolitan" class="txt1" onkeyup='listenEnter()'>
			            			<option value=""  <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"") %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			               			<option value="Y" <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"Y") %> >Y</option>
			               			<option value="N" <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"N") %> >N</option>
		              	</select></td>
                <td width="20">&nbsp;</td>
            </tr>
            <tr>
            	<td width="20">&nbsp;</td>
                <td align="right">ผู้ช่วยผู้จัดการ </td>
                <td width="5">&nbsp;</td>
                <td align="left">
                		<select name="SelectABM" size="1" id="SelectABM" class="txt1" onkeyup='listenEnter()' >
			                		<option value=""  <%=getSelect(isNull(request.getParameter("SelectABM")),"")  %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			                		<option value="Y" <%=getSelect(isNull(request.getParameter("SelectABM")),"Y") %> >Y</option>
			                		<option value="N" <%=getSelect(isNull(request.getParameter("SelectABM")),"N") %> >N</option>
		              	</select></td>
		        <td width="10">&nbsp;</td>
		                <td align="right">Group</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><select name="SelectGroup" id="GroupB" size="1"  class="txt1" onkeyup='listenEnter()' >
		                					<option value=""  <%=getSelect(isNull(request.getParameter("SelectGroup")),"") %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					                		<option value="1" <%=getSelect(isNull(request.getParameter("SelectGroup")),"1") %> >1</option>
					                		<option value="2" <%=getSelect(isNull(request.getParameter("SelectGroup")),"2") %> >2</option>
					                		<option value="3" <%=getSelect(isNull(request.getParameter("SelectGroup")),"3") %> >3</option>
										</select></td>
				</tr>
		            <tr>
		            	<td width="20">&nbsp;</td>
		            	<td align="right">Area</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" name="Area" id="AreaNo" value="<%=isNull(request.getParameter("Area"))%>" onkeypress='return isTHAI(event.charCode)' onkeyup='listenEnter()' /></td>		                
		                <td width="10">&nbsp;</td>
						<td align="right">Network </td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" name="Network" id="Network" value="<%=isNull(request.getParameter("Network"))%>" onkeypress='return isTHAI(event.charCode)' onkeyup='listenEnter()' /></td>
		            </tr>
		     <tr>                     	
                <td width="10">&nbsp;</td>
                <td colspan="5"><span id="msg1" class="style3">
								<%
							String temp = (String)session.getAttribute("success2");
							
							if( "ss".equals(temp) )
								out.println("ค้นหา branch สำเร็จ");
							else if( "se".equals(temp) )
								out.println("ค้นหา branch มีปัญหาลองตรวจสอบเงือนไขการค้นหา");	
							else if( "as".equals(temp) )
								out.println("เพิ่ม branch สำเร็จ");
							else if( "ae".equals(temp) )
								out.println("เพิ่ม branch มีปัญหา อาจเกิดจากเลข branch ซ้ำหรือใส่ข้อมูลไม่ครบ");
							else if( "es".equals(temp) )
								out.println("แก้ไข branch สำเร็จ");
							else if( "ee".equals(temp) )
								out.println("แก้ไข branch มีปัญหา");
							else if(null==temp){
								out.println(" ");
							}else{
								out.println(temp);
							}
							session.removeAttribute("success2");
						%>		
						</span>						</td>
            </tr>
            <tr>
            	<td width="20"><img src="imgs/spacer.gif" style="border:0;" /></td>
            	<td colspan="15" align="center" height="35">
            		<input type="button" id="btSearch" value="Search" class="btn1" onClick="doSubmit('search')"/>
            		<input type="button" id="btAdd" value="Add" class="btn1" onclick="window.open('..\\jsp\\branchAdd.jsp', '_blank','width=720,height=400')"/>
            		<input type="button" id="btUpdate" value="Update" class="btn1" onClick="doSubmit('edit')"/>
            		<input type="button" id="btReset" value="Reset" class="btn1" onClick="resetTextBox()"/>
            	</td>
            </tr>
        </table>
        <span id="msg2" class="style3"><% 	
		
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
		
							session.removeAttribute("success");
						%></span>
      </form>
      
            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
        	<!-- หัวตาราง -->
        	<tr>
            	<td class="header_tbl_content">ลำดับ</td>
                <td width="80" class="header_tbl_content">รหัสสาขา</td>
                <td width="80" class="header_tbl_content">รหัสหน่วยงาน</td>
                <td class="header_tbl_content" width="250">ชื่อสาขา(ไทย)</td>
                <td class="header_tbl_content" width="250">ชื่อสาขา(อังกฤษ)</td>
                <td width="80" class="header_tbl_content">วันทำการ</td>
                <td class="header_tbl_content">Low&nbsp;Counter</td>
                <td class="header_tbl_content">High&nbsp;Counter</td>
                <td class="header_tbl_content">ปริมณฑล</td>
                <td width="80" class="header_tbl_content">ผู้ช่วยผู้จัดการ</td>
                <td class="header_tbl_content">Area</td>
                <td width="80" class="header_tbl_content">Network</td>
                <td class="header_tbl_content">Group</td>
                <td id="tabRV" class="header_tbl_content_last">ลบสาขา</td>
            </tr>
            <!-- หัวตาราง -->
            
            	<%  int i =0;
						  	  while(resultSet.next() )
							  { 	i++;
							    	out.println("<tr><td class=\"tbl_row1\" >"+i+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"OrgCode"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("OrgCode")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"OcCode"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("OcCode")+"</td>");
								  	out.println("<td class=\"tbl_row1\" style=\"text-align:left;\" id=\"BNameTH"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("BNameTH")+"</td>");
								  	out.println("<td class=\"tbl_row1\" style=\"text-align:left;\" id=\"BNameEN"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("BNameEN")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"OperationDay"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("OperationDay")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"CounterLimit1"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("CounterLimit1")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"CounterLimit2"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("CounterLimit2")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"Metropolitan"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("Metropolitan")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"ABM"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("ABM")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"AreaNo"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("AreaNo")+"</td>");
								  	out.println("<td class=\"tbl_row3\" id=\"Network"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("Network")+"</td>");
								  	out.println("<td class=\"tbl_row1\" id=\"GroupB"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("GroupB")+"</td>");
								  	if(!read){
									  	out.println("<td class=\"tbl_row1\"  >"+
									  	"<img src=\"imgs/delete_icon.png\" style=\"border:0;\" height=\"24\" title=\"Delete\" onClick=\"doSubmit('remove"+i+"')\" />"
									  	+"</td></tr>");
								  	}else{
								  		out.println("</tr>");
								  	}
								}
							    connect.close();
								%>
       </table>
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
	var ac = document.getElementById("branch");
	ac.setAttribute("class", "active");
</script>
