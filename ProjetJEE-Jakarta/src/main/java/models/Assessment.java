package models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> gradeList = new ArrayList<>();

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

    public List<Grade> getGradeList(){ return gradeList; }

    public void setGradeList(List<Grade> gradeList){ this.gradeList = gradeList; }
}

