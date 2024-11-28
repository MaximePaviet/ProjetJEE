
-- Supprimer les tables si elles existent déjà
DROP TABLE IF EXISTS grade;
DROP TABLE IF EXISTS assessment;
DROP TABLE IF EXISTS student_course;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS administrator;

-- Table teacher
CREATE TABLE teacher (
                         id_teacher INT AUTO_INCREMENT PRIMARY KEY,
                         login VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         surname VARCHAR(255) NOT NULL,
                         contact VARCHAR(255)
);

-- Table student
CREATE TABLE student (
                         id_student INT AUTO_INCREMENT PRIMARY KEY,
                         login VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         surname VARCHAR(255) NOT NULL,
                         dateBirth DATE NOT NULL,
                         contact VARCHAR(255),
                         schoolYear VARCHAR(50) NOT NULL
);

-- Table course
CREATE TABLE course (
                        id_course INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        teacher_id INT NULL,
                        CONSTRAINT fk_teacher_course FOREIGN KEY (teacher_id) REFERENCES teacher(id_teacher) ON DELETE CASCADE
);

-- Table intermédiaire student_course
CREATE TABLE student_course (
                                student_id INT NOT NULL,
                                course_id INT NOT NULL,
                                CONSTRAINT fk_student_course FOREIGN KEY (student_id) REFERENCES student(id_student) ON DELETE CASCADE,
                                CONSTRAINT fk_course_student FOREIGN KEY (course_id) REFERENCES course(id_course) ON DELETE CASCADE,
                                UNIQUE(student_id, course_id)
);

-- Table assessment
CREATE TABLE assessment (
                            id_assessment INT AUTO_INCREMENT PRIMARY KEY, -- Clé primaire auto-incrémentée
                            name VARCHAR(255) NOT NULL,                  -- Nom de l'évaluation (obligatoire)
                            average FLOAT,                               -- Moyenne des notes pour l'évaluation (peut être NULL)
                            idCourse INT NOT NULL,                       -- Clé étrangère vers la table course

    -- Contrainte de clé étrangère
                            CONSTRAINT fk_course_assessment FOREIGN KEY (idCourse) REFERENCES course(id_course) ON DELETE CASCADE
);

-- Table grade
CREATE TABLE grade (
                       id_grade INT AUTO_INCREMENT PRIMARY KEY,      -- Clé primaire auto-incrémentée
                       idStudent INT NOT NULL,                      -- Clé étrangère vers la table student
                       idAssessment INT NOT NULL,                   -- Clé étrangère vers la table assessment
                       grade FLOAT NOT NULL,                        -- Note attribuée (obligatoire)


    -- Contraintes de clé étrangère
                       CONSTRAINT fk_student_grade FOREIGN KEY (idStudent) REFERENCES student(id_student) ON DELETE CASCADE,
                       CONSTRAINT fk_assessment_grade FOREIGN KEY (idAssessment) REFERENCES assessment(id_assessment) ON DELETE CASCADE

);

-- Table administrator
CREATE TABLE administrator (
                               id_administrator INT AUTO_INCREMENT PRIMARY KEY,
                               login VARCHAR(255) NOT NULL UNIQUE,
                               password VARCHAR(255) NOT NULL
);





-- Insérer des données dans 'teacher'
INSERT INTO teacher (id_teacher, login, password, name, surname, contact)
VALUES
    (1, 'sty', 'passw3', 'hola', 'ty', 'ht@example.com');

-- Insérer des données dans 'student'
INSERT INTO student (id_student, login, password, name, surname, dateBirth, contact, schoolYear)
VALUES
    (1, 'sara123', 'pass123', 'Sara', 'Guendouz', '2003-02-12', 'sguendouz@example.com', '2026');

-- Insérer des données dans 'administrator'
INSERT INTO administrator (id_administrator, login, password)
VALUES
    (1, 'admin1', 'adminpass1');

-- Insérer des données dans 'course'
INSERT INTO course (id_course, name, teacher_id)
VALUES
    (1, 'Artificial Intelligence', 1);

-- Insérer des données dans 'student_course'
INSERT INTO student_course (student_id, course_id)
VALUES
    (1, 1);

-- Insérer des données dans 'assessment'
INSERT INTO assessment (id_assessment, name, average, idCourse)
VALUES
    (1, 'Midterm Exam', NULL, 1);

-- Insérer des données dans 'grade'
INSERT INTO grade (id_grade, idStudent, idAssessment, grade)
VALUES
    (1, 1, 1, 18.5);


-- Commit pour enregistrer les modifications
COMMIT;

DESCRIBE administrator;
