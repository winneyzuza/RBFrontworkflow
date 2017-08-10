package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControlSequenceTable {

	  private static Connection connect = null;
	  private static Statement statement = null;
	  private static PreparedStatement preparedStatement = null;
	  private static ResultSet resultSet = null;
	  public static List<String> list = new ArrayList();
	  public static List<String> list2 = new ArrayList();
	  
	public ControlSequenceTable() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("finally")
	public static String getSeqFormValidate()
	{	
		String seq = null;
		Integer month1, year1;
		
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0), DATE_FORMAT(lastUpdate, '%m'), DATE_FORMAT(lastUpdate, '%Y') from tblmt_sequence t where t.nameSeq like 'seqFormValidate' FOR UPDATE");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    month1 = Integer.parseInt(resultSet.getString(2));
			    year1 = Integer.parseInt(resultSet.getString(3));
			    
			    String today1 = new SimpleDateFormat("yyyy-MM").format(new Date());
			    try {
//			    	if ((sdf.parse(date1).before(sdf.parse(today1))) || (sdf.parse(date1).after(sdf.parse(today1)))) {
//			    		// different date - reset sequence
//			    		seq = "00001";
//			    		preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = 2 where id = 1");
//			    	} else {
//			    		// same date - do nothing
//			    		preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 1");
//			    	}
			    	if (year1 < Integer.parseInt(today1.substring(0,4))) {
			    		// different year
			    		seq = "00001";
			    		preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = 2, lastUpdate = now() where id = 1");
			    	} else { // same year or future
			    		if (year1 == Integer.parseInt(today1.substring(0,4))) {
			    			// same year then check month
			    			if (month1 < Integer.parseInt(today1.substring(5,7))) {
			    				// last change month is less than current month
					    		seq = "00001";
					    		preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = 2, lastUpdate = now() where id = 1");
			    			} else {
			    				preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where id = 1");
			    			}
			    		} else {
			    			preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where id = 1");
			    		}
			    	}
			    } catch(Exception e) {
			    	System.out.println("ControlSequence.class:getSeqFormValidate - ERROR - Parsing date OR SQL Prepare statement Failed");
			    }
			    // check for duplication start
			    // build expected request ID
			    PreparedStatement pStatement;
			    String checkstr = "";
			    checkstr = today1.substring(0,4) + today1.substring(5,7) + seq + "%";
			    System.out.println("getSeqFormValidate: Checking request ID duplication for " + checkstr);
			    // start checking for duplication through query
			    try {
			    	pStatement = connect.prepareStatement("select RequestID from tbldt_reqrepository where RequestID like ?");
			    	pStatement.setString(1, checkstr);
			    	pStatement.executeQuery();
			    	resultSet = pStatement.executeQuery();
			    	if (resultSet.next()) {
			    		// duplication found (query result occurred) - need to be fixed 
			    		System.out.println("getSeqFormValidate: FOUND Request ID duplication! Existing # " + resultSet.getString(1) + " and calculation as: " + checkstr);
			    		preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = ?, lastUpdate = now() where id = 1");
			    		// > no-use > int int1 = String.format("%05d", Integer.parseInt(resultSet.getString(1).substring(6,11))+1);
			    		int int1 = Integer.parseInt(resultSet.getString(1).substring(6,11)) + 1;
			    		System.out.println("getSeqFormValidate:  New Request ID =" + String.format("%05d",int1));
			    		preparedStatement.setInt(1, int1);
			    	} else {
			    		System.out.println("getSeqFormValidate: No Request ID duplication found");
			    	}
			    } catch (SQLException se) {
			    	System.out.println("Error in getSeqFormValidate FN - During find for Request ID duplication");
			    }
			    // check for duplication end
				preparedStatement.executeUpdate();
				
		} catch (Exception ex )
		{
			ex.printStackTrace();
			System.out.println("Error in getSeqFormValidate FN SEQUENCE SQL execution");
		}finally
		{		
			try {
				connect.close();
			} catch (SQLException e) {
				System.out.println("getSeqFormValidate: Error in closing connection");
			}
			return seq;	
		}
	}

	@SuppressWarnings("finally")
	public static String getRunningFormValidate()
	{	
		String seq = null;
//		String seq2 = null;
//		String seqfinal = null;
	
		try{
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
//			resultSet = statement
//			        .executeQuery("SELECT LPAD(MAX(CAST(rq.PKey AS UNSIGNED)) + 1,8,0), LPAD(MAX(CAST(rs.PKey AS UNSIGNED)) + 1,8,0) FROM tbldt_wsformvalidaterq rq, tbldt_wsformvalidaters rs");
			resultSet = statement.executeQuery("SELECT LPAD(IF (MAX(CAST(rq.PKey AS UNSIGNED)) >= (MAX(CAST(rs.PKey AS UNSIGNED))), MAX(CAST(rq.PKey AS UNSIGNED)) + 1, MAX(CAST(rs.PKey AS UNSIGNED))) + 1,8,0)  FROM tbldt_wsformvalidaterq rq, tbldt_wsformvalidaters rs");
			resultSet.next();
			    seq = resultSet.getString(1);
//			    seq2 = resultSet.getString(2);
//			    seqfinal = (Integer.parseInt(seq) > Integer.parseInt(seq2)) ? seq : seq2;
			    
			//  preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1 where id = 2");
			//	preparedStatement.executeUpdate();
			
		}catch (Exception ex )
		{
			ex.printStackTrace();
			System.out.println("Error in getRunningFormValidate FN SEQUENCE SQL execution");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where id = 2");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where id = 3");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where id = 4");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where nameSeq like 'seqAuditLog'");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where nameSeq like 'seqValidation'");
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
	
	public static String maxPKey()
    {
        String value;
        Connection connect;
        value = "";
        connect = null;
        PreparedStatement preparedStatement = null;
        connect = DatbaseConnection.getConnectionMySQL();
        try
        {
            preparedStatement = connect.prepareStatement("select  max(pkey) as kmax from tblmt_approver");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            value = resultSet.getString("kmax");
            value = value.replaceFirst("^0*", "").trim();
            int val = Integer.parseInt(value);
            value = String.valueOf(++val);
            value = String.format("%5s", new Object[] {
                value
            }).replace(' ', '0');
            
            //list.add(value);
           
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
        return value;
    }
	
	public static void checkPkey()
    {
        Connection connect;
        connect = null;
        PreparedStatement preparedStatement = null;
        connect = DatbaseConnection.getConnectionMySQL();
        try
        {
            preparedStatement = connect.prepareStatement("select distinct pkey from tblmt_approver");
            String value = "";
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
            	value = resultSet.getString("pkey");
            	list2.add(value);
            }
           
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
        
    }
	
	
	public static String getSeqApprover()
	{	
		String seq = null;
		boolean isdup = false;
		try{
			checkPkey();
			connect = DatbaseConnection.getConnectionMySQL();				
			statement = connect.createStatement();
			resultSet = statement
			        .executeQuery("select LPAD(seqNum,5,0) from tblmt_sequence t  where t.nameSeq like 'seqApprover' FOR UPDATE");
			    resultSet.next();
			    seq = resultSet.getString(1);
			    
			    for (String data : list2) {
			    	if(seq.equals(data)){
	                    seq = maxPKey();
	                    isdup = true;
	                }
			    }
			    
			    if(isdup)
	            {
	                preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = ?, lastUpdate = now() where nameSeq like 'seqApprover'");
	                preparedStatement.setString(1, seq.replaceFirst("^0*", ""));
	            } else
	            {
	                preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where nameSeq like 'seqApprover'");
	            }
			    //System.out.println("SeqNumber:"+seq);
			    //preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where nameSeq like 'seqApprover'");
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
			    preparedStatement = connect.prepareStatement("update tblmt_sequence set seqNum = seqNum+1, lastUpdate = now() where nameSeq like 'seqAuthen'");
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
	
	 public static void main(String args[])
	    {
	        System.out.println(maxPKey());
	        checkPkey();
	        
	    }
	 
}
