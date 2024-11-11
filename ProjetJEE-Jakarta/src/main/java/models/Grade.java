package models;

import jakarta.persistence.*;

import java.beans.ConstructorProperties;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGrade")
    private int idGrade;

    @Column(name = "idStudent")
    private int idStudent;

    @Column(name = "idAssessment")
    private int idAssessment;

    @Column(name = "idCourse")
    private int idCourse;

    @Column(name = "grade")
    private double grade;

    // Getters et Setters

    public int getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(int id) { this.idGrade = id; }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getIdAssessment() {
        return idAssessment;
    }

    public void setIdAssessment(int idAssessment) {
        this.idAssessment = idAssessment;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

}