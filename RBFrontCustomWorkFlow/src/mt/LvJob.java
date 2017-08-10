package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;

public class LvJob {

	public LvJob() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getJobByLv(String lv){
		String Job ="";
		Connection connect = null;
		PreparedStatement preparedStatement = null;
  
		connect = DatbaseConnection.getConnectionMySQL();
		try {
			 String sql = "select * from tblmt_lvjob l join tblmt_job j on l.JobName=j.NameEN where l.lv = ? ";
			preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, lv);
			ResultSet rs = preparedStatement.executeQuery();

			while(rs.next()){

				if(!Job.equals(""))
					Job = Job + ",";
				
				String nameJob = ""+rs.getString("NameTH");
				if("".equals(nameJob)){
					Job = Job + "ไม่มีชือJob";
				}else{
					Job = Job + nameJob;
				}				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}	
		return Job;
	}
	
}
