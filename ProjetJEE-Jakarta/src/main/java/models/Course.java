package models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private int idCourse;

    @Column(name = "idTeacher")
    private int idTeacher;

    @Column(name = "name")
    private String name;

    @Column(name = "studentList", columnDefinition= "json")
    @Type(JsonStringType.class)
    private String studentList;

    // Getters et Setters

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int id) { this.idCourse = id; }

    public int getIdTeacher() { return idTeacher; }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentList() {
        return studentList;
    }

    public void setStudentList(String studentList) {
        this.studentList = studentList;
    }

}