package com.AIMS;

import org.junit.jupiter.api.*;

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

class StudentTest {
    Connection connection;
    StudentController studentController;
    @BeforeAll
    public static void initiateTesting(){
        System.out.println("Testing starts..............");
    }
    @BeforeEach
    public void beforeEachTesting() throws SQLException, IOException, ClassNotFoundException {
        DbConnect Db = new DbConnect();
        connection = Db.connect();
        studentController=new StudentController();
        try{
            connection.setAutoCommit(false);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    @AfterEach
    public void afterEachTesting()throws SQLException{
        try{
            connection.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void userInputManager(String input)throws SQLException{
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    @Test
    void verifyCorrectTest() throws SQLException{
        Student student = new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        assertEquals(true, student.verify(connection,"2020CS2@iitrpr.ac.in","iitropar"));
    }
    @Test
    void verifyIncorrectTest() throws SQLException{
        Student student = new Student("1","CS","2020","2020CS1@iitrpr.ac.in");
        assertEquals(false, student.verify(connection,"2020CS2@iitrpr.ac.in","abc"));
    }
    public boolean checkCourseInEnrollment(String courseId,String studentId)throws SQLException{
        int year=Main.getCurrYear();
        int sem=Main.getCurrSem();
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From enrollment "+
                    " WHERE course_id='"+courseId+"' AND year='"+year+"' AND semester_no='"+sem+"' AND student_id='"+studentId+"'";

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
    void addCourseTest()throws SQLException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "CS302\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,checkCourseInEnrollment("CS302","2"));
    }
    @Test
    void addCourseNotInAcademicCurriculumTest()throws SQLException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "CS311\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(false,checkCourseInEnrollment("CS302","2"));
    }
    public void insertIntoAcademicCalendarCS301Sem6() throws SQLException {
        Statement statement;
        String insertAcademicCurriculum2020sem6 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','6','CS301','2',0,'M','CS','5');";
        statement=connection.createStatement();
        statement.executeUpdate(insertAcademicCurriculum2020sem6);
    }
    @Test
    void addCourseAlreadyTakenTest()throws SQLException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "CS301\n";
        userInputManager(input);
        insertIntoAcademicCalendarCS301Sem6();
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(false,checkCourseInEnrollment("CS302","2"));
    }
    @Test
    void addCourseCreditLimitExceededTest() throws SQLException, IOException {
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "1\nCS302\n1\nCS301";
        userInputManager(input);
        insertIntoAcademicCalendarCS301Sem6();
        Scanner sc=new Scanner(System.in);
        student.utility(connection);
        assertEquals(false,checkCourseInEnrollment("CS301","2"));
    }
    public void insertIntoAcademicCalendar411Sem6() throws SQLException {
        Statement statement;
        String insertAcademicCurriculum2020sem6 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','6','CS411','2',0,'M','CS','5');";
        statement=connection.createStatement();
        statement.executeUpdate(insertAcademicCurriculum2020sem6);
    }
    @Test
    void addCoursePreReqNotClearedTest() throws SQLException, IOException {
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "1\nCS411";
        userInputManager(input);
        insertIntoAcademicCalendar411Sem6();
        student.utility(connection);
        assertEquals(false,checkCourseInEnrollment("CS411","2"));
    }
    @Test
    void utility1Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "1\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void addCourse0phaseTest()throws SQLException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        // User Input
        Scanner sc=new Scanner(System.in);
        assertEquals(false,student.addCourseParser(connection,sc));
    }
    @Test
    void checkCourseTakenEarlier() throws SQLException,IOException{
        assertEquals(false,studentController.checkCourseTakenEarlier(connection,"2","CS402"));
    }

    @Test
    void countCredit0Test() throws SQLException,IOException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
        assertEquals(20,studentController.countCreditsDone(connection,"2",2019,1));
    }
    @Test
    void dropCourseTest()throws SQLException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        studentController.addCourse(connection,"2","CS302","2020");
        String input = "CS302";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.dropCourseParser(connection,sc);
        assertEquals(false,checkCourseInEnrollment("CS302","2"));
    }
    @Test
    void dropCourse0PhaseTest() throws SQLException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        // User Input
        Scanner sc=new Scanner(System.in);
        studentController.addCourse(connection,"2","CS302","2020");
        assertEquals(false,student.dropCourseParser(connection,sc));
    }
    @Test
    void utility2Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "2\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void getCgpaParser() throws SQLException{
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        assertEquals("8.4",student.getCgpaParser(connection,sc));
    }
    public String getPassword(String email){
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT * " +
                    " From student_login" +
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
    void changePasswordParserTest()throws SQLException {
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "iitz";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.changePasswordParser(connection,"2020CS2@iitrpr.ac.in",sc);
        assertEquals("iitz",getPassword("2020CS2@iitrpr.ac.in").trim());
    }
    public String getPhoneNo(String email)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        try{
            String query=" SELECT phone_no " +
                    " From student" +
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
    void changePhoneNoParserTest()throws SQLException {
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "123";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.changePhoneNoParser(connection,sc);
        assertEquals("123",getPhoneNo("2020CS2@iitrpr.ac.in").trim());
    }
    public boolean checkTwoFiles(String filePath1,String filePath2)throws SQLException{
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
    void getAcademicCurriculumTest()throws SQLException,IOException{
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "1\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.getAcademicCurriculumParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculum.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
        Main.setCurrYear(2023);
        Main.setCurrSem(1);
    }
    @Test
    void getAcademicCurriculumEvenTest()throws SQLException,IOException{
        Main.setCurrSem(2);
        Main.setCurrYear(2022);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "1\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.getAcademicCurriculumParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculumEven.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }
    @Test
    void maximumCreditAllowed() throws SQLException {
        Main.setCurrSem(2);
        Main.setCurrYear(2022);
        assertEquals(6.25,studentController.maximumCreditsAllowedThisSem(connection,"2"));
        Main.setCurrSem(1);
        Main.setCurrYear(2023);
    }
    @Test
    void getAcademicCurriculumInvalidTest()throws SQLException,IOException{
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "3\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(false,student.getAcademicCurriculumParser(connection,sc));
    }
    @Test
    void getYourAcademicCurriculumTest()throws SQLException,IOException{
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "2\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.getAcademicCurriculumParser(connection,sc);
        String filePath1="src/main/java/txtFiles/AcademicCurriculum.txt";
        String filePath2="src/test/java/testTxtFile/AcademicCurriculum.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void getCourseCatalogTest() throws SQLException {
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        student.getCourseCatalogParser(connection,sc);
        String filePath1="src/main/java/txtFiles/CourseCatalog.txt";
        String filePath2="src/test/java/testTxtFile/CourseCatalog.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void getAllCourseGradeTest() throws SQLException, IOException {
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        Scanner sc=new Scanner(System.in);
        student.viewAllCoursesGradeParser(connection,sc);
        String filePath1="src/main/java/txtFiles/StudentAllCourseGrade.txt";
        String filePath2="src/test/java/testTxtFile/StudentAllCourseGrade.txt";
        assertEquals(true,checkTwoFiles(filePath1,filePath2));
    }
    @Test
    void utility4Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "4\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utility5Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "5\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utility6Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "6\n3\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utility7Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "7\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utility8Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "8\n3\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utility9Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "9\nabc\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        student.addCourseParser(connection,sc);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void getCourseGradeTest()throws SQLException{
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "CS101\n";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals("CS101:2:B",student.viewCourseGradeParser(connection,sc));
    }
    @Test
    void utility3Test()throws SQLException,IOException{
        Main.setCurrPhase(0);
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "3\n10";
        userInputManager(input);
        Scanner sc=new Scanner(System.in);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void utilityTest() throws SQLException,IOException{
        Student student=new Student("2","CS","2020","2020CS2@iitrpr.ac.in");
        String input = "10\n";
        userInputManager(input);
        assertEquals(true,student.utility(connection));
    }
    @Test
    void getInvalidLTPC()throws SQLException,IOException{
        assertEquals(0.0,studentController.getLtpc(connection,"cs1111"));
    }
    @Test
    void getInvalidGrade()throws SQLException,IOException{
        assertEquals(0,studentController.gradeValue("F"));
    }
    @Test
    void getSemesterNo()throws SQLException,IOException{
        assertEquals("7",studentController.getSemesterNo(2023,2020,2));
    }
}