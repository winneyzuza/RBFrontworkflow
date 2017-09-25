package validate;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import Util.MapUtil;
import db.DatbaseConnection;
import req.FormValidateReq;
import validate.Validation;
import mt.BranchInfo;
import mt.NormalFunction;
import mt.ReqRepository;
import mt.Transaction;

public class ProcessCA {
	
	private String msgError="";
	private String approver="";
	private List<Object> objFwdKey;
	
	public String getApprover() {
		return approver;
	}

	public String getMsgError() {
		return msgError;
	}



	public ProcessCA() {
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean checkCA1(FormValidateReq rq) throws IOException{
		boolean flag = true;
		String empID = rq.getEmpID();
		String sDate = rq.getFwdEffectiveStartDate();
		String eDate = rq.getFwdEffectiveEndDate();
		
		BranchInfo BranchF = new BranchInfo(rq.getFwdBranch());
	
		Map<String, List<String>> map = getReqFwdInfo(sDate, eDate, empID, rq.getFwdBranch());
		
		List<Map<String, ReqRepository>> list = getListCARepository(map); 
		
		List<Transaction> lts = getNumOfCA(list);
		
		Map<String, Integer> sum = lts.stream().collect(
	                Collectors.groupingBy(Transaction::getDate, Collectors.summingInt(Transaction::getCount)));

		
		Validation pCA = new Validation();
		pCA.setValidateByValidateDayBranch("NumCA", BranchF.getOperationDay());
		
		List<String> listDate = new ArrayList<>();
		listDate = getListCAByDate(sDate, eDate);
		
		 Map<String, List<Integer>> result =
	        		lts.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getCount, Collectors.toList())
	                        )
	                );
		 
		 Map<String, List<String>> resultStartList =
	        		lts.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getStartDate, Collectors.toList())
	                        )
	                );
		 // put last start date for each transaction into last key 
		 for(Map.Entry<String, List<String>> entry : resultStartList.entrySet()) {
        	 String newKey = "";
        	 int size = entry.getValue().size() - 1;
        	 newKey = entry.getKey() + "," + entry.getValue().get(size);
        	
        	 result.put(newKey, result.get(entry.getKey()));
        	 result.remove(entry.getKey());  	 
         }
		
		 ////////////////////////////////   FWD  /////////////////////////////////
		 
		 Map<String, List<String>> mapRev = getReqRevInfo(sDate, eDate, empID, rq.getFwdBranch());
			
		 List<Map<String, ReqRepository>> listRev = getListCARepositoryRev(mapRev);
			
		 List<Transaction> ltsRev = getNumOfCA(listRev);
		 Collections.sort(ltsRev, new Transaction());
			
			
	    Map<String, List<Integer>> resultRev =
	    		ltsRev.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getCount, Collectors.toList())
	                        )
	                );
	    
	    Map<String, List<String>> resultRevDateList =
	    		ltsRev.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getStartDate, Collectors.toList())
	                        )
	                );
	   
	    
	    // put last Rev date for each transaction into last key 
	    for(Map.Entry<String, List<String>> entry : resultRevDateList.entrySet()) {
	       	String newKey = "";
       	 	int size = entry.getValue().size() - 1;
       	 	newKey = entry.getKey() + "," + entry.getValue().get(size);

       	 	System.out.println("  WIN LOG   <<<< "+  entry.getKey() + "    size " + entry.getValue().get(size) + "    val " + resultRev.get(entry.getKey()));

       	 	resultRev.put(newKey, resultRev.get(entry.getKey()));
       	 	resultRev.remove(entry.getKey());
       	}
	    
	    
	   //////////////////////////////// REV  /////////////////////////////////
	    
	    List<Integer> lint = new ArrayList<>();
	    lint.add(0);
	    
	    List<Integer> lintOne = new ArrayList<>();
	    lintOne.add(1);
	    
	    List<String> removeKey = new ArrayList<>();
	    
	    HashMap<String, List<Integer>> mapPut = new HashMap<String, List<Integer>>();
	    
	    Iterator<Map.Entry<String, List<Integer>>> iterator = result.entrySet().iterator() ;
	    
	    List<String> resultList = new ArrayList<>();
	    
	    for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
	    	 String key = entry.getKey();
		     String [] keys = key.split(",");
		     String empDate = keys[0]+keys[1];
	    	 resultList.add(empDate);
	    	 
	    	 System.out.println(" BEFORE Result  " + entry.getKey() + " BEFORE Result VAL "+   entry.getValue() + "\n");
	     }
	    
	    for(Map.Entry<String, List<Integer>> entry : resultRev.entrySet()) {
	    	System.out.println(" REV Result  " + entry.getKey() + " REV VAL "+   entry.getValue() + "\n");
	    }
	    
	    while(iterator.hasNext()){
        	
        	Map.Entry<String, List<Integer>> entry = iterator.next();
        	
        	String key = entry.getKey();
	    	String [] keys = key.split(",");
	    	String empDate = keys[0]+keys[1];
	    	String empFWD = keys[0];
	    	
	    	String startDateFwd = keys[2];
	    	int lastPosFwd = entry.getValue().get(entry.getValue().size() - 1);
	    
        	
        	for(Map.Entry<String, List<Integer>> entryRev : resultRev.entrySet()) {
        		String keyForPut = "";
        		List<Integer> valueForput =  new ArrayList<>();
        		
        		String keyRev = entryRev.getKey();
	    		String [] keysRev = keyRev.split(",");
	    		String empREV = keysRev[0];
	    		
	    		String empDateRev = keysRev[0]+keysRev[1];
	    		String startDateRev = keysRev[2];
	    		
	    		
	    		LocalDate dateFwd = LocalDate.parse(startDateFwd);
	    		LocalDate dateRev = LocalDate.parse(startDateRev);
	    		
	    		
	    		System.out.println("entryRev KEY " + keyRev + " VAL " + entryRev.getValue() + "\n");
	    		System.out.println(("COMPARE " + empDate + "    " + empDateRev));
	    		
	    		
	    		if(empFWD.equals(empREV)) {
	    			if(empDate.equals(empDateRev)) {
		    			
		    			if(dateRev.isAfter(dateFwd)) {
		    				//result.put(key, entryRev.getValue());
		    				
		    				keyForPut = keyRev;
		    				valueForput = entryRev.getValue();
		    				removeKey.add(key);
		    			}else {
		    				if(lastPosFwd == 0) {
			    				//result.put(key, lint);
		    					
			    				keyForPut = key;
			    				valueForput = lint;
			    			}else {
			    				//result.put(key, entry.getValue());
			    				
			    				keyForPut = key;
			    				valueForput = entry.getValue();
			    			}
		    			}
		    		}else {
		    			
		    			if(!resultList.contains(empDateRev)) {
		    				keyForPut = keyRev;
		    				valueForput = entryRev.getValue();
		    			}
		    				
		    		}
	    			
	    		}
	    		
	    		if(keyForPut.length() > 0)
	    			mapPut.put(keyForPut, valueForput);
        	}
	    }
    
        for(Map.Entry<String, List<Integer>> entry : mapPut.entrySet()) {
        	for(String key : removeKey){
        		System.out.println("REMOVED KEY " + key);
        		result.remove(key);
        	}
        	//System.out.println("entry NEW PUT KEY " + entry.getKey() + " NEW PUT Value " + entry.getValue());
        	result.put(entry.getKey(), entry.getValue());
        }
        
        for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
        	System.out.println("entry RESULT FINAL  " + entry.getKey() + " NEW PUT Value " + entry.getValue());
        }	   

		int countx = 0;
		
		List<Transaction> sumList = new ArrayList<Transaction>();
		
		 for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
	       	  String key = entry.getKey();
	       	  for (int value : entry.getValue()) {
	   			  countx = entry.getValue().get(entry.getValue().size() - 1);
	   		  }
	       	  //System.out.println("KEY !!! " + key + "             " + entry.getValue() );
	       	  sum.put(key, countx);
	       	  
	       	  String[] keys = key.split(",");
	       	  String date = keys[1];
	       	  
	       	  Transaction tx = new Transaction(date, countx);
	       	  
	       	  sumList.add(tx);
	       	  
	       	  countx = 0;
       	}
		 
		 for(Transaction t : sumList) {
	        	System.out.println("SEE HERE " + t.getDate()  + "               "    + t.getCount());
	     }
        
		 Map<String, Integer> counting = sumList.stream().collect(
	                Collectors.groupingBy(Transaction::getDate, Collectors.summingInt(Transaction::getCount)));
	        
		 System.out.println("counting >>> " + counting);
        
		 
		String fwdPos = rq.getFwdPosition();
		String revPos = rq.getRevPosition();
		
		boolean isNotCAPos = fwdPos.indexOf("CA") < 0 && revPos.indexOf("CA") < 0 ;
		System.out.println("isNotCAPos " + isNotCAPos);
		
		if(!isNotCAPos){
			outerloop:
			for(String lt : listDate) {
				String checkCA = lt.trim();
				System.out.println("SEE checkCA " + checkCA);
				
				for (Entry<String, Integer> entry : counting.entrySet()) {
					System.out.println("SEE checkCA2 " + entry.getKey());
					if(checkCA.equals(entry.getKey())) {
						System.out.println("START CHECK !!! ");
						if( !checkOperationCA(pCA,entry.getValue()) ){
							flag = false;
							this.msgError = "";
							this.msgError = this.msgError +"สาขาที่โยกย้ายจำนวน  CA เกิน ";
							break outerloop;
							
						}
					}
				}
				
			}
		}
		

		if(!flag){
			if(NormalFunction.checkApproverReject(pCA.getAction())){
				this.approver = pCA.getAction();
				flag = true;
			}
		}
		
		
		return flag;
		
	}
	
	
	public boolean checkCA(FormValidateReq rq){
		boolean flag = true;
		BranchInfo BranchF = new BranchInfo(rq.getFwdBranch());
		BranchInfo BranchR = new BranchInfo(rq.getRevBranch());
		
		String empID = rq.getEmpID();
		String sDate = rq.getFwdEffectiveStartDate();
		String eDate = rq.getFwdEffectiveEndDate();
		
		
		//getResInfo()
		
		Validation pCA = new Validation();
		pCA.setValidateByValidateDayBranch("NumCA", BranchF.getOperationDay());
		
		int reqCAF = rq.getFwdPosition().indexOf("CA");
		int reqCAR = rq.getRevPosition().indexOf("CA");
		
		if(reqCAF>=0){
//			int numCAF = getNumCA(BranchF);
//			System.out.println("countCA:"+numCAF);
			
			eDate = NormalFunction.AddDate1(eDate);
			int countCA =0;
			while(!sDate.equals(eDate)){
				countCA = getCountCAbyBranch(sDate,BranchF.getOrgCode(),empID)+1;
				System.out.println("countCAF:"+countCA+":"+sDate);
				if( !checkOperationCA(pCA,countCA) ){
					flag = false;
					this.msgError = this.msgError +"สาขาที่โยกย้ายจำนวน  CA เกิน ";
					break;
				}
					
				sDate = NormalFunction.AddDate1(sDate);
			}
				
		}
		
		if(reqCAR>0){
//			int numCAR = getNumCA(BranchF);
			int countCA =0;
			
			for(int i=0;i<3;i++){
				countCA = getCountCAbyBranch(sDate,BranchR.getOrgCode(),empID)+1;
				System.out.println("countCAR:"+countCA+":"+sDate);
				if( !checkOperationCA(pCA,countCA) ){
					flag = false;
					this.msgError = this.msgError +"สาขาrevokeจำนวน   CA เกิน ";
					break;
				}					
				sDate = NormalFunction.AddDate1(sDate);
			}
				
		}		
		
		if(!flag){
			if(NormalFunction.checkApproverReject(pCA.getAction())){
				this.approver = pCA.getAction();
				flag = true;
			}
		}
		
		return flag;
	}
	
	public static boolean checkOperationCA(Validation valid,int countCA){
		boolean result = true;
		System.out.println("OPERATION " + valid.getOperation() + " COUNT CA " +countCA);
		int value = Integer.parseInt(valid.getValue());
			switch(valid.getOperation()){
				case "<":{
					if(countCA < value){ result = false;	}
					break;}
				case "<=":{
					if(countCA <= value){ result = false;	}
					break;}
				case ">":{
					if(countCA > value){ result = false;	}
					break;}
				case ">=":{
					if(countCA >= value){ result = false;	}
					break;}
				case "=":{
					if(countCA == value){ result = false;	}
					break;}
				case "!=":{
					if(countCA != value){ result = false;	}
					break;}
			}
		
		return result;
	}
	
	public static List<String> getListCAByDate(String sDate, String eDate){
		
		 //sDate = "2014-05-01";
		 //eDate= "2014-05-10";
		 
		 LocalDate start = LocalDate.parse(sDate);
		 LocalDate end = LocalDate.parse(eDate);
		 //List<LocalDate> totalDates = new ArrayList<>();
		 List<String> list = new ArrayList<>();
		 boolean isbefore = start.isBefore(end);
		 
		 if(start.equals(end)) {
			 list.add(end.toString());
		 }else {
			 if(isbefore) {
				 while (!start.isAfter(end)) {
				     //totalDates.add(start);
					 list.add(start.toString());
					 start = start.plusDays(1);
				    
				 } 
			 }
		 }
		 
		 
	 
		 return list;
	}
	
	public static List<Transaction> getNumOfCA(List<Map<String, ReqRepository>> list){
		HashMap <String,Integer> map = new HashMap<String,Integer>();
		List<String> term = new ArrayList<String>();
		List<Integer> numCA = new ArrayList<Integer>(); 
		
		List<Transaction> trList = new ArrayList<Transaction>();
		
		int countCA = 0;
		int sort = 0;
		term.add("");
		
		Transaction tr = null;
		
		for (Map<String, ReqRepository> map3 : list) {
		    for (Map.Entry<String, ReqRepository> entry : map3.entrySet()) {

		        String [] keys = entry.getKey().split(",");
		  
		        String key = keys[1] + "," + keys[2];
		        
		        //System.out.println("entry.getValue()   <<< " + entry.getValue().getFwdBrandch());
		        
		        String sDate = entry.getValue().getEffStartDate();
		        countCA = entry.getValue().getPosition();
		        sort = entry.getValue().getSort();
		        
		        //System.out.println(" key " + key +  " countCA " + countCA);
		        tr = new Transaction();
		        
		        tr.setStartDate(sDate);
		        tr.setDate(key);
		        tr.setCount(countCA);
		        tr.setSort(sort);
		        trList.add(tr);
		    }
		}
		
		return trList;
		
	}
	

	public static List<Map<String, ReqRepository>> getListCARepositoryRev(Map<String, List<String>> map){
		
		List<Map<String, ReqRepository>>  mapList= new ArrayList<Map<String, ReqRepository>>();
		Map<String, ReqRepository> map1 = new TreeMap<String, ReqRepository>();
		
		int i =0;
		for (Map.Entry<String, List<String>> me : map.entrySet()) {
		  String key = me.getKey();
		  List<String> valueList = me.getValue();
	
		  String [] keys =  key.split(",");
		  String reqid = keys[0];
		  String empid = keys[1];
		  String position = keys[3];
		  String branch = keys[4];
		  
		  String [] branchAll = branch.split("u");
		  String fwdBranch = branchAll[0];
		  String revBranch = branchAll[1];
		
		  String revDate = keys[5];
		  
		  ReqRepository r = new ReqRepository();
		  for (String s : valueList) {
			  
			i++;
			
			if(position.indexOf("CA")>-1 ) {
		    	r.setPosition(1);
		    }else {
		    	r.setPosition(0);
		    }
		    r.setEmpID(empid);
		    r.setSort(i);
		    r.setEffStartDate(revDate);
		    r.setFwdBrandch(fwdBranch);
		    r.setRevBranch(revBranch);
		    //System.out.println(reqid+","+empid+","+s + " VAL " + r.getFwdBrandch());
		    map1.put(reqid+","+empid+","+s, r);
		  }
		  
		  
		}
		mapList.add(map1);
		
		return mapList;
	}
	
	public static List<Map<String, ReqRepository>> getListCARepository(Map<String, List<String>> map){
		
		List<Map<String, ReqRepository>>  mapList= new ArrayList<Map<String, ReqRepository>>();
		Map<String, ReqRepository> map1 = new TreeMap<String, ReqRepository>();
		
		int i =0;
		for (Map.Entry<String, List<String>> me : map.entrySet()) {
		  String key = me.getKey();
		  List<String> valueList = me.getValue();
	
		  String [] keys =  key.split(",");
		  String reqid = keys[0];
		  String empid = keys[1];
		  String position = keys[2];
		  String startD = keys[5];
		  
		  ReqRepository r = new ReqRepository();
		  for (String s : valueList) {
			  
			i++;
  
		    if(position.indexOf("CA")>-1 ) {
		    	r.setPosition(1);
		    }else {
		    	r.setPosition(0);
		    }
		    
		    r.setEmpID(empid);
		    r.setSort(i);
		    r.setEffStartDate(startD);
		    
		    map1.put(reqid+","+empid+","+s, r);
		  }
		  
		  
		}
		mapList.add(map1);
		
		return mapList;
	}
	
	
	public static Map<String, String> getMaxEndDateByEmpID(String sDate, String eDate, String empid, String fwdBranch){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		
		PreparedStatement preparedStatement;
		Map<String, String> map = new HashMap<String, String>();
		try {
			
				preparedStatement = connect
				          .prepareStatement("SELECT a.empID, MAX(a.EffEndDate) AS maxDate \r\n" + 
				          		"FROM (SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate > ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate > ?)\r\n " + 
				          		"AND r.status NOT IN ('R') "
				          		+ " UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN DATE(NOW()) AND ?) \r\n" + 
				          		" AND r.status NOT IN ('R') ) a WHERE a.EmpID <> ? AND ( a.FwdBranch = ? OR a.RevBranch = ? ) \r\n" + 
				          		//"AND r.status NOT IN ('R') ) a WHERE a.EmpID IN( '99991')\r\n" + 
				          		"GROUP BY a.EmpID" );
				
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, sDate);
				preparedStatement.setString(3, eDate);
				// ----------------------------------------- Qry1
				preparedStatement.setString(4, sDate);
				preparedStatement.setString(5, eDate);
				// ----------------------------------------- Qry2
				preparedStatement.setString(6, sDate);
				preparedStatement.setString(7, eDate);
				preparedStatement.setString(8, sDate);
				preparedStatement.setString(9, eDate);
				// ----------------------------------------- Qry3
				preparedStatement.setString(10, sDate);
				preparedStatement.setString(11, eDate);
				preparedStatement.setString(12, eDate);
				// ----------------------------------------- Qry4
				preparedStatement.setString(13, sDate);
				
				
				preparedStatement.setString(14, empid);
				preparedStatement.setString(15, fwdBranch);
				preparedStatement.setString(16, fwdBranch);
				
				/*
				preparedStatement = connect
		          .prepareStatement("SELECT  r.empID, MAX(EffEndDate) AS maxDate FROM tbldt_reqrepository r WHERE r.empid IN( '11111', '22222', '33333', '44444') GROUP BY  r.empID");
		*/
				ResultSet resultSet = preparedStatement.executeQuery();
				
				
				
				System.out.println(" preparedStatement MAX DATE QUERY " + preparedStatement);
				while(resultSet.next()){
					
					map.put(resultSet.getString("empID"), resultSet.getString("maxDate"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return map;
				
	}
	
	public static Map<String, List<String>> getReqRevInfo(String sDate, String eDate, String empid, String fwdBranch){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		
		PreparedStatement preparedStatement;
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		
		Map<String, String> mapMaxEndDate = getMaxEndDateByEmpID(sDate, eDate, empid, fwdBranch);
		try {
				
				preparedStatement = connect
				          .prepareStatement("SELECT *\r\n" + 
				          		"FROM (SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch, DATE_ADD(r.EffEndDate, INTERVAL 1 DAY) AS 'RevDate' \r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch, DATE_ADD(r.EffEndDate, INTERVAL 1 DAY) AS 'RevDate' \r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate > ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch, DATE_ADD(r.EffEndDate, INTERVAL 1 DAY) AS 'RevDate' \r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch, DATE_ADD(r.EffEndDate, INTERVAL 1 DAY) AS 'RevDate' \r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate > ?)\r\n " + 
				          		"AND r.status NOT IN ('R') \r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch, DATE_ADD(r.EffEndDate, INTERVAL 1 DAY) AS 'RevDate'\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN DATE(NOW()) AND ?) \r\n" + 
				          		" AND r.status NOT IN ('R') ) a WHERE a.EmpID <> ? AND ( a.FwdBranch = ? OR a.RevBranch = ? ) \r\n" +
				          		//"AND r.status NOT IN ('R') ) a WHERE  a.EmpID IN( '99991')" +  
				          		"ORDER BY a.EmpID, DATE_ADD(EffEndDate, INTERVAL 1 DAY) , a.ReqSubmitDate" );
				    			
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, sDate);
				preparedStatement.setString(3, eDate);
				// ----------------------------------------- Qry1
				preparedStatement.setString(4, sDate);
				preparedStatement.setString(5, eDate);
				// ----------------------------------------- Qry2
				preparedStatement.setString(6, sDate);
				preparedStatement.setString(7, eDate);
				preparedStatement.setString(8, sDate);
				preparedStatement.setString(9, eDate);
				// ----------------------------------------- Qry3
				preparedStatement.setString(10, sDate);
				preparedStatement.setString(11, eDate);
				preparedStatement.setString(12, eDate);
				// ----------------------------------------- Qry4
				preparedStatement.setString(13, sDate);
				// ----------------------------------------- Qry5
				preparedStatement.setString(14, empid);
				preparedStatement.setString(15, fwdBranch);
				preparedStatement.setString(16, fwdBranch);
			
				/*preparedStatement = connect
			          .prepareStatement("SELECT *,DATE_ADD(EffEndDate, INTERVAL 1 DAY) AS 'RevDate'  FROM tbldt_reqrepository WHERE RequestID LIKE '2018%' ORDER BY RequestID"); 
				*/
				ResultSet resultSet = preparedStatement.executeQuery();
				
				System.out.println("preparedStatement REV QUERY " +  preparedStatement);
				List<String> list = new ArrayList<String>();
				
				
				while(resultSet.next()){
					String requestID = resultSet.getString("RequestID");
					String empID = resultSet.getString("EmpID"); 
					String fwdPosition = resultSet.getString("FwdPosition"); 
					String revPosition = resultSet.getString("RevPosition");
					
					String fdBranch = resultSet.getString("FwdBranch");
					String rvBranch = resultSet.getString("RevBranch");
					String revDate = resultSet.getString("RevDate");
					
					
					String endDate = mapMaxEndDate.get(empID);
					
					//System.out.println("revPosition " + revPosition + " check REv " + revPosition.indexOf("CA") );
					//System.out.println(" endDate " + endDate );
					if(fdBranch.equals(rvBranch)) {
						list = getListCAByDate(revDate, endDate);
						//System.out.println("LIST >>  " + list);
					}else {
						if(rvBranch.equals(fwdBranch)) {
							System.out.println(  " GET rvBranch" + rvBranch + "   fwdBranch  " + fwdBranch + "  eDate " + eDate);
							list = getListCAByDate(revDate, eDate);
						}else {
							list.clear();
						}
					}
					
					
					//System.out.println(requestID +"," + empID + "," + fwdPosition + "," + revPosition + "," + fdBranch+"u"+rvBranch +"," +revDate + "\n");
					//System.out.println("List  " + list);
					map.put(requestID +"," + empID + "," + fwdPosition + "," + revPosition + "," + fdBranch+"u"+rvBranch+"," +revDate  , list);
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
		
		return  map;
	}
	
	public static Map<String, List<String>> getReqFwdInfo(String sDate, String eDate, String empid, String fwdBranch){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		
		PreparedStatement preparedStatement;
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		try {
				
				preparedStatement = connect
				          .prepareStatement("SELECT *\r\n" + 
				          		"FROM (SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.EffStartDate < ? AND  (r.EffEndDate > ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate BETWEEN ? AND ?)\r\n" + 
				          		"AND r.status NOT IN ('R')\r\n" + 
				          		"\r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN ? AND ?) AND (r.EffEndDate > ?)\r\n " + 
				          		"AND r.status NOT IN ('R') \r\n" + 
				          		"UNION\r\n" + 
				          		"\r\n" + 
				          		"SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition, r.RevPosition, r.FwdBranch, r.RevBranch\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE (r.EffStartDate BETWEEN DATE(NOW()) AND ?) \r\n" + 
				          		" AND r.status NOT IN ('R') ) a WHERE a.EmpID <> ? AND ( a.FwdBranch = ? OR a.RevBranch = ? ) \r\n" + 
				          		//"AND r.status NOT IN ('R') ) a WHERE  a.EmpID IN( '99991') \r\n" + 
				          		"ORDER BY a.EmpID, a.EffStartDate, a.ReqSubmitDate" );
				    			
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, sDate);
				preparedStatement.setString(3, eDate);
				// ----------------------------------------- Qry1
				preparedStatement.setString(4, sDate);
				preparedStatement.setString(5, eDate);
				// ----------------------------------------- Qry2
				preparedStatement.setString(6, sDate);
				preparedStatement.setString(7, eDate);
				preparedStatement.setString(8, sDate);
				preparedStatement.setString(9, eDate);
				// ----------------------------------------- Qry3
				preparedStatement.setString(10, sDate);
				preparedStatement.setString(11, eDate);
				preparedStatement.setString(12, eDate);
				// ----------------------------------------- Qry4
				preparedStatement.setString(13, sDate);
				// ----------------------------------------- Qry5
				preparedStatement.setString(14, empid);
				preparedStatement.setString(15, fwdBranch);
				preparedStatement.setString(16, fwdBranch);
				
			/*
				preparedStatement = connect
			          .prepareStatement("SELECT *FROM tbldt_reqrepository WHERE RequestID LIKE '2018%' ORDER BY RequestID");*/
				
				ResultSet resultSet = preparedStatement.executeQuery();
				
				System.out.println("preparedStatement FWD QUERY " + preparedStatement);
				List<String> list = new ArrayList<String>();
				
				
				while(resultSet.next()){
					String requestID = resultSet.getString("RequestID");
					String empID = resultSet.getString("EmpID"); 
					String effStartDate = resultSet.getString("EffStartDate"); 
					String effEndDate = resultSet.getString("EffEndDate"); 
					String fwdPosition = resultSet.getString("FwdPosition"); 
					String revPosition = resultSet.getString("RevPosition");
					
					String fdBranch = resultSet.getString("FwdBranch");
					String rvBranch = resultSet.getString("RevBranch");
					
					list = getListCAByDate(effStartDate, effEndDate);
					System.out.println("TEST " + requestID +"," + empID + "," + fwdPosition + "," + revPosition + "," + fdBranch+"u"+rvBranch + "," + effStartDate);
					System.out.println(list);
					map.put(requestID +"," + empID + "," + fwdPosition + "," + revPosition + "," + fdBranch+"u"+rvBranch + "," + effStartDate, list);
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
		
		return  map;
	}
	
	
	public static Map<String, List<String>> getReqInfo1(String sDate, String eDate){
		Connection connect = DatbaseConnection.getConnectionMySQL();
		
		PreparedStatement preparedStatement;
		//Map<String, ReqRepository> map = new TreeMap<String, ReqRepository>();
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		try {
				
				preparedStatement = connect
				          .prepareStatement("SELECT r.RequestID, r.ReqSubmitDate, r.EmpID, r.EffStartDate, r.EffEndDate, r.FwdPosition\r\n" + 
				          		"FROM tbldt_reqrepository r JOIN tblmt_employeeinfo e ON e.EmpID=r.EmpID\r\n" + 
				          		"WHERE r.empid = '90009'\r\n" + 
				          		"AND r.status NOT IN ('R')" );
				    			
			
				ResultSet resultSet = preparedStatement.executeQuery();
				
				System.out.println("preparedStatement " + preparedStatement);
				List<String> list = new ArrayList<String>();
				
				
				while(resultSet.next()){
					String requestID = resultSet.getString("RequestID");
					String empID = resultSet.getString("EmpID"); 
					String effStartDate = resultSet.getString("EffStartDate"); 
					String effEndDate = resultSet.getString("EffEndDate"); 
					String fwdPosition = resultSet.getString("FwdPosition"); 
					
					list = getListCAByDate(effStartDate, effEndDate);
					
					map.put(requestID +"," + empID + "," + fwdPosition , list);
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
		
		return  map;
	}
	
	public int getCountCAbyBranch(String dateT,String branch,String empID){
		int count =0;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			//// get All			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select count(*) from tblmt_employeeinfo e "+
			        		  " where e.BusinessRole like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and e.BranchID = ?");
			preparedStatement.setString(1, empID);
			preparedStatement.setString(2, branch);		
			ResultSet resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = (int)resultSet.getInt(1);
//			System.out.println("A:+"+count);
			
			/// + between forward
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			        		  " where ? BETWEEN r.EffStartDate and r.EffEndDate "+
			        		  " and e.BusinessRole not like '%CA%' "+
			        		  " and r.FwdPosition like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and r.Status not in ('R') "+
			        		  " and r.FwdBranch like ?");
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count + (int)resultSet.getInt(1);
			//System.out.println(preparedStatement);
//			System.out.println("B:+"+(int)resultSet.getInt(1));
			
			
			/// + since revert
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			        		  " where ? > r.EffEndDate "+
			        		  " and e.BusinessRole not like '%CA%' "+
			        		  " and r.RevPosition like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and r.Status not in ('R') "+
			        		  " and r.FwdBranch like ?");
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count + (int)resultSet.getInt(1);			
//			System.out.println("C:+"+(int)resultSet.getInt(1));
			
			// - in forward
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
					" where ? BETWEEN r.EffStartDate and r.EffEndDate "+
					" and e.BusinessRole like '%CA%' "+
					" and r.FwdPosition not like '%CA%' "+
					" and e.EmpID <> ? "+
					" and r.Status not in ('R') "+
					" and r.FwdBranch like ? ");			
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count - (int)resultSet.getInt(1);		
//			System.out.println("D:-"+(int)resultSet.getInt(1));
			
			
			// - since rever
			preparedStatement = connect
	          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			" where ? > r.EffStartDate "+
			" and e.BusinessRole like '%CA%' "+
			" and r.RevPosition not like '%CA%' "+
			" and e.EmpID <> ? "+
			" and r.Status not in ('R') "+
			" and r.FwdBranch like ? ");			
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);	
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count - (int)resultSet.getInt(1);
//			System.out.println("E:-"+(int)resultSet.getInt(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		
		return count;
	}
	
	public static void writeFile(Map<String, Integer> map ,String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
	 
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			fw.write(entry.getKey()+ ", VALUE " + entry.getValue() + "\n");
		}
	 
		fw.close();
	}

	public static <V> void main(String[] args) throws IOException {
		
		Map<String, List<String>> map = getReqFwdInfo("2017-09-25", "2017-09-25", "90009", "0122");
		
		List<Map<String, ReqRepository>> list = getListCARepository(map);
		
		List<Transaction> lts = getNumOfCA(list);
		
		Collections.sort(lts, new Transaction());
		
	    Map<String, Integer> sum = lts.stream().collect(
                Collectors.groupingBy(Transaction::getDate, 
                						Collectors.summingInt(Transaction::getCount)
                					 )
                );
        
        
         Map<String, List<Integer>> result =
        		lts.stream().collect(
                        Collectors.groupingBy(Transaction::getDate,
                                Collectors.mapping(Transaction::getCount, Collectors.toList())
                        )
                );
         

         Map<String, List<String>> resultStartList =
        		lts.stream().collect(
                        Collectors.groupingBy(Transaction::getDate,
                                Collectors.mapping(Transaction::getStartDate, Collectors.toList())
                        )
                );
         
         
         
         for(Map.Entry<String, List<String>> entry : resultStartList.entrySet()) {
        	 
        	 //System.out.println(entry.getKey() + " entry FWD               " +  entry.getValue());
        	 
        	 String newKey = "";
        	 int size = entry.getValue().size() - 1;
        	 newKey = entry.getKey() + "," + entry.getValue().get(size);
        	 //System.out.println("     <<<< "+  entry.getKey() + "     " + entry.getValue().get(size) + "    val " + result.get(entry.getKey()));
        	 result.put(newKey, result.get(entry.getKey()));
        	 
        	 
        	 result.remove(entry.getKey());
        	 
         }
         
         /*
         for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
        	 
        	 System.out.println(entry.getKey() + " entry FWD               " +  entry.getValue());
         }*/
	        		
		////////////////////////////////    FWD    ///////////////////////////////////
         
         
		
		Map<String, List<String>> mapRev = getReqRevInfo("2017-09-25", "2017-09-25", "90009", "0122");
		
		List<Map<String, ReqRepository>> listRev = getListCARepositoryRev(mapRev);
		
		List<Transaction> ltsRev = getNumOfCA(listRev);
		
		Collections.sort(ltsRev, new Transaction());
		
		for(Transaction t : ltsRev) {
			//System.out.println("Date " + t.getDate()+   " Std   " + t.getStartDate());
			LocalDate start = LocalDate.parse(t.getDate().split(",")[1]);
			LocalDate end = LocalDate.parse(t.getStartDate());
			
			if(!(end.isBefore(start) || end.equals(start))) {
				ltsRev.remove(t);
			}	
		}
		
	    Map<String, List<Integer>> resultRev =
	    		ltsRev.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getCount, Collectors.toList())
	                        )
	                );
	    
	    Map<String, List<String>> resultRevDateList =
	    		ltsRev.stream().collect(
	                        Collectors.groupingBy(Transaction::getDate,
	                                Collectors.mapping(Transaction::getStartDate, Collectors.toList())
	                        )
	                );
	    
	    
	    for(Map.Entry<String, List<Integer>> entry : resultRev.entrySet()) {
	       	 
	       	 //System.out.println(entry.getKey() + " entry REV  1              " +  entry.getValue());
	        }
		    
	    

	    for(Map.Entry<String, List<String>> entry : resultRevDateList.entrySet()) {
       	 
       	 	String newKey = "";
       	 	int size = entry.getValue().size() - 1;
       	 	newKey = entry.getKey() + "," + entry.getValue().get(size);
       	 	//System.out.println("     <<<< "+  entry.getKey() + "     " + entry.getValue().get(size) + "    val " + resultRev.get(entry.getKey()));
       	 
       	 	resultRev.put(newKey, resultRev.get(entry.getKey()));
       	 	resultRev.remove(entry.getKey());
       	}
	    
	   
	    for(Map.Entry<String, List<Integer>> entry : resultRev.entrySet()) {
       	 
       	 	System.out.println(entry.getKey() + " entry REV  XXXXX             " +  entry.getValue());
        }
	    
	    //////////////////////////////// REV ///////////////////////////////////
	    
	    List<Integer> lint = new ArrayList<>();
	    lint.add(0);
	    List<String> removeKey = new ArrayList<>();
	    
	    List<String> resultList = new ArrayList<>();

	    Map<String, List<Integer>> mapPut = new HashMap<String, List<Integer>>();
	    
	    
	     for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
	    	 String key = entry.getKey();
		     String [] keys = key.split(",");
		     String empDate = keys[0]+keys[1];
	    	 resultList.add(empDate);
	     }
	    
	    Iterator<Map.Entry<String, List<Integer>>> iterator = result.entrySet().iterator() ;
        while(iterator.hasNext()){
        	
        	Map.Entry<String, List<Integer>> entry = iterator.next();
        	
        	String key = entry.getKey();
	    	String [] keys = key.split(",");
	    	String empDate = keys[0]+keys[1];
	    	String empFWD = keys[0];
	    	
	    	String startDateFwd = keys[2];
	    	int lastPosFwd = entry.getValue().get(entry.getValue().size() - 1);
	    
	    	System.out.println(" KEY x  BEFORE " + key  + " Value x" + entry.getValue() + " lastPosFwd " + lastPosFwd);
        	
	    	
	    	
	    	
        	for(Map.Entry<String, List<Integer>> entryRev : resultRev.entrySet()) {
        		String keyForPut = "";
        		List<Integer> valueForput =  new ArrayList<>();
        		
        		String keyRev = entryRev.getKey();
	    		String [] keysRev = keyRev.split(",");
	    		String empREV = keysRev[0];
	    		
	    		String empDateRev = keysRev[0]+keysRev[1];
	    		String startDateRev = keysRev[2];
	    		
	    		
	    		LocalDate dateFwd = LocalDate.parse(startDateFwd);
	    		LocalDate dateRev = LocalDate.parse(startDateRev);
	    		
	    		
	    		System.out.println("entryRev KEY " + keyRev + " VAL " + entryRev.getValue());
	    		//System.out.println(" dateFwd " + dateFwd + " dateRev " + dateRev);
	    		System.out.println(("COMPARE " + empDate + "    " + empDateRev));
	    		
	    		
	    		if(empFWD.equals(empREV)) {
	    			if(empDate.equals(empDateRev)) {
		    			
		    			if(dateRev.isAfter(dateFwd)) {
		    				//result.put(key, entryRev.getValue());
		    				
		    				keyForPut = keyRev;
		    				valueForput = entryRev.getValue();
		    				removeKey.add(key);
		    			}else {
		    				if(lastPosFwd == 0) {
			    				//result.put(key, lint);
		    					
			    				keyForPut = key;
			    				valueForput = lint;
			    			}else {
			    				//result.put(key, entry.getValue());
			    				
			    				keyForPut = key;
			    				valueForput = entry.getValue();
			    			}
		    			}
		    		}else {
		    			
		    			if(!resultList.contains(empDateRev)) {
		    				keyForPut = keyRev;
		    				valueForput = entryRev.getValue();
		    			}
		    				
		    		}
	    			
	    		}
	    		
	    		if(keyForPut.length() > 0)
	    			mapPut.put(keyForPut, valueForput);
        	}
        	
        	//System.out.println(" KEY x  AFTER " + key  + " Value x" + entry.getValue());

        	
        }
        
        
       for(String key : removeKey){
        	System.out.println("CHECK KEY REMOVE " + key);
        	
        }
        
        for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
        	System.out.println("55555 KEY  " + entry.getKey() + " 55555 VAL " + entry.getValue());
        }
        
        for(Map.Entry<String, List<Integer>> entry : mapPut.entrySet()) {
        	for(String key : removeKey){
        		result.remove(key);
        	}
        	System.out.println("entry NEW PUT KEY " + entry.getKey() + " NEW PUT Value " + entry.getValue());
        	result.put(entry.getKey(), entry.getValue());
        }
       
        for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
        	System.out.println("55555 KEY  AFTER " + entry.getKey() + " 55555 VAL " + entry.getValue());
        }
	    
	    
	    /*
	    for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
	    	String key = entry.getKey();
	    	String [] keys = key.split(",");
	    	String empDate = keys[0]+keys[1];
	    	
	    	String startDateFwd = keys[2];
	    	int lastPosFwd = entry.getValue().get(entry.getValue().size() - 1);
	    
	    	System.out.println(" KEY x  BEFORE " + key  + " Value x" + entry.getValue() + " lastPosFwd " + lastPosFwd);
	    	for(Map.Entry<String, List<Integer>> entryRev : resultRev.entrySet()) {
	
	    		String keyRev = entryRev.getKey();
	    		String [] keysRev = keyRev.split(",");
	    		
	    		String empDateRev = keysRev[0]+keysRev[1];
	    		String startDateRev = keysRev[2];
	    		
	    		System.out.println("keyRev " + keyRev);
	    		
	    		LocalDate dateFwd = LocalDate.parse(startDateFwd);
	    		LocalDate dateRev = LocalDate.parse(startDateRev);

	    		if(empDate.equals(empDateRev)) {
	    			//System.out.println("REPLACE WITH REV >>>>>>>>>>>>>>>>>>>\n");
	    			//System.out.println("FWD    KEY  " + entry.getKey() + " FWD VALUE   " + entry.getValue());
		    		//System.out.println("Rev    KEY  " + entryRev.getKey() + " REV VALUE   " + entryRev.getValue() );
	    			if(dateRev.isAfter(dateFwd)) {
	    				result.put(key, entryRev.getValue());
	    			}else {
	    				if(lastPosFwd == 0) {
		    				result.put(key, lint);
		    			}else {
		    				result.put(key, entry.getValue());
		    			}
	    			}
	    			
	    			//System.out.println("- - - - - - - - - - - - - \n");
	    			
	    		}
	    	}
	    	System.out.println(" KEY x  AFTER " + key  + " Value x" + entry.getValue());
	  	}
	    
		*/
	    
	    
		BranchInfo BranchF = new BranchInfo("0111");
	
		
		Validation pCA = new Validation();
		pCA.setValidateByValidateDayBranch("NumCA", BranchF.getOperationDay());
		
		
		List<String> listDate = new ArrayList<>();
		listDate = getListCAByDate("2017-10-05", "2017-10-05");
		boolean flag = true;
		
		List<Transaction> sumList = new ArrayList<Transaction>();
        int countx = 0;
        
        for(Map.Entry<String, List<Integer>> entry : result.entrySet()) {
        	  String key = entry.getKey();
        	  for (int value : entry.getValue()) {
    			  countx = entry.getValue().get(entry.getValue().size() - 1);
    		  }
        	  System.out.println("KEY !!! " + key + "             " + entry.getValue() );
        	  sum.put(key, countx);
        	  
        	  String[] keys = key.split(",");
        	  String date = keys[1];
        	  
        	  Transaction tx = new Transaction(date, countx);
        	  
        	  sumList.add(tx);
        	  
        	  countx = 0;
        	  
        }
        
        
        for(Transaction t : sumList) {
        	System.out.println(t.getDate()  + "               "    + t.getCount());
        	
        }
        Map<String, Integer> counting = sumList.stream().collect(
                Collectors.groupingBy(Transaction::getDate, Collectors.summingInt(Transaction::getCount)));
        
        
		for(String lt : listDate) {
			String checkCA = "90009"+","+ lt;
			for (Entry<String, Integer> entry : counting.entrySet()) {
				
				System.out.println(entry.getKey()+ " VALUE " + entry.getValue());
				if(!checkCA.equals(entry.getKey())) {
					//System.out.println(entry.getKey()+ " VALUE " + entry.getValue());
					//System.out.println("NUM CA" + entry.getValue());
					if( !checkOperationCA(pCA,entry.getValue()) ){
						flag = false;
						System.out.println("สาขาที่โยกย้ายจำนวน  CA เกิน ");
						//this.msgError = this.msgError +"สาขาที่โยกย้ายจำนวน  CA เกิน ";
						break;
					}
				}
			}
		}

		writeFile(counting, "D:\\log.txt");  
		
	}
}
