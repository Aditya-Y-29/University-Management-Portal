package com.AIMS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FacultyTest {
    private Connection connection;
    FacultyController facultyController;
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
        facultyController=new FacultyController();
    }
    @AfterEach
    public void afterEachTesting()  throws SQLException{
        try{
            connection.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void userInputManager(String input) throws SQLException{
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    void verifyCorrectTest()  throws SQLException{
        Faculty faculty=new Faculty("dummyI","dummyD","dummyE");
        assertEquals(true, faculty.verify(connection,"facCS1@iitrpr.ac.in","iitropar"));
    }
    @Test
    void verifyIncorrectTest()  throws SQLException{
        Faculty faculty=new Faculty("dummyI","dummyD","dummyE");
        assertEquals(false, faculty.verify(connection,"facCS1@iitrpr.ac.in","iit"));
    }

    @Test
    void utilityTest()  throws SQLException,IOException{
        Faculty faculty=new Faculty("dummyI","dummyD","dummyE");
        String input = "9\n";
        userInputManager(input);
        assertEquals(true, faculty.utility(connection));
    }

    public boolean courseExistInFacultyOffer(String courseId,String facultyId) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From faculty_offer " +
                    " WHERE faculty_id='"+facultyId+"' AND course_id='"+courseId+"' AND year='"+Main.getCurrYear()+"' AND semester_no='"+Main.getCurrSem()+"'";
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
    void registerCourseParser() throws SQLException{
        Main.setCurrPhase(0);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "CS101";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.registerCourseParser(connection,sc);
        assertEquals(true, courseExistInFacultyOffer("CS101","2"));
    }
    @Test
    void registerCourse1phaseParser() throws SQLException{
        Main.setCurrPhase(1);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        faculty.registerCourseParser(connection,sc);
        assertEquals(false, faculty.registerCourseParser(connection,sc));
        Main.setCurrPhase(0);
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }
    @Test
    void utility1phase1Parser() throws SQLException,IOException{
        Main.setCurrPhase(1);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "1\n9";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true, faculty.utility(connection));
        Main.setCurrPhase(0);
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }

    @Test
    void deregisterCourseTest()  throws SQLException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "CS102";
        userInputManager(input);
        Main.setCurrPhase(0);
        Scanner sc=new Scanner(System.in);
        facultyController.registerCourse(connection,"2","CS102");
        faculty.deregisterCourseParser(connection,sc);
        assertEquals(false, courseExistInFacultyOffer("CS102","2"));
    }
    @Test
    void deregisterCoursePhase1Test()  throws SQLException,IOException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        Main.setCurrPhase(1);
        Scanner sc=new Scanner(System.in);
        assertEquals(false, faculty.deregisterCourseParser(connection,sc));
        Main.setCurrPhase(0);
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }
    @Test
    void utility2Phase1Parser()  throws SQLException,IOException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        Main.setCurrPhase(1);
        String input = "2\n9";
        userInputManager(input);
        assertEquals(true, faculty.utility(connection));
        Main.setCurrPhase(0);
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }

    @Test
    void utility3PhaseParser()  throws SQLException,IOException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "3\n3\n9";
        userInputManager(input);
        assertEquals(true, faculty.utility(connection));
    }


    public void makeEnrollment(String studentId,String courseId) throws SQLException{
        Statement statement;
        try{
            String insertEnrollment = "INSERT INTO enrollment" +
                    " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                    " ('"+studentId+"','"+Main.getCurrYear()+"','"+Main.getCurrSem()+"','"+courseId+"','"+"5"+"','N/A');";
            statement=connection.createStatement();
            statement.executeUpdate(insertEnrollment);

        }catch(Exception e){
            System.out.println("Error in inserting faculty");
            System.out.println(e);
        }
    }
    public String getGradesByCourseId(Connection connection,String courseId,String year,String sem,String studentId) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From enrollment "+
                    " WHERE course_id='"+courseId+"' AND year='"+year+"' AND semester_no='"+sem+"' AND student_id='"+studentId+"'";

            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            System.out.println("studentId: grade");
            while(rs.next()){
                String grade=rs.getString("grade");
                return grade;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return "-1";
    }
    @Test
    void uploadGradeParser() throws SQLException,IOException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(3);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        makeEnrollment("2","CS302");
        String input = "CS302\nUploadGrade\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.uploadGradeParser(connection,sc);

        String year=Integer.toString(Main.getCurrYear());
        String sem=Integer.toString(Main.getCurrSem());
        assertEquals("A-",getGradesByCourseId(connection,"CS302",year,sem,"2").trim());
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
    }
    @Test
    void uploadGradeNoPermissionParser() throws SQLException,IOException{
        Main.setCurrPhase(3);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        // User Input
        makeEnrollment("2","CS302");
        String input = "CS301\nUploadGrade\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false,faculty.uploadGradeParser(connection,sc));
        Main.setCurrPhase(0);
    }
    @Test
    void uploadGradePhase0Test() throws SQLException,IOException{
        Main.setCurrPhase(0);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        assertEquals(false,faculty.uploadGradeParser(connection,sc));
    }
    @Test
    void utility4phase0Parser() throws SQLException,IOException{
        Main.setCurrPhase(0);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "4\n9";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true,faculty.utility(connection));
    }
    @Test
    void checkCourseByFacultyExistOddSemNotExistTest()throws SQLException,IOException{
        assertEquals(false,facultyController.checkCourseByFacultyExist(connection,"2","CS301"));
    }
    @Test
    void checkCourseByFacultyExistEvenTest()throws SQLException,IOException{
        Main.setCurrSem(2);
        Main.setCurrYear(2022);
        assertEquals(true,facultyController.checkCourseByFacultyExist(connection,"1","CS301"));
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
    }
    @Test
    void checkCourseByFacultyExistEvenNotExistTest()throws SQLException,IOException{
        Main.setCurrSem(2);
        Main.setCurrYear(2022);
        assertEquals(false,facultyController.checkCourseByFacultyExist(connection,"2","CS301"));
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(3);
    }

    @Test
    void getGradesParserByStudentId() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "1\n2";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.getGradesParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcadViewGradeStuId.txt";
        String filePath2="src/test/java/testTxtFile/AcadViewGradeStuId.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }

    @Test
    void getGradesParserByCourseId() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "2\nCS101\n2020\n2";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.getGradesParser(connection,sc);
        String filePath1="src/test/java/testTxtFile/AcadViewGradeCourseId.txt";
        String filePath2="src/test/java/testTxtFile/AcadViewGradeCourseId.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void getGradesParserInvalid() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "3\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false,faculty.getGradesParser(connection,sc));
    }

    @Test
    void getAcademicCurriculumParserTest() throws SQLException,IOException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        faculty.getAcademicCurriculumParser(connection);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculum.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void utility5Test() throws SQLException,IOException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "5\n9";
        userInputManager(input);
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        faculty.utility(connection);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculum.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    public boolean checkTwoFiles(String filePath1,String filePath2){
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
    void getCourseCatalogParserTest() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        faculty.getCourseCatalogParser(connection);
        String filePath1="src/main/java/txtFiles/CourseCatalog.txt";
        String filePath2="src/test/java/testTxtFile/CourseCatalog.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void getAcademicCurriculumEvenTest()throws SQLException,IOException{
        Main.setCurrSem(2);
        Main.setCurrYear(2022);
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "1\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.getAcademicCurriculumParser(connection);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculumEven.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
        Main.setCurrSem(2);
        Main.setCurrYear(2023);
    }
    @Test
    void utility6Test() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        String input = "6\n9\n";
        userInputManager(input);
        faculty.utility(connection);
        String filePath1="src/main/java/txtFiles/CourseCatalog.txt";
        String filePath2="src/test/java/testTxtFile/CourseCatalog.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    public String getPhoneNo(String email) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT phone_no " +
                    " From faculty" +
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
    void changePhoneNoParserTest() throws SQLException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        // User Input
        String input = "123";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.changePhoneNoParser(connection,sc);
        assertEquals("123",getPhoneNo("facCS2@iitrpr.ac.in").trim());
    }
    @Test
    void utility7Test() throws SQLException, IOException {
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        // User Input
        String input = "7\n123\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.utility(connection);
        assertEquals("123",getPhoneNo("facCS2@iitrpr.ac.in").trim());
    }
    public String getPassword(String email) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT password " +
                    " From faculty_login" +
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
    void changePasswordParserTest() throws SQLException{
        Faculty faculty=new Faculty("2","CS","facCS2@iitrpr.ac.in");
        // User Input
        String input = "iitz";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        faculty.changePasswordParser(connection,sc);
        assertEquals("iitz",getPassword("facCS2@iitrpr.ac.in").trim());
    }
    @Test
    void utility8Test() throws SQLException, IOException {
        Faculty faculty = new Faculty("2", "CS", "facCS2@iitrpr.ac.in");
        // User Input
        String input = "8\niitz\n";
        userInputManager(input);
        Scanner sc = new Scanner(System.in);
        faculty.utility(connection);
        assertEquals("iitz", getPassword("facCS2@iitrpr.ac.in").trim());
    }
}