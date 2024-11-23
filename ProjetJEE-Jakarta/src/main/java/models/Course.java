package models;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import models.Teacher;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.util.List;


@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private int idCourse;

    @Column(name = "name")
    private String name;


    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "idStudent"),
            inverseJoinColumns = @JoinColumn(name = "idCourse")
    )
    private List<Student> studentList;


    @ManyToOne
    @JoinColumn(name = "idTeacher", nullable = true ) // Clé étrangère vers Teacher
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

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
