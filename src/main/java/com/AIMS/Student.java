package com.AIMS;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Student {
    String id;
    String department;
    String joiningYear;
    String email;
    StudentController studentController;

    public Student(String id,String department,String joiningYear,String studentEmail)throws SQLException{
        this.email=studentEmail;
        this.department=department;
        this.id=id;
        this.joiningYear=joiningYear;
        studentController=new StudentController();
    }
    public boolean verify(Connection connection,String email,String password)throws SQLException{
        System.out.println(email);
        System.out.println(password);
        Boolean loginVerdict=studentController.login(email,password,connection);
        if(loginVerdict==false){
            System.out.println("hey");
            return false;
        }
        this.email=email;
        String[] splitEmail = email.split("@");
        id=splitEmail[0].substring(6);
        department=splitEmail[0].substring(4,6);
        joiningYear=splitEmail[0].substring(0,4);
        return loginVerdict;
    }
    public boolean utility(Connection connection) throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Choose from following actions----");
            System.out.println("1. Add a course.");
            System.out.println("2. Drop a course.");
            System.out.println("3. View course grade.");
            System.out.println("4. View all courses grade.");
            System.out.println("5. Get CGPA.");
            System.out.println("6. Get academic curriculum .");
            System.out.println("7. Get course Catalog.");
            System.out.println("8. Change Phone number.");
            System.out.println("9. Change Password.");
            System.out.println("10. Exit.");

            String userChoice;
            if(sc.hasNext()){
                userChoice=sc.nextLine();
            }
            else{
                userChoice="10";
            }
            System.out.println(userChoice);
            if(userChoice.trim().equals("1")){
                addCourseParser(connection,sc);
            }
            else if(userChoice.trim().equals("2")){
                dropCourseParser(connection,sc);
            }
            else if(userChoice.trim().equals("3")){
                viewCourseGradeParser(connection,sc);
            }
            else if(userChoice.trim().equals("4")){
                viewAllCoursesGradeParser(connection,sc);
            }
            else if(userChoice.trim().equals("5")){
                getCgpaParser(connection,sc);
            }
            else if(userChoice.trim().equals("6")){
                getAcademicCurriculumParser(connection,sc);
            }
            else if(userChoice.trim().equals("7")){
                getCourseCatalogParser(connection,sc);
            }
            else if(userChoice.trim().equals("8")){
                changePhoneNoParser(connection,sc);
            }
            else if(userChoice.trim().equals("9")){
                changePasswordParser(connection,email,sc);
            }
            else if(userChoice.trim().equals("10")){
                System.out.println("You Succesfully Exicted");
                break;
            }
            else{
                System.out.println("Invalid Choice");
            }
        }
        return true;
    }
    public boolean addCourseParser(Connection connection,Scanner sc) throws SQLException{
        int currPhase=Main.getCurrPhase();
        if(currPhase!=1){
            System.out.println("Action is not supported at this moment");
            return false;
        }
        System.out.println("Enter the course id you want to add.");
        String courseId = sc.nextLine();
        String stuId=id;
        String stuJoiningYear=joiningYear;
        studentController.addCourse(connection,stuId,courseId,stuJoiningYear);
        return true;
    }
    public boolean dropCourseParser(Connection connection,Scanner sc) throws SQLException{
        int currPhase=Main.getCurrPhase();
        if(currPhase!=1){
            System.out.println("Action is not supported at this moment");
            return false;
        }
        System.out.println("Enter the course id you want to drop.");
        String courseId = sc.nextLine();
        studentController.dropCourse(connection,id,courseId);
        return true;
    }
    public String viewCourseGradeParser(Connection connection,Scanner sc)throws SQLException{
        System.out.println("Enter course id for which you want to view grade.");
        String courseId = sc.nextLine();
        return studentController.viewCourseGrade(connection,id,courseId);
    }
    public void viewAllCoursesGradeParser(Connection connection,Scanner sc) throws SQLException, IOException {
        System.out.println("Here are all the grades.");
        studentController.viewAllCoursesGrade(connection,id);
    }
    public String getCgpaParser(Connection connection,Scanner sc) throws SQLException {
        System.out.println("Here is your Cgpa.");
        return studentController.getCgpa(connection,id);
    }
    public boolean getAcademicCurriculumParser(Connection connection,Scanner sc) throws SQLException,IOException {
        System.out.println("Choose from the below options.");
        System.out.println("1. To view complete academic curriculum");
        System.out.println("2. To view your academic curriculum");

        int userChoice = sc.nextInt();

        if(userChoice==1){
            studentController.viewCompleteAcademicCurriculum(connection);
        }
        else if(userChoice==2){
            studentController.viewYourAcademicCurriculum(connection,department,joiningYear);
        }
        else{
            System.out.println("Invalid choice!!");
            return false;
        }
        return true;
    }
    public void getCourseCatalogParser(Connection connection,Scanner sc) throws SQLException {
        System.out.println("Here is the course catalog.");
        StudentController.getCourseCatalog(connection);
    }
    public void changePhoneNoParser(Connection connection,Scanner sc)throws SQLException{
        System.out.println("Please enter new phone no.");
        String newPhoneNo = sc.nextLine();
        studentController.changePhoneNumber(connection,newPhoneNo,id);
    }
    public void changePasswordParser(Connection connection,String email,Scanner sc) throws SQLException {
        System.out.println("Please enter new password.");
        String newPassword = sc.nextLine();
        studentController.changePassword(connection,newPassword,email);
    }
}
