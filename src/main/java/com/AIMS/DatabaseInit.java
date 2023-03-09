package com.AIMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseInit
{
    public static void init(Connection connection) throws SQLException, IOException {
        Statement statement;
        String insertAcademicOfficeUser = "INSERT INTO academic_office" +
                " (name, email,phone_no) VALUES " +
                " ('acadoffice','acadoffice@iitrpr.ac.in','0000000000');";

        String insertAcademicOfficeCredentials = "INSERT INTO academic_office_login" +
                " (email, password) VALUES " +
                " ('acadoffice@iitrpr.ac.in','iitropar');";

        statement=connection.createStatement();
        statement.executeUpdate(insertAcademicOfficeUser);
        statement.executeUpdate(insertAcademicOfficeCredentials);

        String insertStudent1Credentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('2020CS1@iitrpr.ac.in','iitropar');";

        String insertStudent1 = "INSERT INTO student " +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('1','stu1','2020CS1@iitrpr.ac.in','111','CS','2019');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudent1);
        statement.executeUpdate(insertStudent1Credentials);

        String insertStudent2Credentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('2020CS2@iitrpr.ac.in','iitropar');";

        String insertStudent2 = "INSERT INTO student " +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('2','stu2','2020CS2@iitrpr.ac.in','111','CS','2020');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudent2);
        statement.executeUpdate(insertStudent2Credentials);

        String insertStudent3Credentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('2020CS3@iitrpr.ac.in','iitropar');";

        String insertStudent3 = "INSERT INTO student " +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('3','stu3','2020CS3@iitrpr.ac.in','111','CS','2021');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudent3);
        statement.executeUpdate(insertStudent3Credentials);

        String insertStudent4Credentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('2020CS4@iitrpr.ac.in','iitropar');";

        String insertStudent4 = "INSERT INTO student " +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('4','stu4','2020CS4@iitrpr.ac.in','111','CS','2022');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudent4);
        statement.executeUpdate(insertStudent4Credentials);

        String insertStudent5Credentials = "INSERT INTO student_login" +
                " (email, password) VALUES " +
                " ('2020CS5@iitrpr.ac.in','iitropar');";

        String insertStudent5 = "INSERT INTO student " +
                " (id,name,email,phone_no,department,joining_year) VALUES " +
                " ('5','stu5','2018CS5@iitrpr.ac.in','111','CS','2018');";

        statement=connection.createStatement();
        statement.executeUpdate(insertStudent5);
        statement.executeUpdate(insertStudent5Credentials);

        String insertFaculty1Credentials = "INSERT INTO faculty_login" +
                " (email, password) VALUES " +
                " ('facCS1@iitrpr.ac.in','iitropar');";

        String insertFaculty1 = "INSERT INTO faculty " +
                " (id,name,email,phone_no,department) VALUES " +
                " ('1','fac1','facCS1@iitrpr.ac.in','111','CS');";

        statement=connection.createStatement();
        statement.executeUpdate(insertFaculty1);
        statement.executeUpdate(insertFaculty1Credentials);

        String insertFaculty2Credentials = "INSERT INTO faculty_login" +
                " (email, password) VALUES " +
                " ('facCS2@iitrpr.ac.in','iitropar');";

        String insertFaculty2 = "INSERT INTO faculty " +
                " (id,name,email,phone_no,department) VALUES " +
                " ('2','fac2','facCS2@iitrpr.ac.in','111','CS');";

        statement=connection.createStatement();
        statement.executeUpdate(insertFaculty2);
        statement.executeUpdate(insertFaculty2Credentials);


        String insertCourseCatalog1Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS101','Discrete Math','5','CS','None');";
        String insertCourseCatalog2Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS102','DLD','5','CS','None');";
        String insertCourseCatalog3Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS201','DS','5','CS','None');";
        String insertCourseCatalog4Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS202','Paradigms','5','CS','None');";
        String insertCourseCatalog5Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS301','DB','5','CS','None');";
        String insertCourseCatalog6Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS302','ADA','5','CS','CS201');";
        String insertCourseCatalog7Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS401','BTP1','5','CS','None');";
        String insertCourseCatalog8Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS402','BTP2','5','CS','BTP1');";
        String insertCourseCatalog9Credentials = "INSERT INTO course_catalog" +
                " (course_id,course_name,l_t_p_c,department,pre_requisite) VALUES " +
                " ('CS411','dummyElec','5','CS','CS410');";

        statement=connection.createStatement();
        statement.executeUpdate(insertCourseCatalog1Credentials);
        statement.executeUpdate(insertCourseCatalog2Credentials);
        statement.executeUpdate(insertCourseCatalog3Credentials);
        statement.executeUpdate(insertCourseCatalog4Credentials);
        statement.executeUpdate(insertCourseCatalog5Credentials);
        statement.executeUpdate(insertCourseCatalog6Credentials);
        statement.executeUpdate(insertCourseCatalog7Credentials);
        statement.executeUpdate(insertCourseCatalog8Credentials);
        statement.executeUpdate(insertCourseCatalog9Credentials);

        String insertPassingCriteria2018 = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('2018','5');";
        String insertPassingCriteria2019 = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('2019','40');";
        String insertPassingCriteria2020 = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('2020','40');";
        String insertPassingCriteria2021 = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('2021','40');";
        String insertPassingCriteria2022 = "INSERT INTO passing_criteria" +
                " (joining_year,minimum_credits) VALUES " +
                " ('2022','40');";

        statement=connection.createStatement();
        statement.executeUpdate(insertPassingCriteria2018);
        statement.executeUpdate(insertPassingCriteria2019);
        statement.executeUpdate(insertPassingCriteria2020);
        statement.executeUpdate(insertPassingCriteria2021);
        statement.executeUpdate(insertPassingCriteria2022);

        String insertAcademicCurriculum2018sem1 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2018','1','CS101','1',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem1 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','1','CS101','1',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem2 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','2','CS102','2',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem3 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','3','CS201','1',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem4 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','4','CS202','2',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem5 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','5','CS301','1',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem6 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','6','CS302','2',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem7 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','7','CS401','1',0,'M','CS','5');";
        String insertAcademicCurriculum2019sem8 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2019','8','CS402','2',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem1 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','1','CS101','1',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem2 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','2','CS102','2',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem3 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','3','CS201','1',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem4 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','4','CS202','2',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem5 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','5','CS301','1',0,'M','CS','5');";
        String insertAcademicCurriculum2020sem6 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2020','6','CS302','2',0,'M','CS','5');";
        String insertAcademicCurriculum2021sem1 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2021','1','CS101','1',0,'M','CS','5');";
        String insertAcademicCurriculum2021sem2 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2021','2','CS102','2',0,'M','CS','5');";
        String insertAcademicCurriculum2021sem3 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2021','3','CS201','1',0,'M','CS','5');";
        String insertAcademicCurriculum2021sem4 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2021','4','CS202','2',0,'M','CS','5');";
        String insertAcademicCurriculum2022sem1 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2022','1','CS101','1',0,'M','CS','5');";
        String insertAcademicCurriculum2022sem2 = "INSERT INTO academic_curriculum" +
                " (joining_year,semester_no,course_id,faculty_id,cgpa_constraint,course_type,department,l_t_p_c) VALUES " +
                " ('2022','2','CS102','2',0,'M','CS','5');";

        statement=connection.createStatement();
        statement.executeUpdate(insertAcademicCurriculum2018sem1);
        statement.executeUpdate(insertAcademicCurriculum2019sem1);
        statement.executeUpdate(insertAcademicCurriculum2019sem2);
        statement.executeUpdate(insertAcademicCurriculum2019sem3);
        statement.executeUpdate(insertAcademicCurriculum2019sem4);
        statement.executeUpdate(insertAcademicCurriculum2019sem5);
        statement.executeUpdate(insertAcademicCurriculum2019sem6);
        statement.executeUpdate(insertAcademicCurriculum2019sem7);
        statement.executeUpdate(insertAcademicCurriculum2019sem8);
        statement.executeUpdate(insertAcademicCurriculum2020sem1);
        statement.executeUpdate(insertAcademicCurriculum2020sem2);
        statement.executeUpdate(insertAcademicCurriculum2020sem3);
        statement.executeUpdate(insertAcademicCurriculum2020sem4);
        statement.executeUpdate(insertAcademicCurriculum2020sem5);
        statement.executeUpdate(insertAcademicCurriculum2020sem6);
        statement.executeUpdate(insertAcademicCurriculum2021sem1);
        statement.executeUpdate(insertAcademicCurriculum2021sem2);
        statement.executeUpdate(insertAcademicCurriculum2021sem3);
        statement.executeUpdate(insertAcademicCurriculum2021sem4);
        statement.executeUpdate(insertAcademicCurriculum2022sem1);
        statement.executeUpdate(insertAcademicCurriculum2022sem2);

        String insertEnrollmentStu5CS101 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('5','2018','2','CS101','5','B');";
        String insertEnrollmentStu2CS101 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('2','2020','2','CS101','5','B');";
        String insertEnrollmentStu2CS102 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('2','2021','1','CS102','5','A');";
        String insertEnrollmentStu2CS201 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('2','2021','2','CS201','5','A-');";
        String insertEnrollmentStu2CS202 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('2','2022','1','CS202','5','B');";
        String insertEnrollmentStu2CS301 = "INSERT INTO enrollment" +
                " (student_id,year,semester_no,course_id,l_t_p_c,grade) VALUES " +
                " ('2','2022','2','CS301','5','B-');";

        statement=connection.createStatement();
        statement.executeUpdate(insertEnrollmentStu5CS101);
        statement.executeUpdate(insertEnrollmentStu2CS101);
        statement.executeUpdate(insertEnrollmentStu2CS102);
        statement.executeUpdate(insertEnrollmentStu2CS201);
        statement.executeUpdate(insertEnrollmentStu2CS202);
        statement.executeUpdate(insertEnrollmentStu2CS301);
        String insertFacultyOfferCS101 = "INSERT INTO faculty_offer" +
                " (faculty_id,year,semester_no,course_id) VALUES " +
                " ('2','2023','1','CS102');";

        statement=connection.createStatement();
        statement.executeUpdate(insertFacultyOfferCS101);

    }
}
