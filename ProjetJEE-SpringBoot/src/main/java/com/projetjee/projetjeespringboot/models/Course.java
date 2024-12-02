package com.projetjee.projetjeespringboot.models;


import jakarta.persistence.*;
import jakarta.persistence.Table;


import java.util.List;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private int idCourse;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "courseList")
    private List<Student> studentList;

    @ManyToOne
    @JoinColumn(name = "idTeacher", nullable = true)
    private Teacher teacher;

    public Course(){}

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
