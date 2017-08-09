<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add Terminal</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<style type="text/css">
.style3 {color: #FF0000}
</style>
<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		if(cmd == 'add'){
			mainForm.setAttribute("action","terminalAddServlet.jsp");
			mainForm.submit();
		}
	}
	
	function isNumber(input){

		var flage = (input >= 48 && input <= 57);
		if(!flage){
			alert('กรุณาใส่่เป็นตัวเลข')
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

	function isENGNUM(input){
		//alert(input)
		var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||(input >= 48 && input <= 57)||input == 32||input ==43||input ==46; //43+,32' ',46.;
		if(!flage){
			alert('กรุณาใส่่ภาษาอังกฤษหรือตัวเลขเท่่านั้น')
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
// 	System.out.println(Y);
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
            <td><span class="header_caption">RBFront Custom Workflow</span></td>
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
		                        <td align="left">
		                          <input class="txt1" maxlength="5" type="text" id="OrgCode" name="OrgCode" value="<%=isNull(request.getParameter("OrgCode"))%>" onkeypress='return isNumber(event.charCode)' />*
		                        </td>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">รหัสพนักงาน</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="5" type="text" id="EmpID" name="EmpID" value="<%=isNull(request.getParameter("EmpID"))%>" onkeypress='return isNumber(event.charCode)' />*
		                        </td>
		                      </tr><tr>
		                      	<td width="30">&nbsp;</td>
		                        <td align="right">ชื่อพนักงาน</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="Name" name="Name" value="<%=isNull(request.getParameter("Name"))%>" onkeypress='return isENG(event.charCode)' />*
								</td>
								<td width="10">&nbsp;</td>
		                        <td align="right">TechnicalRole</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="TechnicalRole" name="TechnicalRole" value="<%=isNull(request.getParameter("TechnicalRole"))%>" onkeypress='return isENGNUM(event.charCode)' />*
		                        </td>
		                      </tr><tr>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">Function</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="50" type="text" id="BusinessRole" name="BusinessRole" value="<%=isNull(request.getParameter("BusinessRole"))%>" onkeypress='return isENGNUM(event.charCode)'/>*
		                        </td>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">TerminalID</td>
		                        <td width="5">&nbsp;</td>
		                        <td align="left">
		                          <input class="txt1" maxlength="5" type="text" id="TerminalID" name="TerminalID" value="<%=isNull(request.getParameter("TerminalID"))%>" onkeypress='return isENGNUM(event.charCode)'/>*
		                        </td>
		                      </tr><tr>
		                        <td width="10">&nbsp;</td>
		                        <td align="right">สถานะTerminal</td>
		                        <td width="5">&nbsp;</td>
		                        <td>
		                        	<select class="txt1" name="SelectReserved" size="1" id="SelectReserved">
			                			<option value="Y" <%=getSelect(isNull(request.getParameter("SelectReserved")),"Y")  %> >Y</option>
			                			<option value="N" <%=getSelect(isNull(request.getParameter("SelectReserved")),"N")  %> >N</option>
		              				</select>
		                        </td>

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
