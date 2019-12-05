package cs174a;                         // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// DO NOT REMOVE THIS IMPORT.
import cs174a.Testable.*;

/**
 * This is the class that launches your application.
 * DO NOT CHANGE ITS NAME.
 * DO NOT MOVE TO ANY OTHER (SUB)PACKAGE.
 * There's only one "main" method, it should be defined within this Main class, and its signature should not be changed.
 */
public class Main
{
	/**
	 * Program entry point.
	 * DO NOT CHANGE ITS NAME.
	 * DON'T CHANGE THE //!### TAGS EITHER.  If you delete them your program won't run our tests.
	 * No other function should be enclosed by the //!### tags.
	 */
	//!### COMENZAMOS
	public static void main( String[] args )
	{
		App app = new App();                        // We need the default constructor of your App implementation.  Make sure such
													// constructor exists.
		String r = app.initializeSystem();          // We'll always call this function before testing your system.
		if( r.equals( "0" ) )
		{
			app.exampleAccessToDB();                // Example on how to connect to the DB.

			// Example tests.  We'll overwrite your Main.main() function with our final tests.
			

			// Another example test.
			
			r=app.createTables();
			System.out.println( r );
			r=app.createCustomer("1234", "1234", "Bob1", "P. Sherman Wollaby, Sydney");
			System.out.println( r );
			r=app.createCustomer("account1","theTaxID", "Bob1", "P. Sherman Wollaby, Sydney");
			System.out.println( r );
			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "account1", 1234.56, "theTaxID", "Im YoungMing", "Known" );
			
						

			System.out.println( r );
			r=app.createCheckingSavingsAccount(AccountType.SAVINGS, "1234", 1000.0, "1234", "Bob1", "P. Sherman Wollaby, Sydney");
			System.out.println( r );

			/*r=app.createCheckingSavingsAccount(AccountType.SAVINGS, "1235", 1001.99, "1234", "Bob1", "P. Sherman Wollaby, Sydney");
			System.out.println( r );

			r=app.transfer("1235", "account1", 25.0);
			System.out.println( r );

			r=app.createPocketAccount("1111", "1234", 500.0, "1234");
			r=app.createPocketAccount("theTaxID", "account1", 500.0, "theTaxID");
			System.out.println( r );			

		        r = app.deposit("1234", 500);
			System.out.println(r);
		
			r = app.setPin("1234");
                        System.out.println(r);
*/

			r = app.setDate(2011, 3, 21);
                        System.out.println(r);			

			r = app.withdrawal("1234", 1000);
                        System.out.println(r);

			r = app.setDate(2011, 3, 31);
                        System.out.println(r);

			//r = app.accureInterest("1234");
                        //System.out.println(r);
/*
			r = app.showBalance("1234");
                        System.out.println(r);

			r = app.payFriend("theTaxID", "1111", 250);
                        System.out.println(r);

			r = app.setPocketPin("1111");
                        System.out.println(r);


			r = app.purchase("1111", 50);
                        System.out.println(r);
*/

			r = app.listClosedAccounts();
			System.out.println( r );

			r=app.dropTables();
			System.out.println( r );
		}
	}
	//!### FINALIZAMOS
}
