
<%@ page contentType="text/html;charset=UTF-8"%>
 
<%@ page import="java.io.*" %>

<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="db.DatbaseConnection" %>
<%@ page import="db.DatbaseConnectionMsSQL" %>
<%@ page import="validate.CheckFile" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>
<%@ page import="servlet.AuditLog" %>
<%@ page import="db.Configuration" %>
<%@ page import="servlet.ABMUpdate" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta equiv="Content-Type" content="text/html; charset=TIS-620">

<title>Branch File Import</title>
</head>

<body>
<script language="javascript">
	function forwardPage(param){
		if(param == 'y')
	    	window.location.href = "branch.jsp?success=y";
		else if(param == 'n')
			window.location.href = "branch.jsp?success=n";
		else if(param == 'c')
			window.location.href = "branch.jsp?success=c";
	}
</script>
<%! 
public boolean checkFile(String file,String succes){
	boolean flag = true;
	if("".equals(file)){
		System.out.println("file = ''");
	}
	if(file==null){
		System.out.println("file = null ");
	}
// 	System.out.println("C");
	return flag;
}

public int checkNULL(String orgCode,String ocCode,String BNameTH,String BNameEN,String OperationDay
		,String CounterLimit1,String CounterLimit2,String Metropolitan,String ABM){
	int fail=0;
	if("".equals(orgCode))
		fail++;
	if("".equals(ocCode))
		fail++;
	if("".equals(BNameTH))
		fail++;
	if("".equals(BNameEN))
		fail++;
	if("".equals(OperationDay))
		fail++;
	if("".equals(CounterLimit1))
		fail++;
	if("".equals(CounterLimit2))
		fail++;
	if("".equals(Metropolitan))
		fail++;
	if("".equals(ABM))
		fail++;
	return fail;
}

%>

<%	

System.out.println("Start branchAddFile.jsp ");

Date curDate = new Date();
Locale lc = new Locale("en","US");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);

System.out.println("start:"+sdf.format(curDate));


String success = "";
boolean  check = true;
boolean check2 = true;
int count = 0;
int fail = 0;
Connection connect = null;
Connection msconnect = null;
PreparedStatement preparedStatement = null;


// String urlName = request.getParameter("fileBranch");
// System.out.println("urlName:"+urlName);
// if(urlName==null){
// 	success = "ยังไม่ได้เลือก file สำหรับ  upload";
// 	check = false;
// }else if(urlName.indexOf("csv")>0){
// 	success = "เลือกชนิด file ไม่ถูกต้อง";
// 	check = false;
// };
		
System.out.println(success);

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
			//System.out.println(file);
			if(file.indexOf("filename=\"\"")>0){
				success = "ยังไม่ได้เลือก file สำหรับ  upload";
				check = false;
			}else if(file.indexOf("csv")<0 && file.indexOf("CSV")<0){
				success = "เลือกชนิด file ไม่ถูกต้อง";
				check = false;
			};
			
// 			if(file.indexOf("StartBranchInformation")<0){
// 				check = false;
// 				success = "file ยังไม่มี Header StartBranchInformation";
// 			}
			
// 			if(file.indexOf("EndBranchInformation")<0){
// 				check = false;
// 				success = success+"file ยังไม่มี Footer EndBranchInformation";
// 			}
			System.out.println("success:"+success);
			if(check){
				ABMUpdate abm = null;
				try{					  
					  connect = DatbaseConnection.getConnectionMySQL();
					  msconnect = DatbaseConnectionMsSQL.getConnectionMsSQL();
					  
					  if(connect != null && msconnect != null){
						  System.out.println("Database Connected.");
					  } else {
						  System.out.println((connect==null)?"MySQL Database Connection Failed":"MS SQL Database Connection Failed");
					  }
					  
					  preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo_log ");
					  preparedStatement.executeUpdate();
					  System.out.println("delete log tblmt_brandhinfo_log success");
					  
					  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo_log (SELECT * FROM tblmt_branchinfo) ");
					  preparedStatement.executeUpdate();
					  System.out.println("insert log branch success.");
					  
					  preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo ");
					  preparedStatement.executeUpdate();
					  System.out.println("delete from tblmt_brandhinfo success");
					  
						int indexStart = file.indexOf("filename=\"");
						indexStart = file.indexOf("\n", indexStart) + 1;
						indexStart = file.indexOf("\n", indexStart) + 1;
						indexStart = file.indexOf("\n", indexStart) + 1;
						int indexEnd = file.length();
						//file = file.substring(file.indexOf("StartBranchInformation")+24,file.indexOf("EndBranchInformation"));
						file = file.substring(indexStart+1,indexEnd);
						//index +1 สำหรับ file utf8
// 						System.out.println(file);
						int s1 = 0;
						int l1 = file.indexOf("\n",s1);
						String lineAll[] = file.split("\n");
						
						for(int i=0;i<lineAll.length-1 && check2 ;i++){
							count++;
							String line = lineAll[i];
  							//System.out.println(i+":"+line);
							//if("".equals(line.trim()) || line == null){
// 								System.out.println("null"+line);

							if(line.length() > 1){
							
								if(i> 1170)
									System.out.println(i+":"+line + " " + line.length() + " " + line.trim().isEmpty());
								
								int s = 0;
								int l = line.indexOf(",");
								String orgCode = line.substring(s,l);
								//System.out.println("B"+orgCode.length()+orgCode);
							
								s=l+1; l=line.indexOf(",",s);
								String ocCode = line.substring(s,l);
								check2 = check2 && CheckFile.checkBranch(ocCode);
								
								
								s=l+1;  l=line.indexOf(",",s);
								String BNameTH = line.substring(s,l);
								
								
								s=l+1;	l=line.indexOf(",",s);
								String BNameEN = line.substring(s,l);
								
								
								s=l+1;	l=line.indexOf(",",s);
								String OperationDay = line.substring(s,l);
								
								
								s=l+1;	l=line.indexOf(",",s);
								String CounterLimit1 = line.substring(s,l);
								
								
								s=l+1;	l=line.indexOf(",",s);
								String CounterLimit2 = line.substring(s,l);
								
								
								s=l+1;	l=line.indexOf(",",s);
								String Metropolitan = line.substring(s,l);
								check2 = check2 && CheckFile.checkYN(Metropolitan);
								
								s=l+1;l=line.indexOf(",",s);
								String ABM = line.substring(s,l);
								check2 = check2 && CheckFile.checkYN(ABM);
								
								
								s=l+1;l=line.indexOf(",",s);
								String AreaNo = line.substring(s,l);
								
								
								s=l+1;l=line.indexOf(",",s);
								String Network = line.substring(s,l);
								
								
								s=l+1;l=line.indexOf(",",s);
								String Group = line.substring(s,line.length());
								
								
								if(!check2)
									fail = fail +1;
								
								
	// 							System.out.println("test"+i+"="+orgCode+":"+ocCode+":"+BNameTH+":"+BNameEN+":"+OperationDay+":"+CounterLimit1+":"+CounterLimit2+":"+Metropolitan+":"+ABM);
								fail = fail + checkNULL(orgCode,ocCode,BNameTH,BNameEN,OperationDay,CounterLimit1,CounterLimit2,Metropolitan,ABM);
								
								System.out.println("p13");
								preparedStatement = connect.prepareStatement("insert into  tblmt_branchinfo values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
							    preparedStatement.setString(1, orgCode);
							    preparedStatement.setString(2, ocCode);
							    preparedStatement.setString(3, BNameTH);
							    preparedStatement.setString(4, BNameEN);
							    preparedStatement.setString(5, OperationDay);
							    preparedStatement.setString(6, CounterLimit1);
							    preparedStatement.setString(7, CounterLimit2);
							    preparedStatement.setString(8, Metropolitan);
							    preparedStatement.setString(9, ABM);
							    preparedStatement.setString(10, AreaNo);
							    preparedStatement.setString(11, Network);
							    preparedStatement.setString(12, Group);
							    
	 						    //System.out.println(preparedStatement);  
							    preparedStatement.executeUpdate();
							   // System.out.println("complete " + i);
							  
							}
						}
						
						if(!check2){
							success = "file formate ไม่ถูกต้อง  rollback";
							  preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback delete from tblmt_brandhinfo success");
						
							  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo (SELECT * FROM tblmt_branchinfo_log) ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback insert log branch success.");
					
							  
						}else if(fail>20){
							success = "มี branch ข้อมูลผิดพลาดมากเกินไป  rollback";
							  
							  preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback delete from tblmt_brandhinfo success");
							  
							  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo (SELECT * FROM tblmt_branchinfo_log) ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback insert log branch success.");
							  
						}else if(count<1000){
							 success = "branch สำหรับอัพเดดมีน้อยเกินไป rollback";
							  
							  preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback delete from tblmt_brandhinfo success");
							  
							  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo (SELECT * FROM tblmt_branchinfo_log) ");
							  preparedStatement.executeUpdate();
							  System.out.println("Rollback insert log branch success.");
				  
						}else{
							abm = new ABMUpdate();
						
							boolean resultcheck = abm.update();
							String msg = (resultcheck)? "สำเร็จ" : "ไม่สำเร็จ";
							success = "อัพเดดข้อมูล branch เรียบร้อย และปรับปรุงค่า ABM " + msg; 
					
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("error branchAddFile:"+e.getMessage());
						e.printStackTrace();
						success = "format file column "+count+" ไม่ถูกต้อง";
						
						preparedStatement = connect.prepareStatement("delete from tblmt_branchinfo ");
						  preparedStatement.executeUpdate();
						  System.out.println("Rollback delete from tblmt_brandhinfo success");
						  
						  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_branchinfo (SELECT * FROM tblmt_branchinfo_log) ");
						  preparedStatement.executeUpdate();
						  System.out.println("Rollback insert log branch success.");
						
					} finally{
						connect.close();
					}
			}	
		}
}

AuditLog al = new AuditLog();
al.setModule("ManualUploadBranch");
al.setDesc(success);
al.setStatus("C");
al.insertAuditLog();

System.out.println("success:"+success);
session.setAttribute("success", success);
response.sendRedirect("branch.jsp");

Date curDate2 = new Date();

System.out.println("end:"+sdf.format(curDate2));

	%>
</body>
</html>