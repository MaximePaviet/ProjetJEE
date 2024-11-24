package models;


import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAssessment")
    private int idAssessment;

    @Column(name = "name")
    private String name;

    @Column(name = "average")
    private Double average;

    @ManyToOne(optional = false) // Relation obligatoire avec Course
    @JoinColumn(name = "idCourse", nullable = false) // Clé étrangère non nullable
    private Course course;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> gradeList = new ArrayList<>();
    public Assessment() {}

    // Getters et Setters

    public int getIdAssessment() {
        return idAssessment;
    }

    public void setIdAssessment(int idAssessment) {
        this.idAssessment = idAssessment;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}
