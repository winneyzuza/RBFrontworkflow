package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;

public class Authen {
	
	String PKey;
	String Module;
	String EmpID;
	String remark;
	
	public Authen() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getAuthen(String userID){
		String module="";
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_authen a where a.EmpID like ? ");
			preparedStatement.setString(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			//System.out.println("checkBranch result: "+resultSet.getInt(1));
			while( resultSet.next()){
				module = module+resultSet.getString("Module");
			}
			System.out.println("Module:"+module);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		return module;
	}
	
	public static boolean isAdmin(String empID){
		boolean rs = true;
		String userID = "s"+empID;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_authen a where Module in ('Administrator','User') "
			          		+ " and a.EmpID like ? ");
			preparedStatement.setString(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
//			System.out.println("preparedStatement: "+preparedStatement);
			if( resultSet.next()){
				System.out.println("requester is User in system ");
				rs = true;
			}else{
				rs = false;
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		return rs;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
