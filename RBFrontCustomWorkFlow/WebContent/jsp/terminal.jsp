<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Terminal Information</title>

<style type="text/css">
<!--
.style3 {color: #FF0000}
.style4 {color: #f3defc}
-->
</style>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<script type="text/javascript">
	function forwardSearch(){
		window.location.href = "terminalSearch.jsp?OrgCode="+document.getElementById("OrgCode").value;
	}	
	function forwardAddEdit(){
		window.location.href = "terminalAddEdit.jsp?status=a";
	}
	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")

		if(cmd == 'search'){
			if(checkFildNull()){
				alert('โปรดใส่่เงือนไขสำหรับค้นหาข้อมูล')
			}else{
				mainForm.setAttribute("action","terminalSearch.jsp");
				mainForm.submit();
			}
			
		}else{ 
			
			if(confirm("ยืนยันทำรายการ")){
				document.getElementById("btSearch").disabled = true;
				document.getElementById("btAdd").disabled = true;
				document.getElementById("btUpdate").disabled = true;
				document.getElementById("reset").disabled = true;
						
				if(cmd == 'addFile'){
					document.getElementById("file2").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","terminalAddFile.jsp");
					mainForm.submit();
				}else if(cmd == 'addFile2'){
					document.getElementById("file1").disabled = true;
					mainForm.setAttribute("enctype","multipart/form-data");
					mainForm.setAttribute("action","terminalAddFile2.jsp");
					mainForm.submit();
				}else if(cmd == 'add'){
					mainForm.setAttribute("action","terminalAdd.jsp");
					mainForm.submit();
				}else if(cmd == 'edit'){
					mainForm.setAttribute("action","terminalEdit.jsp");
					mainForm.submit();
				}else if(cmd.substring(0,6)=='remove'){
					var i = cmd.substring(6)
 					//alert(i)
					//alert(document.getElementById ( "OrgCode"+i ).innerText)
					document.getElementById("TRemove").value = document.getElementById ( "TerminalID"+i ).innerText;
					document.getElementById("TBRemove").value = document.getElementById ( "OrgCode"+i ).innerText;
					document.getElementById("EmpRemove").value = document.getElementById ( "EmpID"+i ).innerText;
				
// 					alert(document.getElementById("TRemove").value);
// 					alert(document.getElementById("TBRemove").value);
					mainForm.setAttribute("action","terminalRemove.jsp");
					mainForm.submit();
				}
			}
		
		}

	}
	
	function isNumber(input){

		var flage = (input >= 48 && input <= 57) || input==13;
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}
		return flage;
	}
	
	function isNumber2(input){
		//alert(input)
		var flage = (input >= 48 && input <= 57)||input == 46 || input==13;
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
		}
		return flage;
	}
	
	function isTHAI(input){
		//alert(input)
		var flage = (input >= 3585 && input <= 3662)||input == 32 || input==13;
		if(!flage){
			alert('กรุณาใส่่ภาษาไทย')
		}
		return flage;
	}
	
	function isENG(input){
		//alert(input)
		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||input == 32|| input==13;
		if(!flage){
			alert('กรุณาใส่่ภาษาอังกฤษ')
		}
		return flage;
	}	

	function isENGNUM(input){
		//alert(input)
		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||(input >= 48 && input <= 57)||input == 32||input ==43||input ==46|| input==13; //43+,32' ',46.;
		if(!flage){
			alert('กรุณาใส่่ภาษาอังกฤษหรือตัวเลขเท่่านั้น')
		}
		return flage;
	}
	
	function inputField(i){

		//alert(document.getElementById ( "SelectMetropolitan"+i ).innerText);
		document.getElementById("OrgCode").value = document.getElementById ( "OrgCode"+i ).innerText;
		document.getElementById("EmpID").value = document.getElementById ( "EmpID"+i ).innerText;
		document.getElementById("Name").value = document.getElementById ( "Name"+i ).innerText;
		document.getElementById("TechnicalRole").value = document.getElementById ( "TechnicalRole"+i ).innerText;
		document.getElementById("BusinessRole").value = document.getElementById ( "BusinessRole"+i ).innerText;
		document.getElementById("TerminalID").value = document.getElementById ( "TerminalID"+i ).innerText;
		document.getElementById("SelectReserved").value = document.getElementById ( "Reserved"+i ).innerText;
		
		document.getElementById("OrgCode").readOnly = true;
		document.getElementById("TerminalID").readOnly = true;
		document.getElementById("OrgCode").style.backgroundColor = "#f3defc";
		document.getElementById("TerminalID").style.backgroundColor = "#f3defc";

	}
	
	function resetTextBox(){
		document.getElementById ( "msg1" ).innerText = '';
		document.getElementById ( "msg2" ).innerText = '';
		
		document.getElementById("OrgCode").value = '';
		document.getElementById("EmpID").value = '';
		document.getElementById("Name").value = '';
		document.getElementById("TechnicalRole").value = '';
		document.getElementById("BusinessRole").value = '';
		document.getElementById("TerminalID").value = '';
		document.getElementById("SelectReserved").value = '';
		
		document.getElementById("OrgCode").readOnly = false;
		document.getElementById("TerminalID").readOnly = false;
		document.getElementById("OrgCode").style.backgroundColor = "";
		document.getElementById("TerminalID").style.backgroundColor = "";
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
	
	function checkFildNull(){
		if( document.getElementById("OrgCode").value == '' && 
		document.getElementById("EmpID").value == '' && 
		document.getElementById("Name").value == '' && 
		document.getElementById("TechnicalRole").value == '' && 
		document.getElementById("BusinessRole").value == '' && 
		document.getElementById("TerminalID").value == '' && 
		document.getElementById("SelectReserved").value == ''){
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
// 	System.out.println(Y);
	if("Y".equals(Y))
		return "checked=\"checked\"";
	return "";
}

%>
<body>
<%	
		boolean read = false;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
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
		String first = "";
		if("".equals(isNull(request.getParameter("OrgCode"))+isNull(request.getParameter("EmpID"))+isNull(request.getParameter("Name")) 
		+isNull(request.getParameter("TechnicalRole"))+isNull(request.getParameter("BusinessRole"))+isNull(request.getParameter("TerminalID"))
		+isNull(request.getParameter("SelectReserved")) )){
			first="X";
		}
		
		connect = DatbaseConnection.getConnectionMySQL();
// 		String sql = "select t.OrgCode,IFNULL(EmpID,'-') EmpID,IFNULL(Name,'-') Name,IFNULL(TechnicalRole,'-') TechnicalRole,IFNULL(BusinessRole,'-') BusinessRole,IFNULL(TerminalID,'-') TerminalID,IFNULL(Reserved,'-') Reserved "+
// 					 "from tblmt_terminallist t order by t.OrgCode";
		
		  String sql = "select t.OrgCode,IFNULL(EmpID,'-') EmpID,IFNULL(Name,'-') Name,IFNULL(TechnicalRole,'-') TechnicalRole,IFNULL(BusinessRole,'-') BusinessRole,IFNULL(TerminalID,'-') TerminalID,IFNULL(Reserved,'-') Reserved "+  
				   "from tblmt_terminallist t where t.OrgCode like ? "+
	  			   "and EmpID like ? and Name like ? and TechnicalRole like ? "+
			       "and BusinessRole like ? and TerminalID like ? and Reserved like ? "+
	  			   "and t.OrgCode like ? "+
			       "order by OrgCode,TerminalID ";
		  
		  preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, "%"+isNull(request.getParameter("OrgCode"))+"%");
			preparedStatement.setString(2, "%"+isNull(request.getParameter("EmpID"))+"%");
			preparedStatement.setString(3, "%"+isNull(request.getParameter("Name"))+"%");
			preparedStatement.setString(4, "%"+isNull(request.getParameter("TechnicalRole"))+"%");
			preparedStatement.setString(5, "%"+isNull(request.getParameter("BusinessRole"))+"%");
			preparedStatement.setString(6, "%"+isNull(request.getParameter("TerminalID"))+"%");
			preparedStatement.setString(7, "%"+isNull(request.getParameter("SelectReserved"))+"%");
			preparedStatement.setString(8, "%"+first+"%");	
			  
// 		System.out.println(preparedStatement);
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
		<form id="mainForm" method="post" action="">
		<input type="hidden" id="TRemove" name="TRemove" value="">
		<input type="hidden" id="TBRemove" name="TBRemove" value="">
		<input type="hidden" id="EmpRemove" name="EmpRemove" value="">
		
		      <!-- main page --> 
				<table border="0" cellpadding="0" cellspacing="0"  class="tbl_search">
		                      <tr>
		                      	<td width="30">&nbsp;</td>
		                        <td align="right">รหัสสาขา</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="5" type="text" id="OrgCode" name="OrgCode" value="<%=isNull(request.getParameter("OrgCode"))%>" onkeypress='return isNumber(event.charCode)' onkeyup='listenEnter()' />
		                        </td>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">รหัสพนักงาน</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left"><label>
		                          <input class="txt1" maxlength="5" type="text" id="EmpID" name="EmpID" value="<%=isNull(request.getParameter("EmpID"))%>" onkeypress='return isNumber(event.charCode)' onkeyup='listenEnter()' />
		                        </label></td>
		                      </tr><tr>
		                      	<td width="30">&nbsp;</td>
		                        <td align="right">ชื่อพนักงาน</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="Name" name="Name" value="<%=isNull(request.getParameter("Name"))%>" onkeypress='return isENG(event.charCode)' onkeyup='listenEnter()' />
								</td>
								<td width="10">&nbsp;</td>
		                        <td align="right">TechnicalRole</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="TechnicalRole" name="TechnicalRole" value="<%=isNull(request.getParameter("TechnicalRole"))%>" onkeypress='return isENGNUM(event.charCode)' onkeyup='listenEnter()' />
		                        </td>
		                      </tr><tr>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">Function</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="BusinessRole" name="BusinessRole" value="<%=isNull(request.getParameter("BusinessRole"))%>" onkeypress='return isENGNUM(event.charCode)' onkeyup='listenEnter()'/>
		                        </td>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">TerminalID</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="5" type="text" id="TerminalID" name="TerminalID" value="<%=isNull(request.getParameter("TerminalID"))%>" onkeypress='return isENGNUM(event.charCode)' onkeyup='listenEnter()'/>
		                        </td>
		                      </tr><tr>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">สถานะTerminal</td>
		                        <td width="5">&nbsp;</td>
		                        <td>
		                        	<select class="txt1" name="SelectReserved" id="SelectReserved" onkeyup='listenEnter()' >
			                			<option value=""  <%=getSelect(isNull(request.getParameter("SelectReserved")),"")  %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			                			<option value="Y" <%=getSelect(isNull(request.getParameter("SelectReserved")),"Y")  %> >Y</option>
			                			<option value="N" <%=getSelect(isNull(request.getParameter("SelectReserved")),"N")  %> >N</option>
		              				</select>
		                        </td>
		                        <td colspan="20"><span id="msg1" class="style3">
		                    <%		                    
		                    String temp = (String)session.getAttribute("success2");
		                        
							if( "ss".equals(temp) )
								out.println("ค้นหา terminal สำเร็จ");
							else if( "se".equals(temp) )
								out.println("ค้นหา terminal ไม่พบข้อมูลตามเงือนไขการค้นหา");	
							else if( "as".equals(temp) )
								out.println("เพิ่ม terminal สำเร็จ");
							else if( "ae".equals(temp) )
								out.println("เพิ่ม terminal มีปัญหา อาจเกิดจากเลข terminal ซ้ำ");
							else if( "ae2".equals(temp) )
								out.println("เพิ่ม terminal มีปัญหา กรอกข้อมูลไม่ครบถ้วน");
							else if( "es".equals(temp) )
								out.println("แก้ไข terminal สำเร็จ");
							else if( "ee".equals(temp) )
								out.println("แก้ไข terminal มีปัญหา");
							else if(null==temp){
								out.println(" ");
							}else{
								out.println(temp);
							}
							session.removeAttribute("success2");
							
						%>                        </span></td>
		                      </tr><tr>
		                      	<td width="20">&nbsp;</td>
		                        <td colspan="15" align="center" height="35">
		                            <input class="btn1"  type="button" id="btSearch" value="Search" onClick="doSubmit('search')"  />
		                            <input class="btn1" type="button" id="btAdd" value="Add" onclick="window.open('..\\jsp\\terminalAdd.jsp', '_blank','width=720,height=350')"  />
		                            <input class="btn1" type="button" id="btUpdate" value=Update  onClick="doSubmit('edit')" />
		                            <input class="btn1" type="button" id="reset" value="Reset" onClick="resetTextBox()">
		                        </td>
		                      </tr>
						</div></td>
					  </tr>
					  <tr>
						<td colspan="15"><span id="msg2" class="style3"><% 	
							
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
							session.removeAttribute("success");
						%></span>
				</table>
						
				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
						  <tr>
						    <td class="header_tbl_content"><strong>ลำดับ</strong></td>
							<td class="header_tbl_content"><strong>รหัสสาขา</strong></td>
							<td class="header_tbl_content"><strong>รหัสพนักงาน</strong></td>
							<td class="header_tbl_content"><strong>ชื่อพนักงาน</strong></td>
							<td class="header_tbl_content"><strong>TechnicalRole</strong></td>
							<td class="header_tbl_content"><strong>Function</strong></td>
							<td class="header_tbl_content"><strong>TerminalID</strong></td>
							<td class="header_tbl_content"><strong>สถานะ</strong></td>
							<td id="tabRV" class="header_tbl_content_last"><strong>ลบ</strong></td>
						  </tr>
						  <%  int i =0;
						  	  while(resultSet.next())
							  { i++;
							    out.println("<tr><td class=\"tbl_row1\">"+i+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"OrgCode"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("OrgCode")+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"EmpID"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("EmpID")+"</td>");
							  	out.println("<td class=\"tbl_row1\" style=\"text-align:left;\" id=\"Name"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("Name")+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"TechnicalRole"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("TechnicalRole")+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"BusinessRole"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("BusinessRole")+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"TerminalID"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("TerminalID")+"</td>");
							  	out.println("<td class=\"tbl_row1\" id=\"Reserved"+i+"\" onClick=\"inputField('"+i+"')\" >"+resultSet.getString("Reserved")+"</td>");
// 							  	out.println("<td class=\"tbl_row1\" id=\"remove"+i+"\" onClick=\"doSubmit('remove"+i+"')\" >remove</td></tr>");
							  	
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
	var ac = document.getElementById("terminal");
	ac.setAttribute("class", "active");
</script>
