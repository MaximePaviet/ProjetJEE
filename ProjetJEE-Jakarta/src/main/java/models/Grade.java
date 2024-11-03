package models;

import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @Column(name = "idGrade", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idStudent")
    private models.Student idStudent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAssessment")
    private Assessment idAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCourse")
    private Course idCourse;

    @Column(name = "grade")
    private Float grade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public models.Student getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(models.Student idStudent) {
        this.idStudent = idStudent;
    }

    public Assessment getIdAssessment() {
        return idAssessment;
    }

    public void setIdAssessment(Assessment idAssessment) {
        this.idAssessment = idAssessment;
    }

    public Course getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Course idCourse) {
        this.idCourse = idCourse;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

}