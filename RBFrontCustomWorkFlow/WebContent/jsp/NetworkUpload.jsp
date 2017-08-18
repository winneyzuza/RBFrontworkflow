
<%@ page contentType="text/html;charset=UTF-8"%>
 
<%@ page import="java.io.*" %>

<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="validate.CheckFile" %>
<%@ page import="servlet.AuditLog" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=TIS-620">

<title>Network Data Upload</title>
</head>
<body>
<%	

System.out.println("Start NetworkUpload.jsp ");
String success = "";
boolean  check = true;
boolean  check2 = true;
int count =0;
int failcount =0;
Connection connect = null;
PreparedStatement preparedStatement = null;
PreparedStatement preparedStatement2 = null;

String urlName = request.getParameter("file");
System.out.println("urlName: "+urlName);
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
			DataInputStream in = new DataInputStream(request.getInputStream());
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
				success = "ยังไม่ได้เลือก file สำหรับ  upload (File has not been choose yet)";
				check = false;
			}else if(file.indexOf("csv")<0 && file.indexOf("CSV")<0){
				success = "เลือกชนิด file ไม่ถูกต้อง (File type mismatch)";
				check = false;
			};

			if(check){
				boolean delsuccess = false;
				boolean tablecleared = false;
				try{					  
					  	connect = DatbaseConnection.getConnectionMySQL();
					  
					  	if(connect != null){
						  	System.out.println("Database Connection Success.");
					  	} else {
						  	System.out.println("Database Connection Fail");
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
								System.out.println("NetworkUpload: (Warning) Line "+count+" is blank "+line);
								continue;
//							} else {
								//System.out.println("NetworkUpload: Data found on line #"+count);
							}
							
							int s = 0;
							int l = line.indexOf(",",0);
							//System.out.println("NetworkUpload: line data" + line + " found 1st comma at" + l);
							String region_id = line.substring(s,l);							
							s=l+1;	
							l=line.indexOf(",",s);
							String region_name = "";
							//System.out.println("NetworkUpload: line data" + line + " found 2nd comma at" + s);
							region_name = line.substring(s,line.length());
							check2 = true;
							if(!check2){
								success = "File formate ไม่ถูกต้อง (File type mismatch)";
								break;
							}else{
								ResultSet resultSet = null;
								int rownum = 0;
								String sql_del = "delete from tbldt_regionlist";
								if (!tablecleared) { 
									try{ 
										preparedStatement = connect.prepareStatement(sql_del);
										rownum = preparedStatement.executeUpdate();
										if (rownum >= 0) {
											System.out.println("NetworkUpload: Delete Regionlist table succeed");
											delsuccess = true;
											tablecleared = true;
										}else{
											System.out.println("NetworkUpload: Delete Regionlist table failed");
											delsuccess = false;
											tablecleared = true;
										}
										preparedStatement = null;
									} catch (Exception se) {
										System.out.println("NetworkUpload: Error! SQL execution error! (Delete Regionlist table)");
										delsuccess = false;
										tablecleared = true;
										se.printStackTrace();
										success = "Table RegionList ไม่ได้ถูกลบอย่างถูกต้อง โปรดติดต่อผู้พัฒนาระบบ";
									}
								}
										
								String sql = "INSERT INTO tbldt_regionlist(regionid, regionname) VALUES(?,?)";							
								preparedStatement = connect.prepareStatement(sql);
								// cleansing data
								region_id = (region_id.length() > 10) ? region_id.substring(0, 10) : region_id.substring(0, region_id.length()) ;
								region_name = (region_name.length() > 200) ? region_name.substring(0, 200) : region_name.substring(0, region_name.length());
								// cleansing data end
								preparedStatement.setString(1, region_id);
							    preparedStatement.setString(2, region_name);

							    try{ 
							    	preparedStatement.executeUpdate();
							    }catch (Exception e){
							    	System.out.println("NetworkUpload: Error - Insert fault on ID:"+region_id+" - "+region_name+ " ! "+e.getMessage());
							    	failcount++;
							    	e.printStackTrace();
							    }
			 												    
							}
							success = "ปรับปรุงรายการข้อมูล Network จำนวน " + count + "รายการเรียบร้อยแล้ว ";
							if (failcount > 0) { success = success + " พบข้อผิดพลาด " + failcount + " รายการ"; }
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error NetworkUpload:"+e.getMessage());
						e.printStackTrace();
						success = "พบข้อผิดพลาดในข้อมูลบรรทัดที่  "+count+ " (format file at line number "+count+" incorrect)";
					} finally{
						connect.close();
					}
			}	
		}
}

AuditLog al = new AuditLog();
Object user = session.getAttribute("userModify");
String userModified = user.toString();

al.setModule("NetworkUpload");
al.setDesc(success);
al.setStatus("C");
al.setUserModified(userModified);

al.insertAuditLog();		
		
System.out.println("success:"+success);
session.setAttribute("success", success);
response.sendRedirect("importExport.jsp");


	%>
	
</body>
</html>