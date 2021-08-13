package com.fund2grow.jdbcproject;

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
public class F2GUserMain {
    private static String dbURL = "jdbc:derby://192.168.1.51:1527/FUND2GROW;create=true;user=DEVUSER;password=Fund2groW$";
    private static String tableName = "APP.USER_REG";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement pstmt = null;
    private static String USER_INSERT_QUERY = "insert into APP.USER_REG values (?,?)";
    
    public static void main(String[] args) {
        
        String username = null;
        String password = null;
        Scanner sc= new Scanner(System.in);
        
        F2GUser f2guser = new F2GUser();
        
        System.out.println("Enter your username to store: ");
        username = sc.nextLine();
        
        System.out.println("Enter your password to store: ");
        password = sc.nextLine();
        
        f2guser.setUserid(username);
        f2guser.setPassword(password);
        
        createConnection();
        insertUsers(f2guser.getUserid(),f2guser.getPassword());
        //selectRestaurants();
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
    
    private static void insertUsers(String userId, String userPwd)
    {
        try
        {
            pstmt = conn.prepareStatement(USER_INSERT_QUERY);
            pstmt.setString(1, userId);
            pstmt.setString(2, userPwd);
            int noOfRecords = pstmt.executeUpdate();
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void selectRestaurants()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = pstmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                String userId = results.getString(1);
                String userPwd = results.getString(2);
                System.out.println(userId + "\t\t" + userPwd);
            }
            results.close();
            stmt.close();
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
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
}
