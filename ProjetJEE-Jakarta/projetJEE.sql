-- Supprimer les tables si elles existent déjà
DROP TABLE IF EXISTS grade;
DROP TABLE IF EXISTS assessment;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS administrator;

-- Créer la table 'teacher'
CREATE TABLE teacher (
                         idTeacher INT PRIMARY KEY,
                         name VARCHAR(50),
                         surname VARCHAR(50),
                         contact VARCHAR(50),
                         login VARCHAR(50),
                         password VARCHAR(50),
                         courseList JSON
);

-- Créer la table 'student'
CREATE TABLE student (
                         idStudent INT PRIMARY KEY,
                         name VARCHAR(50),
                         surname VARCHAR(50),
                         dateBirth DATE,
                         contact VARCHAR(50),
                         schoolYear VARCHAR(50),
                         login VARCHAR(50),
                         password VARCHAR(50),
                         courseList JSON
);

-- Créer la table 'administrator'
CREATE TABLE administrator (
                               idAdministrator INT PRIMARY KEY,
                               login VARCHAR(50),
                               password VARCHAR(50)
);

-- Créer la table 'course'
CREATE TABLE course (
                        idCourse INT PRIMARY KEY,
                        idTeacher INT,
                        name VARCHAR(50),
                        studentList JSON,
                        FOREIGN KEY (idTeacher) REFERENCES teacher(idTeacher)
);

-- Créer la table 'assessment'
CREATE TABLE assessment (
                            idAssessment INT PRIMARY KEY,
                            idCourse INT,
                            name VARCHAR(50),
                            average FLOAT,
                            gradeList JSON,
                            FOREIGN KEY (idCourse) REFERENCES course(idCourse)
);

-- Créer la table 'grade'
CREATE TABLE grade (
                       idGrade INT PRIMARY KEY,
                       idStudent INT,
                       idAssessment INT,
                       idCourse INT,
                       grade FLOAT,
                       FOREIGN KEY(idStudent) REFERENCES Student(idStudent),
                       FOREIGN KEY(idAssessment) REFERENCES Assessment(idAssessment),
                       FOREIGN KEY(idCourse) REFERENCES Course(idCourse)
);

-- Insérer des données dans 'teacher'
INSERT INTO teacher VALUES (1, 'Forest', 'Jean-Paul','jpforest@cy-tech.fr','jpforest','ABCDEFGHIJ',null);

-- Insérer des données dans 'student'
INSERT INTO student VALUES (1, 'Sara', 'Guendouz', '2003-02-12', 'sguendouz@cy-tech.fr','2026','sguendouz','ABCDEFGHIJ',null);

-- Insérer des données dans 'administrator'
INSERT INTO administrator VALUES (1, 'admin', 'admin');

-- Insérer des données dans 'course'
INSERT INTO course VALUES (1, 1, 'IA',null);

-- Insérer des données dans 'assessment'
INSERT INTO assessment VALUES (1, 1, 'QCM 1',null,null);

-- Insérer des données dans 'grade'
INSERT INTO grade VALUES (1, 1, 1,1,19.0);
-- Commit pour s'assurer que tout est bien enregistré
COMMIT;


/*
UPDATE teacher
SET courseList = JSON_ARRAY_APPEND(courseList, '$', 3)
WHERE idTeacher = 1;
*/
