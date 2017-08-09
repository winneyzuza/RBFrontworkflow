package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DatbaseConnection;

public class ControlView extends HttpServlet{

	public ControlView() {
		
	}
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{ 	System.out.println("ControlViewServlet");
		ControlView test = new ControlView();
			test.contralView();
		  
	} 

	public boolean contralView(){

		boolean flag = true;
		  // PreparedStatements can use variables and are more efficient
		Connection connect = DatbaseConnection.getConnectionMySQL();				
	    try {
	    	PreparedStatement preparedStatement = connect.prepareStatement("delete from tblmt_controlview ");
			preparedStatement.executeUpdate();
			
	    	preparedStatement = connect.prepareStatement("select e.EmpID,e.BusinessRole from tblmt_employeeinfo e where e.TechnicalRole not like 'BLANK' and e.TechnicalRole is not null ORDER BY e.EmpID ,e.TechnicalRole ");
//	    	System.out.println(preparedStatement);
	    	ResultSet resultSet = preparedStatement.executeQuery();
			int i =0;
			while(resultSet.next()){
				
				if(i%500==0)
					System.out.print("i"+i);
				i++;
				
				String temp1 = null,temp2 = null,temp3 = null;
				String EmpID = resultSet.getString("EmpID");
				String BRole = resultSet.getString("BusinessRole");
//				System.out.println(EmpID+":"+BRole);
				
				String posAll[] = BRole.split("\\+");
				for(int c=0;c<posAll.length;c++){
					insertContralView(EmpID,posAll[c]);
				}
				
//				int s=0,l1=0,l2=0,f=0;
//				l1 = BRole.indexOf("+");
//				
//				f = BRole.length();
//				if(l1==-1){
//						temp1 = BRole.substring(s, f);
//						insertContralView(EmpID,temp1);
//				}else{
//					temp1 = BRole.substring(s, l1);
//					insertContralView(EmpID,temp1);
//					l2 = BRole.indexOf("+",l1+1);
//					if(l2 == -1){
//						temp2 = BRole.substring(l1+1, f);
//						insertContralView(EmpID,temp2);
//					}else {
//						temp2 = BRole.substring(l1+1, l2);
//						temp3 = BRole.substring(l2+1, f);
//						insertContralView(EmpID,temp2);
//						insertContralView(EmpID,temp3);
//					}
//				}
				//System.out.println("return:"+temp1+":"+temp2+":"+temp3);	
				
			}
	    	
		}catch(SQLException ex){
			flag = false;
			ex.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}
			System.out.println("control view finish.");
		}
		return flag;
	}
	
	private void insertContralView(String EmpID,String TRole) {
		Locale lc = new Locale("en","US");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
		Date curDate = new Date();
		String dd = sdf.format(curDate);		

		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			PreparedStatement preparedStatement = connect
			          .prepareStatement("insert into  tblmt_controlview values ( ?, ?, ?, ?)");
			      preparedStatement.setString(1, EmpID);
			      preparedStatement.setString(2, TRole);
			      preparedStatement.setString(3, "TRole");
			      preparedStatement.setString(4, dd);	      
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
	}
	
}
