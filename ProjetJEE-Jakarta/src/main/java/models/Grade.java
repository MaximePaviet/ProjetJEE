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

    @Column(name = "idCourse")
    private int idCourse;

    @Column(name = "grade")
    private double grade;

    @ManyToOne
    @JoinColumn(name = "idAssessment", nullable = false)
    private Assessment assessment;

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

    public Assessment getAssessment(){ return assessment;}

    public void setAssessment(Assessment assessment){ this.assessment = assessment;}

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