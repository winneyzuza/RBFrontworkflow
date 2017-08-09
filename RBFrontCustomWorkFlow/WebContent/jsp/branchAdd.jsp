<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add Branch</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<style type="text/css">
.style3 {color: #FF0000}
</style>
<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		if(cmd == 'add'){
			mainForm.setAttribute("action","branchAddServlet.jsp");
			mainForm.submit();
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
	
	function isNumber2(input){
		//alert(input)
		var flage = (input >= 48 && input <= 57)||input == 46;
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

<div id="container">
	<div id="header">
    	<table border="0" cellpadding="0" cellspacing="0" class="header_banner" width="100%" height="90">
        <tr>
        	<td class="header_logo" width="229"><img src="imgs/spacer.gif" style="border:0;" /></td>
            <td><span class="header_caption">RBFront Custom WorkFlow</span></td>
        </tr>
        </table>
        
        <!-- top menu -->
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   
    <!-- Main Content -->
    <div id="main_content">  
         <form id="mainForm"  method="post" action="" >						        
				    <table border="0" cellpadding="0" cellspacing="0" class="tbl_search">
		        	<tr>
		            	<td width="30">&nbsp;</td>
		                <td align="right">รหัสสาขา</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="5" id="OrgCode" name="OrgCode" value="<%=isNull(request.getParameter("OrgCode"))%>" onkeypress='return isNumber(event.charCode)' />*</td>
		                <td width="10">&nbsp;</td>
		                <td align="right">รหัสหน่วยงาน</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="5" id="OcCode" name="OcCode" value="<%=isNull(request.getParameter("OcCode"))%>" onkeypress='return isNumber(event.charCode)' />*</td>
		            </tr><tr>    
		                <td width="10">&nbsp;</td>
		                <td align="right">ชื่อสาขา(ไทย)</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="50" id="BNameTH" name="BNameTH" value="<%=isNull(request.getParameter("BNameTH"))%>" onkeypress='return isTHAI(event.charCode)' />*</td>
		                <td width="10">&nbsp;</td>
		                <td align="right">ชื่อสาขา(อังกฤษ)</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="50" id="BNameEN" name="BNameEN" value="<%=isNull(request.getParameter("BNameEN"))%>" onkeypress='return isENG(event.charCode)' />*</td>
		                <td width="20">&nbsp;</td>
		            </tr>
		            <tr>
		                <td width="10">&nbsp;</td>
		                <td align="right">Low&nbsp;Counter</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="5" id="CounterLimit1" name="CounterLimit1" value="<%=isNull(request.getParameter("CounterLimit1"))%>" onkeypress='return isNumber2(event.charCode)' />*</td>
		                <td width="10">&nbsp;</td>
		                <td align="right">High&nbsp;Counter</td>
		                <td width="5">&nbsp;</td>                
		                <td align="left"><input type="text" class="txt1" maxlength="5" id="CounterLimit2" name="CounterLimit2" value="<%=isNull(request.getParameter("CounterLimit2"))%>" onkeypress='return isNumber2(event.charCode)' />*</td>
		            </tr><tr>
		                <td width="30">&nbsp;</td>
		                <td align="right">วันทำการ </td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" maxlength="5" id="OperationDay" name="OperationDay" value="<%=isNull(request.getParameter("OperationDay"))%>" onkeypress='return isNumber(event.charCode)' />*</td>           
		                <td width="10">&nbsp;</td>
		                <td align="right">สาขาปริมณฑล </td>
		                <td width="5">&nbsp;</td>
		                <td align="left">
		                		<select name="SelectMetropolitan" size="1" id="SelectMetropolitan" class="txt1">
					            			<option value=""  <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"") %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					               			<option value="Y" <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"Y") %> >Y</option>
					               			<option value="N" <%=getSelect(isNull(request.getParameter("SelectMetropolitan")),"N") %> >N</option>
				              	</select>*</td>
		                <td width="20"><img src="imgs/spacer.gif" style="border:0;" /></td>
		            </tr>
		            <tr>
		            	<td width="20">&nbsp;</td>
		                <td align="right">ผู้ช่วยผู้จัดการ </td>
		                <td width="5">&nbsp;</td>
		                <td align="left">
		                		<select name="SelectABM" size="1" id="SelectABM" class="txt1">
					                		<option value=""  <%=getSelect(isNull(request.getParameter("SelectABM")),"")  %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					                		<option value="Y" <%=getSelect(isNull(request.getParameter("SelectABM")),"Y") %> >Y</option>
					                		<option value="N" <%=getSelect(isNull(request.getParameter("SelectABM")),"N") %> >N</option>
				              	</select>*</td>
		                <td width="10">&nbsp;</td>
		                <td align="right">Group</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><select name="SelectGroup" size="1"  class="txt1">
		                					<option value=""  <%=getSelect(isNull(request.getParameter("SelectGroup")),"") %> >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					                		<option value="1" <%=getSelect(isNull(request.getParameter("SelectGroup")),"1") %> >1</option>
					                		<option value="2" <%=getSelect(isNull(request.getParameter("SelectGroup")),"2") %> >2</option>
					                		<option value="3" <%=getSelect(isNull(request.getParameter("SelectGroup")),"3") %> >3</option>
				              	</select>*</td>
		                
		            </tr>
		            <tr>
		            	<td width="20">&nbsp;</td>
		            	<td align="right">Area</td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" name="Area" value="<%=isNull(request.getParameter("Area"))%>" onkeypress='return isTHAI(event.charCode)' />*</td>		                
		                <td width="10">&nbsp;</td>
						<td align="right">Network </td>
		                <td width="5">&nbsp;</td>
		                <td align="left"><input type="text" class="txt1" name="Network" value="<%=isNull(request.getParameter("Network"))%>" onkeypress='return isTHAI(event.charCode)' />*</td>
		            </tr>
		            <tr><td colspan="20"><span id="msg1" class="style3">
								<%																
								if( session.getAttribute("success")!=null )
									out.println(session.getAttribute("success"));
								
								session.removeAttribute("success");						
						%>		
						</span>						</td>
					</tr>
					<tr><td colspan="20" align="center" ><input class="btn1" type="submit" name="Submit" value="Add" onClick="doSubmit('add')" /></td></tr>
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
