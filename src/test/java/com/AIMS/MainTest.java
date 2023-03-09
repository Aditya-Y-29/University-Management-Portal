package com.AIMS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Statement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Connection connection;
    @BeforeAll
    public static void initiateTesting(){
        System.out.println("Testing starts..............");
    }
    @BeforeEach
    public void beforeEachTesting() throws SQLException, IOException, ClassNotFoundException {
        DbConnect Db = new DbConnect();
        connection = Db.connect();
        try{
            connection.setAutoCommit(false);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    @AfterEach
    public void afterEachTesting(){
        try{
            connection.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void userInputManager(String input){
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    void academicOfficeCorrectTest() throws SQLException,IOException {
        String input = "acadoffice@iitrpr.ac.in\niitropar\n12";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true, Main.acadOfficePrompt(connection,sc));
    }
    @Test
    void academicOfficeIncorrectTest() throws SQLException,IOException{
        String input = "a\np\na\np\na\np\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false, Main.acadOfficePrompt(connection,sc));
    }
    @Test
    void facultyCorrectTest() throws SQLException,IOException{
        String input ="facCS2@iitrpr.ac.in\niitropar\n9";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true, Main.facultyPrompt(connection,sc));
    }
    @Test
    void facultyIncorrectTest() throws SQLException,IOException {
        String input = "a\np\na\np\na\np";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false, Main.facultyPrompt(connection,sc));
    }

    @Test
    void studentCorrectTest() throws SQLException,IOException {
        String input ="2020CS2@iitrpr.ac.in\niitropar\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true, Main.studentPrompt(connection,sc));
    }
    @Test
    void studentIncorrectTest() throws SQLException,IOException {
        String input = "a\np\na\np\na\np";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false, Main.studentPrompt(connection,sc));
    }
    @Test
    void initialPromptIncorrect1() throws SQLException,IOException {
        String input = "1\na\np\na\np\na\np";
        userInputManager(input);
        assertEquals(true, Main.initialPrompt(connection));
    }
    @Test
    void initialPromptIncorrect2() throws SQLException,IOException {
        String input = "2\na\np\na\np\na\np";
        userInputManager(input);
        assertEquals(true, Main.initialPrompt(connection));
    }
    @Test
    void initialPromptIncorrect3() throws SQLException,IOException {
        String input = "3\na\np\na\np\na\np";
        userInputManager(input);
        assertEquals(true, Main.initialPrompt(connection));
    }
    @Test
    void databaseInitCheck() throws SQLException,IOException{
        Statement statement = connection.createStatement();

        String sql = "DROP TABLE enrollment;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE faculty_offer;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE student;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE academic_office;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE student_login;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE faculty_login;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE faculty;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE academic_office_login;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE academic_curriculum;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE course_catalog;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE passing_criteria;";
        statement.executeUpdate(sql);
        DbConnect db=new DbConnect();
        assertEquals(true, Main.initialisingDB(this.connection,db));
    }
    @Test
    void initialPrompt4Test() throws SQLException,IOException{
        String input = "4";
        userInputManager(input);
        String[] args={"dummy"};
        assertEquals(true, Main.initialPrompt(connection));
    }
    @Test
    void initialPromptElseTest() throws SQLException, IOException {
        String input = "5";
        userInputManager(input);
        String[] args={"dummy"};
        assertEquals(true, Main.initialPrompt(connection));
    }
}
