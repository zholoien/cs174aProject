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
	}

	/**
	 * This is an example access operation to the DB.
	 */
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
		   " balance FLOAT, " +  
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


	public String setPin(String accountID){
		

		Statement stmt=null, stmt2=null;

		String sql1 = "Select A.taxid, A.pin from ownRelationship A where A.aid = '" +accountID + "'";	
		 try{ stmt = _connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);
            while(rs.next() ){

                    String pin = rs.getString("pin");
                    boolean result = checkPin(pin);

                    if(result == true){
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
                            "' where aid = '" + accountID + "'";
			try{stmt2 = _connection.createStatement();
                            stmt2.executeUpdate(sql3);
			    return "0 New Pin Set";

                        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
                    }
                } return "Incorrect Pin";
         }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
		

	}



	public String setPocketPin(String accountID){


                Statement stmt=null, stmt2=null;

                String sql1 = "Select  A.pin from PocketOwn A where A.aid = '" +accountID + "'";
                 try{ stmt = _connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);
            rs.next();

                    String pin = rs.getString("pin");
                    boolean result = checkPin(pin);

                    if(result == true){
                        Scanner in = new Scanner(System.in);
                        System.out.println("Enter New PIN\n");
                        String input = in.nextLine();
                        String newPin ="";
                        try{
                                newPin = encrypt(input, key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                        String sql3 = "Update PocketOwn " +
                            "set pin = '" + newPin +
                            "' where aid = '" + accountID + "'";
                        try{stmt2 = _connection.createStatement();
                            stmt2.executeUpdate(sql3);
                            return "0 New Pin Set";

                        }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }
                    }
         }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";

                }

		return "0 PIN not set";
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
			rate=3.0;
		else if (accountType==AccountType.SAVINGS)
			rate=4.8;
		String pin ="";
                        try{
                                pin = encrypt("1717", key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
		String sql1 = "INSERT INTO Account2 " +
                   "VALUES ('"+tin+"', '"+name+"', '"+accountType+"', 'OPEN',"+ initialBalance+", "+rate+", '"+address+"', '"+id+"')";
		String sql2 = "INSERT INTO OwnRelationship " +
                   "VALUES ('"+tin+"', '"+id+"', '"+pin+"')";

		try{stmt = _connection.createStatement();			
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			

		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}


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
		    boolean result = checkPin(pin);
		    
		    if(result == true){
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
                    boolean result = checkPin(pin);

                    if(result == true){
                        String sql2 = "Insert into Transaction2 " +
                            "Values ( '" + accountID + "', 'null', 'withdrawal','" + rs.getString("taxid") + "', '" + amount + "', 'date')";

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
         return "0 " + balance+ " " + newAmount;



		


	}


    public boolean checkPin(String pin){
	Scanner in = new Scanner(System.in);
	System.out.println("Enter PIN\n");
	String input = in.nextLine();
			String enteredPin ="";
                        try{
                                enteredPin = encrypt(input, key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
	if(enteredPin.equals(pin)){
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
	String sql1 = "Select A.balance from Account2 A where A.aid = '" +accountId + "'"  + " Union Select B.balance from Pocket2 B where B.aid = '" + accountId + "'";
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
	return String.format("0 %.2f", balance);
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
	    if(checkPin(pin)){
		String sql5 = "SELECT a.balance FROM pocket2 a WHERE a.aid='"+from+"'";
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
	}else{
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
		String pin ="";
                        try{
                                pin = encrypt("1717", key);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }

		String sql1 = "select R.pin from OwnRelationship R where R.aid='"+linkedId+"' and R.taxid='"+tin+"'";		
		String sql2= "INSERT INTO Pocket2 " +
		"VALUES ('"+tin+"', 'OPEN', "+ 0.0+", '"+linkedId+"', '"+id+"')";
		String sql3 = "INSERT INTO PocketOwn " +
                   "VALUES ('"+tin+"', '"+id+"', '"+pin +"')";
		
		
		if (checkBalance(linkedId, initialTopUp)==false)
			return "0 Pocket Account not created. Insufficient Funds\n";

		try{stmt = _connection.createStatement();	
			ResultSet rs = stmt.executeQuery(sql1);
			if (rs.next()){
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
			
			if (checkPin(pin)){
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			ResultSet rs = stmt.executeQuery(sql4);
			rs.next();
			System.out.println(String.format("0 %.2f %.2f", rs.getFloat(1), rs.getFloat(2)));
							
			return String.format("0 %.2f %.2f", rs.getFloat(1), rs.getFloat(2));
			}else
				return "1 Incorrect Pin\n";
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

                        if (checkPin(pin)){
                        ResultSet rs = stmt.executeQuery(sql4);
                        rs.next();
                        balance = rs.getFloat(1);
			stmt.executeUpdate(sql2);
                        stmt.executeUpdate(sql3);
                        ResultSet rs2 = stmt.executeQuery(sql4);
                        rs2.next();
                        newAmount = rs2.getFloat(1);
                        return String.format("0 %.2f %.2f", balance, newAmount);
                        }else
                                return "1 Incorrect Pin\n";
                }catch( SQLException e )
                {
                        System.err.println( e.getMessage() );
                        return "1";
                }







	}


	/**
	 * Example of one of the testable functions.
	 */
	//@Override
	public String listClosedAccounts()
	{
		return "0 it works!";
	}

	/**
	 * Another example.
	 */
	
}
