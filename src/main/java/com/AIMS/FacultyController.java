package com.AIMS;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FacultyController {
    public boolean login(String email,String password,Connection connection) throws SQLException {
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " FROM faculty_login" +
                " WHERE email='"+email+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String actualPassword=rs.getString("password");
            if((actualPassword.trim()).equals(password.trim())){
                return true;
            }
        }
        return false;
    }
    public void viewCompleteAcademicCurriculum(Connection connection) throws SQLException,IOException{
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
    public void getGradesByStudentId(Connection connection,String studentId) throws SQLException, IOException {
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        String toWrite="year: semesterNo: courseId: ltpc: grade\n";
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
        String toWrite="studentId: grade\n";
        while(rs.next()){
            String studentId=rs.getString("student_id");
            String grade=rs.getString("grade");
            toWrite=toWrite+studentId+":"+grade+"\n";
        }
        String filePath="src/main/java/txtFiles/AcadViewGradeCourseId.txt";
        writeToFile(filePath,toWrite);
    }
    public void writeToFile(String fileName,String toWrite) throws SQLException,IOException{

        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(toWrite);
        fileWriter.flush();
        fileWriter.close();
    }
    public void getCourseCatalog(Connection connection) throws SQLException, IOException{
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
    public void changePhoneNumber(Connection connection,String newPhoneNo,String facultyId)throws SQLException{
        Statement statement;
        String updatePhoneNo = "UPDATE faculty" +
                " SET phone_no= ' " +newPhoneNo+" ' "+
                " WHERE id='"+facultyId+"'";
        statement=connection.createStatement();
        statement.executeUpdate(updatePhoneNo);
    }
    public void changePassword(Connection connection,String newPassword,String currFacultyEmail) throws SQLException{
        Statement statement;
        String updatePassword = "UPDATE faculty_login" +
                " SET password= ' " +newPassword+" ' "+
                " WHERE email='"+currFacultyEmail+"'";

        statement=connection.createStatement();
        statement.executeUpdate(updatePassword);
    }
    //register course
    public void registerCourse(Connection connection,String facultyId,String courseId)throws SQLException{
        Statement statement;

        String insertFacultyOffers = "INSERT INTO faculty_offer" +
                " (faculty_id,year,semester_no,course_id) VALUES " +
                " ('"+facultyId+"','"+Main.getCurrYear()+"','"+Main.getCurrSem()+"','"+courseId+"')";
        statement=connection.createStatement();
        statement.executeUpdate(insertFacultyOffers);
    }
    // Deregister a course
    public void deregisterCourse(Connection connection,String facultyId,String courseId) throws SQLException{
        Statement statement;
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        String removeCourse = "DELETE FROM faculty_offer" +
                " WHERE course_id= '"+courseId+"' AND faculty_id='"+facultyId+"' AND year='"+currYear+"' AND semester_no='"+currSem+"'";

        statement=connection.createStatement();
        statement.executeUpdate(removeCourse);
    }
    // Check if faculty has permission to upload grade
    public boolean checkCourseByFacultyExist(Connection connection,String facultyId,String courseId) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        for(int i=0;i<4;i++){
            if(currSem==2){
                System.out.println("hey");
                int joiningYear=currYear-i;
                int semesterNo=2*i+1;
                String query=" SELECT * " +
                        " From academic_curriculum " +
                        " WHERE course_id='"+courseId+"' AND joining_year='"+joiningYear+"' AND semester_no='"+semesterNo+"'";
                statement= connection.createStatement();
                rs=statement.executeQuery(query);
                while(rs.next()){
                    String facultyIds=rs.getString("faculty_id");
                    String[]  facultyIdTeaching= facultyIds.split(",");
                    for(String id:facultyIdTeaching){
                        if(id.equals(facultyId)){
                            return true;
                        }
                    }
                }
            }
            else{
                int joiningYear=currYear-i-1;
                int semesterNo=2*i+2;
                String query=" SELECT * " +
                        " From academic_curriculum " +
                        " WHERE faculty_id='"+facultyId+"' AND course_id='"+courseId+"' AND joining_year='"+joiningYear+"' AND semester_no='"+semesterNo+"'";
                statement= connection.createStatement();
                rs=statement.executeQuery(query);
                while(rs.next()){
                    String facultyIds=rs.getString("faculty_id");
                    String[]  facultyIdTeaching= facultyIds.split(",");
                    for(String id:facultyIdTeaching){
                        if(id.equals(facultyId)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    //Uploading grades of a student
    public void uploadCourseGrade(Connection connection,String facultyId,String courseId,String studentId,String grade) throws SQLException{
        Statement statement;
        String updateCourseGrade = "UPDATE enrollment" +
                " SET grade= ' " +grade+" ' "+
                " WHERE course_id='"+courseId+"' AND student_id='"+studentId+"' AND year='"+Main.getCurrYear()+"' AND semester_no='"+Main.getCurrSem()+"'";

        statement=connection.createStatement();
        statement.executeUpdate(updateCourseGrade);
    }
}
