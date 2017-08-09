package validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;

public class Validation {
	
	String validate;
	String dayBranch;
	String operation;
	String value;
	String action;
	String active;
	
	public String getValidte() {
		return validate;
	}



	public String getDayBranch() {
		return dayBranch;
	}



	public String getOperation() {
		return operation;
	}



	public String getValue() {
		return value;
	}



	public String getAction() {
		return action;
	}



	public void setActive(String active) {
		this.active = active;
	}



	public Validation() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public void setValidateByValidateDayBranch(String validate,String dayBranch){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select Validate,DayBranch,Operation,Value,Action,Active from tblmt_validation v where v.Validate = ? and v.DayBranch = ? and v.Active = 'Y' ");
			preparedStatement.setString(1, validate);
			preparedStatement.setString(2, dayBranch);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				this.validate = resultSet.getString("Validate");
				this.dayBranch = resultSet.getString("DayBranch");
				this.operation = resultSet.getString("Operation");
				this.value = resultSet.getString("Value");
				this.action = resultSet.getString("Action");
				this.active = resultSet.getString("Active");
			}else{
				System.out.println("set Validation invalid !!!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
