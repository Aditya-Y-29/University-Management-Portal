package com.AIMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
public class Main {
    private static int currYear=2023;
    private static int currSem=1;
    private static int currPhase=0;

    Connection connection;

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Establishing database connection.
        DbConnect Db=new DbConnect();
        Connection connection=Db.connect();

        // Initialising dummy database.
        initialisation(connection,Db);

        // Prompting user for input.
        initialPrompt(connection);
    }

    public static boolean initialisation(Connection connection,DbConnect Db) throws SQLException, IOException {
        // Initialising DB
        initialisingDB(connection,Db);
        return true;
    }
    public static boolean initialPrompt(Connection connection) throws SQLException,IOException{

        Scanner sc = new Scanner(System.in);

        System.out.println("----------------Welcome To The Portal--------------");
        System.out.println("Who are you? 1.Student  2.Faculty  3.AcademicOffice 4.Quit");

        int userRole = sc.nextInt();

        if(userRole == 1) {
            studentPrompt(connection,sc);
        }
        else if(userRole == 2) {
            facultyPrompt(connection,sc);
        }
        else if(userRole == 3) {
            acadOfficePrompt(connection,sc);
        }
        else if(userRole==4){

        } else {
            System.out.println("Invalid Choice");
        }
        return true;
    }
    public static boolean initialisingDB(Connection connection, DbConnect db) throws SQLException, IOException {
        CreateTable.createTables(connection,db);
        DatabaseInit.init(connection);
        return true;
    }
    public static boolean studentPrompt(Connection connection,Scanner sc) throws SQLException, IOException {
        System.out.println("--------Welcome Student-------");
        Boolean loginVerdict = false;

        int times = 3;
        while (times > 0) {
            Student student=new Student("1","CS","2020","2020CS1@iitrpr.ac.in");

            System.out.println("To login--> Enter your email\n");
            String email=sc.nextLine();
            System.out.println("Enter password\n");
            String password=sc.nextLine();

            loginVerdict = student.verify(connection,email,password);
            if (loginVerdict == true) {
                System.out.println("Login Successful-----");
                student.utility(connection);
                return true;
            }
            System.out.println("Wrong credentials");
            times--;
        }
        if(times==0){
            System.out.print("You entered wrong password for many times.....try again later!!");
        }
        return false;
    }
    public static boolean facultyPrompt(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("--------Welcome Faculty-------");
        Boolean loginVerdict = false;

        int times = 3;
        while (times > 0) {
            Faculty faculty=new Faculty("1","dummyD","dummyE");

            System.out.println("To login--> Enter your email\n");
            String email=sc.nextLine();
            System.out.println("Enter password\n");
            String password=sc.nextLine();

            loginVerdict = faculty.verify(connection,email,password);
            if (loginVerdict == true) {
                System.out.println("Login Successful-----");
                faculty.utility(connection);
                return true;
            }
            System.out.println("Wrong credentials");
            times--;
        }
        if(times==0){
            System.out.print("You entered wrong password for many times.....try again later!!");
        }
        return false;
    }
    public static boolean acadOfficePrompt(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("--------Welcome Academic Office-------");
        Boolean loginVerdict = false;

        int times = 3;
        while (times > 0) {
            AcademicOffice academicOffice=new AcademicOffice("dummyText");

            System.out.println("To login--> Enter your email\n");
            String email=sc.nextLine();
            System.out.println("Enter password\n");
            String password=sc.nextLine();

            loginVerdict = academicOffice.verify(connection,email,password);
            if (loginVerdict == true) {
                System.out.println("Login Successful-----");
                academicOffice.utility(connection);
                return true;
            }
            System.out.println("Wrong credentials");
            times--;

        }
        if(times==0){
            System.out.print("You entered wrong password for many times.....try again later!!");
        }
        return false;
    }
    public static int getCurrYear() {
        return currYear;
    }
    public static void setCurrYear(int year) {
        currYear = year;
    }
    public static int getCurrSem() {
        return currSem;
    }
    public static void setCurrSem(int sem) {
        currSem = sem;
    }
    public static int getCurrPhase() {
        return currPhase;
    }
    public static void setCurrPhase(int phase) {
        currPhase = phase;
    }
}