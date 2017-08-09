
<%@ page contentType="text/html;charset=UTF-8"%>
 
<%@ page import="java.io.*" %>

<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="validate.CheckFile" %>
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

System.out.println("Start examFileUpload.jsp ");
String success = "";
boolean  check = true;
boolean  check2 = true;
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

			if(check){
				
				try{					  
					  connect = DatbaseConnection.getConnectionMySQL();
					  
					  if(connect != null){
						  System.out.println("Database Connected.");
					  } else {
						  System.out.println("Database Connect Failed.");
					  }
					  
						int indexStart = file.indexOf("filename=\"");
						indexStart = file.indexOf("\n", indexStart) + 1;
						indexStart = file.indexOf("\n", indexStart) + 1;
						indexStart = file.indexOf("\n", indexStart) + 1;
						int indexEnd = file.length();
						//file = file.substring(file.indexOf("StartTerminalInformation")+26,file.indexOf("EndTerminalInformation"));
						file = file.substring(indexStart,indexEnd);
						//System.out.println(file);
						int s1 = 0;
						int l1 = file.indexOf("\n",s1);
						String lineAll[] = file.split("\n");
						
						//for(int i=0;i<10;i++){
						for(int i=0;i<lineAll.length-1;i++){
							count++;
							String line = lineAll[i];
// 							System.out.println(i+":"+line);
							if("".equals(line.trim())){
								System.out.println(line);
								continue;
							}
							
							int s = 0;
							int l = line.indexOf(",");
							String empID = line.substring(s,l);							
							check2 = check2 && CheckFile.checkEmpID(empID);
							
							s=l+1;	l=line.indexOf(",",s);
							String exam = line.substring(s,s+1);
							check2 = check2 && CheckFile.checkYN(exam);
							
							//System.out.println("<br>test2="+OrgCode+":"+TechnicalRole+":"+BusinessRole+":"+TerminalID);
							if(!check2){
								success = "file formate ไม่ถูกต้อง";
								break;
							}else{

								String sql = "UPDATE tblmt_employeeinfo SET Exam=? WHERE EmpID= ? ";							
								preparedStatement = connect.prepareStatement(sql);
							    preparedStatement.setString(1, exam);
							    preparedStatement.setString(2, empID);
								try {
							      	preparedStatement.executeUpdate();
			 					  	success = "อัพเดดข้อมูล  exam เรียบร้อย (Exam: Update Success)";
								} catch(Exception e) {
									success = "อัพเดตข้อมูล exam ผิดพลาด (Exam: Update Fail)";
								}
							}

						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("error terminalAddFile:"+e.getMessage());
						e.printStackTrace();
						success = "format file column "+count+" ไม่ถูกต้อง";
					} finally{
						connect.close();
					}
			}	
		}
}

AuditLog al = new AuditLog();
al.setModule("ManualUploadExam");
al.setDesc(success);
al.setStatus("C");
al.insertAuditLog();		
		
System.out.println("success:"+success);
session.setAttribute("success", success);
response.sendRedirect("importExport.jsp");


	%>
	
</body>
</html>