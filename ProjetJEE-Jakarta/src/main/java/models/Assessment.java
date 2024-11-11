package models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;


@Entity
@Table(name="assessment")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAssessment")
    private int idAssessment;

    @Column(name = "idCourse")
    private int idCourse;

    @Column(name = "name")
    private String name;

    @Column(name = "average")
    private double average;

    @Column(name = "gradeList", columnDefinition = "json")
    @Type(JsonStringType.class)
    private String gradeList;

    // Getters et Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getGradeList() {
        return gradeList;
    }

    public void setGradeList(String gradeList) {
        this.gradeList = gradeList;
    }
}

