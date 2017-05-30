package com.notifyme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 21.04.17.
 */
@Service
public class UserService  {

    @Autowired
    UserRepository userRepository;

    private List<User> users = new ArrayList<User>();

    public User findByFirstName(String firstName) {

        for(User user : users)
        {
            if(user.getFirstName().equals(firstName))
            {
                return user;

            }
        }
        return null;
    }

    public List<User> findByLastName(String lastName) {
        List<User> answer = new ArrayList<User>();

        for(User user : users)
        {
            if(user.getLastName().equals(lastName))
            {
                answer.add(user);
            }
        }
        return answer;
    }

    public List<User> findByProjects(String projects) {

        List<User> answer = new ArrayList<User>();

        for(User user : users)
        {
            for(String project : user.getProjects())
            {
                if(project.equals(projects))
                {
                    answer.add(user);
                    break;
                }
            }
        }
        return answer;
    }

    public void insertUser(User user)
    {
        users.add(user);
        userRepository.save(user);
    }

    public void dropUserCollection()
    {
        users.clear();
        userRepository.deleteAll();
    }
}
