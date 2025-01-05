package com.andrea.dao;

import com.andrea.dto.LessonListPresenceStudentDto;
import com.andrea.exception.NotSubjectException;
import com.andrea.model.Lesson;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LessonDao {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    //TODO aggiungere eccezione per lezione gia esistente
    public Lesson addLesson(Lesson lessons) throws NotSubjectException {
        String addLesson = "INSERT INTO lesson (id_course, id_subject, lesson_date, hour_start, hour_end, classroom, title, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String checkSubjectQuery = "SELECT 1 FROM subject WHERE id_subject = ?";
        String getAllAccountCourse = "SELECT id_account FROM Enrolled WHERE id_course = ?";
        String addPresence = "INSERT INTO presence (id_account, id_lesson) VALUES (?, ?)";

        try {
            PreparedStatement checkSubject = connection.prepareStatement(checkSubjectQuery);
            checkSubject.setInt(1, lessons.getId_subject());
            ResultSet rs = checkSubject.executeQuery();


            if (!rs.next()) {
                throw new NotSubjectException("Subject with id " + lessons.getId_subject() + " not found.");
            }


            PreparedStatement newLesson = connection.prepareStatement(addLesson, Statement.RETURN_GENERATED_KEYS);

            newLesson.setInt(1, lessons.getId_course());
            newLesson.setInt(2, lessons.getId_subject());
            newLesson.setObject(3, lessons.getLesson_date());
            newLesson.setTime(4, java.sql.Time.valueOf(lessons.getHour_start()));
            newLesson.setTime(5, java.sql.Time.valueOf(lessons.getHour_end()));
            newLesson.setString(6, lessons.getClassroom());
            newLesson.setString(7, lessons.getTitle());
            newLesson.setString(8, lessons.getDescription());

            int affectedRows = newLesson.executeUpdate();
            int generatedLessonId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newLesson.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedLessonId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Lesson creation failed, no rows added");
            }

            //Recuperare gli account iscritti al corso
            PreparedStatement getAccounts = connection.prepareStatement(getAllAccountCourse);
            getAccounts.setInt(1, lessons.getId_course());
            ResultSet accountResults = getAccounts.executeQuery();

            PreparedStatement insertPresence = connection.prepareStatement(addPresence);
            while (accountResults.next()) {
                int idAccount = accountResults.getInt("id_account");
                insertPresence.setInt(1, idAccount);
                insertPresence.setInt(2, generatedLessonId);
                insertPresence.addBatch();
            }
            insertPresence.executeBatch();

            return new Lesson(generatedLessonId, lessons.getId_course(), lessons.getId_subject(), lessons.getTitle(), lessons.getDescription(), lessons.getLesson_date(), lessons.getHour_start(), lessons.getHour_end(), lessons.getClassroom());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteLesson(int id_lesson) {
        String removeLesson = "DELETE FROM lesson WHERE id_lesson = ?";

        try {
            PreparedStatement deleteLesson = connection.prepareStatement(removeLesson);
            deleteLesson.setInt(1, id_lesson);

            int affectedRows = deleteLesson.executeUpdate();

            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting lesson with id " + id_lesson, e);
        }
    }

    public Lesson updateLesson(Lesson lessons) {
        String updateLesson = """
        UPDATE lesson
        SET id_course = ?, lesson_date = ?, hour_start = ?, hour_end = ?, classroom = ?, title = ?, description = ?
        WHERE id_lesson = ?;
    """;

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateLesson);


            updateStatement.setInt(1, lessons.getId_course());
            updateStatement.setObject(2, lessons.getLesson_date());
            updateStatement.setTime(3, java.sql.Time.valueOf(lessons.getHour_start()));
            updateStatement.setTime(4, java.sql.Time.valueOf(lessons.getHour_end()));
            updateStatement.setString(5, lessons.getClassroom());
            updateStatement.setString(6, lessons.getTitle());
            updateStatement.setString(7, lessons.getDescription());
            updateStatement.setInt(8, lessons.getId_lesson());

            int affectedRows = updateStatement.executeUpdate();

            if (affectedRows > 0) {

                return getLessonById(lessons.getId_lesson());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating lesson with id " + lessons.getId_lesson(), e);
        }
    }

    public Lesson getLessonById(int id_lesson) {
        String getLessonQuery = "SELECT * FROM lesson WHERE id_lesson = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLessonQuery)) {
            preparedStatement.setInt(1, id_lesson);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return new Lesson(
                        resultSet.getInt("id_lesson"),
                        resultSet.getInt("id_course"),
                        resultSet.getInt("id_subject"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("lesson_date").toLocalDate(),
                        resultSet.getTime("hour_start").toLocalTime(),
                        resultSet.getTime("hour_end").toLocalTime(),
                        resultSet.getString("classroom")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lesson with id " + id_lesson, e);
        }
    }

    public List<Lesson> getLessonsByCourseId(int courseId) {
        String getLessonsQuery = """
            SELECT *
            FROM lesson
            WHERE id_course = ?
            ORDER BY lesson_date;""";

        List<Lesson> lessons = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLessonsQuery)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesson lesson = new Lesson();
                lesson.setId_lesson(resultSet.getInt("id_lesson"));
                lesson.setId_course(resultSet.getInt("id_course"));
                lesson.setLesson_date(resultSet.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(resultSet.getTime("hour_start").toLocalTime());
                lesson.setHour_end(resultSet.getTime("hour_end").toLocalTime());
                lesson.setClassroom(resultSet.getString("classroom"));
                lesson.setTitle(resultSet.getString("title"));
                lesson.setDescription(resultSet.getString("description"));
                lesson.setId_subject(resultSet.getInt("id_subject"));

                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lessons for course with id " + courseId, e);
        }

        return lessons;
    }

    public List<Lesson> getLessonsBySubjectId(int subjectId) {
        String getLessonsQuery = "SELECT * FROM lesson WHERE id_subject = ? ORDER BY lesson_date ASC;";

        List<Lesson> lessons = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLessonsQuery)) {
            preparedStatement.setInt(1, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesson lesson = new Lesson();
                lesson.setId_lesson(resultSet.getInt("id_lesson"));
                lesson.setId_course(resultSet.getInt("id_course"));
                lesson.setLesson_date(resultSet.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(resultSet.getTime("hour_start").toLocalTime());
                lesson.setHour_end(resultSet.getTime("hour_end").toLocalTime());
                lesson.setClassroom(resultSet.getString("classroom"));
                lesson.setTitle(resultSet.getString("title"));
                lesson.setDescription(resultSet.getString("description"));
                lesson.setId_subject(resultSet.getInt("id_subject"));

                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lessons for subject with id " + subjectId, e);
        }

        return lessons;
    }

    public List<Lesson> getPrevLessons(int id_course, boolean flag) {
        String query = "";
        if (flag) {
            query = """
                SELECT id_lesson, id_course, lesson_date, hour_start, hour_end, classroom, title, description, id_subject
                FROM lesson
                WHERE id_course = ? AND lesson_date >= CURRENT_DATE
                ORDER BY lesson_date ASC, hour_start ASC
                LIMIT 5;
            """;
        } else {
            query = """
                SELECT id_lesson, id_course, lesson_date, hour_start, hour_end, classroom, title, description, id_subject
                FROM lesson
                WHERE id_course = ? AND lesson_date < CURRENT_DATE
                ORDER BY lesson_date DESC, hour_start DESC
                LIMIT 5;
            """;
        }

        List<Lesson> lessons = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_course);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesson lesson = new Lesson();

                lesson.setId_lesson(resultSet.getInt("id_lesson"));
                lesson.setId_course(resultSet.getInt("id_course"));
                lesson.setLesson_date(resultSet.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(resultSet.getTime("hour_start").toLocalTime());
                lesson.setHour_end(resultSet.getTime("hour_end").toLocalTime());
                lesson.setClassroom(resultSet.getString("classroom"));
                lesson.setTitle(resultSet.getString("title"));
                lesson.setDescription(resultSet.getString("description"));
                lesson.setId_subject(resultSet.getInt("id_subject"));

                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving last lessons", e);
        }

        return lessons;
    }

    public List<Lesson> get5LessonsBySubject(int id_subject, boolean next) {
        List<Lesson> lessons = new ArrayList<>();
        String getNextLessonsQuery = "";
        if (next) {
            getNextLessonsQuery = "SELECT * FROM lesson WHERE id_subject = ? AND lesson_date >= CURRENT_DATE ORDER BY lesson_date ASC, hour_start ASC LIMIT 5";
        } else {
            getNextLessonsQuery = "SELECT * FROM lesson WHERE id_subject = ? AND lesson_date < CURRENT_DATE ORDER BY lesson_date DESC, hour_start DESC LIMIT 5";
        }

        try {
            // Prepara la statement per ottenere le lezioni
            PreparedStatement statement = connection.prepareStatement(getNextLessonsQuery);
            statement.setInt(1, id_subject);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Lesson lesson = new Lesson();

                lesson.setId_lesson(resultSet.getInt("id_lesson"));
                lesson.setId_course(resultSet.getInt("id_course"));
                lesson.setLesson_date(resultSet.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(resultSet.getTime("hour_start").toLocalTime());
                lesson.setHour_end(resultSet.getTime("hour_end").toLocalTime());
                lesson.setClassroom(resultSet.getString("classroom"));
                lesson.setTitle(resultSet.getString("title"));
                lesson.setDescription(resultSet.getString("description"));
                lesson.setId_subject(resultSet.getInt("id_subject"));

                lessons.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching lessons", e);
        }

        return lessons;
    }

    public List<LessonListPresenceStudentDto> get5LastLessonByAccount(int id_account, boolean limit) {
        String query = "";
        if (limit) {
            query = """
                    SELECT
                        L.id_lesson,
                        L.title,
                        L.lesson_date,
                        L.hour_start,
                        L.hour_end,
                        P.presence
                    FROM
                        Lesson L
                    INNER JOIN
                        Enrolled E ON L.id_course = E.id_course
                    LEFT JOIN
                        Presence P ON L.id_lesson = P.id_lesson AND P.id_account = ?
                    WHERE
                        E.id_account = ?
                        AND L.lesson_date <= CURRENT_DATE
                    ORDER BY
                        L.lesson_date ASC
                    LIMIT 5;
                    """;
        } else {
            query = """
                    SELECT
                        L.id_lesson,
                        L.title,
                        L.lesson_date,
                        L.hour_start,
                        L.hour_end,
                        P.presence
                    FROM
                        Lesson L
                    INNER JOIN
                        Enrolled E ON L.id_course = E.id_course
                    LEFT JOIN
                        Presence P ON L.id_lesson = P.id_lesson AND P.id_account = ?
                    WHERE
                        E.id_account = ?
                        AND L.lesson_date <= CURRENT_DATE
                    ORDER BY
                        L.lesson_date DESC
                    
                    """;
        }
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id_account);
            stm.setInt(2, id_account);

            ResultSet rs = stm.executeQuery();

            List<LessonListPresenceStudentDto> list = new ArrayList<>();
            while (rs.next()) {
                LessonListPresenceStudentDto lesson = new LessonListPresenceStudentDto();
                lesson.setId_lesson(rs.getInt("id_lesson"));
                lesson.setTitle(rs.getString("title"));
                lesson.setLesson_date(rs.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(rs.getTime("hour_start").toLocalTime());
                lesson.setHour_end(rs.getTime("hour_end").toLocalTime());
                lesson.setPresence(rs.getBoolean("presence"));

                list.add(lesson);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<LessonListPresenceStudentDto> getLessonByAccountByDate(int id_account, LocalDate date) {
        String queryListLesson = """
                SELECT
                    L.id_lesson,
                    L.title,
                    L.description,
                    L.lesson_date,
                    L.hour_start,
                    L.hour_end,
                    L.classroom,
                    L.id_subject,
                    P.presence
                FROM
                    Lesson L
                INNER JOIN
                    Enrolled E ON L.id_course = E.id_course
                LEFT JOIN
                    Presence P ON L.id_lesson = P.id_lesson AND P.id_account = ?
                WHERE
                    E.id_account = ?
                    AND L.lesson_date = ?
                ORDER BY
                    L.lesson_date DESC;
                """;
        String queryNameSubject = "SELECT name FROM subject WHERE id_subject = ?";

        try {

            List<LessonListPresenceStudentDto> list = new ArrayList<>();
            PreparedStatement stmListLesson = connection.prepareStatement(queryListLesson);
            stmListLesson.setInt(1, id_account);
            stmListLesson.setInt(2, id_account);
            stmListLesson.setObject(3, date);

            ResultSet rs = stmListLesson.executeQuery();

            while (rs.next()) {
                LessonListPresenceStudentDto lesson = new LessonListPresenceStudentDto();
                lesson.setId_lesson(rs.getInt("id_lesson"));
                lesson.setTitle(rs.getString("title"));
                lesson.setDescription(rs.getString("description"));
                lesson.setLesson_date(rs.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(rs.getTime("hour_start").toLocalTime());
                lesson.setHour_end(rs.getTime("hour_end").toLocalTime());
                lesson.setClassroom(rs.getString("classroom"));

                PreparedStatement stmNameSub = connection.prepareStatement(queryNameSubject);
                stmNameSub.setInt(1, rs.getInt("id_subject"));

                ResultSet rs1 = stmNameSub.executeQuery();
                if (rs1.next()) {
                    lesson.setName_subject(rs1.getString("name"));
                }

                list.add(lesson);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LessonListPresenceStudentDto> getLessonBySubjectByDate(int id_subject, LocalDate date) {
        String queryListLesson = """
                SELECT
                	L.id_lesson,
                	L.title,
                	L.description,
                	L.lesson_date,
                	L.hour_start,
                	L.hour_end,
                	L.classroom,
                	L.id_subject
                FROM
                	Lesson L
                WHERE
                	L.id_subject = ? 
                	AND L.lesson_date = ?
                """;
        String queryNameSubject = "SELECT name FROM subject WHERE id_subject = ?";

        try {
            List<LessonListPresenceStudentDto> listLesson = new ArrayList<>();
            PreparedStatement stmListLesson = connection.prepareStatement(queryListLesson);

            stmListLesson.setInt(1, id_subject);
            stmListLesson.setObject(2, date);
            ResultSet rs = stmListLesson.executeQuery();

            while (rs.next()) {
                LessonListPresenceStudentDto lesson = new LessonListPresenceStudentDto();
                lesson.setId_lesson(rs.getInt("id_lesson"));
                lesson.setTitle(rs.getString("title"));
                lesson.setLesson_date(rs.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(rs.getTime("hour_start").toLocalTime());
                lesson.setHour_end(rs.getTime("hour_end").toLocalTime());
                lesson.setClassroom(rs.getString("classroom"));

                PreparedStatement stmNameCourse = connection.prepareStatement(queryNameSubject);
                stmNameCourse.setInt(1, id_subject);
                ResultSet rs1 = stmNameCourse.executeQuery();

                if (rs1.next()) {
                    lesson.setName_subject(rs1.getString("name"));
                }

                listLesson.add(lesson);
            }

            return listLesson;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
