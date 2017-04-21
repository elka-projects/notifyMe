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
        setup:

        User user = new User("Karol","Polak")
        user.addProject("Pik")
        user.addProject("Iop")

        when:
        userService.insertUser(user)
        //userRepository.insert(user)
        then:
        //def users = userService.findByLastName("Polak")
        //users.size() == 1
        1*userRepository.save(user)
    }

    def"find all users in the same Pik project"()
    {
        setup:
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")
        user1.addProject("Iop")

        User user2 = new User("Wojtek","Slonzok")
        user2.addProject("Tin")
        user2.addProject("Rob")

        User user3 = new User("Adam","Czech")
        user3.addProject("Pik")
        user3.addProject("Rob")

        when:
        userService.insertUser(user1)
        userService.insertUser(user2)
        userService.insertUser(user3)
        def users = userService.findByProjects("Pik")

        then:
        users.size() == 2
        users.get(0).firstName == "Karol"
        users.get(1).firstName == "Adam"
    }
}
