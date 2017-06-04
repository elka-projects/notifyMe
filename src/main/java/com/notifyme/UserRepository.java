package com.notifyme;

/**
 * Created by gepard on 20.04.17.
 */

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByLoginAndPassword(String login, String password);
    public User findByLogin(String login);
    public User findByFirstName(String firstName);
    public List<User> findByLastName(String lastName);
    public List<User> findByProjects(String projects);
    public User findById(String id);
}
