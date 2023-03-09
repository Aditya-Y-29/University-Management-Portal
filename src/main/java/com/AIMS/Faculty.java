package com.AIMS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.*;
public class Faculty {
    private String facultyId;
    private String facultyDepartment;
    private String facultyEmail;
    FacultyController facultyController;
    Faculty(String facultyId,String facultyDepartment,String facultyEmail) throws SQLException {
        this.facultyId=facultyId;
        this.facultyDepartment=facultyDepartment;
        this.facultyEmail=facultyEmail;
        facultyController=new FacultyController();
    }
    public boolean verify(Connection connection,String email,String password) throws SQLException{
        Boolean loginVerdict=facultyController.login(email,password,connection);
        if(loginVerdict==false){
            return false;
        }
        facultyEmail=email;
        String[] splitEmail = email.split("@");
        System.out.println(splitEmail[0]);
        int firstNumberIndex=-1;
        for(int i=0;i<(splitEmail[0]).length();i++){
            if(Character.isDigit(splitEmail[0].charAt(i))==true){
                firstNumberIndex=i;
                break;
            }
        }
        facultyId=splitEmail[0].substring(firstNumberIndex);
        facultyDepartment=splitEmail[0].substring(firstNumberIndex-2,firstNumberIndex);
        return loginVerdict;
    }
    public boolean utility(Connection connection) throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Choose from following actions----");
            System.out.println("1. Register a course.");
            System.out.println("2. Deregister a course.");
            System.out.println("3. View grades.");
            System.out.println("4. Upload grades of a course.");
            System.out.println("5. Get academic curriculum .");
            System.out.println("6. Get course Catalog.");
            System.out.println("7. Change Phone number.");
            System.out.println("8. Change Password.");
            System.out.println("9. Quit.");

            String userChoice;
            if(sc.hasNext()){
                userChoice=sc.nextLine();
            }
            else{
                userChoice="12";
            }
            if(userChoice.trim().equals("1")){
                registerCourseParser(connection,sc);
            }
            else if(userChoice.trim().equals("2")){
                deregisterCourseParser(connection,sc);
            }
            else if(userChoice.trim().equals("3")){
                getGradesParser(connection,sc);
            }
            else if(userChoice.trim().equals("4")){
                uploadGradeParser(connection,sc);
            }
            else if(userChoice.trim().equals("5")){
                getAcademicCurriculumParser(connection);
            }
            else if(userChoice.trim().equals("6")){
                getCourseCatalogParser(connection);
            }
            else if(userChoice.trim().equals("7")){
                changePhoneNoParser(connection,sc);
            }
            else if(userChoice.trim().equals("8")){
                changePasswordParser(connection,sc);
            }
            else if(userChoice.trim().equals("9")){
                System.out.print("You successfully exited");
                break;
            }
            else{
                System.out.println("Invalid Choice!!!!");
                return false;
            }
        }
        return true;
    }
    public boolean registerCourseParser(Connection connection,Scanner sc) throws SQLException{
        int currPhase=Main.getCurrPhase();
        if(currPhase!=0){
            System.out.println("Action is not supported at this moment");
            return false;
        }
        System.out.println("Enter the course Id you want to register");
        String courseId = sc.nextLine();
        facultyController.registerCourse(connection,facultyId,courseId);
        return true;
    }
    public boolean deregisterCourseParser(Connection connection,Scanner sc) throws SQLException{
        int currPhase=Main.getCurrPhase();
        if(currPhase!=0){
            System.out.println("Action is not supported at this moment");
            return false;
        }
        System.out.println("Enter the course Id you want to deregister");
        String courseId = sc.nextLine();
        facultyController.deregisterCourse(connection,facultyId,courseId);
        return true;
    }
    public boolean uploadGradeParser(Connection connection,Scanner sc) throws SQLException,IOException{
        int currPhase=Main.getCurrPhase();
        if(currPhase!=3){
            System.out.println("Action is not supported at this moment");
            return false;
        }
        System.out.println("Enter the course Id for which you want to upload grade");
        String courseId = sc.nextLine();
        if(facultyController.checkCourseByFacultyExist(connection,facultyId,courseId)){
            System.out.println("Enter file name");
            String fileName=sc.nextLine();
            System.out.println("Enter studentId, grade");
            String filePath="src/main/java/txtFiles/"+fileName+".txt";
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null){
                    String gradeUpload=st;
                    System.out.println(gradeUpload);
                    String[] gradeUploadSplit=gradeUpload.split(",");
                    facultyController.uploadCourseGrade(connection,facultyId,courseId,gradeUploadSplit[0],gradeUploadSplit[1]);
                }
        }
        else{
            System.out.println("Either course doesn't exist or you don't have permission to upload grades");
            return false;
        }
        return true;
    }
    public boolean getGradesParser(Connection connection,Scanner sc) throws SQLException, IOException {
        System.out.println("Choose from the below option");
        System.out.println("1. View grades of student");
        System.out.println("2. View grades of a course");
        int userChoice = sc.nextInt();
        sc.nextLine();
        if(userChoice==1){
            System.out.println("Enter the student Id to view grades.");
            String studentId = sc.nextLine();
            facultyController.getGradesByStudentId(connection,studentId);
        }
        else if(userChoice==2){
            System.out.println("Enter the course Id to view grades.");
            String courseId = sc.nextLine();
            System.out.println("Enter the year to view grades.");
            String year = sc.nextLine();
            System.out.println("Enter the semester to view grades.");
            String sem = sc.nextLine();
            facultyController.getGradesByCourseId(connection,courseId,year,sem);
        }
        else{
            System.out.println("Invalid Choice!!!");
            return false;
        }
        return true;
    }
    public void getAcademicCurriculumParser(Connection connection) throws SQLException,IOException{
        System.out.println("Here is a complete academic curriculum");
        facultyController.viewCompleteAcademicCurriculum(connection);
    }
    public void getCourseCatalogParser(Connection connection) throws SQLException, IOException {
        System.out.println("Here is the course catalog.");
        facultyController.getCourseCatalog(connection);
    }
    public void changePhoneNoParser(Connection connection,Scanner sc) throws SQLException{
        System.out.println("Please enter new phone no.");
        String newPhoneNo = sc.nextLine();
        facultyController.changePhoneNumber(connection,newPhoneNo,facultyId);
    }
    public void changePasswordParser(Connection connection,Scanner sc) throws SQLException{
        System.out.println("Please enter new password.");
        String newPassword = sc.nextLine();
        facultyController.changePassword(connection,newPassword,facultyEmail);
    }
}

