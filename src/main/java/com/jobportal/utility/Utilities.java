package com.jobportal.utility;



import java.security.SecureRandom;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.jobportal.entity.Sequence;
import com.jobportal.exception.JobPortalException;

@Component
public class Utilities {
    private final MongoOperations mongoOperations;

    public Utilities(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Long getNextSequence(String key) throws JobPortalException {
        Query query = new Query(Criteria.where("_id").is(key));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
    
        Sequence seq = mongoOperations.findAndModify(query, update, options, Sequence.class);
    
        if (seq == null) {
            throw new JobPortalException("❌ Unable to get sequence id for key: " + key);
        }
        
        System.out.println("✅ Generated sequence ID: " + seq.getSeq());
        return seq.getSeq();
    }
    
    
    
    public static String generateOtp(){
        StringBuilder otp=new StringBuilder();
        SecureRandom random=new SecureRandom();
        for(int i=0; i<6; i++)otp.append(random.nextInt(10));
        return otp.toString();
    }
}