<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="db.DatbaseConnectionMsSQL" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add Business Role</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css" />

<script type="text/javascript">

	function doSubmit(cmd){
		
		if(confirm("ยืนยันทำรายการ")){
			var mainForm = document.getElementById("mainForm")
			
			var job = document.getElementById("job").value;
			var role = document.getElementById("role").value;
			
			if(job.trim()==''){
				alert('โปรดเลือก Job Title ก่อน Add');
			}else if(role.trim()==''){
				alert('โปรดเลือก Role ก่อน Add');
			}else{
				if(cmd == 'add'){
					mainForm.setAttribute("action","logicApproverAddServlet.jsp");
					mainForm.submit();
				}			
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

public boolean checkJobInRBFront(String jobIAM){
	boolean rs = true;
	Connection connect = DatbaseConnection.getConnectionMySQL();
	try {
		String sql = "select JobName from tblmt_lvjob j where j.JobName like ? ";
		PreparedStatement preparedStatement = connect.prepareStatement(sql);
		preparedStatement.setString(1,jobIAM);
		ResultSet rsJobPass = preparedStatement.executeQuery();
		if(rsJobPass.next()){
			rs = true;
		}else{
			rs = false;
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		try {	connect.close();} catch (Exception e) {	}			
	}	
	return rs;
}
%>

<%
  
///////////////////////////////////////////////////////////////////////////////////////////

			Connection connectMy = DatbaseConnection.getConnectionMySQL();
			 String sqlRole = "select distinct RoleName "+
			 			"from tblmt_role t "+ 
					 	"order by t.RoleName ";
			 PreparedStatement preparedStatementMy = connectMy.prepareStatement(sqlRole);
			ResultSet rsRole = preparedStatementMy.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////			
			
			Connection connectMs = DatbaseConnectionMsSQL.getConnectionMsSQL();
			String sql2 = "select DISTINCT i.Job_Title_EN from IAM i where i.Job_Title_EN is not null order by Job_Title_EN ";
			PreparedStatement preparedStatementMs = connectMs.prepareStatement(sql2);
			ResultSet rsJobTitle = preparedStatementMs.executeQuery();
///////////////////////////////////////////////////////////////////////////////////////////

			String sqlCorp = "select * from tblmt_lvcorpstaff t order by CONVERT(t.lv,UNSIGNED INTEGER)  ";
			PreparedStatement preparedStatementMy2 = connectMy.prepareStatement(sqlCorp);
			ResultSet rsCorp = preparedStatementMy2.executeQuery();

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
				<% 								
					if( session.getAttribute("success")!=null )
						out.println(session.getAttribute("success"));
					
					session.removeAttribute("success");
				%>
							        
		         <table border="0" cellpadding="0" cellspacing="0" class="tbl_search" >
		          <tr><td width="100"align="right">Job Title: </td>
		          	  <td width="10">&nbsp;</td>
									<td align="left" ><select class="txt1" id="job"  name="job" size="1" >
										<option value="">Choose Job Title</option>
										<% 
											while(rsJobTitle.next()){
												String jobT = rsJobTitle.getString("Job_Title_EN");
												if(checkJobInRBFront(jobT)){
													out.println("<option value=\""+jobT+"\""+getSelect(isNull(request.getParameter("job")),jobT)+" >"+jobT+"</option>");
												}												
											}
										%>
						              </select></td>
				  </tr><tr><td align="right">Role Name:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" ><select class="txt1" id="role"  name="role" size="1" >
										<option value="">Choose Role Name </option>
										<% 
											while(rsRole.next()){
												String jobR = rsRole.getString("RoleName");
												out.println("<option value=\""+jobR+"\"" +getSelect(isNull(request.getParameter("role")),jobR)+" >"+jobR+"</option>");
											}
										%>
						              </select></td>
				  </tr>
				  
				  <tr><td align="right">Requester:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" >
										<select class="txt1" name="SelectRequester" size="1" >
						                	<option value="All" <%=getSelect(isNull(request.getParameter("SelectRequester")),"All") %> >All</option>
						                	<option value="Branch" <%=getSelect(isNull(request.getParameter("SelectRequester")),"Branch") %> >Branch</option>
						                	<option value="Area" <%=getSelect(isNull(request.getParameter("SelectRequester")),"Area") %> >Area</option>
						                	<option value="Region" <%=getSelect(isNull(request.getParameter("SelectRequester")),"Region") %> >Network</option>
						                	<option value="Division" <%=getSelect(isNull(request.getParameter("SelectRequester")),"Division") %> >HQ</option>						                	
						              	</select>
						              	</td>
				 </tr>
				 <tr><td align="right">Corperate:</td>
				  <td width="10">&nbsp;</td>
				   					<td align="left" ><select class="txt1" id="SelectCorp"  name="SelectCorp" size="1" >
				   						<option value="">Choose Corperate </option>
										<% 
											while(rsCorp.next()){
												String lv = rsCorp.getString("lv");
												String CorpName = rsCorp.getString("CorpStaff");
// 												System.out.println(lv+":"+corp);
												out.println("<option value=\""+lv+"\" >"+CorpName+"</option>");
											}
											rsCorp.beforeFirst();
										%>
						              </select></td>
				 </tr>
				 <tr><td align="right">Approver:</td>
				  <td width="10">&nbsp;</td>
									<td align="left" >
										<select class="txt1" name="SelectApprover" size="1" >						                	
						                	<option value="BM" <%=getSelect(isNull(request.getParameter("SelectApprover")),"BM")%> >Branch Manager</option>
						                	<option value="AM" <%=getSelect(isNull(request.getParameter("SelectApprover")),"AM")%> >Area Manager</option>
						                	<option value="NM" <%=getSelect(isNull(request.getParameter("SelectApprover")),"AM")%> >Network Manager</option>
						                	<option value="Supervisor" <%=getSelect(isNull(request.getParameter("SelectApprover")),"Supervisor")%> >Supervisor</option>
						                	<option value="EVP" <%=getSelect(isNull(request.getParameter("SelectApprover")),"EVP")%> >EVP</option>						                	
						              	</select></td>
						              	
				</tr><tr><td align="right">DefaultApp:</td>
						<td width="10">&nbsp;</td>		    
						           <td align="left" >
						           		<select class="txt1" name="SelectDefaultApp" >
									                	
									                	<option value="BM" <%=getSelect(isNull(request.getParameter("SelectDefaultApp")),"BM")%> >Branch Manager</option>
									                	<option value="AM" <%=getSelect(isNull(request.getParameter("SelectDefaultApp")),"AM")%> >Area Manager</option>
									                	<option value="NM" <%=getSelect(isNull(request.getParameter("SelectDefaultApp")),"NM")%> >Network Manager</option>
									                	<option value="Supervisor" <%=getSelect(isNull(request.getParameter("SelectDefaultApp")),"Supervisor")%> >Supervisor</option>
									                	<option value="EVP" <%=getSelect(isNull(request.getParameter("SelectDefaultApp")),"EVP")%> >EVP</option>						                	
									              	</td></select> 
								  </td>  	
						          
				 </tr><tr><td align="right">Active:</td>
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
connectMy.close();
connectMs.close();
%>