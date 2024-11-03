package models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "assessment")
public class Assessment {
    @Id
    @Column(name = "idAssessment", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCourse")
    private models.Course idCourse;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "average")
    private Float average;

    @Column(name = "gradeList")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> gradeList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public models.Course getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(models.Course idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Map<String, Object> getGradeList() {
        return gradeList;
    }

    public void setGradeList(Map<String, Object> gradeList) {
        this.gradeList = gradeList;
    }

}