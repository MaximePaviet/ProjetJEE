package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetjee.projetjeespringboot.repositories.GradeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    // Lire les grades d'un étudiant pour un cours
    public List<Grade> readGrade(int idStudent, int idCourse) {
        return gradeRepository.findGradesByStudentAndCourse(idStudent, idCourse);
    }

    // Calculer la moyenne d'un étudiant pour un cours
    public float calculateAverage(int idStudent, int idCourse) {
        List<Grade> grades = readGrade(idStudent, idCourse);

        if (grades.isEmpty()) {
            return 0.0f;
        }

        float sum = 0.0f;
        for (Grade grade : grades) {
            sum += grade.getGrade();
        }
        return sum / grades.size();
    }

    // Lire le relevé de notes d'un étudiant
    public List<Grade> readTranscript(int idStudent) {
        List<Integer> courseIds = gradeRepository.findCourseIdsByStudent(idStudent);
        List<Grade> transcript = new ArrayList<>();

        for (Integer courseId : courseIds) {
            transcript.addAll(readGrade(idStudent, courseId));
        }

        return transcript;
    }
}


