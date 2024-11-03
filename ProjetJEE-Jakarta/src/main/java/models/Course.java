package models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "idCourse", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTeacher")
    private models.Teacher idTeacher;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "studentList")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> studentList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public models.Teacher getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(models.Teacher idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getStudentList() {
        return studentList;
    }

    public void setStudentList(Map<String, Object> studentList) {
        this.studentList = studentList;
    }

}