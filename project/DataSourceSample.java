/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/
/*
   DESCRIPTION
   The code sample shows how to use the DataSource API to establish a connection
   to the Database. You can specify properties with "setConnectionProperties".
   This is the recommended way to create connections to the Database.

   Note that an instance of oracle.jdbc.pool.OracleDataSource doesn't provide
   any connection pooling. It's just a connection factory. A connection pool,
   such as Universal Connection Pool (UCP), can be configured to use an
   instance of oracle.jdbc.pool.OracleDataSource to create connections and
   then cache them.

    Step 1: Enter the Database details in this file.
            DB_USER, DB_PASSWORD and DB_URL are required
    Step 2: Run the sample with "ant DataSourceSample"

   NOTES
    Use JDK 1.7 and above

   MODIFIED    (MM/DD/YY)
    nbsundar    02/17/15 - Creation
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.Timestamp;
import java.util.Date;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
 
public class DataSourceSample {
  // The recommended format of a connection URL is the long format with the
  // connection descriptor.
  final static String DB_URL= "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/ORCL";
  // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
  // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
  // In case of windows, use the following URL
  // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:\\Users\\test\\wallet_dbname";
  final static String DB_USER = "c##zholoien";
  final static String DB_PASSWORD = "9463274";

 /*
  * The method gets a database connection using
  * oracle.jdbc.pool.OracleDataSource. It also sets some connection
  * level properties, such as,
  * OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH,
  * OracleConnection.CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES, etc.,
  * There are many other connection related properties. Refer to
  * the OracleConnection interface to find more.
  */
  public static void main(String args[]) throws SQLException {
    Properties info = new Properties();
    info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
    info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
    info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");


    OracleDataSource ods = new OracleDataSource();
    ods.setURL(DB_URL);
    ods.setConnectionProperties(info);

    // With AutoCloseable, the connection is closed automatically.
    try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
      // Get the JDBC driver name and version
      DatabaseMetaData dbmd = connection.getMetaData();
      System.out.println("Driver Name: " + dbmd.getDriverName());
      System.out.println("Driver Version: " + dbmd.getDriverVersion());
      // Print some connection properties
      System.out.println("Default Row Prefetch Value is: " +
         connection.getDefaultRowPrefetch());
      System.out.println("Database Username is: " + connection.getUserName());
      System.out.println();
      // Perform a database operation
	addOwner(connection, 0000, "Bob1");
       addAccount(connection, 0001, 0000 , 1999.0,"Savings", 0.032, "branch1");
      //deleteOwner(connection, 0000);
    }
  }
 /*
  * Displays first_name and last_name from the employees table.
  */


public static void addAccount(Connection connection, int acc_id_, int p_owner_, double balance_, String type_, double irrate_, String bname_) throws SQLException{
    if (type_.compareTo("Savings")==0 || type_.compareTo("Checking")==0){
      if (balance_<1000.0){
        System.out.println("Intial Balance is below minimum"); return;
      }
    }

      try (Statement statement = connection.createStatement()) {

            statement.executeUpdate("INSERT INTO Account " +
                       "VALUES ("+acc_id_+", '"+type_+"', '"+ bname_+"', 0.01, "+ irrate_+ ", "+ p_owner_+", 'open')"); {
                      System.out.println("New Account added: "+ acc_id_);

          }
    }



    //TODO: Deposit balance_ into account balance
	deposit(connection, p_owner_, acc_id_, (balance_-.01));
}
private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH:MM:SS[.ssssss]");	


public static void addPocketAccount(Connection connection, int acc_id_, int p_accid_, double balance_, int owner_) throws SQLException{
    
	try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement
          .executeQuery("select account from coowner where account ="+ p_accid_+" and c_owner="+owner_+")") ){
        	try (Statement statement2 = connection.createStatement()) {
      try (ResultSet resultSet2 = statement2
          .executeQuery("select p_owner, balance from account where acc_id = "+p_accid_ +")") ){
       if (!resultSet.next() && resultSet2.getInt(1)!=owner_){
	System.out.println("Owner does not own parent account"); return;
}
	//else if (check balance>0)

      }
    }
 
}
 }     

//TODO: Create Pocket Account

//TODO: TOP Up balance








}


public static void deposit(Connection connection, int owner_, int account_, double amount_) throws SQLException {
	
	Date date = new Date();	
	Timestamp ts = new Timestamp(date.getTime());	
	try (Statement statement = connection.createStatement()) {

            statement.executeUpdate("INSERT INTO Transaction " +
                       "VALUES ("+account_+", NULL, 'Deposit', "+owner_+", "+ amount_+", '"+ts+ "')"); {
                      	System.out.println("Depositted: "+ amount_);
			
			try (Statement statement2 = connection.createStatement()) {

            			statement2.executeUpdate("UPDATE Account SET balance = balance +"+amount_+ "WHERE acc_id = "+ account_);
		}

          }
    }
	


}


public static void addOwner(Connection connection, int taxid_, String name_) throws SQLException {

  try (Statement statement3 = connection.createStatement()) {
    try (ResultSet resultSet3 = statement3
        .executeQuery("select taxid, name from owner where taxid=" + taxid_)) {
      if (resultSet3.next()){
        System.out.println("Tax ID: "+ taxid_ + " is already in use."); return;}
          }
        }

  try (Statement statement = connection.createStatement()) {

        statement.executeUpdate("INSERT INTO Owner  " +
                   "VALUES ("+taxid_+", '"+name_+"', 1717, 0)"); {
                  System.out.println("New Owner added: "+ taxid_ + ", "+ name_);

      }
}
}

public static void addCoowner(Connection connection, int taxid_, int accountid) throws SQLException {
	
	try (Statement statement = connection.createStatement()) {

        statement.executeUpdate("INSERT INTO coowner  " +
                   "VALUES ("+taxid_+", '"+accountid+")"); {
                  

      	}
	}

}

public static void deleteOwner(Connection connection, int taxid_) throws SQLException {

  try (Statement statement3 = connection.createStatement()) {
    try (ResultSet resultSet3 = statement3
        .executeQuery("select taxid, name from owner where taxid=" + taxid_)) {
      if (!resultSet3.next()){
        System.out.println("Tax ID: "+ taxid_ + " does not exist in records."); return;}
          }
        }

  try (Statement statement = connection.createStatement()) {

        statement.executeUpdate("DELETE FROM Owner  " +
                   "WHERE taxid = "+ taxid_); {
                  System.out.println("Owner deleted: "+ taxid_ );

      }
}
}



public static boolean checkTransaction(Connection connection, int account, float amount) throws SQLException{
	try (Statement statement = connection.createStatement()) {
          try (ResultSet resultSet = statement
              .executeQuery("select balance from Account where acc_id = "+account)) {
            if (resultSet.next())
			if (resultSet.getFloat(1)-amount<0) return false;
			else return true;              
		  }
              }

	return false;
}







//(taxid, name, pin, num_account)
      public static void printOwners(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
          try (ResultSet resultSet = statement
              .executeQuery("select taxid, name from owner")) {
            while (resultSet.next())
              System.out.println(resultSet.getString(1) + " "
                  + resultSet.getString(2) + " ");
                }
              }
            }

  public static void printEmployees(Connection connection) throws SQLException {
    // Statement and ResultSet are AutoCloseable and closed automatically.
    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement
          .executeQuery("select owner,table_name from all_tables")) {
        while (resultSet.next())
          System.out.println(resultSet.getString(1) + " "
              + resultSet.getString(2) + " ");
      }
    }
  }
}
