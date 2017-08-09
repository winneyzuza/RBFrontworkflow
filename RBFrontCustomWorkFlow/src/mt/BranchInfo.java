package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;

public class BranchInfo {
	String orgCode;
	String ocCode;
	String bNameTH;
	String bNameEN;
	String operationDay;
	String counterlimit1;
	String counterlimit2;
	String metropolitan;
	String ABM;
	String areaNo;
	String network;
	String groupB;
	
	public String getOrgCode() {
		return orgCode;
	}

	public String getOcCode() {
		return ocCode;
	}

	public String getbNameTH() {
		return bNameTH;
	}

	public String getbNameEN() {
		return bNameEN;
	}

	public String getOperationDay() {
		return operationDay;
	}

	public String getCounterlimit1() {
		return counterlimit1;
	}

	public String getCounterlimit2() {
		return counterlimit2;
	}

	public String getMetropolitan() {
		return metropolitan;
	}

	public String getABM() {
		return ABM;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public String getNetwork() {
		return network;
	}

	public String getGroupB() {
		return groupB;
	}

	public BranchInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public BranchInfo(String bid) {
		setBranchByID(bid);
	}
	
	public void setBranchByID(String bid){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_branchinfo b where b.OrgCode = ? ");
			preparedStatement.setString(1, bid);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
			
				this.orgCode = resultSet.getString("OrgCode");
				this.ocCode = resultSet.getString("OcCode");
				this.bNameTH = resultSet.getString("BNameTH");
				this.bNameEN = resultSet.getString("BNameEN");
				this.operationDay = resultSet.getString("OperationDay");
				this.counterlimit1 = resultSet.getString("Counterlimit1");
				this.counterlimit2 = resultSet.getString("Counterlimit2");
				this.metropolitan = resultSet.getString("Metropolitan");
				this.ABM = resultSet.getString("ABM");
				this.areaNo = resultSet.getString("AreaNo");
				this.network = resultSet.getString("Network");
				this.groupB = resultSet.getString("GroupB");
				
			}else{
				System.out.println("set branch invalid !!!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
	}
	
	public static int getGroupBranch(String branchID){
		int Group = 3;
		Connection connect = DatbaseConnection.getConnectionMySQL();
			try {
				
				PreparedStatement preparedStatement = connect
				          .prepareStatement("select GroupB from tblmt_branchinfo b where b.OrgCode like ? ");
				preparedStatement.setString(1, branchID);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if( resultSet.next()){
					Group = Integer.parseInt(resultSet.getString("GroupB").trim());				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			} 
		
		return Group;
	}
	
	public static String getAO(String branchID){
		String AO = "";
		
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select AreaNo from tblmt_branchinfo b where b.OrgCode like ? ");
			preparedStatement.setString(1, branchID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			//System.out.println("checkBranch result: "+resultSet.getInt(1));
			if( resultSet.next())
				AO = resultSet.getString("AreaNo");				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		} 
		
		
		return AO;
	}
	
	public static String getNW(String branchID){
		String NW = "";
		
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select Network from tblmt_branchinfo b where b.OrgCode like ? ");
			preparedStatement.setString(1, branchID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			//System.out.println("checkBranch result: "+resultSet.getInt(1));
			if( resultSet.next())
				NW = resultSet.getString("Network");				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		} 
		
		
		return NW;
	}
}
