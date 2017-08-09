<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Request</title>

<link href="Datepicker_files/bootstrap.css" rel="stylesheet">
<link href="Datepicker_files/datepicker.css" rel="stylesheet">

<script src="Datepicker_files/jquery.js"></script>
<script src="Datepicker_files/bootstrap-datepicker.js"></script> 
<script>
$(function(){
window.prettyPrint && prettyPrint();
	$('#start_d').datepicker({
	format: 'yyyy-mm-dd'
	});
	$('#stop_d').datepicker({
	format: 'yyyy-mm-dd'
	});
	$('#holiday').datepicker({
		format: 'yyyy-mm-dd'
	});
});
</script>

<style type="text/css">
<!--
.style3 {color: #FF0000}
-->
</style>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<script type="text/javascript">
	function doSubmit(cmd){
		
		var mainForm = document.getElementById("mainForm")
		if(cmd == 'search'){
			mainForm.setAttribute("action","request.jsp");
			mainForm.submit();
		}else{		
			if(confirm("ยืนยันทำรายการ")){	
								
				if(cmd.substring(0,3)=='add'){

					mainForm.setAttribute("action","holidayAdd.jsp");
					mainForm.submit();
				}else if(cmd.substring(0,6)=='remove'){
					var i = cmd.substring(6)
					document.getElementById("HRemove").value = i;
// 					alert(i);
// 					alert(document.getElementById("HRemove").value);
					mainForm.setAttribute("action","holidayRemove.jsp");
					mainForm.submit();
				}
			}
			
		}	
	}
	
	function resetTextBox(){
// 		document.getElementById ( "msg2" ).innerText = '';
		
		document.getElementById("branch_day").value = '';
		document.getElementById("stop_d").value = '';
		
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
		
		if( session.getAttribute("resultLogin") == null && !Configuration.TYPE.equals("DEV") ){
			response.sendRedirect("main.jsp");
		}else{
			String module = session.getAttribute("authen").toString();
			//module = "Administrator";
			System.out.println("authen:"+module);
			out.println("<input type=\"hidden\" id=\"module\" value="+module+">");
			if(module.indexOf("Audit")>-1){
				read= true;
			}
		}
		

		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		String sql = "select * from tblmt_holiday ORDER BY branchDay ,Holiday ";
		preparedStatement = connect.prepareStatement(sql);
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
   		   	<ul>
	        	<li><a href="..\jsp\logicValidate.jsp">Validation</a></li>
	            <li><a href="..\jsp\logicApprover.jsp" >Approver</a></li>
	            <li><a href="..\jsp\logicJobLv.jsp" >Job Title</a></li>
	            <li><a href="..\jsp\logicCorpLv.jsp" >Corporate Staff</a></li>
	            <li><a href="..\jsp\holiday.jsp" class="active" >Holiday</a></li>
	        </ul>
    <!-- Main Content -->
    <div id="main_content">
    
		<form id="mainForm" method="post" action="" >
		    <input type="hidden" id="RequestID" name="RequestID" value="">
		    <input type="hidden" id="TypeAdd" name="TypeAdd" value="">
		    <br><br>
		    <table border="0" cellpadding="0" cellspacing="0" class="tbl_search">
        	<tr>
            	<td width="30">&nbsp;</td>
                <td align="right">Branch Day</td>
                <td width="5">&nbsp;</td>
                <td align="left"><select class="txt1"  id="branch_day"  name="branch_day" >
									    <option value="3" <%=getSelect(isNull(request.getParameter("branch_day")),"3") %> >3 day</option>										
							            <option value="5" <%=getSelect(isNull(request.getParameter("branch_day")),"5") %> >5 day</option>
							            <option value="6" <%=getSelect(isNull(request.getParameter("branch_day")),"6") %> >6 day</option>
							            <option value="7" <%=getSelect(isNull(request.getParameter("branch_day")),"7") %> >7 day</option>
							            
						              </select>
				</td>
                <td width="20">&nbsp;</td>
                <td align="right">Holiday</td>
                <td width="5">&nbsp;</td>
                <td align="left"><input type="text" class="txt1" maxlength="50" id="stop_d" name="stop_d" value="<%=isNull(request.getParameter("stop_d"))%>" readonly /></td>

		        <td width="20">&nbsp;</td>
		        <td align="left">
			        <input type="button" id="btAdd" value="Add " class="btn1" onClick="doSubmit('add')"/>
			        <input type="button" id="btReset" value="Reset" class="btn1" onClick="resetTextBox()"/>
		        </td>		                              
            </tr>
            <tr>
            	<td width="20">&nbsp;</td>
            	<td colspan="15" align="center">
            	</td>
            </tr>
        </table>
        <span id="msg2" class="style3"><% 	
		
							if( session.getAttribute("success")!=null )
		 						out.println(session.getAttribute("success"));
		
							session.removeAttribute("success");
						%></span>

      
                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="tbl_content">						  					  
						<%  int i =0;
						    int j =0;
							String tempJ = "";
				
							  	  while(resultSet.next() )
								  { i++;j++;
								  	String branch_day = resultSet.getString("branchDay");								  	
								  	String holiday 	= resultSet.getString("Holiday");							  	
								  	
								  	
								  	if(!tempJ.equals(branch_day)){
								  		%>
								  		<tr><td colspan=50>
								  			<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tbl_content">
								  				<tr><td class="header_tbl_content2" > &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;<%=branch_day %> Day</td>
								  				</tr>
								  			</table>
								  		</td></tr>
								  		<% 
								  		tempJ=branch_day;
								  		j=1;
								  		out.println("<tr>");
								  	}
								  	if((j-1)%3==0){
								  		out.println("</tr><tr>");
								  	}
								  %>
								  <td width="100" >&nbsp;</td><td><%=j+". " %></td>
								  <td width="10" >&nbsp;</td>
								  <td ><%=holiday %></td>
								  <td width="10" >&nbsp;</td>
 								  
								  <td width="50" class="tbl_row1"  >
								  <% if(!read){ %>
								  	<img src="imgs/delete_icon.png" style="border:0;" height="25" title="Delete" onClick="doSubmit('remove<%=i%>')" />
								  <% } %>
								  </td>
									<input type="hidden" id="PKey<%=i%>" name="PKey<%=i%>" value="<%=branch_day+"$"+holiday%>">									
								  <%
								  }
							connect.close();
						%>	</tr>
							<input type="hidden"  name="numRow" value="<%=i%>">
							<input type="hidden"  id="HRemove" name="HRemove" value="">
						</table>
           	<br>
    		<br>
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
<script src="js/menu.js"></script>
<script type="text/javascript">
	var ac = document.getElementById("BusinessChange");
	ac.setAttribute("class", "active");
</script>
