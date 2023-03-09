package com.AIMS;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentController {
    // Checking the credentials.
    public Boolean login(String email, String password, Connection connection)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT password " +
                " From student_login" +
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

    // Checking weather the course has been taken earlier on not.....or already enrolled this sem.
    public boolean checkCourseTakenEarlier(Connection connection,String studentId,String courseId)throws SQLException{
        Statement statement;
        ResultSet rs=null;
            String query=" SELECT * " +
                    " FROM enrollment "+
                    " WHERE course_id='"+courseId+"' AND student_id='"+studentId+"'";

            statement= connection.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                String Grade=rs.getString("grade");
                if(gradeValue(Grade)>0||Grade.trim().equals("N/A")){
                    return true;
                }
            }
        return false;
    }
    // Counting credits done in a specific year, sem.
    public int countCreditsDone(Connection connection,String studentId,int year,int sem)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        int totalCredit=0;
        int defaultCredit=20;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE student_id='"+studentId+"' AND year='"+year+"' AND semester_no='"+sem+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String Grade=rs.getString("grade");
            double ltpc=getLtpc(connection,courseId);
            if(gradeValue(Grade)>0){
                totalCredit+=ltpc;
            }
        }
        if(totalCredit==0){
            // if not done any credit in prev semester than returning default credits = 20
            return defaultCredit;
        }
        return totalCredit;
    }
    // Counting credits enrolled this semester.
    public double countCreditsThisSem(Connection connection,String studentId) throws SQLException{
        Statement statement;
        ResultSet rs=null;
        int year=Main.getCurrYear();
        int sem=Main.getCurrSem();
        double totalCredit=0;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE student_id='"+studentId+"' AND year='"+year+"' AND semester_no='"+sem+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            System.out.println("hey");
            String courseId=rs.getString("course_id");
            double ltpc=getLtpc(connection,courseId);
            totalCredit+=ltpc;
        }
        System.out.println(totalCredit);
        return totalCredit;
    }
    // Maximum credits allowed this sem.
    public double maximumCreditsAllowedThisSem(Connection connection,String studentId)throws SQLException{
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        int creditAllowed=30;
        int oddSem=1;
        int evenSem=2;
        if(currSem==1){
            creditAllowed=countCreditsDone(connection,studentId,currYear-1,oddSem)+countCreditsDone(connection,studentId,currYear-1,evenSem);
        }
        else{
            creditAllowed=countCreditsDone(connection,studentId,currYear,oddSem)+countCreditsDone(connection,studentId,currYear-1,evenSem);
        }
        return creditAllowed*(0.625);
    }
    public boolean checkCreditExceeded(Connection connection,String studentId,String courseId)throws SQLException{
        double maxCreditAllowed=maximumCreditsAllowedThisSem(connection,studentId);
        System.out.println(maxCreditAllowed+"------");
        double credited=countCreditsThisSem(connection,studentId);
        System.out.println(credited+"------");
        if(credited+getLtpc(connection,courseId)>maxCreditAllowed){
            return true;
        }
        return false;
    }
    public boolean checkPreRequisiteNotCleared(Connection connection,String studentId,String courseId)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        String preRequisites="";
        String query=" SELECT * " +
                " FROM course_catalog "+
                " WHERE course_id='"+courseId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            preRequisites=rs.getString("pre_requisite");
        }
        if(preRequisites.trim().equals("None")){
            return false;
        }
        String[] preRequisiteCourses = preRequisites.split(",");
        for(String preRequisiteCourse: preRequisiteCourses){
            if(checkCourseTakenEarlier(connection,studentId,preRequisiteCourse)==false){
                return true;
            }
        }
        return false;
    }
    public String getSemesterNo(int currYear,int joiningYear,int currSem){
        String semesterNo="0";
        int yearDiff=currYear-joiningYear;
        if(currSem==2){
            semesterNo=Integer.toString(yearDiff*2+1);
        }
        else{
            semesterNo=Integer.toString(yearDiff*2);
        }
        return semesterNo;
    }
    public boolean checkCourseInAcademicCurriculum(Connection connection,String joiningYear,String courseId)throws SQLException{

        String currSemester=getSemesterNo(Main.getCurrYear(),Integer.parseInt(joiningYear),Main.getCurrSem());
        System.out.println(currSemester);
        Statement statement;
        ResultSet rs=null;
        String preRequisites="";
        String query=" SELECT * " +
                " FROM academic_curriculum "+
                " WHERE course_id='"+courseId+"' AND joining_year='"+joiningYear+"' AND semester_no='"+currSemester+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            return true;
        }
        return false;
    }
    public void addCourse(Connection connection,String studentId,String courseId,String joiningYear) throws SQLException{
        if(!checkCourseInAcademicCurriculum(connection,joiningYear,courseId)){
            System.out.println("Course with this course id is not offer this sem...");
            return;
        }
        if(checkCreditExceeded(connection,studentId,courseId)){
            System.out.println("Credit limit reached....to enroll this you have to drop some course");
            return;
        }
        if(checkCourseTakenEarlier(connection,studentId,courseId)){
            System.out.println("You have already taken the course....or you are enrolled this time");
            return;
        }
        if(checkPreRequisiteNotCleared(connection,studentId,courseId)){
            System.out.println("Pre requisite not cleared.......");
            return;
        }
        Statement statement;
        String insertEnrollment = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,grade) VALUES " +
                " ('"+studentId+"','"+Main.getCurrYear()+"','"+Main.getCurrSem()+"','"+courseId+"','N/A');";
        statement=connection.createStatement();
        statement.executeUpdate(insertEnrollment);

    }
    // Can drop courses enrolled this semester only.
    public void dropCourse(Connection connection,String studentId,String courseId)throws SQLException{
        Statement statement;
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        String removeCourse = "DELETE FROM enrollment" +
                " WHERE course_id= '"+courseId+"' AND student_id='"+studentId+"' AND year='"+currYear+"' AND semester_no='"+currSem+"' AND grade='N/A'";

        statement=connection.createStatement();
        statement.executeUpdate(removeCourse);
    }
    public void changePhoneNumber(Connection connection,String newPhoneNo,String studentId)throws SQLException{
        Statement statement;
        String updatePhoneNo = "UPDATE student" +
                " SET phone_no= ' " +newPhoneNo+" ' "+
                " WHERE id='"+studentId+"'";

        statement=connection.createStatement();
        statement.executeUpdate(updatePhoneNo);
    }
    public void changePassword(Connection connection,String newPassword,String currStudentEmail)throws SQLException{
        Statement statement;
        String updatePassword = "UPDATE student_login " +
                " SET password= ' " +newPassword+" ' "+
                " WHERE email='"+currStudentEmail+"'";

        System.out.println(updatePassword);
        statement=connection.createStatement();
        statement.executeUpdate(updatePassword);
    }
    public String viewCourseGrade(Connection connection,String studentId, String courseId)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE course_id='"+courseId+"' AND student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        System.out.println("courseId: studentId: Grade");
        while(rs.next()){
            String Grade=rs.getString("grade");
            System.out.println(courseId+":"+studentId+":"+Grade);
            return courseId+":"+studentId+":"+Grade;
        }
        return "Dummy";
    }
    public void writeToFile(String fileName,String toWrite) throws IOException {

        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(toWrite);
        fileWriter.flush();
        fileWriter.close();
    }
    public void viewAllCoursesGrade(Connection connection,String studentId) throws SQLException, IOException {
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        String toWrite="courseId: Grade"+"\n";
        while(rs.next()){
            String Grade=rs.getString("grade");
            String courseId=rs.getString("course_id");
            toWrite=toWrite+courseId+":"+Grade+"\n";
        }
        String filePath="src/main/java/txtFiles/StudentAllCourseGrade.txt";
        writeToFile(filePath,toWrite);
    }
    public int gradeValue(String gradeInput)throws SQLException{
        int gradeVal=10;
        String grade[]={"A","A-","B","B-","C","C-","D"};
        for(String currGrade: grade){
            if(currGrade.trim().equals(gradeInput.trim())){
                return gradeVal;
            }
            gradeVal--;
        }
        return 0;
    }
    public double getLtpc(Connection connection,String courseId)throws SQLException{
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " FROM course_catalog "+
                " WHERE course_id='"+courseId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String ltpc=rs.getString("l_t_p_c");
            return Integer.parseInt(ltpc);
        }
        return 0;
    }
    public String getCgpa(Connection connection, String studentId) throws SQLException {
        Statement statement;
        ResultSet rs=null;
        int earnedCredits=0;
        int totalCredits=0;
        String query=" SELECT * " +
                " FROM enrollment "+
                " WHERE student_id='"+studentId+"'";

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String grade=rs.getString("grade");
            double ltpc=getLtpc(connection,courseId);
            int gradeObtained=gradeValue(grade);
            if(gradeObtained!=0) {
                earnedCredits += (ltpc * gradeObtained);
                totalCredits += ltpc;
            }
        }
        double cgpa=(double)earnedCredits/totalCredits;
        System.out.println("CGPA :: "+cgpa);
        return Double.toString(cgpa);
    }

    public void viewCompleteAcademicCurriculum(Connection connection) throws SQLException,IOException {
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

    public void viewYourAcademicCurriculum(Connection connection,String currStudentDepartment,String currStudentJoiningYear) throws SQLException {
        Statement statement;
        ResultSet rs=null;
        int joiningYear=Integer.parseInt(currStudentJoiningYear);
        String semesterNo=getSemesterNo(Main.getCurrYear(),joiningYear,Main.getCurrSem());
        String query=" SELECT * " +
                " FROM academic_curriculum "+
                " WHERE joining_year='"+currStudentJoiningYear+"' AND department='"+currStudentDepartment+"' AND semester_no='"+semesterNo+"'";
        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        System.out.println("CourseId: FacultyId: CgpaConstraint: CourseType: Ltpc");
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String facultyId=rs.getString("faculty_id");
            String cgpaConstraint=rs.getString("cgpa_constraint");
            String courseType=rs.getString("course_type");
            String ltpc=rs.getString("l_t_p_c");
            System.out.println(courseId+":"+facultyId+":"+cgpaConstraint+":"+courseType+":"+ltpc);
        }
    }
    public static void getCourseCatalog(Connection connection) throws SQLException {
        Statement statement;
        ResultSet rs=null;
        String query=" SELECT * " +
                " From course_catalog" ;

        statement= connection.createStatement();
        rs=statement.executeQuery(query);
        System.out.println("courseId: courseName: ltpc: department: preRequisite");
        while(rs.next()){
            String courseId=rs.getString("course_id");
            String courseName=rs.getString("course_name");
            String ltpc=rs.getString("l_t_p_c");
            String department=rs.getString("department");
            String preRequisites=rs.getString("pre_requisite");
            System.out.println(courseId+":"+courseName+":"+ltpc+":"+department+":"+preRequisites);
        }
    }
}
