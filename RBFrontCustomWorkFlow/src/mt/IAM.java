package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;

public class IAM {
	
	private String employeeID;
	private String samAccountName;
	private String cn;
	private String CorporateTitleEN;
	private String OC_TYPE;
	private String err;
	private String Job_Title_EN;
	private String Job_Title_TH;
	private String Full_Name_TH;
	
	public String getJob_Title_TH() {
		return Job_Title_TH;
	}

	public String getFull_Name_TH() {
		return Full_Name_TH;
	}
	
	public String getJob_Title_EN() {
		return Job_Title_EN;
	}

	public String getSamAccountName() {
		return samAccountName;
	}

	public void setSamAccountName(String samAccountName) {
		this.samAccountName = samAccountName;
	}
	
	public String getOC_TYPE() {
		return OC_TYPE;
	}
	
	public String getErr() {
		return err;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public String getCn() {
		return cn;
	}

	public String getCorporateTitleEN() {
		return CorporateTitleEN;
	}

	public IAM() {
		// TODO Auto-generated constructor stub
	}
	
	public IAM(String empID) {
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect
			          .prepareStatement("select * from IAM i where employeeID like ? ");
			preparedStatement.setString(1, empID);
//			System.out.println("iam:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				this.employeeID = resultSet.getString("employeeID");
				this.cn 	= resultSet.getString("cn");
				this.CorporateTitleEN	= resultSet.getString("Corporate_Title_EN");
				this.OC_TYPE = resultSet.getString("OC_TYPE");
				this.Job_Title_EN = resultSet.getString("Job_Title_EN");
				this.Full_Name_TH = resultSet.getString("Full_Name_TH");
				this.Job_Title_TH = resultSet.getString("Job_Title_TH");
				this.err = "N";
	
			}else{
				this.employeeID = empID;
				this.cn	= "System User";
				this.CorporateTitleEN = "";
				this.Job_Title_EN = "";
				this.Full_Name_TH = "System User";
				this.Job_Title_TH = "";
				this.err = "Y";
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.err = "Y";
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}

	}
	
	public IAM(String empID,String samAccountName) {
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect
			          .prepareStatement("select * from IAM i where samAccountName like ? ");
			preparedStatement.setString(1, empID);
//			System.out.println("iam:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				this.employeeID = resultSet.getString("employeeID");
				this.samAccountName = resultSet.getString("samAccountName");
				this.cn 	= resultSet.getString("cn");
				this.CorporateTitleEN	= resultSet.getString("Corporate_Title_EN");
				this.OC_TYPE = resultSet.getString("OC_TYPE");
				this.Job_Title_EN = resultSet.getString("Job_Title_EN");				
				this.Full_Name_TH = resultSet.getString("Full_Name_TH");
				this.Job_Title_TH = resultSet.getString("Job_Title_TH");
				this.err = "N";
	
			}else{
				this.employeeID = "";
				this.samAccountName = empID;
				this.cn	= "System User";
				this.CorporateTitleEN = "";
				this.Job_Title_EN = "";
				this.Full_Name_TH = "System User";
				this.Job_Title_TH = "";
				this.err = "Y";
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.err = "Y";
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}

	}
	
	public static String getOcType(String branch,String oc_type){
		String rs = null;
		String tempE ="";
		String tempB ="";
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select manager,OC_TYPE,OC_CODE from IAM i where OC_CODE = ? ");		
			preparedStatement.setString(1, branch);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				tempB = resultSet.getString("OC_TYPE");
				tempE = resultSet.getString("manager");
				rs	  = resultSet.getString("OC_CODE");
			}
		
			while(!tempB.equals(oc_type)){
				preparedStatement = connect
						.prepareStatement("select manager,OC_TYPE,OC_CODE from IAM i where employeeID = ? ");		
				preparedStatement.setString(1, tempE);
//				System.out.println(oc_type+":"+tempB+preparedStatement);
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()){
					tempB = resultSet.getString("OC_TYPE");
					tempE = resultSet.getString("manager");
					rs	  = resultSet.getString("OC_CODE");
				}else{
					break;
				}
				if(tempB.equals("Region"))
					break;
			};
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return rs;
	}
	
	public static String getOCByEmp(String empID) {
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		String Branch="";
		try {
			preparedStatement = connect
			          .prepareStatement("select OC_CODE from IAM i where employeeID like ? ");
			preparedStatement.setString(1, empID);
//			System.out.println("iam:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				Branch = resultSet.getString("OC_CODE");	
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return Branch;
	}
	
	public static String getBranchNetworkByEmp(String branch) {
		String rs = null;
		String tempE ="";
		String tempB ="";
		String keyNW = "Branch Network";
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select manager,ORGANIZATION_NAME_EN,OC_CODE from IAM i where OC_CODE = ? ");		
			preparedStatement.setString(1, branch);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				tempB = resultSet.getString("ORGANIZATION_NAME_EN");
				tempE = resultSet.getString("manager");
				rs	  = resultSet.getString("OC_CODE");
			}
		
			while( tempB.indexOf(keyNW) < 0 ){
				preparedStatement = connect
						.prepareStatement("select manager,ORGANIZATION_NAME_EN,OC_CODE from IAM i where employeeID = ? ");		
				preparedStatement.setString(1, tempE);
				
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()){
					tempB = resultSet.getString("ORGANIZATION_NAME_EN");
					tempE = resultSet.getString("manager");
					rs	  = resultSet.getString("OC_CODE");
				}else{
					break;
				}
				if(tempB.equals("Region"))
					break;
			};
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return rs;
	}

	public static boolean isPersonalBankerOrTeller(String empID){		
		boolean rs=true;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;

		try {
			preparedStatement = connect
			          .prepareStatement("select Job_Title_EN from IAM i where employeeID like ? and Job_Title_EN in (?,?) ");
			preparedStatement.setString(1, empID);
			preparedStatement.setString(2, "Personal Banker");
			preparedStatement.setString(3, "Teller");
//			System.out.println("preparedStatement:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				rs = true;	
			}else{
				rs = false;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return rs;
	}
	
	public static int getlvCorpByEmp(String empID){		
		int lv=0;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;

		try {
			preparedStatement = connect
			          .prepareStatement("select Corporate_Title_Code from IAM i where employeeID like ? ");
			preparedStatement.setString(1, empID);

//			System.out.println("preparedStatement:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				lv = Integer.parseInt(resultSet.getString("Corporate_Title_Code"));	
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return lv;
	}
	
	public static String getJobNameTH(String jobNameEN){		
		String Job_Title_TH="";
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;

		try {
			preparedStatement = connect
			          .prepareStatement("select distinct Job_Title_TH from IAM i where Job_Title_EN like ? ");
			preparedStatement.setString(1, jobNameEN);

//			System.out.println("getJobNameTH:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				Job_Title_TH = (resultSet.getString("Job_Title_TH"));	
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return Job_Title_TH;
	}
}
