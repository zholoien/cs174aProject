package cs174a;                         // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// DO NOT REMOVE THIS IMPORT.
import cs174a.Testable.*;
import cs174a.Interface.*;

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
			
			r=app.createCustomer("17431","344151573", "Joe Pepsi", "3210 State St");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.STUDENT_CHECKING, "17431", 1200, "344151573", "Joe Pepsi", "3210 State St" );
			System.out.println( r );

			r=app.createCustomer("17431","412231856", "Cindy Laugher", "7000 Hollister");
			System.out.println( r );
			
			r=app.createCustomer("17431","322175130", "Ivan Lendme", "1235 Johnson Dr");
			System.out.println( r );

			r=app.createCustomer("54321","212431965", "Hurryson Ford", "678 State St");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.STUDENT_CHECKING, "54321", 21000, "212431965", "Hurryson Ford", "678 State St" );
			System.out.println( r );
		
			r=app.addCoowner("54321","412231856");
			System.out.println( r );
			
			r=app.createCustomer("54321","122219876", "Elizabeth Sailor", "4321 State St");
			System.out.println( r );

			r=app.createCustomer("54321","203491209", "Nam-Hoi Chung", "1997 Peoples St HK");
			System.out.println( r );

			r=app.createCustomer("12121","207843218", "David Copperfill", "1357 State St");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.STUDENT_CHECKING, "12121", 1200, "207843218", "David Copperfill", "1357 State St" );
			System.out.println( r );

			r=app.createCustomer("41725","201674933", "George Brush", "5346 Foothill Av");
			System.out.println( r );

			
			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "41725", 15000, "201674933", "George Brush", "5346 Foothill Av");
			System.out.println( r );

			
			r=app.createCustomer("41725","401605312", "Fatal Castro", "3756 La Cumbre Plaza");
			System.out.println( r );

			r=app.createCustomer("41725","231403227", "Billy Clinton", "5777 Hollister");
			System.out.println( r );

			r=app.createCustomer("76543","212116070", "Li Kung", "2 Peoples Rd Beijing");
			System.out.println( r );


			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "76543", 8456, "212116070", "Li Kung", "2 Peoples Rd Beijing");
			System.out.println( r );

			r=app.createCustomer("76543","188212217", "Magic Jordon", "3852 Court Rd");
			System.out.println( r );

			r=app.createCustomer("93156","209378521", "Kelvin Costner", "Santa Cruz #3579");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "93156", 2000000, "209378521", "Kelvin Costner", "Santa Cruz #3579");
			System.out.println( r );
			
			r=app.addCoowner("93156","188212217");
			System.out.println( r );
			
			r=app.createCustomer("93156","210389768", "Olive Stoner", "6689 El Colegio #151");
			System.out.println( r );

			r=app.addCoowner("93156","122219876");
			System.out.println( r );

			r=app.addCoowner("93156","203491209");
			System.out.println( r );

			r=app.createCustomer("43942","361721022", "Alfred Hitchcock", "6667 El Colegio #40");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.SAVINGS, "43942", 1289, "361721022", "Alfred Hitchcock", "6667 El Colegio #40");
			System.out.println( r );

			r=app.createCustomer("43942","400651982", "Pit Wilson", "911 State St");
			System.out.println( r );

			r=app.addCoowner("43942","212431965");
			System.out.println( r );

			r=app.addCoowner("43942","322175130");
			System.out.println( r );

			r = app.createCheckingSavingsAccount( AccountType.SAVINGS, "29107", 34000, "209378521", "Kelvin Costner", "Santa Cruz #3579");
			System.out.println( r );

			r=app.addCoowner("29107","212116070");
			System.out.println( r );

			r=app.addCoowner("29107","210389768");
			System.out.println( r );
	

			r = app.createCheckingSavingsAccount( AccountType.SAVINGS, "19023", 1000, "412231856", "Cindy Laugher", "7000 Hollister");
			System.out.println( r );

			r=app.addCoowner("19023","201674933");
			System.out.println( r );

			r=app.addCoowner("19023","401605312");
			System.out.println( r );
			
			r = app.createCheckingSavingsAccount( AccountType.SAVINGS, "32156", 34000, "188212217", "Magic Jordon", "3852 Court Rd");
			System.out.println( r );

			r=app.addCoowner("32156","207843218");
			System.out.println( r );

			r=app.addCoowner("32156","122219876");
			System.out.println( r );

			r=app.addCoowner("32156","344151573");
			System.out.println( r );

			r=app.addCoowner("32156","203491209");
			System.out.println( r );
			
			r=app.addCoowner("32156","210389768");
			System.out.println( r );
			
			r=app.createPocketAccount("53027", "12121", 50, "207843218");
			System.out.println( r );
			
			r=app.createPocketAccount("43947", "29107", 30, "212116070");
			System.out.println( r );

			r=app.createPocketAccount("60413", "43942", 20, "400651982");
			System.out.println( r );

			r=app.createPocketAccount("67521", "19023", 20, "401605312");
			System.out.println( r );

///////////////////////////////////Setting Pins

			r=app.setPin("361721022","1234");
			System.out.println( r );

			r=app.setPin("231403227","1468");
			System.out.println( r );

			r=app.setPin("412231856","3764");
			System.out.println( r );

			r=app.setPin("207843218","8582");
			System.out.println( r );

			r=app.setPin("122219876","3856");
			System.out.println( r );

			r=app.setPin("401605312","8193");
			System.out.println( r );

			r=app.setPin("201674933","9824");
			System.out.println( r );

			r=app.setPin("212431965","3532");
			System.out.println( r );

			r=app.setPin("322175130","8471");
			System.out.println( r );

			r=app.setPin("344151573","3692");
			System.out.println( r );

			r=app.setPin("209378521","4659");
			System.out.println( r );

			r=app.setPin("212116070","9173");
			System.out.println( r );

			//r=app.setPin("188212217","7351");
			//System.out.println( r );

			r=app.setPin("203491209","5340");
			System.out.println( r );

			r=app.setPin("400651982","1821");
			System.out.println( r );



////////////////////////////////// Transactions

			r = app.setDate(2011, 3, 2);
                        System.out.println(r);

			r = app.deposit("17431", 8800);
			System.out.println(r);

			r = app.setDate(2011, 3, 3);
                        System.out.println(r);

			r = app.withdrawal("54321", 3000);
                        System.out.println(r);

			r = app.setDate(2011, 3, 5);
                        System.out.println(r);

			r = app.withdrawal("76543", 2000);
                        System.out.println(r);

			r = app.purchase("53027", 5);
                        System.out.println(r);

			r = app.setDate(2011, 3, 6);
                        System.out.println(r);

			r = app.withdrawal("93156", 1000000);
                        System.out.println(r);

			r = app.writeCheck("93156", 950000);
                        System.out.println(r);

			r = app.withdrawal("29107", 4000);
                        System.out.println(r);

			r = app.collect("43947", 10);
                        System.out.println(r);

			r = app.topUp("43947", 30);
                        System.out.println(r);
			
			r = app.setDate(2011, 3, 7);
                        System.out.println(r);

			r = app.transfer("43942", "17431", 289);
                        System.out.println(r);

			r = app.withdrawal("43942", 289);
                        System.out.println(r);

			r = app.setDate(2011, 3, 8);
                        System.out.println(r);

			r = app.payFriend("60413", "67521", 10);
                        System.out.println(r);

			r = app.deposit("93156", 50000);
			System.out.println(r);

			r = app.writeCheck("12121", 200);
                        System.out.println(r);

			r = app.transfer("41725", "19023", 1000);
                        System.out.println(r);

			r = app.setDate(2011, 3, 9);
                        System.out.println(r);

			r = app.wire("41725", "32156", 4000, "401605312");
                        System.out.println(r);

			r = app.payFriend("53027", "60413", 10);
                        System.out.println(r);

			r = app.setDate(2011, 3, 10);
                        System.out.println(r);
	
			r = app.purchase("60413", 15);
                        System.out.println(r);

			r = app.setDate(2011, 3, 12);
                        System.out.println(r);
	
			r = app.withdrawal("93156", 20000);
                        System.out.println(r);


			r = app.writeCheck("76543", 456);
                        System.out.println(r);

			r = app.topUp("67521", 50);
                        System.out.println(r);

			r = app.setDate(2011, 3, 14);
                        System.out.println(r);

			r = app.payFriend("67521", "53027", 20);
                        System.out.println(r);

			r = app.collect("43947", 15);
                        System.out.println(r);

			Interface init = new Interface(app);

			/*r=app.createCustomer("1234", "1234", "Bob1", "P. Sherman Wollaby, Sydney");
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
		    //r=app.createPocketAccount("theTaxID", "account1", 500.0, "theTaxID");
			System.out.println( r );			
/*
		        r = app.deposit("1234", 500);
			System.out.println(r);
		
			r = app.setPin("1234");
                        System.out.println(r);

		    
			r = app.setDate(2011, 3, 21);
                        System.out.println(r);			

			r = app.withdrawal("1234", 1000);
                        System.out.println(r);

			r = app.setDate(2011, 3, 31);
                        System.out.println(r);

			//r = app.accureInterest("1234");
                        //System.out.println(r);

			r = app.showBalance("1234");
                        System.out.println(r);

			r = app.payFriend("theTaxID", "1111", 250);
                        System.out.println(r);

			//r = app.resetInitialBalance();
                        //System.out.println(r);

			//r = app.setPocketPin("1111");
                        //System.out.println(r);


			r = app.purchase("1111", 50);
                        System.out.println(r + "Hello");

			
			Interface init = new Interface(app);
			//app.generateMonthlyStatement("1234");
			app.dter();
			r = app.listClosedAccounts();
			System.out.println( r );*/

			r=app.dropTables();
			System.out.println( r );
		}
	}
	//!### FINALIZAMOS
}
