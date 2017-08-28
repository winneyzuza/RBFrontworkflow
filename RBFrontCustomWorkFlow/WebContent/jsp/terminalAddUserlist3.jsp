
<%@ page contentType="text/html;charset=UTF-8"%>
 
<%@ page import="java.io.*" %>

<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%@ page import="servlet.UploadUserlist3" %>

<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="servlet.AuditLog" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta equiv="Content-Type" content="text/html; charset=TIS-620">

<title>Insert title here</title>
</head>
<body>
<script language="javascript">
	function forwardPage(param){
		if(param == 'y')
	    	window.location.href = "terminal.jsp?success=y";
		else if(param == 'n')
			window.location.href = "terminal.jsp?success=n";
		else if(param == 'c')
			window.location.href = "terminal.jsp?success=c";
	}
</script>
	
	<%	

System.out.println("Start TerminalAddFile.jsp ");
String success = "";
boolean  check = true;
int count =0;
Connection connect = null;
PreparedStatement preparedStatement = null;
PreparedStatement preparedStatement2 = null;


String urlName = request.getParameter("file");
System.out.println("urlName:"+urlName);
// if(urlName==null){
// 	success = "ยังไม่ได้เลือก file สำหรับ  upload";
// 	check = false;
// }else if(urlName.indexOf("csv")>0){
// 	success = "เลือกชนิด file ไม่ถูกต้อง";
// 	check = false;
// };

if(check){
		//to get the content type information from JSP Request Header
		String contentType = request.getContentType();
		//here we are checking the content type is not equal to Null and

		if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
// 			System.out.println("s3:"+success);
			DataInputStream in = new DataInputStream(
					request.getInputStream());
			//we are taking the length of Content type data
			int formDataLength = request.getContentLength();
			byte dataBytes[] = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			//this loop converting the uploaded file into byte code
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead,
						formDataLength);
				totalBytesRead += byteRead;
			}
			String file = new String(dataBytes,StandardCharsets.UTF_8);
			if(file.indexOf("filename=\"\"")>0){
				success = "ยังไม่ได้เลือก file สำหรับ  upload";
				check = false;
			}else if(file.indexOf("csv")<0 && file.indexOf("CSV")<0){
				success = "เลือกชนิด file ไม่ถูกต้อง";
				check = false;
			};
			
// 			if(file.indexOf("StartTerminalInformation")<0){
// 				check = false;
// 				success = "file ยังไม่มี Header StartTerminalInformation";
// 			}
			
// 			if(file.indexOf("EndTerminalInformation")<0){
// 				check = false;
// 				success = success+"file ยังไม่มี Footer EndTerminalInformation";
// 			}

			if(check){
				
				int indexStart = file.indexOf("filename=\"");
				indexStart = file.indexOf("\n", indexStart) + 1;
				indexStart = file.indexOf("\n", indexStart) + 1;
				indexStart = file.indexOf("\n", indexStart) + 1;
				int indexEnd = file.length();
				//file = file.substring(file.indexOf("StartTerminalInformation")+26,file.indexOf("EndTerminalInformation"));
				file = file.substring(indexStart,indexEnd);
				
				//System.out.println("file : "+file);
				UploadUserlist3 u = new UploadUserlist3();
				u.insertTerminal(file);
				success = u.getSuccess();
				
				AuditLog al = new AuditLog();
			    al.setModule("ManualUploadUserlist3");
			    al.setDesc(success);
			    al.setStatus("C");
			    al.insertAuditLog();
				//u.importHR();
	
			}	
		}
}

System.out.println("success:"+success);
session.setAttribute("success", success);
response.sendRedirect("terminal.jsp");


	%>
	
</body>
</html>