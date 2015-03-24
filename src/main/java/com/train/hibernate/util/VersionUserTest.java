package com.train.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.train.hibernate.entity.xml.User;

public class VersionUserTest {
	static User add(User user) {
		Session s = null;
	
		try {
			s = HibernateUtils.getSession();
			Transaction tx = s.beginTransaction();
			s.save(user);
			tx.commit();
		} finally {
			if (s != null)
				s.close();
		}

		return user;
	}
	
	static void addUser() {
		User user = new User();
		user.setName("genName");
		add(user);
	}
	public static void main(String[] args) {
		addUser();
		update(1);
	}
	static void update(int id) {
		Session s1 = null;
		Session s2 = null;
		Transaction tx1 = null;
		Transaction tx2 = null;
		try {
			s1 = HibernateUtils.getNewSession();
			tx1 = s1.beginTransaction();
			User user1 = (User) s1.get(User.class, id);
			System.out.println(user1.getName());
			s2 = HibernateUtils.getNewSession();
			tx2 = s2.beginTransaction();
			User user2 = (User) s2.get(User.class, id);

			user1.setName("user1Name");
			user2.setName("user2Name");
			tx2.commit();
			tx1.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			//s1.close();
			//s2.close();
		}
	}
}