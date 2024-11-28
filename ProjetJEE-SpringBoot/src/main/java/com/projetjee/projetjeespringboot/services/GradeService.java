package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.GradeRepository;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    // Méthode pour lire les notes d'un étudiant pour un cours spécifique
    public List<Grade> readGrade(int idStudent, int idCourse) {
        return gradeRepository.findByStudentAndCourse(idStudent, idCourse);
    }

    // Méthode pour calculer la moyenne des notes d'un étudiant pour un cours spécifique
    public float calculateAverage(int idStudent, int idCourse) {
        List<Grade> grades = gradeRepository.findByStudentAndCourse(idStudent, idCourse);
        if (grades.isEmpty()) {
            return 0.0f;
        }

        float sum = 0.0f;
        for (Grade grade : grades) {
            sum += grade.getGrade();  // Additionner toutes les notes
        }
        return sum / grades.size();  // Retourner la moyenne
    }

    // Méthode pour récupérer le relevé de notes complet d'un étudiant
    @Transactional
    public List<Grade> readTranscript(int idStudent) {
        List<Grade> transcript = gradeRepository.findByStudent(idStudent);
        return transcript;  // Retourner toutes les notes de l'étudiant
    }
}

