package com.AIMS;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class AcademicOffice {
    private String email;
    private int studentCount=4;
    private int facultyCount=2;
    AcademicOfficeController academicOfficeController;
    AcademicOffice(String email){
        this.email=email;
        this.studentCount=4;
        this.facultyCount=2;
        academicOfficeController=new AcademicOfficeController();
    }
    public boolean verify(Connection connection,String email,String password) throws SQLException,IOException{
        //Initialising the values
        this.email=email;
        Boolean loginVerdict=academicOfficeController.login(email,password,connection);
        return loginVerdict;
    }
    public boolean utility(Connection connection) throws SQLException,IOException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Choose from following actions----");
            System.out.println("1. Move to next phase.");
            System.out.println("2. Get courses offered this semester.");
            System.out.println("3. Get course catalog.");
            System.out.println("4. Add course in course catalog.");
            System.out.println("5. Remove course in course catalog.");
            System.out.println("6. Add students.");
            System.out.println("7. Add faculty.");
            System.out.println("8. View grades.");
            System.out.println("9. Generate Transcript.");
            System.out.println("10. Update contact details.");
            System.out.println("11. Change Password.");
            System.out.println("12. Exit");

            String userChoice;
            if(sc.hasNext()){
                userChoice=sc.nextLine();
            }
            else{
                userChoice="12";
            }
            System.out.println(userChoice);
            if (userChoice.trim().equals("1")) {
                moveToNextPhaseParser(connection,sc);
            }
            else if (userChoice.trim().equals("2")) {
                getAcademicCurriculumParser(connection,sc);
            }
            else if (userChoice.trim().equals("3")) {
                getCourseCatalogParser(connection,sc);
            }
            else if (userChoice.trim().equals("4")) {
                addCourseInCourseCatalogParser(connection,sc);
            }
            else if (userChoice.trim().equals("5")) {
                removeCourseInCourseCatalogParser(connection,sc);

            } else if (userChoice.trim().equals("6")) {
                addStudentParser(connection,studentCount,sc);

            } else if (userChoice.trim().equals("7")) {
                addFacultyParser(connection,facultyCount,sc);

            } else if (userChoice.trim().equals("8")) {
                getGradesParser(connection,sc);

            } else if (userChoice.trim().equals("9")) {
                getTranscriptParser(connection,sc);

            } else if (userChoice.trim().equals("10")) {
                changePhoneNoParser(connection,sc);

            } else if (userChoice.trim().equals("11")) {
                changePasswordParser(connection,sc);
            }
            else if (userChoice.trim().equals("12")){
                System.out.print("You successfully exited");
                break;
            } else {
                System.out.println("Invalid Input!!!!");
            }
        }
        return true;
    }
    public void incrementSemester(Connection connection) throws SQLException,IOException{
        int currSem=Main.getCurrSem();
        int currYear=Main.getCurrYear();
        if(currSem==1){
            Main.setCurrSem(2);
        }
        else{
            Main.setCurrSem(1);
            Main.setCurrYear(currYear+1);
        }
    }
    public int moveToNextPhaseParser(Connection connection,Scanner sc) throws SQLException,IOException{
        int currPhase=Main.getCurrPhase();
        currPhase+=1;
        currPhase%=4;
        Main.setCurrPhase(currPhase);
        int phase=currPhase;
        if(phase==0){
            System.out.println("Grade submission end...........Students can view grades......");
            System.out.println("Faculty can offer courses for the incoming semester......");
        }
        else if(phase==1){
            System.out.println("New academic semester start starts... students can add and drop courses now........");
            incrementSemester(connection);
        }
        else if(phase==2){
            System.out.println("Course add and drop end......");
        }
        else{
            System.out.println("Faculty can upload grades now....");
        }
        return currPhase;
    }
    public void getAcademicCurriculumParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Here is Academic Curriculum of this semester");
        academicOfficeController.getAcademicCurriculum(connection);
    }
    public void academicCurriculumParser(Connection connection,Scanner sc) throws SQLException,IOException{
        if(Main.getCurrPhase()!=1){
            System.out.println("This action is not supported at the moment");
            return;
        }
        System.out.println("Enter academic curriculum for this semester");
        System.out.println("Order seperated by ','-> Joining Year, Semester, Course Id, Faculty Id, Cgpa Constraint, Course Type, Department, L-T-P-C");
        System.out.println("Enter file name------------------------------");
        String fileName=sc.nextLine();
        String filePath="src/main/java/txtFiles/"+fileName+".txt";
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String academicCurriculumEntry;
        while ((academicCurriculumEntry = br.readLine()) != null){
            String[] parsedAcademicCurriculumEntry = academicCurriculumEntry.split(",");
            int size= parsedAcademicCurriculumEntry.length;
            if(size!=8){
                System.out.println("Invalid Input!!!");
            }
            else {
                academicOfficeController.setAcademicCurriculum(connection, parsedAcademicCurriculumEntry);
            }
        }
    }
    public void passingCriteriaParser(Connection connection,Scanner sc) throws SQLException,IOException{
        if(Main.getCurrPhase()!=1){
            System.out.println("This action is not supported at the moment");
            return;
        }
        System.out.println("Enter the year for which you want to set.");
        String joiningYear=sc.nextLine();
        System.out.println("Enter the credit required.");
        String minimumCredits=sc.nextLine();
        academicOfficeController.setPassingCriteria(connection,joiningYear,minimumCredits);
    }
    public void getCourseCatalogParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Here are all the courses!!!!");
        academicOfficeController.getCourseCatalog(connection);
    }
    public boolean addCourseInCourseCatalogParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Enter course details you want to add");
        System.out.println("Order seperated by ','-> Course Id, Course Name, LTPC, Department, PreRequisite");
        String courseEntry= sc.nextLine();
        String[] parsedCourseEntry = courseEntry.split(",");
        System.out.println(courseEntry);
        int size= parsedCourseEntry.length;
        if(size!=5){
            System.out.println("Invalid Input!!!");
            return false;
        }
        else{
            academicOfficeController.addCourseInCourseCatalog(connection,parsedCourseEntry);
        }
        return true;
    }
    public void removeCourseInCourseCatalogParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Enter course id you want to remove");
        String courseId= sc.nextLine();
        academicOfficeController.removeCourseInCourseCatalog(connection,courseId);
    }
    public boolean addStudentParser(Connection connection,int studentCount,Scanner sc) throws SQLException,IOException{
        System.out.println("Enter student details you want to add");
        System.out.println("Order seperated by ','-> Name, Phone No, Department, Joining Year");
        System.out.println("Enter file name------------------------------");
        String fileName=sc.nextLine();
        String filePath="src/main/java/txtFiles/"+fileName+".txt";
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String studentEntry;
        while ((studentEntry = br.readLine()) != null){
            System.out.println(studentEntry);
            String[] parsedStudentEntry = studentEntry.split(",");
            int size= parsedStudentEntry.length;
            if(size!=4){
                System.out.println("Invalid Input!!!");
                return false;
            }
            else{
                studentCount++;
                int studentNum=studentCount;
                academicOfficeController.addStudent(connection,parsedStudentEntry,studentNum);
            }
        }
        return true;
    }
    public boolean addFacultyParser(Connection connection,int facultyCount,Scanner sc) throws SQLException,IOException{
        System.out.println("Enter faculty details you want to add");
        System.out.println("Order seperated by ','-> Name, Phone No, Department");
        System.out.println("Enter file name------------------------------");
        String fileName=sc.nextLine();
        String filePath="src/main/java/txtFiles/"+fileName+".txt";
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String facultyEntry;
        while ((facultyEntry = br.readLine()) != null){
            String[] parsedFacultyEntry = facultyEntry.split(",");
            int size= parsedFacultyEntry.length;
            if(size!=3){
                System.out.println("Invalid Input!!!");
                return false;
            }
            else{
                facultyCount++;
                int facultyNum=facultyCount;
                academicOfficeController.addFaculty(connection,parsedFacultyEntry,facultyNum);
            }
        }
        return true;
    }
    public boolean getGradesParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Choose from the below option");
        System.out.println("1. View grades of student");
        System.out.println("2. View grades of a course  ");
        int userChoice = sc.nextInt();
        sc.nextLine();
        if(userChoice==1){
            System.out.println("Enter the student Id to view grades.");
            String studentId = sc.nextLine();
            academicOfficeController.getGradesByStudentId(connection,studentId);
        }
        else if(userChoice==2){
            System.out.println("Enter the course Id to view grades.");
            String courseId = sc.nextLine();
            System.out.println("Enter the year to view grades.");
            String year = sc.nextLine();
            System.out.println("Enter the semester to view grades.");
            String sem = sc.nextLine();
            academicOfficeController.getGradesByCourseId(connection,courseId,year,sem);
        }
        else{
            System.out.println("Invalid Choice!!!");
            return false;
        }
        return true;
    }
    public void getTranscriptParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Enter the student Id to get transcript.");
        String studentId = sc.nextLine();
        academicOfficeController.getTranscriptByStudentId(connection,studentId);
    }
    public void changePhoneNoParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Please enter new phone no.");
        String newPhoneNo = sc.nextLine();
        academicOfficeController.changePhoneNumber(connection,newPhoneNo,email);
    }
    public void changePasswordParser(Connection connection,Scanner sc) throws SQLException,IOException{
        System.out.println("Please enter new password.");
        String newPassword = sc.nextLine();
        academicOfficeController.changePassword(connection,newPassword,email);
    }
}
