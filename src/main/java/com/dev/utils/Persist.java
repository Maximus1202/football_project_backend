package com.dev.utils;
import com.dev.objects.Group;
import com.dev.objects.Match;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {

    private Connection connection;
    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            if (checkIfTableEmpty()) {
                initGroups();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Match> getAllMatches() {
        return sessionFactory.openSession().createQuery("from Match")
                .list();
    }
    public List<Match> getMatchesFinished() {
        System.out.println(sessionFactory.openSession().createQuery("from Match where isLive =: false")
                .setParameter("false", false).list());
        return  sessionFactory.openSession().createQuery("from Match where isLive =: false")
                .setParameter("false", false).list();

    }
    public boolean finishMatch(String team1) {
        int matchId = (Integer) sessionFactory.openSession().createQuery("select id from Match where team1 = : team1")
                .setParameter("team1",team1).list().get(0);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Match match =  session.get(Match.class, matchId);
        match.setLive(false);
        session.update(match);
        tx.commit();
        return match.isLive();
    }
    public void updateGoal(String team1, int updateGoals1,String team2, int updateGoals2) {

        int matchId = (Integer) sessionFactory.openSession().createQuery("select id from Match where team1 = : team1")
                .setParameter("team1", team1).list().get(0);


        List list1 = sessionFactory.openSession().createQuery("select team1 from Match where id = : matchId and team1=:team")
                .setParameter( "matchId",matchId).setParameter("team",team1).list();

        List list = sessionFactory.openSession().createQuery("select team2 from Match where team2 = : team2")
                .setParameter("team2", team2).list();

        String team2comp= list.get(0).toString();
        String team1comp= list1.get(0).toString();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("update Match set team1Goals=:updateGoals1 , team2Goals=:updateGoals2 " +
                        "where team1= :team1 AND team2= :team2 AND isLive = true ")
                .setParameter("team1",team1comp)
                .setParameter("team2", team2comp)
                .setParameter("updateGoals1", updateGoals1)
                .setParameter("updateGoals2", updateGoals2)
                .executeUpdate();
        transaction.commit();
        session.close();
    }

    private boolean checkIfTableEmpty() {
        boolean empty = false;
        List<Group> groups = sessionFactory.openSession()
                .createQuery("FROM Group").list();
        if (groups.isEmpty()) {
            empty = true;
        }
        return empty;
    }

    private void initGroups() {

        List<Group> club = new ArrayList<>();
        club.add(new Group("PSJ"));
        club.add(new Group("Barcelona"));
        club.add(new Group("Real-Madrid"));
        club.add(new Group("Bayern-Munchen"));
        club.add(new Group("Napoli"));
        club.add(new Group("Liverpool"));
        club.add(new Group("Manchester"));
        club.add(new Group("Arsenal"));
        club.add(new Group("Milan"));
        club.add(new Group("Benfica"));
        club.add(new Group("Inter"));
        club.add(new Group("Uventus"));

        for (Group group : club) {
            sessionFactory.openSession().save(group);
        }


    }
    public List<Group> getGroups() {
        return sessionFactory.openSession().createQuery("from Group ").list();
    }
    public boolean usernameAvailableH(String username) {
        boolean available = true;
        List<UserObject> userObjects = sessionFactory.openSession()
                .createQuery("from UserObject where username =: username")
                .setParameter("username", username).list();

        if (userObjects.size() > 0) {
            available = false;
        }
        return available;
    }

    public UserObject getUserByTokenH(String token) {
        UserObject userObject = null;
        List<UserObject> userObjectList = sessionFactory.openSession()
                .createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .list();

        if (userObjectList.size() > 0) {
            userObject = userObjectList.get(0);
        }
        return userObject;
    }
    public void saveUser(UserObject userObject) {
        sessionFactory.openSession()
                .save(userObject);
    }
    public void addLiveGameH (Match match) {
        sessionFactory.openSession().save(match);
    }
    public String getUserByCredsH(String username, String token) {
        String response = null;
        List<UserObject> userObjectList = sessionFactory.openSession()
                .createQuery("from UserObject where username =: username and token = : token")
                .setParameter("username", username).setParameter("token", token).list();
        if (userObjectList.size() > 0) {
            UserObject userObject = userObjectList.get(0);
            response = userObject.getToken();
        }
        return response;
    }
    public List<Match> getLiveMatches() {
        return sessionFactory.openSession().createQuery("from Match where isLive =:true")
                .setParameter("true", true)
                .list();
    }


}