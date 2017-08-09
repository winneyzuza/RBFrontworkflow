<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add  Role</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css" />

<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		
		var skillsSelect = document.getElementById("statement");
		document.getElementById("statementHiden").value = skillsSelect.options[skillsSelect.selectedIndex].text;
// 		alert(document.getElementById("statementHiden").value);
		
		var topic = document.getElementById("topic").value;
		var checkValueGroup = '1';
		var value = '';
		var count = 0;
		var SelectOperation = document.getElementById("SelectOperation").value;
		if(topic.indexOf("Group")>-1){
			checkValueGroup = '0';
			value = document.getElementById("Value").value;
			count = document.getElementById("countRName").value;
			for(i=1;i<=count;i++){
				var rName = document.getElementById('RName'+i).value;
				if(rName==value)
					checkValueGroup = '1';
			}
		} else { // not group condition
			if(topic == 'NumCA'){
				checkValueGroup = '2';
				value = document.getElementById("Value").value;
			}
			if(topic == 'NumRole'){
				checkValueGroup = '3';
				value = document.getElementById("Value").value;
			}
		}
		if(confirm("ยืนยันทำรายการ")){
			if(topic.trim()==''){
				alert('โปรดเลือก topic ก่อน Add');
				checkValueGroup = '0';
			}else if(SelectOperation.trim()==''){
				alert('โปรดเลือก Operation ก่อน Add');
				checkValueGroup = '0';
			}else if(value.trim()==''){
				alert('โปรดใส่ข้อมูล Value ก่อน Add');
				checkValueGroup = '0';
			}
			switch(checkValueGroup) {
			case '1': // Group1,Group2,Group3
				if(cmd == 'add'){
					document.getElementById("SelectOperation").disabled = false; // enable Operator drop box
					mainForm.setAttribute("action","logicValidateAddServlet.jsp"); 
					mainForm.submit();
				}
				break;
			case '2': // NumCA
				if(cmd == 'add'){
//					alert('NumCA start');
					mainForm.setAttribute("action","logicValidateAddServlet.jsp");
					mainForm.submit();
				}
				break;
			case '3': 
				if(cmd == 'add'){
//					alert('NumRole start');
					mainForm.setAttribute("action","logicValidateAddServlet.jsp");
					mainForm.submit();
				}
				break;
			default: 
				alert("ค่า value ไม่ถูกต้อง EX.(AT,CS,AT+CS,SC,SSC,...)");
				break;
			}
		} else {
			alert("Operation cancelled...");
		}
				
	}
	
	function setStatement(){
		
		var topic = document.getElementById("topic").value;

		document.getElementById('statement').options.length = 0;
		var x = document.getElementById("statement");
		
		if(topic=='NumCA' || topic=='NumRole'){			
			var option = document.createElement("option");
			option.text = "3 day";
			option.value = "3";
			x.add(option);
			var option = document.createElement("option");
			option.text = "5 day";
			option.value = "5";
			x.add(option);
			var option = document.createElement("option");
			option.text = "6 day";
			option.value = "6";
			x.add(option);
			var option = document.createElement("option");
			option.text = "7 day";
			option.value = "7";
			x.add(option);
			var option = document.createElement("option");
			option.text = "All";
			option.value = "All";
			x.add(option);
		
			document.getElementById("SelectOperation").disabled = false;
			
		}else if(topic.indexOf("Group")>-1){
			
			var option3 = document.createElement("option");
			option3.text = 'Corp.ตั้งแต่ Staff2 ลงไป';
			option3.value = 'Corp2';
			x.add(option3);
			
			var option2 = document.createElement("option");
			option2.text = 'Corp.Staff3 และตำแหน่งงาน พนักงานธนกิจ หรือ พนักงานธุรกิจ';
			option2.value = 'Corp3';
			x.add(option2);
			
			var count = document.getElementById("countJName").value;
			for(i=1;i<=count;i++){
				var jName = document.getElementById('JName'+i).value;
				var l = jName.indexOf(":");
				var option = document.createElement("option");
				option.text = jName;
				option.value = jName.substring(0,l);
				x.add(option);
			}
			var se = document.getElementById("SelectOperation");			
			se.value = '=';
			se.disabled = true;
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
	
	function isValue(input){
		var topic = document.getElementById("topic").value;
		
		if(topic=='NumCA' || topic=='NumRole'){	
			return isNumber(input);
		}else if(topic.indexOf("Group")>-1){
// look like missing - 20170531 2101PM
			return true;
		}
		return true;
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

<%
		Connection connect = null;
		PreparedStatement preparedStatement = null;
  
///////////////////////////////////////////////////////////////////////////////////////////

			connect = DatbaseConnection.getConnectionMySQL();
			 String sqlRole = "select CONCAT(lv,':',JobName) JName from tblmt_lvjob order by lv ";
			preparedStatement = connect.prepareStatement(sqlRole);
			ResultSet rsJobName = preparedStatement.executeQuery();
			
			sqlRole = "select RoleName from tblmt_role order by RoleName ";
			preparedStatement = connect.prepareStatement(sqlRole);
			ResultSet rsRoleName = preparedStatement.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////

%>

<body>

<div id="container">
	<div id="header">
    	<table border="0" cellpadding="0" cellspacing="0" class="header_banner" width="100%" height="90">
        <tr>
        	<td class="header_logo" width="229">&nbsp;</td>
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
				<% 								
					if( session.getAttribute("success")!=null )
						out.println(session.getAttribute("success"));
					
					session.removeAttribute("success");
				%>
				
				<%				
				int i=0;
				while(rsJobName.next() )
				  { i++;				  	
				  	String JName = rsJobName.getString("JName"); %>
				  <input type="hidden" id="JName<%=i%>" value="<%=JName%>">
				<%  
				  }
				%> <input type="hidden" id="countJName" value="<%=i%>">
				
				<% 
				int j=0;
				while(rsRoleName.next()){
					j++;
					String RName = rsRoleName.getString("RoleName");%>
					  <input type="hidden" id="RName<%=j%>" value="<%=RName%>">
				<%	
				}

				%>	<input type="hidden" id="countRName" value="<%=j%>">
				
				 <input type="hidden" id="statementHiden" name="statementHiden" >
							        
		         <table border="0" cellpadding="0" cellspacing="0" width="60%" class="tbl_search" >
		          <tr><td align="right">Topic: </td>
		          	  <td width="10">&nbsp;</td>
									<td align="left" ><select class="txt1"  id="topic"  name="topic" size="1" onchange="setStatement()" >
									    <option value="" <%=getSelect(isNull(request.getParameter("topic")),"") %> >Choose Topic</option>										
							            <option value="NumCA" <%=getSelect(isNull(request.getParameter("topic")),"NumCA") %> >Number of CA</option>
							            <option value="NumRole" <%=getSelect(isNull(request.getParameter("topic")),"NumRole") %> >Number of Role</option>
							            <option value="Group1" <%=getSelect(isNull(request.getParameter("topic")),"Group1") %> >Group1</option>
							            <option value="Group2" <%=getSelect(isNull(request.getParameter("topic")),"Group2") %> >Group2</option>
							            <option value="Group3" <%=getSelect(isNull(request.getParameter("topic")),"Group3") %> >Group3</option>
							            
						              </select></td>
				  </tr><tr><td align="right">Statement:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" ><select id="statement"  name="statement" class="txt1" size="1" >
										<option value="<%=isNull(request.getParameter("statement"))%>"><%=isNull(request.getParameter("statementHiden"))%> </option>
						              </select></td>
				  </tr><tr><td align="right">Operator:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" ><select class="txt1"  name="SelectOperation" size="1" id="SelectOperation">
								    	<% 

									    		out.println("<option value=\"\"" + getSelect(isNull(request.getParameter("SelectOperation")),"")+ ">  </option>");
									    	   	out.println("<option value=\"<=\"" + getSelect(isNull(request.getParameter("SelectOperation")),"<=")+ "> <= </option>");
									    		out.println("<option value=\"<\""  + getSelect(isNull(request.getParameter("SelectOperation")),"<")+ "> <  </option>");
									    		out.println("<option value=\">=\"" + getSelect(isNull(request.getParameter("SelectOperation")),">=")+ "> >= </option>");
									    		out.println("<option value=\">\""  + getSelect(isNull(request.getParameter("SelectOperation")),">")+ "> >  </option>");
									    		out.println("<option value=\"=\""  + getSelect(isNull(request.getParameter("SelectOperation")),"=")+ "> =  </option>");
									    		out.println("<option value=\"!=\"" + getSelect(isNull(request.getParameter("SelectOperation")),"!=")+ "> != </option>");								    			
								    		
								    	%>
						              </select></td>
				 </tr><tr><td align="right">Value:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" ><input align="center" class="txt1" id="Value" name="Value"  type="text" value="" onkeypress='return isValue(event.charCode)' /></td>
				 </tr><tr><td align="right">Action:</td>
				  <td width="10">&nbsp;</td>
									<td align="left"  >
								    	<select class="txt1" name="SelectAction" size="1" >
						                	<option value="Reject" <%=getSelect(isNull(request.getParameter("SelectAction")),"Reject") %> >Reject</option>
						                	<option value="Cond.Reject" <%=getSelect(isNull(request.getParameter("SelectAction")),"Cond.Reject") %> >Cond.Reject</option>
						                	<option value="BM" <%=getSelect(isNull(request.getParameter("SelectAction")),"BM") %> >Branch Manager</option>
						                	<option value="AM" <%=getSelect(isNull(request.getParameter("SelectAction")),"AM") %> >Area Manager</option>
						                	<option value="NM" <%=getSelect(isNull(request.getParameter("SelectAction")),"NM") %> >Network Manager</option>
						                	<option value="EVP" <%=getSelect(isNull(request.getParameter("SelectAction")),"EVP") %> >Executive Manager</option>
						                	
						              	</select>
						              </td>
				 </tr>
				 
				 <tr><td align="right">Active:</td>
				 <td width="10">&nbsp;</td>
									<td align="left" ><input class="txt1" name="CheckBoxActive" type="checkbox"  value="Y" <%= getCheckBox(request.getParameter("CheckBoxActive")) %> /></td>										
				 </tr>
				 <tr><td colspan="3" align="center" ><input class="btn1" type="submit" name="Submit" value="Add" onClick="doSubmit('add')" /></td></tr>
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
<%
connect.close();
%>