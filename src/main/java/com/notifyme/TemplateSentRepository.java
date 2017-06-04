package com.notifyme;

/**
 * Created by gepard on 30.05.17.
 */

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateSentRepository extends MongoRepository<TemplateSent, String> {
    TemplateSent findById(String id);
    List<TemplateSent> findByDate(String date);
    List<TemplateSent> findByTemplateId(String templateId);
}

