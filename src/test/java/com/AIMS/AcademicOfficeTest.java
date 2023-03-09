package com.AIMS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AcademicOfficeTest {
    private Connection connection;
    AcademicOfficeController academicOfficeController;
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
        academicOfficeController=new AcademicOfficeController();
    }
    @AfterEach
    public void afterEachTesting() throws SQLException,IOException{
        try{
            connection.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void userInputManager(String input) throws SQLException,IOException{
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    void verifyCorrectTest() throws SQLException,IOException {
        AcademicOffice academicOffice = new AcademicOffice("dummy");
        assertEquals(true, academicOffice.verify(connection,"acadoffice@iitrpr.ac.in","iitropar"));
    }
    @Test
    void verifyIncorrectTest()  throws SQLException,IOException {
        AcademicOffice academicOffice = new AcademicOffice("dummy");
        assertEquals(false, academicOffice.verify(connection,"acadoffice@iitrpr.ac.in","iit"));
    }
    @Test
    void getCourseCatalogTest() throws SQLException,IOException {
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
        AcademicOffice academicOffice = new AcademicOffice("dummy");
        String input = "3\n12";
        userInputManager(input);
        academicOffice.utility(connection);
        String filePath1="src/main/java/txtFiles/CourseCatalog.txt";
        String filePath2="src/test/java/testTxtFile/CourseCatalog.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
    }
    public String getPassword(String email) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT password " +
                    " From academic_office_login" +
                    " WHERE email='"+email+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                String password=rs.getString("password");
                return password;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return "dummy";
    }
    @Test
    void changePasswordParserTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "iitz\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.changePasswordParser(connection,sc);
        assertEquals("iitz",getPassword("acadoffice@iitrpr.ac.in").trim());
    }
    public String getPhoneNo(String email) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT phone_no " +
                    " From academic_office" +
                    " WHERE email='"+email+"'";
            System.out.println(query);
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                String phoneNo=rs.getString("phone_no");
                return phoneNo;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return "dummy";
    }
    @Test
    void changePhoneNoParserTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "123";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.changePhoneNoParser(connection,sc);
        assertEquals("123",getPhoneNo("acadoffice@iitrpr.ac.in").trim());
    }
    @Test
    void utility1Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "1\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
        Main.setCurrYear(2023);
        Main.setCurrPhase(1);
        Main.setCurrPhase(0);
    }
    @Test
    void utility2Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "2\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility3Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "3\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility4Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "4\na\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility5Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "5\na\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility6Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "6\nAddStudentIncorrect\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility7Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "7\nAddFacultyIncorrect\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility8Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "8\n3\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility9Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "9\n3\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility10Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "10\n123\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    @Test
    void utility11Test() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "11\n123\n12";
        userInputManager(input);
        assertEquals(true,academicOffice.utility(connection));
    }
    public boolean checkTwoFiles(String filePath1,String filePath2) throws SQLException{
        Path file1 = Paths.get(filePath1); // Replace with the path of the first file
        Path file2 = Paths.get(filePath2); // Replace with the path of the second file

        try {
            String content1 = Files.readString(file1);
            String content2 = Files.readString(file2);
            content1 = content1.replaceAll("\\s", "");
            content2 = content2.replaceAll("\\s", "");

            if (content1.equals(content2)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }
    @Test
    void getGradeParserTestByStudentId() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "1\n2\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.getGradesParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcadViewGradeStuId.txt";
        String filePath2="src/test/java/testTxtFile/AcadViewGradeStuId.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void getGradeParserTestInvalidInput() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "3\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false,academicOffice.getGradesParser(connection,sc));
    }
    @Test
    void getGradeParserTestByCourseId() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "2\nCS101\n2020\n2\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.getGradesParser(connection,sc);
        String filePath1="src/test/java/testTxtFile/AcadViewGradeCourseId.txt";
        String filePath2="src/test/java/testTxtFile/AcadViewGradeCourseId.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    public boolean checkFacultyExist(String email) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From faculty " +
                    " WHERE email='"+email+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    @Test
    void AddFaculty() throws SQLException, IOException {
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "AddFaculty\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.addFacultyParser(connection,2,sc);
        assertEquals(true,checkFacultyExist("facCS3@iitrpr.ac.in"));
    }
    @Test
    void AddFacultyIncorrect() throws SQLException, IOException {
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        String input = "AddFacultyIncorrect\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.addFacultyParser(connection,2,sc);
        assertEquals(false,checkFacultyExist("facCS3@iitrpr.ac.in"));
    }
    public boolean checkStudentExist(String email) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From student " +
                    " WHERE email='"+email+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    @Test
    void AddStudentTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "AddStudent\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.addStudentParser(connection,5,sc);
        assertEquals(true,checkStudentExist("2020CS6@iitrpr.ac.in"));
    }
    @Test
    void AddStudentIncorrectTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "AddStudentIncorrect\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false,academicOffice.addStudentParser(connection,5,sc));
    }
    @Test
    public boolean checkCourseInCourseCatalogExist(String courseId) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From course_catalog " +
                    " WHERE course_id='"+courseId+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    @Test
    void removeCourseInCourseCatalogTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "CS411\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.removeCourseInCourseCatalogParser(connection,sc);
        assertEquals(false,checkCourseInCourseCatalogExist("CS411"));
    }

    @Test
    void addCourseInCourseCatalogTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "CS99,Test Course,5,CS,None\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.addCourseInCourseCatalogParser(connection,sc);
        assertEquals(true,checkCourseInCourseCatalogExist("CS99"));
    }
    @Test
    void addCourseInCourseCatalogIncorrectTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        String input = "CS99,Test Course,5,CS\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.addCourseInCourseCatalogParser(connection,sc);
        assertEquals(false,checkCourseInCourseCatalogExist("CS99"));
    }
    @Test
    void incrementSemesterTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        Main.setCurrSem(2);
        int year=Main.getCurrYear();
        academicOffice.incrementSemester(connection);
        assertEquals(year+1,Main.getCurrYear());
    }
    @Test
    void getAcademicCurriculumTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        academicOffice.getAcademicCurriculumParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculum.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    public boolean checkCourseInAcademicCurriculumExist(String courseId) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From academic_curriculum " +
                    " WHERE course_id='"+courseId+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    @Test
    void addCourseInAcademicCurriculumTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        Main.setCurrPhase(1);
        String input = "PostAcademicCurriculum\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.academicCurriculumParser(connection,sc);
        assertEquals(true,checkCourseInAcademicCurriculumExist("CS411"));
    }
    @Test
    void addCourseInAcademicCurriculumIncorrectTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        String input = "PostAcademicCurriculumIncorrect\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.academicCurriculumParser(connection,sc);
        assertEquals(false,checkCourseInAcademicCurriculumExist("CS411"));
    }
    @Test
    void generateTranscriptTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        String input = "5\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        academicOffice.getTranscriptParser(connection,sc);
        String filePath1="src/main/java/txtFiles/GeneratedTranscript.txt";
        String filePath2="src/test/java/testTxtFile/GeneratedTranscript.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    String getPassingCriteria(String year) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From passing_criteria " +
                    " WHERE joining_year='"+year+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                String minimumCredits=rs.getString("minimum_credits");
                return minimumCredits;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return "-1";
    }
    @Test
    void passingCriteriaTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        String input = "2017\n0";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        Main.setCurrPhase(1);
        academicOffice.passingCriteriaParser(connection,sc);
        assertEquals("0",getPassingCriteria("2017"));
    }
    @Test
    void passingCriteriaNotExistTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        String input = "2016\n0";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        Main.setCurrPhase(1);
        assertEquals(1000,academicOfficeController.getPassingCriteria(connection,"2016"));
    }
    @Test
    void checkAllMandatoryCoursesBefore4Years() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        // User Input
        Scanner sc=new Scanner(System.in);
        assertEquals(false,academicOfficeController.checkAllMandatoryCourses(connection,"2",2020,"CS"));
    }
    @Test
    void getNonExistingStudentByEmailId() throws SQLException,IOException{
        Scanner sc=new Scanner(System.in);
        assertEquals("",academicOfficeController.getStudentNameByStudentId(connection,"10"));
    }
    @Test
    void getNonExistingStudentEmailByStudentId() throws SQLException,IOException{
        Scanner sc=new Scanner(System.in);
        assertEquals("",academicOfficeController.getStudentEmailByStudentId(connection,"10"));
    }
    @Test
    void moveToNextPhaseTest() throws SQLException,IOException{
        AcademicOffice academicOffice = new AcademicOffice("acadoffice@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        Main.setCurrPhase(0);
        academicOffice.moveToNextPhaseParser(connection,sc);
        assertEquals(1,Main.getCurrPhase());
        academicOffice.moveToNextPhaseParser(connection,sc);
        assertEquals(2,Main.getCurrPhase());
        academicOffice.moveToNextPhaseParser(connection,sc);
        assertEquals(3,Main.getCurrPhase());
        academicOffice.moveToNextPhaseParser(connection,sc);
        assertEquals(0,Main.getCurrPhase());
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }
}