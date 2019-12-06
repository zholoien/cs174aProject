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
	    if(input.equals("4")){
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
		app.setDate(Integer.parseInt(newDate[0]), Integer.parseInt(newDate[1]), Integer.parseInt(newDate[2]));
		
	    }
	}
    }

    private void customerInterface(){
	System.out.println("Please Enter your Tax ID");
	Scanner in = new Scanner(System.in);
	String taxid = in.nextLine();
	if(app.checkPin(taxid)){
	    System.out.println("Welcome! Please enter the Account ID for the Account you wish to use:");
	    app.printAccounts(taxid);
	    String input = in.nextLine();
	    String type = app.getAccountType(input);
	    switch (type){
	    case "SAVINGS":
		savingsInterface(input, taxid);
		break;
	    case "POCKET":
		pocketInterface(input, taxid);
		break;
	    case "INTEREST_CHECKING":
		interestInterface(input, taxid);
		break;
	    }
	}
	else{
	    return;
	}
	
	
    }


	private void studentInterface(String accountId, String taxid){
	System.out.println("Welcome to your Interest Account. Please Enter a number for an Action");
	while(true){
		if (app.checkClosed(accountId)){
			System.out.println("Account is closed");
			break;
		}
	    System.out.println("1. Deposit");
	    System.out.println("2. Withdrawal");
	    System.out.println("3. Transfer");
	    System.out.println("4. Request Transfer");
	    System.out.println("5. Wire");
	    System.out.println("6. Write Check");
	    System.out.println("7. Show Balance");
	    System.out.println("8. Set Pin");
	    System.out.println("9. Exit");
	    Scanner scan = new Scanner(System.in);
	    String choice = scan.nextLine();
	    if (choice.equals("9")){
		break;
	    }
	    switch (choice) {
	    case "1":
		System.out.println("How much do you wish to deposit");
		String amount = scan.nextLine();
		app.deposit(accountId, Double.parseDouble(amount));
		break;
	    case "2":
		System.out.println("How much do you wish to Withdraw");
		String amo = scan.nextLine();
		app.withdrawal(accountId,  Double.parseDouble(amo));
		break;
	    case "3":
		System.out.println("How much do you wish to Transfer");
		String amoun = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		String to = scan.nextLine();
		app.transfer(accountId, to,  Double.parseDouble(amoun));
		break;
	    case "4":
		System.out.println("How much do you wish to Request");
		String amoun1 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to request from");
		String from = scan.nextLine();
		app.requestTransfer(from, accountId,  taxid, Double.parseDouble(amoun1));
		break;
	    case "5":
		System.out.println("How much do you wish to Wire");
		String amoun2 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		to = scan.nextLine();
		app.wire(accountId, to,  Double.parseDouble(amoun2), taxid);
		break;
	    case "6":
		System.out.println("How much do you wish to write this check for");
		String amo1 = scan.nextLine();
		app.writeCheck(accountId,  Double.parseDouble(amo1));
		break;
	    case "7": app.showBalance(accountId); break;
	    case "8": app.setPin(taxid); break;
		}
	    System.out.println("Enter another number for an action");
	}
    }

	private void interestInterface(String accountId, String taxid){
	System.out.println("Welcome to your Interest Account. Please Enter a number for an Action");
	while(true){
		if (app.checkClosed(accountId)){
			System.out.println("Account is closed");
			break;
		}
	    System.out.println("1. Deposit");
	    System.out.println("2. Withdrawal");
	    System.out.println("3. Transfer");
	    System.out.println("4. Request Transfer");
	    System.out.println("5. Wire");
	    System.out.println("6. Accrue-Interest");
	    System.out.println("7. Write Check");
	    System.out.println("8. Show Balance");
	    System.out.println("9. Set Pin");
	    System.out.println("10. Exit");
	    Scanner scan = new Scanner(System.in);
	    String choice = scan.nextLine();
	    if (choice.equals("10")){
		break;
	    }
	    switch (choice) {
	    case "1":
		System.out.println("How much do you wish to deposit");
		String amount = scan.nextLine();
		app.deposit(accountId, Double.parseDouble(amount));
		break;
	    case "2":
		System.out.println("How much do you wish to Withdraw");
		String amo = scan.nextLine();
		app.withdrawal(accountId,  Double.parseDouble(amo));
		break;
	    case "3":
		System.out.println("How much do you wish to Transfer");
		String amoun = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		String to = scan.nextLine();
		app.transfer(accountId, to,  Double.parseDouble(amoun));
		break;
	    case "4":
		System.out.println("How much do you wish to Request");
		String amoun1 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to request from");
		String from = scan.nextLine();
		app.requestTransfer(from, accountId,  taxid, Double.parseDouble(amoun1));
		break;
	    case "5":
		System.out.println("How much do you wish to Wire");
		String amoun2 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		to = scan.nextLine();
		app.wire(accountId, to,  Double.parseDouble(amoun2), taxid);
		break;
	    case "6": app.accureInterest(accountId, taxid); break;
	    case "7":
		System.out.println("How much do you wish to write this check for");
		String amo1 = scan.nextLine();
		app.writeCheck(accountId,  Double.parseDouble(amo1));
		break;
	    case "8": app.showBalance(accountId); break;
	    case "9": app.setPin(taxid); break;
		}
	    System.out.println("Enter another number for an action");
	}
    }

    private void savingsInterface(String accountId, String taxid){
	System.out.println("Welcome to your Savings Account. Please Enter a number for an Action");
	while(true){
		if (app.checkClosed(accountId)){
			System.out.println("Account is closed");
			break;
		}
	    System.out.println("1. Deposit");
	    System.out.println("2. Withdrawal");
	    System.out.println("3. Transfer");
	    System.out.println("4. Request Transfer");
	    System.out.println("5. Wire");
	    System.out.println("6. Accrue-Interest");
	    System.out.println("7. Show Balance");
	    System.out.println("8. Set Pin");
	    System.out.println("9. Exit");
	    Scanner scan = new Scanner(System.in);
	    String choice = scan.nextLine();
	    if (choice.equals("9")){
		break;
	    }
	    switch (choice) {
	    case "1":
		System.out.println("How much do you wish to deposit");
		String amount = scan.nextLine();
		app.deposit(accountId, Double.parseDouble(amount));
		break;
	    case "2":
		System.out.println("How much do you wish to Withdraw");
		String amo = scan.nextLine();
		app.withdrawal(accountId,  Double.parseDouble(amo));
		break;
	    case "3":
		System.out.println("How much do you wish to Transfer");
		String amoun = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		String to = scan.nextLine();
		app.transfer(accountId, to,  Double.parseDouble(amoun));
		break;
	    case "4":
		System.out.println("How much do you wish to Request");
		String amoun1 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to request from");
		String from = scan.nextLine();
		app.requestTransfer(from, accountId,  taxid, Double.parseDouble(amoun1));
		break;
	    case "5":
		System.out.println("How much do you wish to Wire");
		String amoun2 = scan.nextLine();
		System.out.println("Please Enter the Account Id you wish to transfer to");
		to = scan.nextLine();
		app.wire(accountId, to,  Double.parseDouble(amoun2), taxid);
		break;
	    case "6": app.accureInterest(accountId, taxid); break;
	    case "7": app.showBalance(accountId); break;
	    case "8": app.setPin(taxid); break;
		}
	    System.out.println("Enter another number for an action");
	}
    }

    private void pocketInterface(String accountId, String taxid){
	System.out.println("Welcome to your Pocket Account. Please Enter a number for an Action");
	while(true){
	System.out.println("1. Top-up");
	System.out.println("2. Purchase");
	System.out.println("3. Collect");
	System.out.println("4. Pay-Friend");
	System.out.println("5. Exit");
	Scanner scan = new Scanner(System.in);
	String choice = scan.nextLine();
	 if (choice.equals("5")){
		break;
	    }
	switch (choice) {
	case "1":
	    System.out.println("How much do you wish to Top-up");
	    String amount = scan.nextLine();
	    app.topUp(accountId, Double.parseDouble(amount));
	    break;
	case "2":
	    System.out.println("How much do you wish to Purchase");
	    String amo = scan.nextLine();
	    app.purchase(accountId,  Double.parseDouble(amo));
	    break;
	case "4":
	     System.out.println("How much do you wish to Pay your Friend");
	     String amoun = scan.nextLine();
	     System.out.println("Please Enter the Friends Account Id");
	     String to = scan.nextLine();
	     app.transfer(accountId, to,  Double.parseDouble(amoun));
	     break;
	}
	System.out.println("Enter another number for an action");
	}


	
    }
	
    private void bankTellerInterface(){
	System.out.println("Welcome to Bank Teller. Please Enter a number for an Action");
	while(true){
	    System.out.println("1. Enter Check Transaction");
	    System.out.println("2. Generate Monthly Statement");
	    System.out.println("3. List Closed Accounts");
	    System.out.println("4. Generate Government Drug and Tax Evasion Report (DTER)");
	    System.out.println("5. Reset Initial Monthly Balances");
	    System.out.println("6. Add Interest");
	    System.out.println("7. Create Account");
	    System.out.println("8. Delete Closed Accounts and Customers");
	    System.out.println("9. Delete Transactions");
	    System.out.println("10. Exit");
	    Scanner scan = new Scanner(System.in);
	    String choice = scan.nextLine();
	    if (choice.equals("10")){
		break;
	    }
	    switch (choice) {
	    case "2":
		System.out.println("Enter the TaxId of the monthly statement to generate");
	     	String taxid = scan.nextLine();
		app.generateMonthlyStatement(taxid);
		break;
	    case "3":
		System.out.println("Closed Accounts:");
		app.listClosedAccounts();
		break;
	    case "4":
		//System.out.println("Adding Interest to all Accounts:");
		app.dter();
		break;
	    case "5":
		//System.out.println("Adding Interest to all Accounts:");
		app.resetInitialBalance();
		break;
	    case "6":
		System.out.println("Adding Interest to all Accounts:");
		app.addInterest();
		break;
		 
	    }
	 	System.out.println("Enter another number for an action");
	}
    }



    
}
