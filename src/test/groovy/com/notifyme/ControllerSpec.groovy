package com.notifyme

import spock.lang.Specification
import com.notifyme.controller.Controller

class ControllerSpec extends Specification {

    ProjectRepository projectRepository
    UserRepository userRepository
    TemplateRepository templateRepository

    def"Check project collaborators"(){
        given:"We have 3 users and 2 of them are working on the same project"
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")

        User user2 = new User("Wojtek","Polak")
        user2.addProject("Tin")

        User user3 = new User("Adam","Czech")
        user3.addProject("Pik")

        List<User> users = new ArrayList<User>()
        users.add(user1)
        users.add(user3)
        def userRepo = Stub(UserRepository){
            findByProjects("Pik") >> users
        }
        when:"We are looking only for people working on Pik"
        def answer = userRepo.findByProjects("Pik")
        then:"We found 2 users"
        answer.size() == 2
    }

    def"Add user to project"(){
        given:"We have user and existing project"
        User user1 = new User("Karol","Polak")
        user1.setId("1")
        Project project = new Project()
        project.setTitle("Pik")
        def userRepo = Stub(UserRepository){
            findById("1") >> user1
        }
        when:"We find user and add him to the project"
        def answer = userRepo.findById("1")
        answer.addProject(project.getTitle())

        then:"He will have this project in his history"
        answer.projects.size() == 1
    }

    def"Delete user from project"(){
        given:"We have existing user with a project"
        User user1 = new User("Karol","Polak")
        user1.addProject("Pik")

        def userRepo = Stub(UserRepository){
            findById("1") >> user1
        }
        when:"We find user and remove him from the project"
        def answer = userRepo.findById("1")
        answer.deleteProject("Pik")

        then:"He will have no projects in his history"
        answer.projects.size() == 0
    }

    def"Get all user projects"(){
        given:"We have user working on 3 projects"
        User user1 = new User("Karol","Polak")
        Project proj1 = new Project("Pik")
        Project proj2 = new Project("Tin")
        Project proj3 = new Project("Tkom")
        user1.addProject("Pik")
        user1.addProject("Tin")
        user1.addProject("Tkom")

        when:"We get all his projects"
        List<String> projects = new ArrayList<String>()
        projects.add("Pik")
        projects.add("Tin")
        projects.add("Tkom")
        def userStub = Stub(User) {
            getProjects() >> projects
        }
        def answer = userStub.getProjects()

        then:"We receive correct number of projects"
        answer == user1.getProjects()
        answer.size() == 3
    }

    def"Get templates by project"(){
        given:"We have 2 templates for project Pik"
        Template template1 = new Template("Etap1","Workspace")
        template1.setProject("Pik")
        Template template2 = new Template("Etap2","Hello worlds")
        template2.setProject("Pik")

        List<Template> templates = new ArrayList<Template>()
        templates.add(template1)
        templates.add(template2)
        def templateRepo = Stub(TemplateRepository){
            findByProject("Pik") >> templates
        }
        when:"We look for templates for Pik project"
        def answer = templateRepo.findByProject("Pik")

        then:"We have 2 templates"
        answer == templates
        answer.size() == 2
    }

    def"Save project"(){
        given:"We create a project"
        Project project = new Project("Pik")
        def projRepo = Mock(ProjectRepository)

        when:"We save project"
        projRepo.save(project)

        then:"Project save is invoked"
        1 * projRepo.save(project)
    }

    def"Save template to project"(){
        given:"We create a template"
        Template temp = new Template("Etap2","Hello worlds")
        def tempRepo = Mock(TemplateRepository)
        when:"We save a template"
        tempRepo.save(temp)
        then:"Template save is invoked"
        1 * tempRepo.save(temp)
    }

    def"Register a not existing user"(){
        given:"We heave 2 users"
        User user1 = new User("Karol","Polak")
        User user2 = new User("Piotr","Gawek")


        when:"First user"

        def userStub = Stub(UserRepository){
            findByLogin(_) >>> [user1,null]
        }

        then:"Karol exists and Piotr doesnt"

        userStub.findByLogin("Karol") == user1
        userStub.findByLogin("Piotr") == null
    }

    def"We send mail to 2 users"(){
        given:"We have 2 users"
        def emailService = Mock(EmailService)
        User user1 = new User("Karol","Polak")
        User user2 = new User("Piotr","Gawek")
        Template template = new Template("Etap2","Hello worlds")

        List<User> users = new ArrayList<User>()
        users.add(user1)
        users.add(user2)

        when:"We send mail to users"
        for(User u : users){
            emailService.sendTemplatedMessage(u.getLogin(),template)
        }
        then:"Mail is sent twice"
        2 * emailService.sendTemplatedMessage(_,_)
    }







}
