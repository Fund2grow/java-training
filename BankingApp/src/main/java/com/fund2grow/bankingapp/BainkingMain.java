
package com.fund2grow.bankingapp;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class BankingMain {
    
    private static Connection conn = null;
    private static final Statement stmt = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static final String CREATE_ACCT_QUERY = "insert into BANK.YGR_BANK values (?,?,?,?,?,?,?,?)";
    private static final String GET_ACCT_DETAILS_QUERY = "select * from BANK.YGR_BANK where ACCT_NUMBER=?";
    private static final String UPDATE_BALANCE_QUERY = "update BANK.YGR_BANK set BALANCE=? where ACCT_NUMBER=?";
    private static final String USER_DELETE_QUERY = "delete from BANK.YGR_BANK where USERID=?";
    private static final String USER_SELECT_QUERY = "select USERNAME, PASSWORD from BANK.YGR_BANK";
    private static final String GET_NEXT_ACCT_QUERY = "select ACCT_NUMBER from BANK.YGR_BANK";
    private static final String CLOSE_ACCT_QUERY = "update BANK.YGR_BANK set ACCT_ACTIVE=false where ACCT_NUMBER=?";
    
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        createConnection();
        
        System.out.println("------- Welcome to YGR Bank website -------");
        System.out.println("To access YGR Bank's website you need to login first!");
        loginUser();
        
        System.out.println("------- Welcome to YGR Bank website -------");
        System.out.println("1. Create an Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Close Account");
        System.out.println("Enter your choice:");
        String choice = sc.nextLine();
        
        if (choice.equalsIgnoreCase("1"))
            createAccount();
        else if(choice.equalsIgnoreCase("2"))
            depositMoney();
        else if(choice.equalsIgnoreCase("3"))
            withdrawMoney();
        else if(choice.equalsIgnoreCase("4"))
            checkBalance();
        else if(choice.equalsIgnoreCase("5"))
            closeAccount();
        else 
            System.out.println("You entered a wrong choice! Quitting!");
        //shutdown();
    }
    
    private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection("jdbc:derby://192.168.1.51:1527/FUND2GROW","DEVUSER","Fund2groW$"); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    private static void createAccount()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            
            System.out.println("Enter your First Name: ");
            String firstName = sc.nextLine();
        
            System.out.println("Enter your Last Name: ");
            String lastName = sc.nextLine();
            
            System.out.println("Enter your userid for online access: ");
            String userId = sc.nextLine();
        
            System.out.println("Enter your password for online access: ");
            String userPwd = sc.nextLine();
            
            
            pstmt = conn.prepareStatement(CREATE_ACCT_QUERY);
            pstmt.setInt(1, getNextAccountNumber());
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setDouble(4, 0.0);
            pstmt.setBoolean(5, false);
            pstmt.setBoolean(6, true);
            pstmt.setString(7, userId);
            pstmt.setString(8, userPwd);
            
            if(pstmt.executeUpdate() == 1) 
                System.out.println("Account created successfully for " + firstName +"!");
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static int getNextAccountNumber()
    {
        int prevActNumber = 0;
        int nextActNumber = 0;
            
        try
        {    
            PreparedStatement pstmt1 = conn.prepareStatement(GET_NEXT_ACCT_QUERY);
            ResultSet rs1 = pstmt1.executeQuery();
            while(rs1.next()) {
                nextActNumber = rs1.getInt(1);
                nextActNumber = nextActNumber > prevActNumber ? nextActNumber : prevActNumber;
                prevActNumber = nextActNumber;
            }
            pstmt1.close();
            rs1.close();
        }
        catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        
        return nextActNumber+1;
    }
    
    private static void depositMoney()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            double currentBalance = 0;
            System.out.println("Enter your Account Number: ");
            String actNumber = sc.nextLine();
        
            System.out.println("Enter how much money you want to deposit: ");
            String depositAmount = sc.nextLine();
            
            pstmt = conn.prepareStatement(GET_ACCT_DETAILS_QUERY);
            pstmt.setInt(1, Integer.parseInt(actNumber));
            rs = pstmt.executeQuery();
            rs.next();
            currentBalance = rs.getDouble(4);
            rs.close();
            
            pstmt = conn.prepareStatement(UPDATE_BALANCE_QUERY);
            pstmt.setDouble(1, currentBalance + Double.parseDouble(depositAmount));
            pstmt.setInt(2, Integer.parseInt(actNumber));
            if(pstmt.executeUpdate() == 1) 
                System.out.println( "$" + depositAmount +" deposited successfully in " + actNumber + "!");
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void withdrawMoney()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            double currentBalance = 0;
            System.out.println("Enter your Account Number: ");
            String actNumber = sc.nextLine();
        
            System.out.println("Enter how much money you want to withdraw: ");
            String withdrawAmount = sc.nextLine();
            
            pstmt = conn.prepareStatement(GET_ACCT_DETAILS_QUERY);
            pstmt.setInt(1, Integer.parseInt(actNumber));
            rs = pstmt.executeQuery();
            rs.next();
            currentBalance = rs.getDouble(4);
            rs.close();
            
            pstmt = conn.prepareStatement(UPDATE_BALANCE_QUERY);
            pstmt.setDouble(1, currentBalance - Double.parseDouble(withdrawAmount));
            pstmt.setInt(2, Integer.parseInt(actNumber));
            if(pstmt.executeUpdate() == 1) 
                System.out.println( "$"+withdrawAmount+ " withdrawn successfully from " + actNumber);
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void checkBalance()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            double currentBalance = 0;
            System.out.println("Enter your Account Number to check balance: ");
            String actNumber = sc.nextLine();
            
            pstmt = conn.prepareStatement(GET_ACCT_DETAILS_QUERY);
            pstmt.setInt(1, Integer.parseInt(actNumber));
            rs = pstmt.executeQuery();
            rs.next();
            currentBalance = rs.getDouble(4);
            rs.close();
            System.out.println("Current available balance for A/C No " + actNumber + " is: " + currentBalance);
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void closeAccount()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            double currentBalance = 0;
            System.out.println("Enter your Account Number to close: ");
            String actNumber = sc.nextLine();
            
            pstmt = conn.prepareStatement(GET_ACCT_DETAILS_QUERY);
            pstmt.setInt(1, Integer.parseInt(actNumber));
            rs = pstmt.executeQuery();
            rs.next();
            currentBalance = rs.getDouble(4);
            rs.close();
            if(currentBalance > 0){
                System.out.println("You can't close the account. Please withdraw all the money first!");
                return;
            }
            
            pstmt = conn.prepareStatement(CLOSE_ACCT_QUERY);
            pstmt.setInt(1, Integer.parseInt(actNumber));
            
            if(pstmt.executeUpdate() == 1)
                System.out.println(actNumber + " account closed successfully!");
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void loginUser()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            boolean login_success = false;
            
            System.out.println("Enter your username : ");
            String userId = sc.nextLine();
        
            System.out.println("Enter your password : ");
            String userPwd = sc.nextLine();
            
            pstmt = conn.prepareStatement(USER_SELECT_QUERY);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                if(userId.equalsIgnoreCase(rs.getString(1)) && userPwd.equalsIgnoreCase(rs.getString(2))) {
                    login_success = true;
                }
            }
            if (login_success == true) {
                System.out.println("User logged in successfully...");
                return;
            }
            else {
                System.out.println("UserId or Password is wrong...");
                System.exit(0);
            }
            
            rs.close();
            //stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                //DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
    
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
}