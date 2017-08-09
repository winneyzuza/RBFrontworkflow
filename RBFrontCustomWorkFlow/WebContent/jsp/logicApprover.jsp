<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.DatbaseConnectionMsSQL" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Businesss Role</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>

<script type="text/javascript">

	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		
		if(confirm("ยืนยันทำรายการ")){			
			if(cmd == 'update'){
				mainForm.setAttribute("action","logicApproverUpdate.jsp");
				mainForm.submit();
			}else if(cmd.substring(0,6)=='remove'){
				var i = cmd.substring(6)
				document.getElementById("vRemove").value = document.getElementById("PKey"+i).value;
// 				alert(document.getElementById("vRemove").value);
				mainForm.setAttribute("action","logicApproverRemove.jsp");
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

public String convertJob(String sjob){
	String ljob=sjob;
	if (ljob == null) sjob = "NA";
	if(sjob.equals("BM")){
		ljob = "Branch Manager";
	}else if(sjob.equals("AM")){
		ljob = "Area Manager";
	}else if(sjob.equals("NM")){
		ljob = "Network Manager";
	}else {
		ljob = "Not Available";
	}
	return ljob;	
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
		Connection connectMs = null;
		PreparedStatement preparedStatement = null;
  
		connect = DatbaseConnection.getConnectionMySQL();
			 String sql = "select * from tblmt_approver t join tblmt_job j join tblmt_lvjob l on t.Job=j.NameEN and l.JobName=j.NameEN order by l.lv,t.Job,t.role ";
					 
			preparedStatement = connect.prepareStatement(sql);
// 			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////

			String sqlCorp = "select * from tblmt_lvcorpstaff t order by CONVERT(t.lv,UNSIGNED INTEGER)  ";
			PreparedStatement preparedStatementMy = connect.prepareStatement(sqlCorp);
			ResultSet rsCorp = preparedStatementMy.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////
			
			connectMs = DatbaseConnectionMsSQL.getConnectionMsSQL();
			String sqlOCType = "select DISTINCT OC_TYPE ocType from IAM where OC_TYPE is not null order by OC_TYPE ";
			PreparedStatement preparedStatementOCType = connectMs.prepareStatement(sqlOCType);
			ResultSet rsOcType = preparedStatementOCType.executeQuery();
			
			List<String> myList = new ArrayList<String>();
			while(rsOcType.next()){
				myList.add(rsOcType.getString("ocType"));
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
    	<ul id=menu style="width: 1250px;">
        </ul>
    	<!-- top menu -->
	    
	    </div>
	    
	    <div id="content">
	   		<ul>
	        	<li><a href="..\jsp\logicValidate.jsp" >Validation</a></li>
	            <li><a href="..\jsp\logicApprover.jsp" class="active" >Approver</a></li>
	            <li><a href="..\jsp\logicJobLv.jsp" >Job Title</a></li>
	            <li><a href="..\jsp\logicCorpLv.jsp" >Corporate Staff</a></li>
	            <li><a href="..\jsp\holiday.jsp" >Holiday</a></li>
	        </ul>
	    <!-- Main Content -->
	    <div id="main_content">              
						<form id="mainForm"  method="post" action="" >
						<% 								
							if( session.getAttribute("success")!=null )
								out.println(session.getAttribute("success"));
				
						session.removeAttribute("success");
						%>
						<br>
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">
							<tr><td class="header_tbl_content" >เงือนไขสิทธิ์อนุมัติตำแหน่งงาน</td>
								<td width="10"><img src="imgs/spacer.gif" style="border:0;" /></td>
						    	<td id="imgAdd" align="left" ><img src="imgs/add_icon.png" style="border:0;" height="24" title="Add" onclick="window.open('..\\jsp\\logicApproverAdd.jsp', '_blank','width=720,height=400')" /></td>
						    	<td align="right"><span id="msg1" width="200">
						    	&nbsp;&nbsp;&nbsp;
								<%
									
									if( "as".equals(session.getAttribute("module")) )
										out.println(" แก้ไขรายการเรียบร้อย");
									if( "ae".equals(session.getAttribute("module")) )
										out.println(" แก้ไขรายการมีปัญหา !!!");
									if( "rs".equals(session.getAttribute("module")) )
										out.println(" ลบรายการเรียบร้อย");
									if( "re".equals(session.getAttribute("module")) )
										out.println(" ลบรายการมีปัญหา !!!");
									session.removeAttribute("module");
								%>
								&nbsp;&nbsp;&nbsp;		
								</span>						</td>
							</tr>
						</table>
						
						<table align="center" border="0" cellpadding="0" cellspacing="0"  class="tbl_content">						  
<!-- 						  <tr><td class="header_tbl_content" >Seq</td> -->
<!-- 							<td class="header_tbl_content" >Job Title</td> -->
<!-- 							<td class="header_tbl_content" >Role Business</td> -->
<!-- 							<td class="header_tbl_content" >Approver</td> -->
<!-- 							<td class="header_tbl_content_last" >Active</td> -->
<!-- 						  </tr> -->
						  
						<%  int i =0;
						    int j =0;
							String tempJ = "";
								if(!read){
					
							  	  while(rs.next() )
								  { i++;j++;
								  	String pkey = rs.getString("PKey");
								  	
								  	String job 	= rs.getString("Job");
								  	String jobTH = rs.getString("NameTH");
								  	String role = rs.getString("Role");
								  	String reqter = rs.getString("Requester");
								  	String corp = rs.getString("CorpStaff");
								  	String approver = rs.getString("Approver");
								  	String DefaultApp = rs.getString("DefaultApp");								  		
								  	String active = rs.getString("Active");
								  	if(!tempJ.equals(job)){
								  		%>
								  		<tr><td colspan=15>
								  			<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
								  				<tr><td class="header_tbl_content2" ><%=jobTH+" ("+job+")" %>
								  					<input type="hidden" name="Job<%=i%>" value="<%=job%>">
								  					
								  					<td align="right" class="header_tbl_content2"><%="อำนาจอนุมัติกรณีขอนอกเหนือจากที่กำหนด:"%><select class="txt1" name="DefaultApp<%=i %>" >
									                	<option value="Supervisor" <%= getSelect(DefaultApp,"Supervisor") %>>Supervisor</option>
									                	<option value="BM" <%= getSelect(DefaultApp,"BM") %>>Branch Manager</option>
									                	<option value="AM" <%= getSelect(DefaultApp,"AM") %>>Area Manager</option>
									                	<option value="NM" <%= getSelect(DefaultApp,"NM") %>>Network Manager</option>
									                	<option value="EVP" <%= getSelect(DefaultApp,"EVP") %>>EVP</option>						                	
									              	</select>
									              	</td>
								  				</tr>
								  			</table>
								  		</td></tr>
								  		<% 
								  		tempJ=job;
								  		j=1;
								  	}else{
								  		%>
<%-- 								  		  <input type="hidden" name="Job<%=i%>" value="<%=job%>"> --%>
<%-- 								  		  <input type="hidden" name="DefaultApp<%=i%>" value="<%=DefaultApp%>"> --%>
								  		<%
								  	}
								  %>
								  <tr><td width="100" >&nbsp;</td><td><%=j+". " %></td>
 								  <td ><%=role %></td>
 								  <td width="20" >&nbsp;</td>
 								  <td ><%=" ผู้ขอสังกัด "%>&nbsp;</td>
 								  	<td>
<%-- 								    	<select class="txt1" name="SelectRequester<%=i %>" size="1" > --%>
<%-- 						                	<option value="All" <%= getSelect(reqter,"All") %>>All</option> --%>
<%-- 						                	<option value="Branch" <%= getSelect(reqter,"Branch") %>>Branch</option> --%>
<%-- 						                	<option value="Area" <%= getSelect(reqter,"Area") %>>Area</option> --%>
<%-- 						                	<option value="Region" <%= getSelect(reqter,"Region") %>>Network</option> --%>
<%-- 						                	<option value="Division" <%= getSelect(reqter,"Division") %>>HQ</option>						                	 --%>
<!-- 						              	</select> -->
						              	<select class="txt1" name="SelectRequester<%=i %>" size="1" >
						              		<option value="All" <%= getSelect(reqter,"All") %>>All</option>
						              		<%-- <option value="Branch" <%= getSelect(reqter,"Branch") %>>Branch</option> --%>
						              		<%-- <option value="Area" <%= getSelect(reqter,"Area") %>>Area</option> --%>
						              		<%-- <option value="Region" <%= getSelect(reqter,"Region") %>>Region</option> --%>
						              		<option value="Network" <%= getSelect(reqter,"Network") %>>Network</option>  
						              		<%-- <option value="Division" <%= getSelect(reqter,"Division") %>>HQ</option> --%>
						              		<%-- <option value="Sub Branch" <%= getSelect(reqter,"Sub Branch") %>>Sub Branch</option> --%>
						          
						              	<% 
// 											while(rsOcType.next()){
// 												String ocType = rsOcType.getString("ocType");
// 												out.println("<option value=\""+ocType+"\" >"+ocType+"</option>");
// 											}
// 						              		rsOcType.beforeFirst();
						              		
						              		for(String temp : myList) {
						              			if(temp.equals("Division")){
						              				out.println("<option value=\""+temp+"\" >"+"HQ"+"</option>");
						              			}else{
						              				out.println("<option value=\""+temp+"\" >"+temp+"</option>");
						              			}
						              			
						              		}
										%>
										</select>
								    </td>
								  <td width="20" >&nbsp;</td>
 								  <td ><%=" เจ้าของสิทธิ์มี Corporate Title ตั้งแต่ "%>&nbsp;</td>
 									<td align="left" ><select class="txt1" id="SelectCorp"  name="SelectCorp<%=i %>" size="1" >
										<% 
											while(rsCorp.next()){
												String lv = rsCorp.getString("lv");
												String CorpName = rsCorp.getString("CorpStaff");
// 												System.out.println(lv+":"+corp);
												out.println("<option value=\""+lv+"\"" +getSelect(lv,corp)+" >"+CorpName+"</option>");
											}
											rsCorp.beforeFirst();
										%>
						              </select></td>
 								  <td width="20" >&nbsp;</td>
								  <td ><%=" ติดอำนาจอนุมัติ  " %>&nbsp;</td>
								  	<td>
								    	<select class="txt1" name="SelectApprover<%=i %>" size="1" >
						                	<option value="Supervisor" <%= getSelect(approver,"Supervisor") %>>Supervisor</option>
						                	<option value="BM" <%= getSelect(approver,"BM") %>>Branch Manager</option>
						                	<option value="AM" <%= getSelect(approver,"AM") %>>Area Manager</option>
						                	<option value="NM" <%= getSelect(approver,"NM") %>>Network Manager</option>
						                	<option value="EVP" <%= getSelect(approver,"EVP") %>>EVP</option>						                	
						              	</select>
								    </td>
								  
								  <td width="50" align="center" ><input class="txt1"  name="CheckBoxActive<%=i %>" type="checkbox" value="Y" <%= getCheckBox(active) %> /></td>
								  <td width="50" class="tbl_row1"  ><img src="imgs/delete_icon.png" style="border:0;" height="24" title="Delete" onClick="doSubmit('remove<%=i%>')" /></td>
								  
								 
<!-- 								  <td> -->
<%-- 								  <select class="txt1" name="SelectApprover<%=i %>" size="1" > --%>
<%-- 					                <option value="BM" <%= getSelect(rs.getString("approve"),"BM") %>>Branch Manager</option> --%>
<%-- 					                <option value="AM" <%= getSelect(rs.getString("approve"),"AM") %>>Area Manager</option> --%>
<%-- 					                <option value="NM" <%= getSelect(rs.getString("approve"),"NM") %>>Network Manager</option> --%>
<!-- 					              </select></td> -->
<%-- 					              <td align="center" ><input class="txt1"  name="CheckBoxActive<%=i %>" type="checkbox" value="Y" <%= getCheckBox(rsBus.getString("active")) %> /></td> --%>
<%-- 					              <td class="tbl_row1"  ><img src="imgs/delete_icon.png" style="border:0;" height="24" title="Delete" onClick="doSubmit('remove<%=i%>')" /></td> --%>
<!-- 					              </tr> -->
									<input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>">									
								  <%
								  }
							  	  
								}else{  ///// readonly
									
									while(rs.next() )
									  { i++;j++;
									  	String pkey = rs.getString("PKey");
									  	
									  	String job 	= rs.getString("Job");
									  	String role = rs.getString("Role");
									  	String reqter = rs.getString("Requester");
									  	String approver = rs.getString("Approver");
									  	String DefaultApp = rs.getString("DefaultApp");								  		
									  	String active = rs.getString("Active");
									  	
									  	String jobTH = rs.getString("NameTH");
									  	String corp = rs.getString("CorpStaff");
									  	
									  	if(!tempJ.equals(job)){
									  		%>
									  		<tr><td colspan=15>
									  			<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
									  				<tr><td class="header_tbl_content2" ><%=job %>
									  					<input type="hidden" name="Job<%=i%>" value="<%=job%>">									  					
									  					<td align="right" class="header_tbl_content2">
									  						<%="อำนาจอนุมัติกรณีขอนอกเหนือจากที่กำหนด:"%>
									  						<input align="right" class="txt1" style="background-color:#f3defc"  type="text" value="<%= convertJob(DefaultApp) %>" readonly />					                	
										              	</td>
									  				</tr>
									  			</table>
									  		</td></tr>
									  		<% 
									  		tempJ=job;
									  		j=1;
									  	}
									  %>
									  <tr><td width="100" >&nbsp;</td><td><%=j+". " %></td>
	 								  <td ><%=role %></td>
	 								  <td width="20" >&nbsp;</td>
	 								  <td width="70" ><%=" ผู้ขอสังกัด: "%>
	 								  </td>
	 								  	<td>
											<input size="5" align="right" class="txt1" style="background-color:#f3defc"  type="text" value="<%=reqter %>" readonly />
									    </td>
									  <td width="20" >&nbsp;</td>
 								  	  <td width="225" ><%=" เจ้าของสิทธิ์มี Corporate Title ตั้งแต่ "%>&nbsp;</td>
 									  <td align="left" >
										<%  String CorpName = "";
											while(rsCorp.next()){
												String lv = rsCorp.getString("lv");
												CorpName = rsCorp.getString("CorpStaff");
// 												System.out.println(lv+":"+corp);
												if(lv.equals(corp)){
													break;
												}
											}
											rsCorp.beforeFirst();
										%>

						               <input size="5" align="right" class="txt1" style="background-color:#f3defc"  type="text" value="<%=CorpName %>" readonly /></td>
	 								  <td width="20" >&nbsp;</td>
									  <td ><%=" ติดอำนาจอนุมัติ: " %></td>
									  	<td>
											<input align="right" class="txt1" style="background-color:#f3defc"  type="text" value="<%=convertJob(approver) %>" readonly />
									    </td>
									    
									  <td width="20" >&nbsp;</td>
									  <td ><%=" Active: " %></td>
									    <td width="50" align="center">
									    	<input size="1" align="right" class="txt1" style="background-color:#f3defc"  type="text" value="<%=active %>" readonly />
									    </td>
									  </tr>
										<input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=pkey%>">									
									  <%
									  }
									
								}
							connect.close();
							connectMs.close();
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
