package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Formatter;


import java.util.Scanner;

/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
class Date
{
    public int year; 
    public int month;  
    public int day; 
};

public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.
	private String key="databasePinKey";
	/**
	 * Default constructor.
	 * DO NOT REMOVE.
	 */
	App()
	{
		// TODO: Any actions you need.
		today = new Date ();
		setDate(2011, 3, 1);
	}

	/**
	 * This is an example access operation to the DB.
	 */

	private Date today;

	void exampleAccessToDB()
	{
		// Statement and ResultSet are AutoCloseable and closed automatically.
		try( Statement statement = _connection.createStatement() )
		{
			try( ResultSet resultSet = statement.executeQuery( "select owner, table_name from all_tables" ) )
			{
				while( resultSet.next() )
					System.out.println( resultSet.getString( 1 ) + " " + resultSet.getString( 2 ) + " " );
			}
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
		}
	}

	////////////////////////////// Implement all of the methods given in the interface /////////////////////////////////
	// Check the Testable.java interface for the function signatures and descriptions.

	@Override
	public String initializeSystem()
	{
		// Some constants to connect to your DB.
		final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/ORCL";
		final String DB_USER = "c##zholoien";
		final String DB_PASSWORD = "9463274";

		// Initialize your system.  Probably setting up the DB connection.
		Properties info = new Properties();
		info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
		info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
		info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );

		try
		{
			OracleDataSource ods = new OracleDataSource();
			ods.setURL( DB_URL );
			ods.setConnectionProperties( info );
			_connection = (OracleConnection) ods.getConnection();

			// Get the JDBC driver name and version.
			DatabaseMetaData dbmd = _connection.getMetaData();
			System.out.println( "Driver Name: " + dbmd.getDriverName() );
			System.out.println( "Driver Version: " + dbmd.getDriverVersion() );

			// Print some connection properties.
			System.out.println( "Default Row Prefetch Value is: " + _connection.getDefaultRowPrefetch() );
			System.out.println( "Database Username is: " + _connection.getUserName() );
			System.out.println();

			return "0";
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	public String setDate( int year, int month, int day ){
		today.year=year;
		today.month=month;
		today.day=day;
		return "0 "+year+" "+month+" "+day;
	}
	
	public String getDate(){
		return today.year+"-"+today.month+"-"+today.day;
	}

	public int getDaysInMonth(){
		switch (today.month){
			case 1: return 31;
			case 2: return 28;
			case 3: return 31;
			case 4: return 30;
			case 5: return 31;
			case 6: return 30;
			case 7: return 31;
			case 8: return 31;
			case 9: return 30;
			case 10: return 31;
			case 11: return 30;
			case 12: return 31;
		}return 0;
	}

	public String updateAvgBalance(String id){
		int days = today.day;
		String sql1 = "Select A.balance, A.lastTrans, A.avgBalance from Account2 A where A.aid = '" +id + "'";
		
		float avg = 0;
		float balance = 0;
		String date = getDate();
		int month = today.month;
		int day =today.day;
		

		Statement stmt = null;
		try{ stmt = _connection.createStatement();
            		ResultSet rs = stmt.executeQuery(sql1);
			rs.next();
			balance=rs.getFloat("balance");
			avg = rs.getFloat("avgBalance");
			date=rs.getString("lastTrans");
			month=Integer.parseInt(date.split("-")[1]);
			day=Integer.parseInt(date.split("-")[2]);
			if (month==today.month){
				days=days-day;
				System.out.println("It has been "+days+" days");			
			}avg = avg + (balance*days/(getDaysInMonth()-1));
			
			System.out.println("The new average is "+avg);
			String sql2 = "Update Account2 " +
                            "set avgBalance = "+ avg +
                            ", lastTrans = '"+ getDate() +
                            "' where aid = '" + id + "'";
			stmt.executeUpdate(sql2);
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}

	
		return "0";

	}

	public String accureInterest(String aid){
		if (today.day != getDaysInMonth())
			return "Not the last day of the month";

		
		String sql1 = "Select A.balance, A.rate, A.avgBalance from Account2 A where A.aid = '" +aid + "'";

		float rate=0;
		float iBalance=0;
		float interest=0;
		float avg=0;
		String sql2 = "Update Account2 " +
			    "set balance = balance +" + interest + 
			    " where aid = '" + aid + "'";
		Statement stmt = null;
		try{ stmt = _connection.createStatement();
            		ResultSet sr = stmt.executeQuery(sql1);
			if (sr.next()){
			sr.close();
			
			} else return "0 No account exists";
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
		
		updateAvgBalance(aid);
		try{ stmt = _connection.createStatement();	
			ResultSet rs = stmt.executeQuery(sql1);
			rs.next();
			
			iBalance=rs.getFloat("balance");
			rate = rs.getFloat("rate");
			avg=rs.getFloat("avgBalance");
			interest= rate*avg;
			//System.out.println("avg = "+avg);

			stmt.executeUpdate(sql2);
			return "0 "+aid+" "+iBalance+" "+(iBalance+interest);
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}

	}

	@Override
	public String createTables(){
		Statement stmt = null;
		String sql1 = "CREATE TABLE Owner2 " +
                   "(taxid VARCHAR(255) not NULL, " +
                   " name VARCHAR(255), " + 
                   " address VARCHAR(255), " +  
                   " PRIMARY KEY ( taxid ))";

		String sql2 = "CREATE TABLE Account2 " +
                   "(taxid VARCHAR(255) not NULL, " +
                   " name VARCHAR(255), " + 
		   " type VARCHAR(255), " + 
		   " status VARCHAR(255), " + 
		   " lastTrans VARCHAR(255), " + 
		   " balance FLOAT, " +  
		   " avgBalance FLOAT, " +  
		   " rate FLOAT, " + 
                   " address VARCHAR(255), " + 
		   " aid VARCHAR(255) not NULL, " + 
		   " CONSTRAINT FK_PrimaryOwner FOREIGN KEY (taxid) REFERENCES Owner2(taxid) on delete cascade," +
                   " PRIMARY KEY ( aid ))";

		String sql3 = "CREATE TABLE OwnRelationship " +
                   "(taxid VARCHAR(255) not NULL, " +
		   " aid VARCHAR(255) not NULL, " + 
		    " pin VARCHAR(255) not NULL," + 
		   " CONSTRAINT FK_Owner FOREIGN KEY (taxid) REFERENCES Owner2(taxid) on delete cascade," +
		  " CONSTRAINT FK_Account FOREIGN KEY (aid) REFERENCES Account2(aid) on delete cascade," +
                   " PRIMARY KEY ( taxid, aid ))";   

		String sql4 = "Create Table Transaction2 " +
            "(account1 Varchar(255) not NULL, " +
		    "account2 Varchar(255), " +
		    "trans_type VARCHAR(255) not null, " +
		    "ownid Varchar(255) not Null, " +
		    "Amount float not null, " +
		    "t_date varchar(255) not null, " +
		    " Constraint FK_acc1 foreign key (account1) references Account2(aid) on delete cascade," +
		    " Constraint FK_ownT foreign key (ownid) references Owner2(taxid) on delete cascade," +
		    " Primary Key (account1, t_date, trans_type))";


		String sql5 = "CREATE TABLE  Pocket2" +
                   "(taxid VARCHAR(255) not NULL, " +
                   " status VARCHAR(255), " + 
		   " balance FLOAT, " +
		   " parent_aid VARCHAR(255) not NULL, " +  
		   " aid VARCHAR(255) not NULL, " + 
		   " CONSTRAINT FK_ParentAccount FOREIGN KEY (parent_aid) REFERENCES Account2(aid) on delete cascade," +
		   " CONSTRAINT FK_PocketOwner FOREIGN KEY (taxid) REFERENCES Owner2(taxid) on delete cascade," +
                   " PRIMARY KEY ( aid ))";

		String sql6 = "CREATE TABLE PocketOwn " +
                   "(taxid VARCHAR(255) not NULL, " +
		   " aid VARCHAR(255) not NULL, " + 
		    " pin VARCHAR(255) not NULL," + 
		   " CONSTRAINT FK_PocketOwnerRelationship FOREIGN KEY (taxid) REFERENCES Owner2(taxid) on delete cascade," +
		  " CONSTRAINT FK_PocketAccount FOREIGN KEY (aid) REFERENCES Pocket2(aid) on delete cascade," +
                   " PRIMARY KEY ( taxid, aid ))";
 
		String sql7 = "CREATE TABLE PocketTransaction " +
			"( aid VARCHAR(255) not NULL, " + 
		   " aid2 VARCHAR(255) , " +
		   " t_date VARCHAR(255) not NULL," +
		   " t_type VARCHAR(255) not NULL," +
		   " CONSTRAINT FK_PocketAccountT FOREIGN KEY (aid) REFERENCES Pocket2(aid) on delete cascade," +
                   " PRIMARY KEY ( aid, t_date, t_type ) )";

		try{stmt = _connection.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			stmt.executeUpdate(sql4);
			stmt.executeUpdate(sql5);
			stmt.executeUpdate(sql6);
			stmt.executeUpdate(sql7);
			System.out.println("Created Owner2 table");
			return "0";
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}		
		


		
	}

	@Override
	public String dropTables(){
		Statement stmt = null;
		String sql1 = "DROP TABLE Owner2 ";
		String sql2 = "DROP TABLE Account2 "; 
		String sql3 = "DROP TABLE OwnRelationship ";
		String sql4 = "DROP TABLE Transaction2 ";
		String sql5 = "DROP TABLE Pocket2 ";
		String sql6 = "DROP TABLE PocketOwn ";
		String sql7 = "DROP TABLE PocketTransaction ";
		try{stmt = _connection.createStatement();
			stmt.executeUpdate(sql7);
			stmt.executeUpdate(sql6);
			stmt.executeUpdate(sql5);
			stmt.executeUpdate(sql4);
			stmt.executeUpdate(sql3);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql1);

		
		
			System.out.println("Dropped Owner2 table");
			return "0";
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}		
		


	}


	public String setPin(String taxid){
		

		Statement stmt=null, stmt2=null;

		String sql1 = "Select A.pin from ownRelationship A where A.taxid = '" +taxid + "'";	
		 try{ stmt = _connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);
            if(rs.next() ){

                    String oldPin = rs.getString("pin");
                    

                    //TODO: Check if oldPin matches
			Scanner in = new Scanner(System.in);
        		System.out.println("Enter New PIN\n");
        		String input = in.nextLine();
			String newPin ="";
			try{
				newPin = encrypt(input, key);
			} catch (Exception e) {
 			   e.printStackTrace();
			}
                        String sql3 = "Update OwnRelationship " +
                            "set pin = '" + newPin +
                            "' where taxid = '" + taxid + "'";
			String sql4 = "Update PocketOwn " +
                            "set pin = '" + newPin +
                            "' where taxid = '" + taxid + "'";
			try{stmt2 = _connection.createStatement();
                            stmt2.executeUpdate(sql3);
			    return "0 New Pin Set";

                        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
                    }return "0 Owner does not exist in our records";
                
         }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
		

	}

	

	


	public static String encrypt(String strClearText,String strKey) throws Exception{
	String strData="";
	
	try {
		SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
		Cipher cipher=Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
		byte[] encrypted=cipher.doFinal(strClearText.getBytes());
		strData=new String(encrypted);
		
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception(e);
	}
	return strData;
	}

	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address ){
		Statement stmt = null;		
		if (initialBalance<1000.0||accountType==AccountType.POCKET)
			return "1";
		double rate=0;
		if (accountType==AccountType.INTEREST_CHECKING)
			rate=.03;
		else if (accountType==AccountType.SAVINGS)
			rate=.048;
		String pin ="";
		String sql3 = "Select A.pin from ownRelationship A where A.taxid = '" +tin + "'";
		try{stmt = _connection.createStatement();		
			ResultSet rs = stmt.executeQuery(sql3);
			if (rs.next())
				pin=rs.getString("pin");
			else{
                        try{
                                pin = encrypt("1717", key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
			}
		}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
		String sql1 = "INSERT INTO Account2 " +
                   "VALUES ('"+tin+"', '"+name+"', '"+accountType+"', 'OPEN',"+"'"+getDate()+"', "+ 0+", "+0+", "+rate+", '"+address+"', '"+id+"')";
		String sql2 = "INSERT INTO OwnRelationship " +  "VALUES ('"+tin+"', '"+id+"', '1717')";
		    /*"VALUES ('"+tin+"', '"+id+"', '"+pin+"')";*/

		try{stmt = _connection.createStatement();			
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			

		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}

		deposit(id, initialBalance);
		return "0 " + id + " " + accountType + " " + initialBalance + " " + tin;

	}

    //@Override
    public String deposit(String accountID, double amount){
	
	Statement stmt = null, stmt2 = null, stmt3 = null;
	float balance = 0, newAmount = 0;
	boolean flag = true;
        String sql1 = "Select A.taxid, A.pin from ownRelationship A where A.aid = '" +accountID + "'";
	String sql4 = "Select A.balance from Account2 A where A.aid = '" +accountID + "'";
	try{stmt =  _connection.createStatement();
	    ResultSet sr = stmt.executeQuery(sql4);
	     if(sr.next()){
		balance = sr.getFloat("Balance");
	        
		}
	}catch( SQLException e )
	    {
			System.err.println( e.getMessage() );
			flag = false;
			return "1";
			    
	 }
	
	 try{ stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql1);
	    while(rs.next() && flag){
		
		    String pin = rs.getString("pin");
		    boolean result = true;
		    
		    if(result == true){
			updateAvgBalance(accountID);
			String sql2 = "Insert into Transaction2 " +
			    "Values ( '" + accountID + "', 'null', 'deposit','" + rs.getString("taxid") + "', '" + amount + "', 'date')";
			
			String sql3 = "Update Account2 " +
			    "set balance = balance +" + amount + 
			    " where aid = '" + accountID + "'";
		       
			try{stmt2 = _connection.createStatement();
			    stmt2.executeUpdate(sql2);
			    stmt2.executeUpdate(sql3);
			    flag = false;
			}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			flag = false;
			return "1";
			    
		}
		    }
		}
	 }catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";

		}


	String sql5 = "Select A.balance from Account2 A where A.aid = '" +accountID + "'";
	try{stmt =  _connection.createStatement();
	    ResultSet sr = stmt.executeQuery(sql4);
	     if(sr.next()){
		newAmount = sr.getFloat("Balance");
	        
		}
	}catch( SQLException e )
	    {
			System.err.println( e.getMessage() );
			flag = false;
			return "1";
			    
	 }
	 return "0 " + balance+ " " + newAmount;
	

    }


	public String withdrawal(String accountID, double amount ){
		Statement stmt=null, stmt2 =null;
		if (checkBalance(accountID, amount)==false)
			return "0 Insuffiecient Funds";
		float balance = 0, newAmount = 0;
        	boolean flag = true;
        	String sql1 = "Select A.taxid, A.pin from ownRelationship A where A.aid = '" +accountID + "'";
        	String sql4 = "Select A.balance from Account2 A where A.aid = '" +accountID + "'";
        	try{stmt =  _connection.createStatement();
            		ResultSet sr = stmt.executeQuery(sql4);
             		if(sr.next()){
                		balance = sr.getFloat("Balance");

                	}
        	}catch( SQLException e )
            	{
                        System.err.println( e.getMessage() );
                        flag = false;
                        return "1";

         	}

		try{ stmt = _connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);
            while(rs.next() && flag){

                    String pin = rs.getString("pin");
                    boolean result = true;

                    if(result == true){
                        String sql2 = "Insert into Transaction2 " +
                            "Values ( '" + accountID + "', 'null', 'withdrawal','" + rs.getString("taxid") + "', '" + amount + "', 'date')";
			updateAvgBalance(accountID);
                        String sql3 = "Update Account2 " +
                            "set balance = balance -" + amount +
                            " where aid = '" + accountID + "'";

                        try{stmt2 = _connection.createStatement();
                            stmt2.executeUpdate(sql2);
                            stmt2.executeUpdate(sql3);
                            flag = false;
                        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        flag = false;
                        return "1";

                }
                    }
                }
         }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }


        String sql5 = "Select A.balance from Account2 A where A.aid = '" +accountID + "'";
        try{stmt =  _connection.createStatement();
            ResultSet sr = stmt.executeQuery(sql4);
             if(sr.next()){
                newAmount = sr.getFloat("Balance");

                }
        }catch( SQLException e )
            {
                        System.err.println( e.getMessage() );
                        flag = false;
                        return "1";

         }
	conditionalClose(accountID);	
         return "0 " + balance+ " " + newAmount;



		


	}


    public boolean checkPin(String taxId){
	Statement stmt = null;
        String sql1 = "Select A.pin from ownrelationship A where A.taxid = '" +taxId + "'";
	String enteredPin ="";
	String pin = "";
	try{ stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql1);
	    if(rs.next()){
	        pin = rs.getString("pin");
	    }
	}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return false;
		}
    
	

	Scanner scan = new Scanner(System.in);
	System.out.println("Please enter your Pin");
	String input = scan.nextLine();
	System.out.println("Pin:" + pin + " <-");
	try{
	    enteredPin = encrypt(pin, key);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if(input.equals(pin)){
	    return true;
	}
	else{
	    return false;
	    }
    }

    public boolean checkBalance(String aid, double amount){
	Statement stmt = null;
	String sql = "Select A.balance from Account2 A where A.aid = '" + aid + "'";
	try{ stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    if(rs.next()){
	        float result = rs.getFloat("Balance");
		if (result < amount){
		    return false;
		}
		else{
		    return true;
		}
		
	    }
	}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return false;
		}
	return false;
    }


    public boolean checkPocketBalance(String aid, double amount){
        Statement stmt = null;
        String sql = "Select A.balance from pocket2 A where A.aid = '" + aid + "'";
        try{ stmt = _connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                float result = rs.getFloat("Balance");
                if (result < amount){
                    return false;
                }
                else{
                    return true;
                }

            }
        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return false;
                }
        return false;
    }

    @Override
    public String showBalance( String accountId ){
	Statement stmt = null;
	float balance = 0;
	String sql1 = "Select A.balance from Account2 A where A.aid = '" +accountId + "'";
        try{stmt =  _connection.createStatement();
            ResultSet sr = stmt.executeQuery(sql1);
             if(sr.next()){
                balance = sr.getFloat("Balance");

                }
        }catch( SQLException e )
            {
                        System.err.println( e.getMessage() );
                        return "1";

         }
	return "0 " + balance;
    }




    




    public String payFriend( String from, String to, double amount ){
	Statement stmt = null;
	float from_balance = 0, to_balance = 0;
	String pin = "";
	String sql1 = "select R.pin from pocketOwn R where R.aid='"+from+"'";
	String sql2 = "Update Pocket2 set balance=balance-"+amount+" where aid='"+from+"'";
	String sql3 = "Update Pocket2 set balance=balance+"+amount+" where aid='"+to+"'";
        String sql4 = "Insert INTO PocketTransaction " +
                   "VALUES ('"+from+"', '"+ to+ "', 'Today', 'pay-friend')";
        try{stmt = _connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql1);
			if(rs.next()){
			    pin = rs.getString("pin");
			}
	}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
			return "1";
                }
        	
	if(checkPocketBalance(from, amount)){
	    String sql5 = "SELECT a.balance FROM pocket2 a WHERE a.aid='"+from+"'";
	    try{stmt = _connection.createStatement();
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql3);
		stmt.executeUpdate(sql4);
		conditionalClose(from);
		ResultSet rs = stmt.executeQuery(sql5);
		if(rs.next()){
		    System.out.println("first account");
		    from_balance = rs.getFloat("balance");
		}
		else{
		    return "1";
		}
	    }catch( SQLException e )
                {
		    System.err.println( e.getMessage() );
		    return "1";
                }		
	}
	else{
	    return "1";
	}
	
	String sql6 = "SELECT p.balance FROM pocket2 p WHERE p.aid ='"+to+"'";
                try{stmt = _connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql6);
			System.out.println("About to get account2");
                        if(rs.next()){
                            to_balance = rs.getFloat("balance");
                        }
			else{
                            return "1";
			}
                }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }
		return String.format("0 %.2f %.2f", from_balance, to_balance);
    }


	@Override
	public String createPocketAccount( String id, String linkedId, double initialTopUp, String tin ){
		Statement stmt = null;	
		

		String sql1 = "select R.pin from OwnRelationship R where R.aid='"+linkedId+"' and R.taxid='"+tin+"'";		
		String sql2= "INSERT INTO Pocket2 " +
		"VALUES ('"+tin+"', 'OPEN', "+ 0.0+", '"+linkedId+"', '"+id+"')";
		
		
		
		if (checkBalance(linkedId, initialTopUp)==false)
			return "0 Pocket Account not created. Insufficient Funds\n";

		try{stmt = _connection.createStatement();	
			ResultSet rs = stmt.executeQuery(sql1);
			if (rs.next()){

				String pin=rs.getString("pin");
				String sql3 = "INSERT INTO PocketOwn " +
                   "VALUES ('"+tin+"', '"+id+"', '"+pin +"')";


				stmt.executeUpdate(sql2);
//				System.out.println("Trying to add Pocket Account");
				stmt.executeUpdate(sql3);
				topUp(id, initialTopUp);
				return String.format("0 "+id+" POCKET %.2f",initialTopUp);
			}else{ System.out.println("No matching table");}
			
			
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}

		

		return "0";
	}

	@Override
	public String createCustomer( String accountId, String tin, String name, String address ){	Statement stmt = null;
		String sql1 = "INSERT INTO Owner2 " +
                   "VALUES ('"+tin+"', '"+name+"', '"+address+"')";
		String pin ="";
                        try{
                                pin = encrypt("1717", key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }

		String sql2 = "select A.aid from account2 A where A.aid='"+accountId+"'";
		String sql3 = "INSERT INTO OwnRelationship " +
                   "VALUES ('"+tin+"', '"+accountId+"', '"+pin+"')";
		try{stmt = _connection.createStatement();			
			stmt.executeUpdate(sql1);
			ResultSet rs = stmt.executeQuery(sql2);
			if (rs.next()){
				stmt.executeUpdate(sql3);
			}
			System.out.println("New Onwer added");
			return "0";
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}		
	
	}


	String getParentAccount(String pocket){
		String sql="Select P.parent_aid From Pocket2 P Where P.aid ='"+pocket+"'";
		Statement stmt = null;
		try{stmt = _connection.createStatement();			
			ResultSet rs=stmt.executeQuery(sql);
			if (rs.next())
				return rs.getString("parent_aid");
			
		}catch ( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}	
		return "";
	}


		
	@Override
	public String topUp( String accountId, double amount ){
		
		Statement stmt = null;
		String parent = getParentAccount(accountId);
		if (checkBalance(parent, amount)==false)
			return "0 Insufficient Funds\n";
		
		String pin = "";
		String sql5 = "select R.pin from pocketOwn R where R.aid='"+accountId+"'";
		String sql1 = "Update Account2 set balance=balance-"+amount+" where aid='"+parent+"'";
		String sql2 = "Update Pocket2 set balance=balance+"+amount+" where aid='"+accountId+"'";
		String sql3 = "Insert INTO PocketTransaction " +
                   "VALUES ('"+accountId+"', NULL, 'Today', 'TOPUP')";		
		String sql4 = "SELECT a.balance, p.balance FROM Account2 a, pocket2 p WHERE a.aid='"+parent+"' AND p.aid ='"+accountId+"'";
	
	try{stmt = _connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql5);
                        if(rs.next()){
                            pin = rs.getString("pin");
                        }
        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }

	
		try{stmt = _connection.createStatement();			
		    
			updateAvgBalance(parent);
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			conditionalClose(parent);
			ResultSet rs = stmt.executeQuery(sql4);
			rs.next();
			System.out.println(String.format("0 %.2f %.2f", rs.getFloat(1), rs.getFloat(2)));
							
			return String.format("0 %.2f %.2f", rs.getFloat(1), rs.getFloat(2));
		        
		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	
		

	}

	public String purchase( String accountId, double amount ){
		if (checkPocketBalance(accountId, amount)==false)
			return "0 Insufficient Funds";
		Statement stmt;
		 float balance = 0, newAmount = 0;
		String pin = "";
                String sql5 = "select R.pin from pocketOwn R where R.aid='"+accountId+"'";
                String sql2 = "Update Pocket2 set balance=balance-"+amount+" where aid='"+accountId+"'";
                String sql3 = "Insert INTO PocketTransaction " +
                   "VALUES ('"+accountId+"', NULL, 'Today', 'purchase')";
                String sql4 = "SELECT p.balance FROM pocket2 p WHERE p.aid ='"+accountId+"'";

		 try{stmt = _connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql5);
                        if(rs.next()){
                            pin = rs.getString("pin");
                        }
			
        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }



		try{stmt = _connection.createStatement();

                      
                        ResultSet rs = stmt.executeQuery(sql4);
                        rs.next();
                        balance = rs.getFloat(1);
			stmt.executeUpdate(sql2);
                        stmt.executeUpdate(sql3);
                        ResultSet rs2 = stmt.executeQuery(sql4);
                        rs2.next();
                        newAmount = rs2.getFloat(1);
			conditionalClose(accountId);
                        return String.format("0 %.2f %.2f", balance, newAmount);
                        
                }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }







	}

	
	
	public String transfer( String from, String to, double amount ){
	Statement stmt = null;
	float from_balance = 0, to_balance = 0;
	if (amount>2000)
		return "0 Too much money attempting to be transferred";
	String taxid="";
	String pin = "";
	String sql1 = "Select A.taxid, A.pin from ownRelationship A, OwnRelationship B where A.aid = '" +from + "' and B.taxid = A.taxid and B.aid='"+to+"'";
	String sql2 = "Update Account2 set balance=balance-"+amount+" where aid='"+from+"'";
	String sql3 = "Update Account2 set balance=balance+"+amount+" where aid='"+to+"'";
        
        
	try{stmt = _connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql1);
			if(rs.next()){
			    pin = rs.getString("pin");
			    taxid=rs.getString("taxid");
			}else{
				return "0 no Owner in common";
			}
	}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
			return "1";
                }
        	
	if(checkBalance(from, amount)){
		updateAvgBalance(to);
		updateAvgBalance(from);
		String sql5 = "SELECT a.balance FROM Account2 a WHERE a.aid='"+from+"'";
		String sql6 = "SELECT a.balance FROM Account2 a WHERE a.aid='"+to+"'";
		String sql4 = "Insert INTO Transaction2 " +
                   "VALUES ('"+from+"', '"+ to+ "', 'transfer', '"+ taxid+"', "+amount+", 'Today')";
                try{stmt = _connection.createStatement();
                        stmt.executeUpdate(sql2);
                        stmt.executeUpdate(sql3);
                        stmt.executeUpdate(sql4);
                        ResultSet rs = stmt.executeQuery(sql5);
			if(rs.next()){
			    System.out.println("first account");
			    from_balance = rs.getFloat("balance");
			}
			else{
			    return "1";
			}ResultSet sr = stmt.executeQuery(sql6);
			if(sr.next()){
			    System.out.println("second account");
			    from_balance = sr.getFloat("balance");
			}
			else{
			    return "1";
			}
			
		}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }		
	}else{
	    return "1";
	}
	
		conditionalClose(from);
		return String.format("0 %.2f %.2f", from_balance, to_balance);
    }




	public String conditionalClose(String aid){
		String sql1 = "SELECT a.balance FROM Account2 a WHERE a.aid='"+aid+"'";
		String sql2 = "SELECT a.balance FROM Pocket2 a WHERE a.aid='"+aid+"'";
		float balance = 0;
		String sql3 = "Update Account2 set status = 'CLOSED' where aid='"+aid+"'";
		String sql4 = "Update Pocket2 set status = 'CLOSED' where aid='"+aid+"'";
		Statement stmt = null;
		try{stmt = _connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql1);
			if(rs.next()){
			    balance=rs.getFloat("balance");
				if (balance==0) stmt.executeUpdate(sql3);
			}else{
				rs= stmt.executeQuery(sql2);
				if (rs.next()){
					balance=rs.getFloat("balance");
					if (balance==0) stmt.executeUpdate(sql4);
				}
			}	
		}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }
		return "0";
	}
	/**
	 * Example of one of the testable functions.
	 */
	//@Override
	public String listClosedAccounts()
	{
		String  builder="0";
		String sql1 = "SELECT a.aid FROM Account2 a WHERE a.status='CLOSED'";
		String sql2 = "SELECT a.aid FROM Pocket2 a WHERE a.status='CLOSED'";
		Statement stmt = null;
		try{stmt = _connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql1);
			while (rs.next()){
				builder=builder+" "+rs.getString("aid");
			}
			rs = stmt.executeQuery(sql2);
			while (rs.next()){
				builder=builder+" "+rs.getString("aid");
			}
		}catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }
		

		return builder;
	}



    public void printAccounts(String taxId){
	Statement stmt = null;
	String sql1 = "SELECT * FROM Account2 a WHERE a.taxid='"+taxId+"'";
	String sql2 = "SELECT * FROM Pocket2 a WHERE a.taxid='"+taxId+"'";
	try{stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql1);
	    while (rs.next()){
		System.out.println("Account ID: " + rs.getString("aid")+ " " + "Balance: " + rs.getInt("Balance") + " " + "Account Type: " + rs.getString("type"));
	    }
	    rs = stmt.executeQuery(sql2);
	    while (rs.next()){
			System.out.println("Account ID: " + rs.getString("aid")+ " " + "Balance: " + rs.getInt("Balance") + " " + "Account Type: Pocket");
	    }
	}catch( SQLException e )
	    {
		    System.err.println( e.getMessage() );
	    }

	
    }

    public String getAccountType(String accountId){
	Statement stmt = null;
	String sql1 = "SELECT * FROM Account2 a WHERE a.aid='"+accountId+"'";
	String sql2 = "SELECT * FROM Pocket2 a WHERE a.aid='"+accountId+"'";
	try{stmt = _connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql1);
	    if (rs.next()){
		return rs.getString("type");
	    }
	    else{
	    rs = stmt.executeQuery(sql2);
	    if (rs.next()){
		return "POCKET";
	    }
	    }
	}catch( SQLException e )
	    {
		    System.err.println( e.getMessage() );
	    }
	return "";
	
    }

	/**
	 * Another example.
	 */
	
}
