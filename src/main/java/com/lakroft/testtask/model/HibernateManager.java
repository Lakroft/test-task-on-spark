package com.lakroft.testtask.model;

import com.lakroft.testtask.Api;
import com.lakroft.testtask.JsonManager;
import com.lakroft.testtask.model.User;
import net.minidev.json.JSONObject;
import org.hibernate.LockMode;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.List;

public class HibernateManager {
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static List<User> loadAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("FROM User").list();
            session.close();
            return users;
        }
    }

    public static User getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    public static void save(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }
    public static void saveAll(List<User> users) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (User user : users)
                session.save(user);
            session.getTransaction().commit();
        }
    }

    public static void update(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    public static void updateAll(List<User> users){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (User user : users)
                update(user);
            session.getTransaction().commit();
        }
    }

    public static String transfer(Long from, Long to, BigDecimal amount) {
        for (int i = 0; i < 5; i++) {
            try (Session session = sessionFactory.openSession()) {
                session.getTransaction().begin();
                User fromUser = session.get(User.class, from, LockMode.PESSIMISTIC_WRITE); //загрузить пользователя from
                if (fromUser == null) return JsonManager.getError("No 'from' user").toString();
                if (fromUser.getBalance().compareTo(amount) < 0) return JsonManager.getError("Not enough money").toString();


                User toUser = session.get(User.class, to, LockMode.PESSIMISTIC_WRITE);//загрузить пользователя to
                if (toUser == null) return JsonManager.getError("No 'to' user").toString();

                fromUser.setBalance(fromUser.getBalance().subtract(amount));//уменьшить на amount
                session.save(fromUser);//сохранить

                toUser.setBalance(toUser.getBalance().add(amount));//увеличить на amount
                session.save(toUser);//сохранить
                session.getTransaction().commit();//transaction commit

                return JsonManager.getSuccess().toString();
            } catch (PessimisticLockException ignored) { }
        }
        return JsonManager.getError("Database is busy. Try later").toString();
    }

    public static BigDecimal summ() {
        BigDecimal summ = new BigDecimal(0);
        for (User user : loadAll()) {
            summ = summ.add(user.getBalance());
        }
        return summ;
    }
}
