<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.io.IOException"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.io.*"%>

<%@ page import="db.DatbaseConnection"%>
<%@ page import="db.Configuration"%>

<%@ page import="servlet.MakeOutput1"%>
<%@ page import="servlet.AuditLog"%>
<%@ page trimDirectiveWhitespaces="true"%>

<%
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String path = "";
		String filename = "";
		String start_d = request.getParameter("hstart_d");
		String stop_d = request.getParameter("hstop_d");
		String selectType = request.getParameter("hselectType");
		String selectComplete = request.getParameter("hselectComplete");
		
		System.out.println("approveExportFile >>>>");
		 
		String sql = "select t.UserID,t.CurBr,t.Curpos,t.CurLimit,t.TermID,t.Action,t.NewBr,t.NewPos,t.NewLimit,t.NewTermID,t.RequestID,t.ModeOutput "+ 
				 "from tbldt_output2 t order by t.CurBr,t.UserID ";
		preparedStatement = connect.prepareStatement(sql);
		//System.out.println(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		path = Configuration.getParam("PathOutput1");		
		filename = Configuration.getParam("FileNameOutput1");
		String file = path+filename;
		FileWriter writer = new FileWriter(file, false);
		String info = "";
		
		String NewBranch = "";
		String NewPosition = "";
		String NewLimit = "";
		String Mode = "";
		String Action = "";
		String NewTerminalID = "";
		String EmpID = "";
		
		try{
			writer.write("'UserID'	'CurBr'	'Curpos'	'Limit'	'TermID'	'Action'	'NewBr'	'NewPos'	'NewLimit'	'NewTermID'	'Mode' \n");
			while(resultSet.next()){
			String BranchID = resultSet.getString("CurBr");
			NewBranch = resultSet.getString("NewBr");
//			if (selectType.equals("F")){
//				Mode = "Forward";
//			}else if(selectType.equals("B")){
//				Mode = "Backward";
//			}else if(selectType.equals("A")){
//				if(resultSet.getString("RequestID").endsWith("-1")) {
//					Mode = "Forward";
//				} else {
//					if(resultSet.getString("RequestID").endsWith("-2")) {
//						Mode = "Backward";
//					} else {
//						Mode = resultSet.getString("ModeOutput");
//					}
//				}
//						
//			}
			Mode = resultSet.getString("ModeOutput");
			if(BranchID.equals(NewBranch)){
				Action = "Change";
			}else{
				Action = "Move";
			}
			
			EmpID = resultSet.getString("UserID");
			
			MakeOutput1 op1 = new MakeOutput1();
			
			//NewTerminalID = op1.getNewTerminal(EmpID,NewBranch);
			NewTerminalID = resultSet.getString("NewTermID");
			if (NewTerminalID.equals("")) {
				System.out.println(">> Blank Terminal ID <<");
				NewTerminalID = op1.getNewTerminal(EmpID, NewBranch);
			}
			NewBranch = resultSet.getString("NewBr");
			NewPosition = resultSet.getString("NewPos");
			NewLimit = resultSet.getString("NewLimit");
			
			System.out.println(">>>>>>>>>>>>>>> Export EmpID " + EmpID + " compare " + BranchID.equals(NewBranch));
			
			//System.out.println(resultSet.getString("UserID")+","+resultSet.getString("CurBr")+","+resultSet.getString("Curpos"));
			info = info+"'"+resultSet.getString("UserID")+"'\t'"+resultSet.getString("CurBr")+"'\t'"+resultSet.getString("Curpos")+"'\t'"
				 + resultSet.getString("CurLimit")+"'\t'"+NewTerminalID+"'\t'"+Action+"'\t'"
				 + NewBranch+"'\t'"+NewPosition+"'\t'"+NewLimit+"'\t'"
				 + Mode+"'\n";
		}
		
		writer.write(info);
		writer.flush();
        writer.close();
		
        resultSet.close();
	    preparedStatement.close();
	    connect.close();
	 
	}catch(Exception ex){
	 	System.out.println("approveExportFile.jsp: Execption caught!");
		System.out.println(ex);
	 
	}
	
    AuditLog al = new AuditLog();
    al.setModule("ManualMakeOutput1");
    
    MakeOutput1 mop = new MakeOutput1();
    try{
    	//mop.generateFile();
    }catch(Exception e){
    	e.printStackTrace();
    	al.setStatus("E");
    	al.setDesc("Exception generateFile");
    }finally{

    }
   
    
    System.out.println("END ApproveExportFile");
    try{
        InputStream fileInputStream = null;
        ServletOutputStream out1 = response.getOutputStream();
        
        System.out.println(" path :: " + path + " filename :: " + filename);
        
        fileInputStream = new FileInputStream(path+filename);
        
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition","attachment; filename=\""+"output1.csv"+"\"");
        int readBytes = 0;
        byte[] buffer = new byte[10000];
        while ( (readBytes = fileInputStream.read(buffer, 0, 10000)) != -1){
        	out1.write(buffer, 0, readBytes);
        }
        out1.flush();
        out1.close();

        fileInputStream.close();
        Object user = session.getAttribute("userModify");
        String userModified = user.toString();
        
        al.setStatus("C");
        al.setDesc("Generate output1.csv เรียบร้อย");
        al.setUserModified(userModified);
    }catch(Exception e){
    	System.out.println("approveExportFile exception:");
    	e.printStackTrace();
    	al.setStatus("E");
    	al.setDesc("Exception getfile output1");
    }finally{
    	al.insertAuditLog();
    }
%>