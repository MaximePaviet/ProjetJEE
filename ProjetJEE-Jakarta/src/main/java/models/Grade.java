package models;

import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGrade")
    private int idGrade;

    @Column(name = "grade")
    private double grade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idAssessment", nullable = false)
    private Assessment assessment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idStudent", nullable = false)
    private Student student;

    // Getters et Setters

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
