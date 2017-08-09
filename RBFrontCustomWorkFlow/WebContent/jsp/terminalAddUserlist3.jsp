
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
			//System.out.println(file);
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
				
				
			    
				UploadUserlist3 u = new UploadUserlist3();
				u.insertTerminal(file);
				success = u.getSuccess();
				
				AuditLog al = new AuditLog();
			    al.setModule("ManualUploadUserlist3");
			    al.setDesc(success);
			    al.setStatus("C");
			    al.insertAuditLog();
				//u.importHR();
				
				/*try{					  
					  connect = DatbaseConnection.getConnectionMySQL();
					  
					  if(connect != null){
						  System.out.println("Database Connected.");
					  } else {
						  System.out.println("Database Connect Failed.");
					  }
					  
					  preparedStatement = connect.prepareStatement("delete from tblmt_terminallist_log ");
					  preparedStatement.executeUpdate();
					  System.out.println("delete log tblmt_terminallist_log success");
					  
					  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_terminallist_log (SELECT * FROM tblmt_terminallist) ");
					  preparedStatement.executeUpdate();
					  System.out.println("insert log terminal success.");
					  
// 					  preparedStatement = connect.prepareStatement("delete from tblmt_terminallist ");
// 					  preparedStatement.executeUpdate();
// 					  System.out.println("delete from tblmt_terminallist success");
					  
						//System.out.println(file);
						int s1 = 0;
						int l1 = file.indexOf("\n",s1);
						String lineAll[] = file.split("\n");
						
						//for(int i=0;i<10;i++){
						for(int i=0;i<lineAll.length-1;i++){
							count++;
							String line = lineAll[i];
							System.out.println(i+":"+line);
							if("".equals(line.trim())){
								System.out.println(line);
								continue;
							}
							
							int s = 0;
							int l = line.indexOf(",");
							String OrgCode = line.substring(s,l);

							s=l+1; l=line.indexOf(",",s);
							String EmpID = line.substring(s,l);

							s=l+1;  l=line.indexOf(",",s);
							String Name = line.substring(s,l);
							
							s=l+1;	l=line.indexOf(",",s);
							String TechnicalRole = line.substring(s,l);
							
							s=l+1;	l=line.indexOf(",",s);
							String BusinessRole = line.substring(s,l);
							
							s=l+1;	l=line.indexOf(",",s);
							String TerminalID = line.substring(s,l);
							
							s=l+1;	l=line.indexOf(",",s);
							String Reserved = line.substring(s,line.length());
								
							//System.out.println("<br>test="+OrgCode+":"+EmpID+":"+Name+":"+TechnicalRole+":"+BusinessRole+":"+TerminalID+":"+Reserved);
							
							////////////////////////////////////
							preparedStatement2 = connect.prepareStatement("select t.OrgCode,t.TerminalID from tblmt_terminallist t where t.OrgCode = LPAD(?,4,'0') and t.TerminalID like ? ");
							preparedStatement2.setString(1, OrgCode);
							preparedStatement2.setString(2, TerminalID+"%");
							//System.out.println(preparedStatement2);
							ResultSet resultSet2 = preparedStatement2.executeQuery();
							
							if(resultSet2.next()){
								String sql = "UPDATE tblmt_terminallist "+
									"SET EmpID=?,Name=?,TechnicalRole=?,BusinessRole=?,Reserved=? "+
									"WHERE OrgCode= LPAD(?,4,'0') and TerminalID= ? ";
								
								  	preparedStatement = connect.prepareStatement(sql);
								    preparedStatement.setString(1, EmpID);
								    preparedStatement.setString(2, Name);
								    preparedStatement.setString(3, TechnicalRole);
								    preparedStatement.setString(4, BusinessRole);
								    preparedStatement.setString(5, Reserved);
								    preparedStatement.setString(6, OrgCode);
								    preparedStatement.setString(7, TerminalID);

							      preparedStatement.executeUpdate();
							      
							}else{
								  preparedStatement = connect.prepareStatement("insert into  tblmt_terminallist values ( ?, ?, ?, ?, ?, ?, ?)");
							      preparedStatement.setString(1, OrgCode);
							      preparedStatement.setString(2, EmpID);
							      preparedStatement.setString(3, Name);
							      preparedStatement.setString(4, TechnicalRole);
							      preparedStatement.setString(5, BusinessRole);
							      preparedStatement.setString(6, TerminalID);
							      preparedStatement.setString(7, Reserved);
							      
							      preparedStatement.executeUpdate();							      
							}						      
						      
						}
						
						if(count<300){
							 success = "terminal สำหรับอัพเดดมีน้อยเกินไป rollback";
							  
							  preparedStatement = connect.prepareStatement("delete from tblmt_terminallist ");
							  preparedStatement.executeUpdate();
							  System.out.println("delete from tblmt_terminallist success");
							  
							  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_terminallist (SELECT * FROM tblmt_terminallist_log) ");
							  preparedStatement.executeUpdate();
							  System.out.println("insert log terminal success.");
				  
						}else{
							success = "อัพเดดข้อมูล terminal เรียบร้อย";
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("error terminalAddFile:"+e.getMessage());
						e.printStackTrace();
						success = "format file column "+count+" ไม่ถูกต้อง";
					} finally{
						connect.close();
					}
					
					*/
			}	
		}
}

System.out.println("success:"+success);
session.setAttribute("success", success);
response.sendRedirect("terminal.jsp");


	%>
	
</body>
</html>