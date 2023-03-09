package com.AIMS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
public class AcademicOfficeController {
    StudentController studentController;
    AcademicOfficeController(){
        studentController=new StudentController();
    }
    public Boolean login(String email, String password, Connection connection) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT password " +
                     " From academic_office_login" +
                     " WHERE email='"+email+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String actualPassword=rs.getString("password");
            if((actualPassword.trim()).equals(password.trim())){
                return true;
            }
            return false;
        }
        return false;
    }
    public void getAcademicCurriculum(Connection connection) throws SQLException,IOException{
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        int logicalSem=0;
        if(currSem==1){
            logicalSem=1;
        }
        String toWrite="JoiningYear: SemesterNo: CourseId: FacultyId: CgpaConstraint: CourseType: Department: Ltpc"+"\n";
        for(int i=0;i<4;i++){
            Statement statement;
            ResultSet rs=null;
            int joiningYear=currYear-i-logicalSem;
            int semesterNo=2*i+3-currSem;
            String query=" SELECT * " +
                    " FROM academic_curriculum "+
                    " WHERE joining_year='"+joiningYear+"' AND semester_no='"+semesterNo+"'";
            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                String courseId=rs.getString("course_id");
                String facultyId=rs.getString("faculty_id");
                String cgpaConstraint=rs.getString("cgpa_constraint");
                String courseType=rs.getString("course_type");
                String department=rs.getString("department");
                String ltpc=rs.getString("l_t_p_c");
                toWrite=toWrite+courseId+":"+facultyId+":"+cgpaConstraint+":"+courseType+":"+department+":"+ltpc+"\n";
            }
        }
        String filePath="src/main/java/txtFiles/AcademicCurriculum.txt";
        writeToFile(filePath,toWrite);
    }
    public void changePhoneNumber(Connection connection,String newPhoneNo,String currEmail) throws SQLException,IOException{
        Statement statement;
        String updatePhoneNo = "UPDATE academic_office" +
                " SET phone_no= ' " +newPhoneNo+" ' "+
                " WHERE email='"+currEmail+"'";


        statement=connection.createStatement();
        statement.executeUpdate(updatePhoneNo);
    }
    public void changePassword(Connection connection,String newPassword,String currEmail) throws SQLException,IOException{
        System.out.println(newPassword);
        Statement statement;
        String updatePassword = "UPDATE academic_office_login" +
                " SET password= ' " +newPassword+" ' "+
                " WHERE email='"+currEmail+"'";

        statement=connection.createStatement();
        statement.executeUpdate(updatePassword);

    }
    public void getCourseCatalog(Connection connection) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From course_catalog" ;

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        String toWrite="year: semesterNo: courseId: ltpc: grade"+"\n";
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String courseName=rs.getString("course_name");
            String ltpc=rs.getString("l_t_p_c");
            String department=rs.getString("department");
            String preRequisites=rs.getString("pre_requisite");
            toWrite=toWrite+courseId+":"+courseName+":"+ltpc+":"+department+":"+preRequisites+"\n";
        }
        String filePath="src/main/java/txtFiles/CourseCatalog.txt";
        writeToFile(filePath,toWrite);
    }
    public void addCourseInCourseCatalog(Connection connection,String[] parsedCourseEntry) throws SQLException,IOException{
        String courseID=parsedCourseEntry[0];
        String courseName=parsedCourseEntry[1];
        String l_t_p_c=parsedCourseEntry[2];
        String department=parsedCourseEntry[3];
        String preRequisite=parsedCourseEntry[4];

        Statement statement;
        String insertCourse = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('"+courseID+"','"+courseName+"','"+l_t_p_c+"','"+department+"','"+preRequisite+"');";
        System.out.println(insertCourse);
        statement=connection.createStatement();
        statement.executeUpdate(insertCourse);
    }
    public void removeCourseInCourseCatalog(Connection connection,String courseId) throws SQLException,IOException{
        Statement statement;
        String removeCourse = "DELETE FROM course_catalog" +
                " WHERE course_id= '"+courseId+"'";

        statement=connection.createStatement();
        statement.executeUpdate(removeCourse);

    }
    public void setPassingCriteria(Connection connection,String joiningYear,String minimumCredits) throws SQLException,IOException{
        Statement statement;
        String insertPassingCriteria = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('"+joiningYear+"','"+minimumCredits+"');";

        statement=connection.createStatement();
        statement.executeUpdate(insertPassingCriteria);
    }
    public int getPassingCriteria(Connection connection,String joiningYear) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " FROM passing_criteria "+
                " WHERE joining_year='"+joiningYear+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String minimumCredit=rs.getString("minimum_credits");
            return Integer.parseInt(minimumCredit);
        }
        return 1000;
    }
    public void setAcademicCurriculum(Connection connection,String[] parsedCourseEntry) throws SQLException,IOException{
        String joiningYear=parsedCourseEntry[0];
        String semesterNo=parsedCourseEntry[1];
        String courseId=parsedCourseEntry[2];
        String facultyId=parsedCourseEntry[3];
        String cgpaConstraint=parsedCourseEntry[4];
        String courseType=parsedCourseEntry[5];
        String department=parsedCourseEntry[6];
        String ltpc=parsedCourseEntry[7];

        Statement statement;
        String insertAcademicCurriculum = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('"+joiningYear+"','"+semesterNo+"','"+courseId+"','"+facultyId+"','"+cgpaConstraint+"','"+courseType+"','"+department+"','"+ltpc+"');";
        statement=connection.createStatement();
        statement.executeUpdate(insertAcademicCurriculum);
    }
    public void addStudent(Connection connection, String[] parsedStudentEntry,int studentNum) throws SQLException,IOException{
        String name=parsedStudentEntry[0];
        String phoneNo=parsedStudentEntry[1];
        String department=parsedStudentEntry[2];
        String joiningYear=parsedStudentEntry[3];
        String email=joiningYear+department+Integer.toString(studentNum)+"@iitrpr.ac.in";
        Statement statement;
        String insertStudent = "INSERT INTO student" +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('"+studentNum+"','"+name+"','"+email+"','"+phoneNo+"','"+department+"','"+joiningYear+"');";
        statement=connection.createStatement();
        statement.executeUpdate(insertStudent);

        String insertStudentCredentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('"+email+"','iitropar');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudentCredentials);

    }
    public void addFaculty(Connection connection,String[] parsedFacultyEntry,int facultyNum) throws SQLException,IOException{
        String name=parsedFacultyEntry[0];
        String phoneNo=parsedFacultyEntry[1];
        String department=parsedFacultyEntry[2];
        String email=name+department+Integer.toString(facultyNum)+"@iitrpr.ac.in";
        Statement statement;
        String insertFaculty = "INSERT INTO faculty" +
                " (id,name,email,phone_no,department) VALUES " +
                " ('"+facultyNum+"','"+name+"','"+email+"','"+phoneNo+"','"+department+"');";
        statement=connection.createStatement();
        statement.executeUpdate(insertFaculty);

        String insertFacultyCredentials = "INSERT INTO faculty_login" +
                " (email, password) VALUES " +
                " ('"+email+"','iitropar');";

        statement=connection.createStatement();
        statement.executeUpdate(insertFacultyCredentials);

    }
    public void writeToFile(String fileName,String toWrite) throws SQLException,IOException{
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(toWrite);
        fileWriter.flush();
        fileWriter.close();
    }
    public void getGradesByStudentId(Connection connection,String studentId) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;

        String query=" SELECT * " +
                " From enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        String toWrite="year: semesterNo: courseId: ltpc: grade"+"\n";
        while(rs.next()){
            String year=rs.getString("year");
            String semesterNo=rs.getString("semester_no");
            String courseId=rs.getString("course_id");
            String ltpc=rs.getString("l_t_p_c");
            String grade=rs.getString("grade");
            toWrite=toWrite+year+":"+semesterNo+":"+courseId+":"+ltpc+":"+grade+"\n";
        }
        String filePath="src/main/java/txtFiles/AcadViewGradeStuId.txt";
        writeToFile(filePath,toWrite);
    }
    public void getGradesByCourseId(Connection connection,String courseId,String year,String sem) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From enrollment "+
                " WHERE course_id='"+courseId+"' AND year='"+year+"' AND semester_no='"+sem+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        String toWrite="studentId: grade"+"\n";
        while(rs.next()){
            String studentId=rs.getString("student_id");
            String grade=rs.getString("grade");
            toWrite=toWrite+studentId+":"+grade+"\n";
            String filePath="src/main/java/txtFiles/AcadViewGradeCourseId.txt";
        }
        String filePath="src/main/java/txtFiles/AcadViewGradeStuId.txt";
        writeToFile(filePath,toWrite);
    }
    public boolean checkAllMandatoryCourses(Connection connection,String studentId,int joiningYear,String department) throws SQLException,IOException{
        int currYear=Main.getCurrYear();
        int currSem=Main.getCurrSem();
        if((currYear-joiningYear<=3)||(currYear-joiningYear==4&&currSem==1)){
            System.out.println("Cannot generate transcript before 4 years of joining");
            return false;
        }
        else{
            for(int semesterNo=1;semesterNo<=8;semesterNo++){
                Statement statement;
                ResultSet rs=null;
                String query=" SELECT * " +
                        " FROM academic_curriculum "+
                        " WHERE joining_year='"+joiningYear+"' AND department='"+department+"' AND semester_no='"+semesterNo+"' AND course_type='M'";

                statement= connection.createStatement();
                rs=statement.executeQuery(query);
                while(rs.next()) {
                    String courseId = rs.getString("course_id");
                    Statement innerStatement;
                    ResultSet innerRs = null;
                    String innerQuery = " SELECT * " +
                            " From enrollment " +
                            " WHERE student_id='" + studentId + "' AND course_id='" + courseId + "'";
                    innerStatement = connection.createStatement();
                    innerRs = innerStatement.executeQuery(innerQuery);
                    boolean passed = false;
                    while (innerRs.next()) {
                        String grade = innerRs.getString("grade");
                        StudentController studentController=new StudentController();
                        if (studentController.gradeValue(grade) > 0) {
                            passed = true;
                            break;
                        }
                    }
                    if (passed == false) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    public boolean checkMinimumCredit(Connection connection,String studentId,String studentEmail) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        int totalCredits=0;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String grade=rs.getString("grade");
            double ltpc=studentController.getLtpc(connection,courseId);
            int gradeObtained=studentController.gradeValue(grade);
            if(gradeObtained!=0) {
                totalCredits += ltpc;
            }
        }
        String joiningYear=studentEmail.substring(0,4);
        int minimumCredits=getPassingCriteria(connection,joiningYear);
        if(totalCredits>=minimumCredits){
            return true;
        }
        return false;
    }
    public String getStudentEmailByStudentId(Connection connection,String studentId) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From student" +
                " WHERE id='"+studentId+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String email=rs.getString("email");
            return email;
        }
        return "";
    }
    public String getStudentNameByStudentId(Connection connection,String studentId)throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From student" +
                " WHERE id='"+studentId+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String email=rs.getString("name");
            return email;
        }
        String empty="";
        return empty;
    }
    public void writeGradeToFile(Connection connection,String studentId,FileWriter myWriter) throws SQLException,IOException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        System.out.println("year: semesterNo: courseId: ltpc: grade");
        while(rs.next()){
            String year=rs.getString("year");
            String semesterNo=rs.getString("semester_no");
            String courseId=rs.getString("course_id");
            String ltpc=rs.getString("l_t_p_c");
            String grade=rs.getString("grade");
            myWriter.write(year+":"+semesterNo+":"+courseId+":"+ltpc+":"+grade+"\n");
        }
    }
    public void getTranscriptByStudentId(Connection connection,String studentId) throws SQLException,IOException {
        String studentEmail=getStudentEmailByStudentId(connection,studentId);
        String studentName=getStudentNameByStudentId(connection,studentId);
        String department=studentEmail.substring(4,6);
        int joiningYear=Integer.parseInt(studentEmail.substring(0,4));
        Boolean allCoursesDone=checkAllMandatoryCourses(connection,studentId,joiningYear,department);
        Boolean minimumCredit=checkMinimumCredit(connection,studentId,studentEmail);
        if(minimumCredit&&allCoursesDone){
            FileWriter myWriter = new FileWriter("src/main/java/txtFiles/GeneratedTranscript.txt");
            myWriter.write("Student name: "+studentName+"\n");
            System.out.println("Student Id: "+studentId+"\n");
            myWriter.write("Joining year: "+joiningYear+"\n");
            myWriter.write("Department: "+department+"\n");
            writeGradeToFile(connection,studentId,myWriter);
            myWriter.flush();
            myWriter.close();
        }
        else{
            System.out.println("Criteria for getting transcript not satisfied");
        }
    }
}
