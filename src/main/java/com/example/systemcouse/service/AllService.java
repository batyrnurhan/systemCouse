package com.example.systemcouse.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.*;
import java.util.List;
import com.example.systemcouse.cache.MyCache;
import com.example.systemcouse.model.*;
import com.example.systemcouse.repository.*;


@Stateless
public class AllService {

    @EJB
    private ScoreRepository scoreRepository;

    @EJB
    private TaskRepository taskRepository;

    @EJB
    private UserRepository userRepository;

    @Resource(name = "messageQueue")
    private Queue messageQueue;

    @Resource(name = "commonQueue")
    private Queue commonQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    private MyCache UserCache;
    private MyCache TaskCache;
    private MyCache ScoreCache;

    //Task//

    public List<Task> selectTasks() {
        return taskRepository.selectTasks();
    }

    public Task selectTask(int taskId){
        return taskRepository.selectTask(taskId);
    }

    public Task insertTask(Task task){
        return taskRepository.insertTask(task);
    }

    public boolean deleteTask(int id) {
        return taskRepository.deleteMap(id);
    }

    public boolean updateTask(Task task) {
        return taskRepository.updateTask(task);
    }


    //Score//
    public List<Score> selectScores() {
        return scoreRepository.selectScores();
    }

    public Score selectScore(int scoreId) {return scoreRepository.selectScore(scoreId);}

    public Score insertScore(Score score) {
        return scoreRepository.insertScore(score);
    }

    public boolean deleteScore(int id) {
        return scoreRepository.deleteScore(id);
    }

    public boolean updateScore(Score score) {
        return scoreRepository.updateScore(score);
    }


    //USER//
    public User_ insertUser(User_ user){
        return userRepository.insertUser(user);
    }

    public List<User_> selectUsers() {
        return userRepository.selectUsers();
    }

    public User_ selectUser(int userId)  {
        return userRepository.selectUser(userId);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }

    public boolean updateUser(User_ user){
        return userRepository.updateUser(user);
    }


    //JMS//
    public String sendJmsMessage(String message) {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(messageQueue);
             final MessageProducer producer2 = session.createProducer(commonQueue)) {
            connection.start();
            final Message jmsMessage = session.createTextMessage(message);
            producer.send(jmsMessage);
            producer2.send(jmsMessage);
            return "successfylly sended";
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when sending a message", e);
        }
    }

    public String getMessage() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(messageQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                return "jmsMessage is null";
            }

            TextMessage textMessage = (TextMessage) jmsMessage;
            if (textMessage == null) {
                return "Empty textMessage";
            }
            return textMessage.getText();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }

    public Object getCommon() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(commonQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                return "jmsMessage is null";
            }

            TextMessage textMessage = (TextMessage) jmsMessage;
            if (textMessage == null) {
                return "Empty textMessage";
            }
            return textMessage.getText();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }
}
