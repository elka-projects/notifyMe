package com.notifyme

import org.springframework.beans.factory.annotation.Autowired

import spock.lang.Specification

/**
 * Created by marcin on 21.04.17.
 */

class UserServiceTest extends Specification{

    //@Autowired
    UserService userService
    def userRepository = Mock(UserRepository)


    def setup()
    {
        userService = new UserService()
        userService.userRepository = userRepository
        userService.dropUserCollection()
    }

    def "insert user"()
    {
        setup:"Create new user"

        User user = new User("Karol","Polak")
        user.addProject("Pik")
        user.addProject("Iop")

        when:"New user is inserted to base"
        userService.insertUser(user)
        //userRepository.insert(user)
        then:"User account is saved"
        //def users = userService.findByLastName("Polak")
        //users.size() == 1
        1*userRepository.save(user)
    }

    def"find all users in the same Pik project"()
    {
        setup:"3 users are created"
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")
        user1.addProject("Iop")

        User user2 = new User("Wojtek","Slonzok")
        user2.addProject("Tin")
        user2.addProject("Rob")

        User user3 = new User("Adam","Czech")
        user3.addProject("Pik")
        user3.addProject("Rob")

        when:"Users are inserted to base and we look for users working on Pik project"
        userService.insertUser(user1)
        userService.insertUser(user2)
        userService.insertUser(user3)
        def users = userService.findByProjects("Pik")

        then:"There are 2 users working on that project"
        users.size() == 2
        users.get(0).firstName == "Karol"
        users.get(1).firstName == "Adam"
    }

    def"Find user with given first name"(){
        setup:"3 users are created"
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")

        User user2 = new User("Wojtek","Slonzok")
        user2.addProject("Tin")

        User user3 = new User("Adam","Czech")
        user3.addProject("Rob")

        when:"We look for user named Karol"
        userService.insertUser(user1)
        userService.insertUser(user2)
        userService.insertUser(user3)
        def answer = userService.findByFirstName("Karol")

        then:"We found Karol"
        answer.firstName == "Karol"
    }

    def"Find users with last name Polak"(){
        setup:"3 users are created"
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")

        User user2 = new User("Wojtek","Polak")
        user2.addProject("Tin")

        User user3 = new User("Adam","Czech")
        user3.addProject("Rob")

        when:"We add them and look for Polak"
        userService.insertUser(user1)
        userService.insertUser(user2)
        userService.insertUser(user3)
        def answer = userService.findByLastName("Polak")

        then:"We found 2 users"
        answer.size() == 2
    }
}
