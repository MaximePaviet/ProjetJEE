package models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import models.Teacher;
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

    @Column(name = "name")
    private String name;

    // Si vous souhaitez garder `studentList` comme JSON
    @Column(name = "studentList", columnDefinition = "json")
    @Type(JsonStringType.class)
    private String studentList;

    @ManyToOne
    @JoinColumn(name = "idTeacher", nullable = false) // Clé étrangère vers Teacher
    private Teacher teacher;


    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int id) {
        this.idCourse = id;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
