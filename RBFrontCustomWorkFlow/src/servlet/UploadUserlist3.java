package servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Configuration;
import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;
import mt.EmployeeInfo;
import mt.NormalFunction;
import validate.CheckFile;

public class UploadUserlist3  extends HttpServlet {
	
	private AuditLog al = new AuditLog();
	public AuditLog getAl() {
		return al;
	}

	public String getSuccess() {
		return success;
	}

	private String success = "";
	
	public UploadUserlist3() {
		// TODO Auto-generated constructor stub
	}
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{ System.out.println("UploadUserlist3");
	  
			Date curDate = new Date();
			Locale lc = new Locale("en","US");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
			
			Configuration.setConfig();
			UploadUserlist3 test = new UploadUserlist3();
			test.importHR();
			Date curDate3 = new Date();
			System.out.println("finish importHR"+sdf.format(curDate3));
			test.startUploadUserlist3();
			
			Date curDate2 = new Date();
			System.out.println("start:"+sdf.format(curDate)+" end:"+sdf.format(curDate2));
		  
	} 
	
	public void startUploadUserlist3(){
		System.out.println("Start UploadUserlist3 servlet ");
		al.setModule("AutoUploadUserlist3");
		

		InputStream fileInputStream = null;
		String path = Configuration.getParam("PathUserlist3");
		String filename = Configuration.getParam("FileNameUserlist3");
//		System.out.println(Configuration.data);
		System.out.println("path userlist3:"+path+filename);
        try {
        	fileInputStream = new FileInputStream(path+filename);
			//fileInputStream = new FileInputStream("/apps/userlist3.csv");
			//fileInputStream = new FileInputStream("C:/Users/PITSAMAI/Desktop/RBFrontCustomWorkFlow/war/UsrList3.csv");
			
			System.out.println("Total file size to read (in bytes) : "
					+ fileInputStream.available());

			BufferedReader br = new BufferedReader( new InputStreamReader(fileInputStream, StandardCharsets.UTF_8 ));
			   
			StringBuilder sb = new StringBuilder();
			String OriLine;
			while(( OriLine = br.readLine()) != null ) {
			    sb.append( OriLine );
			    sb.append( '\n' );
			}
			br.close();
			//System.out.println(sb.toString());
			
			  String file = sb.toString();
			  if(!insertTerminal(file))
				  System.out.println("insert terminal auto error");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			al.setStatus("E");
			success = "ไม่่พบ file:"+path+filename;
		}
		
        al.setDesc(success);
        al.insertAuditLog();
		System.out.println("success:"+success);

	}
	
	public boolean insertTerminal(String file ) throws SQLException{
		
	  	int count =0;
		boolean flag = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		
		try{					  
			  
			  if(connect != null){
				  System.out.println("Database Connected.");
			  } else {
				  System.out.println("Database Connect Failed.");
			  }
			  
			  PreparedStatement preparedStatement = connect.prepareStatement("delete from tblmt_terminallist_log ");
			  preparedStatement.executeUpdate();
			  System.out.println("delete log tblmt_terminallist_log success");
			  
			  preparedStatement = connect.prepareStatement("delete from tbldt_auditlog where Status ='EU' ");
			  preparedStatement.executeUpdate();
			  System.out.println("delete log audit_log EU success");
			  
			  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_terminallist_log (SELECT * FROM tblmt_terminallist) ");
			  preparedStatement.executeUpdate();
			  System.out.println("insert log terminal success.");				  
			  	
				int s1 = 0;
				int l1 = file.indexOf("\n",s1);
				String lineAll[] = file.split("\n");
				
				int j=0;
				if(lineAll[j].indexOf("Branch")>-1){
					j=1;
				}
				//for(int i=0;i<10;i++){
				boolean check = true;
				for(int i=j;i<lineAll.length-1;i++){
					count++;
					String line = lineAll[i];
					//System.out.println(i+":"+line);
					if("".equals(line.trim())){
						System.out.println("___"+line);
						continue;
					}
					
					String OrgCode = "";
					String EmpID = "";
					String Name = "";
					String TechnicalRole = "";
					String BusinessRole = "";
					String TerminalID = "";
					String Reserved = "";
					
					try{
						
						int s = 0;
						int l = line.indexOf(",");
						 OrgCode = line.substring(s,l);
						check = check && CheckFile.checkBranch(OrgCode);
						
						s=l+1; l=line.indexOf(",",s);
						 EmpID = line.substring(s,l);
						check = check && CheckFile.checkEmpID(EmpID);
	
						s=l+1;  l=line.indexOf(",",s);
						 Name = line.substring(s,l);
						
						s=l+1;	l=line.indexOf(",",s);
						 TechnicalRole = line.substring(s,l);
						
						s=l+1;	l=line.indexOf(",",s);
						 BusinessRole = line.substring(s,l);
						
						s=l+1;	l=line.indexOf(",",s);
						 TerminalID = line.substring(s,l);
						//check = check && CheckFile.checkTerminal(TerminalID);
						
						s=l+1;	l=line.indexOf(",",s);
						 Reserved = "Y";//line.substring(s,line.length());
						//check = check && CheckFile.checkYN(Reserved);
							
//						System.out.println(check+"test="+OrgCode+":2"+EmpID+":3"+Name+":4"+TechnicalRole+":5"+BusinessRole+":6"+TerminalID+":7"+Reserved);
					}catch(Exception e){
						System.out.println("error get data from userlist3 row:"+i);
						e.printStackTrace();
						 AuditLog a2 = new AuditLog();
						 a2.setStatus("EU");
						 a2.setModule("ข้อมูล UsrList3 ไม่ตรงตามเงือนไข");
						 a2.setDesc("แถวที่:"+(i+1)+" ข้อมูล:"+lineAll[i]);
						 a2.insertAuditLog();
					}
						
					////////////////////////////////////
						if(check){
							
							PreparedStatement preparedStatement2 = connect.prepareStatement("select t.OrgCode,t.TerminalID from tblmt_terminallist t where t.OrgCode = LPAD(?,4,'0') and t.TerminalID like ? ");
							preparedStatement2.setString(1, OrgCode);
							preparedStatement2.setString(2, TerminalID);
//							System.out.println(preparedStatement2);
							ResultSet resultSet2 = preparedStatement2.executeQuery();
							
							if(resultSet2.next()){
								String sql = "UPDATE tblmt_terminallist SET EmpID=?,Name=?,TechnicalRole=?,BusinessRole=?,Reserved=? WHERE OrgCode= LPAD(?,4,'0') and TerminalID= ? ";
								
								  	preparedStatement = connect.prepareStatement(sql);
								    preparedStatement.setString(1, EmpID);
								    preparedStatement.setString(2, Name);
								    preparedStatement.setString(3, TechnicalRole);
								    preparedStatement.setString(4, BusinessRole);
								    preparedStatement.setString(5, Reserved);
								    preparedStatement.setString(6, OrgCode);
								    preparedStatement.setString(7, TerminalID);
		
								    preparedStatement.executeUpdate();
								    
								    String BusRole = NormalFunction.convertPositionTecnicalToUser(TechnicalRole);
								    EmployeeInfo.updateEmployee(EmpID,OrgCode,TechnicalRole,BusinessRole,TerminalID,BusRole);
							      
							}else{
								  preparedStatement = connect.prepareStatement("insert into  tblmt_terminallist values ( LPAD(?,4,'0'), ?, ?, ?, ?, ?, ?)");
							      preparedStatement.setString(1, OrgCode);
							      preparedStatement.setString(2, EmpID);
							      preparedStatement.setString(3, Name);
							      preparedStatement.setString(4, TechnicalRole);
							      preparedStatement.setString(5, BusinessRole);
							      preparedStatement.setString(6, TerminalID);
							      preparedStatement.setString(7, Reserved);
							      
							      preparedStatement.executeUpdate();							      
							}
						}// end if check
						else{
							 //count=1000;

							// break;
						}
					      
					}// end for
				
				
				if(count<300){
					 al.setStatus("E");
					 success = "terminal สำหรับอัพเดดมีน้อยเกินไป rollback";
					 rollback();
				}else if(!check){
					 al.setStatus("E");
					 success = "formate file ไม่ถูกต้อง  row ที่ "+count;
					 rollback();
				}else{
					//success = "row sucess="+count+":อัพเดดข้อมูล terminal เรียบร้อย";
					success = "อัพเดดข้อมูล terminal เรียบร้อย";
					al.setStatus("C");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error terminalAddFile:"+e.getMessage());
				e.printStackTrace();
				success = "format file column "+count+" ไม่ถูกต้อง";
				al.setStatus("E");
				flag= false;
				rollback();
			}finally{
				try {	connect.close();} catch (SQLException e) {	}			
			}
		
		return flag;
	}
	
	
	public void rollback() throws SQLException{
			
		  Connection connect = DatbaseConnection.getConnectionMySQL();
		  PreparedStatement preparedStatement = connect.prepareStatement("delete from tblmt_terminallist ");
		  preparedStatement.executeUpdate();
		  System.out.println("delete from tblmt_terminallist success");
		  
		  preparedStatement = connect.prepareStatement("INSERT INTO tblmt_terminallist (SELECT * FROM tblmt_terminallist_log) ");
		  preparedStatement.executeUpdate();
		  System.out.println("insert log terminal success.");
		  
	}
	
	public boolean importHR(){
		boolean flag = true;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect.prepareStatement("select * from IAM i ");
			//System.out.println("iam:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("hr result:"+resultSet);
			int i =0;
			int dup = 0;
			Connection connectMy = DatbaseConnection.getConnectionMySQL();
			while(resultSet.next()){
				
				if(i%5000==0)
					System.out.print("i"+i);
				i++;
				
				String employeeID = resultSet.getString("employeeID");
				String OC_TYPE = resultSet.getString("OC_CODE");
				
				
				try{					
					PreparedStatement preparedStatementMy = connectMy
							.prepareStatement("insert into  tblmt_employeeinfo(EmpID,BranchID) values ( ?, ?) ");
					preparedStatementMy.setString(1, employeeID);
					preparedStatementMy.setString(2, OC_TYPE);
					int rs = preparedStatementMy.executeUpdate();
//					System.out.print("insert finish:"+rs);
				} catch (SQLException e) {
//					System.out.print("employee dup skip--->importHR");
					dup++;
				}finally{
								
				}
				
			}
			System.out.print("total:"+i+"dup:"+dup);
			try { connectMy.close();} catch (SQLException e) { }
		
		} catch (SQLException e) {
			System.out.println("UploadUserlist3.importHR Exception");
			e.printStackTrace();
			// TODO Auto-generated catch block
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		System.out.println("import HR finish");
		return flag;
	}
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		UploadUserlist3 test = new UploadUserlist3();
		test.startUploadUserlist3();
	}

}
