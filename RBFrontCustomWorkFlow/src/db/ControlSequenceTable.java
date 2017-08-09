package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControlSequenceTable {

	  private static Connection connect = null;
	  private static Statement statement = null;
	  private static PreparedStatement preparedStatement = null;
	  private static ResultSet resultSet = null;
	  
	public ControlSequenceTable() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("finally")
	public static String getSeqFormValidate()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t where t.nameSeq like 'seqFormValidate' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    //System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 1");
				preparedStatement.executeUpdate();
				
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
			}
			return seq;	
		}
	}
	
	@SuppressWarnings("finally")
	public static String getSeqRequestApprover()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t where t.nameSeq like 'seqRequestApprover' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
//			    System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 2");
				preparedStatement.executeUpdate();
				
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
			}
			return seq;	
		}
	}
	
	@SuppressWarnings("finally")
	public static String getSeqRequestStatusQuery()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t where t.nameSeq like 'seqRequestStatusQuery' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
//			    System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 3");
				preparedStatement.executeUpdate();
				
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
			}
			return seq;	
		}
	}
	
	@SuppressWarnings("finally")
	public static String getSeqRequestStatusUpdate()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t where t.nameSeq like 'seqRequestStatusUpdate' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
//			    System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 4");
				preparedStatement.executeUpdate();
				
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
			}
			return seq;	
		}
	}
	
	@SuppressWarnings("finally")
	public static String getSeqAuditLog()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t where t.nameSeq like 'seqAuditLog' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    //System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where nameSeq like 'seqAuditLog'");
				preparedStatement.executeUpdate();
				
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
			}
			return seq;	
		}
	}
	
	public static String getSeqValidate()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t  where t.nameSeq like 'seqValidation' FOR UPDATE ");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    //System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where nameSeq like 'seqValidation'");
				preparedStatement.executeUpdate();
		
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		return seq;
	}
	
	public static String getSeqApprover()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t  where t.nameSeq like 'seqApprover' FOR UPDATE");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    //System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where nameSeq like 'seqApprover'");
				preparedStatement.executeUpdate();
		
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return seq;
	}
	
	public static String getSeqAuthen()
	{	
		String seq = null;
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t  where t.nameSeq like 'seqAuthen' FOR UPDATE");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    //System.out.println("SeqNumber:"+seq);
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where nameSeq like 'seqAuthen'");
				preparedStatement.executeUpdate();
		
		}catch (Exception ex )
		{
			ex.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		return seq;
	}
}
