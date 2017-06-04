package com.notifyme;

/**
 * Created by gepard on 30.05.17.
 */
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
    Template findById(String id);
    List<Template> findByProject(String project);
    Template findByTitleAndProject(String title, String project);
}

