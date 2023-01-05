package com.dev.controllers;

import com.dev.objects.Group;
import com.dev.objects.Match;
import com.dev.objects.UserObject;
import com.dev.responses.BasicResponse;
import com.dev.responses.SignInResponse;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;
// new one c

@RestController
public class TestController {
    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/update-goals", method = RequestMethod.POST)
    public int updateGoals(String team1, int team1Goals,String team2, int team2Goals) {
        persist.updateGoal(team1, team1Goals,team2, team2Goals);
        return team1Goals;
    }

    @RequestMapping(value = "/get-all-matches", method = RequestMethod.GET)
    public List<Match> getAllMatches() {
        return persist.getAllMatches();
    }

    @RequestMapping(value = "/finish-match", method = RequestMethod.POST)
    public boolean getStaticTable(String team1) {
        return persist.finishMatch(team1);
    }

    @RequestMapping(value = "/get-groups", method = RequestMethod.GET)
    public List<Group> getStaticTable() {
        return persist.getGroups();
    }

    @RequestMapping(value = "/get-finished-matches", method = RequestMethod.GET)
    public List<Match> getFinishedMatches() {
        return persist.getMatchesFinished();
    }



    @RequestMapping(value = "/save-match", method = RequestMethod.POST)
    public BasicResponse saveMatch(String team1, String team2) {
        BasicResponse basicResponse = null;
        Match match = new Match(team1, team2);
        persist.addLiveGameH(match);
        basicResponse = new BasicResponse(true, null);
        return basicResponse;
    }

    @RequestMapping(value = "/get-live-games", method = RequestMethod.GET)
    public List<Match> getLiveGames() {
        return persist.getLiveMatches();
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public BasicResponse signIn(String username, String password) {
        BasicResponse basicResponse = null;
        String token = createHash(username, password);
        token = persist.getUserByCredsH(username, token);
        if (token == null) {
            if (persist.usernameAvailableH(username)) {
                basicResponse = new BasicResponse(false, 1);
            } else {
                basicResponse = new BasicResponse(false, 2);
            }
        } else {
            UserObject user = persist.getUserByTokenH(token);
            basicResponse = new SignInResponse(true, null, user);
        }
        return basicResponse;
    }


    @RequestMapping(value = "/create-account", method = {RequestMethod.GET, RequestMethod.POST})
    public UserObject createAccount(String username, String password) {
        UserObject newAccount = null;
        if (utils.validateUsername(username)) {
            if (utils.validatePassword(password)) {
                if (persist.usernameAvailableH(username)) {
                    String token = createHash(username, password);
                    newAccount = new UserObject(username, token);
                    persist.saveUser(newAccount);
                } else {
                    System.out.println("username already exits");
                }
            } else {
                System.out.println("password is invalid");
            }
        } else {
            System.out.println("username is invalid");
        }
        return newAccount;
    }
    public String createHash(String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }


}