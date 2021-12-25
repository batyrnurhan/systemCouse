package com.example.systemcouse.database;

import com.example.systemcouse.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        for(int i=1; i<10; i++) {
            Task task = new Task();
            task.setTaskName("Task Name " + i);

            entityManager.persist(task);
        }

        List<Score> scores = new ArrayList<>();
        for(int i=1; i<10; i++) {
            Score score = new Score();
            score.setScoreName("Score Name " + i);
            scores.add(score);
            entityManager.persist(score);
        }

        List<User_> userList = new ArrayList<>();
        for(int i=0; i<scores.size(); i++) {
            User_ user = new User_();
            user.setUserName("User Name " + (i+1));
            user.setScore(scores.get(i));
            user.setLastSession(LocalDate.of(2021, 10, i+20));
            userList.add(user);
            entityManager.persist(user);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
