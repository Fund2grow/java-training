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
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static String USER_INSERT_QUERY = "insert into APP.USER_REG values (?,?)";
    private static String USER_UPDATE_QUERY = "update APP.USER_REG set PASSWORD=? where USERID=?";
    private static String USER_DELETE_QUERY = "delete from APP.USER_REG where USERID=?";
    private static String USER_SELECT_QUERY = "select USERID, PASSWORD from APP.USER_REG";
    
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
            
        System.out.println("------- Welcome to Fun2groW website -------");
        System.out.println("1. Login user");
        System.out.println("2. Create user");
        System.out.println("3. Update user");
        System.out.println("4. Delete user");
        System.out.println("Enter your choice:");
        String choice = sc.nextLine();
        createConnection();
        
        if (choice.equalsIgnoreCase("1"))
            loginUsers();
        else if(choice.equalsIgnoreCase("2"))
            insertUsers();
        else if(choice.equalsIgnoreCase("3"))
            updateUsers();
        else
            deleteUsers();
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
    
    private static void insertUsers()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            
            System.out.println("Enter your username to store: ");
            String userId = sc.nextLine();
        
            System.out.println("Enter your password to store: ");
            String userPwd = sc.nextLine();
            
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
    
    private static void updateUsers()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            
            System.out.println("Enter your username to update: ");
            String userId = sc.nextLine();
        
            System.out.println("Enter your password to update: ");
            String userPwd = sc.nextLine();
            
            pstmt = conn.prepareStatement(USER_UPDATE_QUERY);
            pstmt.setString(1, userPwd);
            pstmt.setString(2, userId);
            int noOfRecords = pstmt.executeUpdate();
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void deleteUsers()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            
            System.out.println("Enter your username to delete: ");
            String userId = sc.nextLine();
            
            pstmt = conn.prepareStatement(USER_DELETE_QUERY);
            pstmt.setString(1, userId);
            int noOfRecords = pstmt.executeUpdate();
            pstmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void loginUsers()
    {
        try
        {
            Scanner sc= new Scanner(System.in);
            boolean login_success = false;
            
            System.out.println("Enter your username to update: ");
            String userId = sc.nextLine();
        
            System.out.println("Enter your password to update: ");
            String userPwd = sc.nextLine();
            
            pstmt = conn.prepareStatement(USER_SELECT_QUERY);
            rs = pstmt.executeQuery();

            while(rs.next())
            {
                if(userId.equalsIgnoreCase(rs.getString(1)) && userPwd.equalsIgnoreCase(rs.getString(2)))
                    login_success = true;
                else
                    continue;
            }
            if (login_success == true)
                System.out.println("User logged in successfully...");
            else
                System.out.println("UserId or Password is wrong...");
            
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
}
