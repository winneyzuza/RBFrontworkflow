package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;

public class EmployeeInfo {
	
	private String EmpID;
	private String BranchID;
	private String Name;
	private String TechnicalRole;
	private String CurLimit;
	private String TerminalID;
	private String LastUpdate;
	private String LimitStatus;
	private String BusinessRole;
	private String Exam;
	
	public EmployeeInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public EmployeeInfo(String empID) {
		
			Connection connect = DatbaseConnection.getConnectionMySQL();
			try {
				PreparedStatement preparedStatement = connect
				          .prepareStatement("select * from tblmt_employeeinfo e where e.EmpID = ? ");
				preparedStatement.setString(1, empID);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()){
					//System.out.println("aaa:"+resultSet.toString());
					this.EmpID = resultSet.getString("EmpID");
					this.BranchID = resultSet.getString("BranchID");
					this.Name		= resultSet.getString("Name");
					this.TechnicalRole = resultSet.getString("TechnicalRole");
					this.CurLimit = resultSet.getString("CurLimit");
					this.TerminalID		= resultSet.getString("TerminalID");
					this.LastUpdate = resultSet.getString("LastUpdate");
					this.LimitStatus = resultSet.getString("LimitStatus");
					this.BusinessRole		= resultSet.getString("BusinessRole");
					this.Exam = resultSet.getString("Exam");
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
	}
	
	
	public String getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}

	public String getEmpID() {
		return EmpID;
	}

	public String getBranchID() {
		return BranchID;
	}

	public String getTechnicalRole() {
		return TechnicalRole;
	}

	public String getCurLimit() {
		return CurLimit;
	}

	public String getTerminalID() {
		return TerminalID;
	}

	public String getLimitStatus() {
		return LimitStatus;
	}

	public String getBusinessRole() {
		return BusinessRole;
	}

	public String getExam() {
		return Exam;
	}
	
	public static boolean updateEmployee(String empID,String branch,String tecnical,String limit,String terminalID,String busRole){
		boolean flag = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		String sql = "UPDATE tblmt_employeeinfo e "+
				"SET BranchID=LPAD(?,4,'0') , TechnicalRole=? , CurLimit=? , TerminalID=? , BusinessRole=? "+
				"WHERE EmpID= ? ";
			
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setString(1, branch);
			    preparedStatement.setString(2, tecnical);
			    preparedStatement.setString(3, limit);
			    preparedStatement.setString(4, terminalID);
			    preparedStatement.setString(5, busRole);
			    preparedStatement.setString(6, empID);

		      preparedStatement.executeUpdate();
//		      System.out.println(preparedStatement);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
			
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		return flag;	
	}
	
	public static String getNameByEmpID(String empID) throws SQLException{
		String empName="Not found !!!";
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			System.out.println(empID);
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select EmpID from tblmt_employeeinfo e where e.EmpID = ? ");
			preparedStatement.setString(1, empID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
//				System.out.println("PPPPPP");
				empName = resultSet.getString("EmpID");
			}else{
//				System.out.println("ERRRRR");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}			
		return empName;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
