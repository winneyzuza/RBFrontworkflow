package servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import db.ControlSequenceTable;
import db.DatbaseConnection;

public class AuditLog {
	
	private String seq;
	private String module;
	private String Desc;
	private String Status;
	private String userModified;
	
	public String getUserModified()
    {
        return userModified;
    }

    public void setUserModified(String userModified)
    {
        this.userModified = userModified;
    }

	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public AuditLog() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertAuditLog(){
		  
		Date curDate = new Date();
		Locale lc = new Locale("en","US");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
		
		Connection connect = DatbaseConnection.getConnectionMySQL();  
		try {
			seq = ControlSequenceTable.getSeqAuditLog();
			
				
			PreparedStatement preparedStatement = connect
		          .prepareStatement("insert into  tbldt_auditlog values ( ?, ?, ?, ?, ?, ? )");
		      
		      preparedStatement.setString(1, seq);
		      preparedStatement.setString(2, this.module);
		      preparedStatement.setString(3, this.Desc);
		      preparedStatement.setString(4, this.Status);
		      preparedStatement.setString(5, sdf.format(curDate));
		      preparedStatement.setString(6, userModified);
		      
		      preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("AuditLog exception:");
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AuditLog test = new AuditLog();
		test.module = "test";
		test.Desc = "testttttttttttttttttttt";
		test.Status = "N";
		
		test.insertAuditLog();
	}
}
