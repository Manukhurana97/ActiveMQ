package com.example.demo.Dao.hibernate;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;


@Component
public class passwordresetimpl implements passwordreset {

    @Autowired
    public EntityManager entityManager;


    @Override
    @Transactional
    public void deleteAllByExpiredtoken() {
        Session session = entityManager.unwrap(Session.class);
        String safedisable="SET SQL_SAFE_UPDATES=0;";
        String qury = "delete from password_reset_token where expiry_date<now() or is_used=1 ;";
        String safeenable="SET SQL_SAFE_UPDATES=1;";
        Query s_query = session.createSQLQuery(safedisable);
        s_query.executeUpdate(); // execute
        Query query = session.createSQLQuery(qury);
        query.executeUpdate(); // execute
        session.clear();
        session.close();
        entityManager.close();
    }
}
