package com.notifyme;

/**
 * Created by gepard on 30.05.17.
 */
import java.util.List;

        import org.springframework.data.mongodb.repository.MongoRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
        public Project findByTitle(String title);
}
