<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=charset=UTF-8" />
<title>RBFrontWorkFlow</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link REL="SHORTCUT ICON" HREF="../favicon.ico">
<style type="text/css">
<!--
.style3 {color: #FF0000}
-->
</style>
</head>
<script type="text/javascript">
	
	function doSubmit(cmd){
		
		if(document.getElementById("userID").value==''){
			alert('โปรดใส่ username');
		}else if(document.getElementById("passUser").value==''){
			alert('โปรดใส่ password');
		}else{
			var mainForm = document.getElementById("mainForm")
			mainForm.setAttribute("action","mainLogin.jsp");
			mainForm.submit();
		}
			
	}
	
	function isENGNUM(input){
		
		var ua = window.navigator.userAgent;
	    var msie = ua.indexOf("MSIE ");

// 	    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
// 	    {
// 	        alert(parseInt(ua.substring(msie + 5, ua.indexOf(".", msie))));
// 	    }
	    
	    if(parseInt(ua.substring(msie + 5, ua.indexOf(".", msie)))<=8){
	    	alert('ระบบรองรับตั้งแต่   internet explorer 9 เป็นต้นไป')
	    	flage = false;
	    }else{   
			var flage = (input >= 65 && input <= 90)||(input >= 97 && input <= 122)||(input >= 48 && input <= 57)||input == 32||input ==43||input ==46; //43+,32' ',46.;
			if(!flage){
				alert('กรุณาใส่่ภาษาอังกฤษหรือตัวเลขเท่่านั้น')
			}    	
	    }
		return flage;
	}
	
</script>

<% 
// 	String rs = null;
// 	//System.out.println(session.getAttribute("resultLogin"));
// 	if( session.getAttribute("resultLogin") != null ){
// 		rs= session.getAttribute("resultLogin").toString();
// 	}else{
// 		rs="false";
// 	}
//    session.removeAttribute("resultLogin");

%>

<body >

<div id="container">
	<div id="header">
    	<table border="0" cellpadding="0" cellspacing="0" class="header_banner" width="100%" height="90">
        <tr>
        	<td class="header_logo" width="229"><img src="imgs/spacer.gif" style="border:0;" /></td>
            <td><span class="header_caption">RBFront Custom Workflow</span></td>
        </tr>
        </table>
        
        <!-- top menu -->
    	<ul style=" visibility:hidden;">
        	<li><a href="#" class="active">SystemStatus</a></li>
        </ul>
    	<!-- top menu -->
    
    </div>
    
    <div id="content">
   
    <!-- Main Content -->
    <div id="main_content">
    	<form id="mainForm" method="post" action="">
    	<table border="0" cellpadding="0" cellspacing="0" width="100%">
        	<tr>
            	<td align="center">
                	<table border="0" cellpadding="0" cellspacing="0" width="400" class="tbl_login">
                    	<tr>
                        	<td align="center" class="header_tb" colspan="3">Log In</td>
                        </tr>
                        <tr>
                        	<td height="5" colspan="3"><img src="imgs/spacer.gif" style="border:0;" /></td>
                        </tr>
						<% 	//System.out.println("l"+session.getAttribute("resultLogin"));		
							if( session.getAttribute("resultLogin")!=null ){
								%>
								<tr><td align="center" colspan="3" ><span id="msg1" class="style3">								
								<% 	
									String loginFail = (String)session.getAttribute("loginFail");
									
									if( loginFail != null )
				 						out.println(session.getAttribute("loginFail"));				
									session.removeAttribute("loginFail");
								%>								
								</span></td></tr>
								<%
							}
							session.removeAttribute("resultLogin");
						%>
                        <tr>
                        	<td align="right" height="30">Username :</td>
                            <td><img src="imgs/spacer.gif" style="border:0;" /></td>
                            <td align="left"><input type="text" class="txt1" name="userID" id="userID" /></td>
                        </tr>
                        <tr>
                        	<td align="right" height="35">Password :</td>
                            <td><img src="imgs/spacer.gif" style="border:0;" /></td>
                            <td align="left"><input type="password" class="txt1" name="passUser" id="passUser" /></td>
                        </tr>
                        <tr>
                        	<td align="right" height="30"><img src="imgs/spacer.gif" style="border:0;" /></td>
                            <td><img src="imgs/spacer.gif" style="border:0;" /></td>
                            <td align="left"><input type="submit" class="btn1" id="Submit" value="Login" onClick="doSubmit('login')" /></td>
                        </tr>
                        <tr>
                        	<td height="5" colspan="3"><img src="imgs/spacer.gif" style="border:0;" /></td>
                        </tr>
                    </table>
                </td>
            </tr>
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
