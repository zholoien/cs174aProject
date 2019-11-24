package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;

/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.

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
		    " pin INTEGER not NULL," + 
		   " CONSTRAINT FK_Owner FOREIGN KEY (taxid) REFERENCES Owner2(taxid) on delete cascade," +
		  " CONSTRAINT FK_Account FOREIGN KEY (aid) REFERENCES Account2(aid) on delete cascade," +
                   " PRIMARY KEY ( taxid, aid ))";   
		try{stmt = _connection.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
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
		try{stmt = _connection.createStatement();
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
		
		String sql1 = "INSERT INTO Account2 " +
                   "VALUES ('"+tin+"', '"+name+"', '"+accountType+"', 'OPEN',"+ initialBalance+", "+rate+", '"+address+"', '"+id+"')";
		String sql2 = "INSERT INTO OwnRelationship " +
                   "VALUES ('"+tin+"', '"+id+"', '1717')";

		try{stmt = _connection.createStatement();			
			stmt.executeUpdate(sql1);
			//stmt.executeUpdate(sql2);
			

		}catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}


		return "0 " + id + " " + accountType + " " + initialBalance + " " + tin;

	}


	@Override
	public String createCustomer( String accountId, String tin, String name, String address ){	Statement stmt = null;
		String sql1 = "INSERT INTO Owner2 " +
                   "VALUES ('"+tin+"', '"+name+"', '"+address+"')";
		String sql2 = "select A.aid from account2 A where A.aid='"+accountId+"'";
		String sql3 = "INSERT INTO OwnRelationship " +
                   "VALUES ('"+tin+"', '"+accountId+"', '1717')";
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
