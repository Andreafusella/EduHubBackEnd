CREATE TABLE Account (
    id_account SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE Credential (
    id_account INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_account FOREIGN KEY (id_account) 
        REFERENCES Account(id_account)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE SettingAccount (
    id_account INT PRIMARY KEY,
    avatar INT,
    CONSTRAINT fk_account FOREIGN KEY (id_account) 
        REFERENCES Account(id_account)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Course (
    id_course SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    date_start DATE,
    date_finish DATE
);

CREATE TABLE Enrolled (
    id_account INT,
    id_course INT,
    enrollment_date DATE,
    PRIMARY KEY (id_account, id_course),
    FOREIGN KEY (id_account) REFERENCES Account(id_account) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_course) REFERENCES Course(id_course) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Subject (
    id_subject SERIAL PRIMARY KEY,
    name VARCHAR(255),
    id_course INT,
    id_teacher INT,
    FOREIGN KEY (id_course) REFERENCES Course(id_course) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_teacher) REFERENCES Account(id_account) ON UPDATE CASCADE
);

CREATE TABLE Lesson (
    id_lesson SERIAL PRIMARY KEY,
    id_course INT,
    lesson_date DATE,
    hour_start TIME,
    hour_end TIME,
    classroom VARCHAR(255),
    title VARCHAR(255),
    description VARCHAR(255),
    id_subject INT,
    FOREIGN KEY (id_course) REFERENCES Course(id_course) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_subject) REFERENCES Subject(id_subject) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Presence (
    id_account INT,
    id_lesson INT,
    presence BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_account, id_lesson),
    FOREIGN KEY (id_account) REFERENCES Account(id_account) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_lesson) REFERENCES Lesson(id_lesson) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Quiz (
    id_quiz SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date DATE NOT NULL,
    id_subject INT,
    FOREIGN KEY (id_subject) REFERENCES Subject(id_subject) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Question (
    id_question SERIAL PRIMARY KEY,
    texta VARCHAR(255) NOT NULL,
    textb VARCHAR(255) NOT NULL,
    textc VARCHAR(255) NOT NULL,
    textd VARCHAR(255) NOT NULL,
    right_answer VARCHAR(255) NOT NULL,
    id_quiz INT,
    question VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_quiz) REFERENCES Quiz(id_quiz) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Score (
    id_score SERIAL PRIMARY KEY,
    id_account INT NOT NULL,
    id_quiz INT NOT NULL,
    score INT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account FOREIGN KEY (id_account) REFERENCES Account (id_account) ON DELETE CASCADE,
    CONSTRAINT fk_quiz FOREIGN KEY (id_quiz) REFERENCES Quiz (id_quiz) ON DELETE CASCADE
);