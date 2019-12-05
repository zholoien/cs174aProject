package cs174a;                                
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

public class Interface
{
    private App app;
    Interface(App application)
    {
	app = application;
	initialize();
	
    }


    private void initialize(){
	Scanner scanner = new Scanner(System.in);
	while (true){
	    System.out.println("1. Customer");
	    System.out.println("2. Bank Teller");
	    System.out.println("3. Set Date");
	    System.out.println("4. Exit");
	    String input = scanner.nextLine();
	    if(input.compareTo("4")){
		break;
	    }
	    switch(input){
	    case "1":
		customerInterface();
		break;
	    case "2":
		bankTellerInterface();
		break;
	    case "3":
		System.out.println("Please enter a new date: Year Month Date");
		input = scanner.nextLine();
		String[] newDate = input.split(" ", -2);
		app.setDate(Integer.parseInt(newDate[0]), Integer.paresInt(newDate[1]), Integer.parseInt(newDate[2]));
		
	    }
	}
    }

    private void customerInterface(){




	
    }



    
}
