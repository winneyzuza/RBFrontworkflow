<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="mt.LvJob" %>

<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Businues Role</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>

<script type="text/javascript">
	function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}
	
	function include(arr, obj) {
	    for(var i=0; i<arr.length; i++) {
	        if (arr[i] == obj) return true;
	    }
	}
	
	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		var validGroup = true;
		var validNum = true;
		var tempVal = "";
		
		if(confirm("ยืนยันทำรายการ")){			
			if(cmd == 'update'){
				
				
				var count = document.getElementById("rows").value;
				var countRName = document.getElementById("countRName").value;
				
				var arr = [];
				
				var a = new Array();
				
				for(k=1;k<=countRName;k++){
					var rName = document.getElementById('RName'+k).value;
					arr.push(rName);
				}
				
				
				for(i=1;i<=count;i++){
					var type = document.getElementById('Validate'+i).value;
					var val = document.getElementById('Value'+i).value;
				
					if(type.indexOf("Group")>-1){
						
						if(!include(arr, val)){
							validGroup = false;
							tempVal = val;
							break;
						}
					}else {
						if(!isNumber(val)){
							validNum = false;
							tempVal = val;
							break;
						}
					} 
				}
				
				if(!validGroup){
					alert("ค่า value " + tempVal +" ไม่ถูกต้อง EX.(AT,CS,AT+CS,SC,SSC,...)");
					return false;
				}else if(!validNum){
					alert("ค่า value " + tempVal +" ไม่ถูกต้อง input only positive number");
					return false;
				}else{
					mainForm.setAttribute("action","logicValidateUpdate.jsp");
					mainForm.submit();
				}
					
				
				
			}else if(cmd.substring(0,6)=='remove'){
				var i = cmd.substring(6)
				document.getElementById("vRemove").value = document.getElementById("PKey"+i).value;
// 				alert(document.getElementById("vRemove").value);
				mainForm.setAttribute("action","logicValidateRemove.jsp");
				mainForm.submit();
			}
		}
		
	}
	
</script>

<%!
public String getSelect(String value,String AP){
	//System.out.println(value);
	if(value.equals(AP))
		return "selected";
	return "";
} 

public String getCheckBox(String Y){
	//System.out.println(Y);
	if(Y.equals("Y"))
		return "checked=\"checked\"";
	return "";
}

public String getStatement(String Validate,String BranchDay) throws SQLException{
	String Statement="";
// 	System.out.println(Validate+":"+Validate.indexOf("Group")+BranchDay);
	if("NumCA".equals(Validate)){
		Statement = "branch สาขา "+BranchDay+" วัน";
	}else if("NumRole".equals(Validate)){
		Statement = "branch สาขา "+BranchDay+" วัน";
	}else if(Validate.indexOf("Group")>-1){
		if("All".equals(BranchDay)){
			Statement = "ทุกสาขา ";
		}else if("Corp3".equals(BranchDay)){
			Statement = "Corp.Staff3 และตำแหน่งงาน พนักงานธนกิจ หรือ พนักงานธุรกิจ";
		}else if("Corp2".equals(BranchDay)){
			Statement = "Corp.ตั้งแต่ Staff2 ลงไป";
		}else{
			String Job = LvJob.getJobByLv(BranchDay);
			Statement = "ตำแหน่งสูงกว่า "+Job;
		}
		
	}
		
	return Statement;
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

		Connection connect = null;
		PreparedStatement preparedStatement = null;
  
		connect = DatbaseConnection.getConnectionMySQL();

			 String sql = "select PKey,Validate,DayBranch,Operation,Value,Action,Active from tblmt_validation order by Validate,DayBranch ";
			preparedStatement = connect.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			
			
			String sqlRole = "select RoleName from tblmt_role order by RoleName ";
			preparedStatement = connect.prepareStatement(sqlRole);
			ResultSet rsRoleName = preparedStatement.executeQuery();	
///////////////////////////////////////////////////////////////////////////////////////////

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
	        	<li><a href="..\jsp\logicValidate.jsp" class="active">Validation</a></li>
	            <li><a href="..\jsp\logicApprover.jsp" >Approver</a></li>
	            <li><a href="..\jsp\logicJobLv.jsp" >Job Title</a></li>
	            <li><a href="..\jsp\logicCorpLv.jsp" >Corporate Staff</a></li>
	            <li><a href="..\jsp\holiday.jsp" >Holiday</a></li>
	        </ul>
	    <!-- Main Content -->
	    <div id="main_content">              
						<form id="mainForm"  method="post" action="" >
		
						<% 
							int j=0;
							while(rsRoleName.next()){
								j++;
								String RName = rsRoleName.getString("RoleName");%>
								  <input type="hidden" id="RName<%=j%>" value="<%=RName%>">
							<%	
							}
						%><input type="hidden" id="countRName" value="<%=j%>">
						
						<% 								
							if( session.getAttribute("success")!=null )
								out.println(session.getAttribute("success"));
				
						session.removeAttribute("success");
						%>
						<br>
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">
							<tr>
								<td class="header_tbl_content" >เงือนไขการตรวจสอบ   </td>
								<td width="10">&nbsp;</td>
						    	<td id="imgAdd" align="left" ><img src="imgs/add_icon.png" style="border:0;" height="24" title="Add" onclick="window.open('..\\jsp\\logicValidateAdd.jsp', '_blank','width=720,height=400')" /></td>
						    	<td align="right"><span id="msg1" width="200">
						    	&nbsp;&nbsp;&nbsp;
								<%
									
									if( "as".equals(session.getAttribute("module")) )
										out.println(" แก้ไขรายการเรียบร้อย");
									if( "ae".equals(session.getAttribute("module")) )
										out.println(" แก้ไขรายการมีปํญหา !!!");
									if( "rs".equals(session.getAttribute("module")) )
										out.println(" ลบรายการเรียบร้อย");
									if( "re".equals(session.getAttribute("module")) )
										out.println(" ลบรายการมีปํญหา !!!");
									session.removeAttribute("module");
								%>
								&nbsp;&nbsp;&nbsp;		
								</span>						</td>
							</tr>
						</table>
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">
						  <tr>
							<td class="header_tbl_content" >Topic</td>
							<td class="header_tbl_content" >Statement</td>
							<td class="header_tbl_content" >Operation</td>
							<td class="header_tbl_content" >Value</td>
							<td class="header_tbl_content" >Action</td>
							<td width="10" class="header_tbl_content_last" >Active</td>
							<td width="30" ></td>
						  </tr>
						  
						<%  int i =0;
								  
							if(!read){
							  	  while(rs.next() )
								  { i++;
								  	String pkey = rs.getString("PKey");
								  	String statement = getStatement(rs.getString("Validate"),rs.getString("DayBranch"));
								  %>
								  <input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>">
								  <tr>
								  <td align="center" ><input size="10" align="left" class="txt1" style="background-color:#f3defc" id="Validate<%=i%>" name="Validate<%=i%>"  type="text" value="<%=rs.getString("Validate") %>" readonly /></td>
								  <td align="center" ><input size="45" align="center" class="txt1" style="background-color:#f3defc" name="statement<%=i%>"  type="text" value="<%=statement %>" readonly /></td>
									<td>
								    	<select class="txt1" name="SelectOper<%=i %>" size="1" >								    	
								    	<% 
								    		String topic = (String)rs.getString("Validate");
								    		boolean g = false;
								    		if("Group1".equals(topic)||"Group2".equals(topic)||"Group3".equals(topic))
								    			g=true;
								    		if(g){
								    			out.println("<option value=\"=\""  + getSelect(rs.getString("Operation"),"=")+ "> =  </option>");
								    		}else{
									    	   	out.println("<option value=\"<=\" "+getSelect(rs.getString("Operation"),"<=")+"> <= </option>");
									    		out.println("<option value=\"<\" "+getSelect(rs.getString("Operation"),"<")+"> < </option>");
									    		out.println("<option value=\">=\" "+getSelect(rs.getString("Operation"),">=")+"> >= </option>");
									    		out.println("<option value=\">\" "+getSelect(rs.getString("Operation"),">")+"> > </option>");
									    		out.println("<option value=\"=\" "+getSelect(rs.getString("Operation"),"=")+"> = </option>");
									    		out.println("<option value=\"!=\" "+getSelect(rs.getString("Operation"),"!=")+"> != </option>");
								    		}
								    		
								    	%>								
						              	</select>
								    </td>
								    	<td align="center" ><input align="center" class="txt1" id="Value<%=i %>"  name="Value<%=i %>"  type="text" value="<%=rs.getString("Value") %>" /></td>
								    <td>
								    	<select class="txt1" name="SelectAction<%=i %>" size="1" >
						                	<option value="Reject" <%= getSelect(rs.getString("Action"),"Reject") %>>Reject</option>
						                	<option value="Cond.Reject" <%= getSelect(rs.getString("Action"),"Cond.Reject") %>>Cond.Reject</option>
						                	<option value="BM" <%= getSelect(rs.getString("Action"),"BM") %>>Branch Manager</option>
						                	<option value="AM" <%= getSelect(rs.getString("Action"),"AM") %>>Area Manager</option>
						                	<option value="NM" <%= getSelect(rs.getString("Action"),"NM") %>>Network Manager</option>
						                	<option value="EVP" <%= getSelect(rs.getString("Action"),"EVP") %>>Executive Manager</option>
						                	
						              	</select>
								    </td>
								    <td align="center" ><input class="txt1"  name="CheckBoxActive<%=i %>" type="checkbox" value="Y" <%= getCheckBox(rs.getString("active")) %> /></td>
								    <td class="tbl_row1"  ><img src="imgs/delete_icon.png" style="border:0;" height="24" title="Delete" onClick="doSubmit('remove<%=i%>')" /></td>
								    </tr>
								    
						<%
								  }
							  	  %><input type="hidden" id="rows" value="<%=i%>">
						<%
							}else{ ///////  read only
								  
							  	  while(rs.next() )
								  { i++;
								  	String pkey = rs.getString("PKey");
								  	String statement = getStatement(rs.getString("Validate"),rs.getString("DayBranch"));
								  %>
								  <input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>">
								  <tr>
								  <td align="center" ><input align="center" class="txt1" style="background-color:#f3defc" name="Validate<%=i%>"  type="text" value="<%=rs.getString("Validate") %>" readonly /></td>
								  <td align="center" ><input align="center" class="txt1" style="background-color:#f3defc" name="statement<%=i%>"  type="text" value="<%=statement %>" readonly /></td>
									<td>
										<input align="center" class="txt1" style="background-color:#f3defc"   type="text" value="<%=rs.getString("Operation") %>" readonly />
								    </td>
								    <td align="center" >
								    	<input align="center" class="txt1" style="background-color:#f3defc" name="Value<%=i %>"  type="text" value="<%=rs.getString("Value") %>" readonly /></td>
								    <td>
								    	<input align="center" class="txt1" style="background-color:#f3defc"  type="text" value="<%=rs.getString("Action") %>" readonly />
								    </td>
								    <td align="center" >
								    	<input align="center" class="txt1" style="background-color:#f3defc"  type="text" value="<%=rs.getString("active") %>" readonly />
								    </tr>
						<%
								  }
								
							}
							connect.close();
						%>
							<input type="hidden"  name="numRow" value="<%=i%>">
						 	<input type="hidden"  id="vRemove" name="vRemove" value="">
						</table>
						<div align="center">
						<input class="btn1" type="submit" id="btUpdate" value="Update" onClick="doSubmit('update')" />
						</div>
						</form>
						<br><br><br>
				    </div>
					 
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
	var ac = document.getElementById("BizLogic");
	ac.setAttribute("class", "active");
</script>
