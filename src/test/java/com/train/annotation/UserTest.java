package com.train.annotation;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.train.hibernate.entity.annotation.User;
import com.train.hibernate.util.HibernateUtils;

public class UserTest {
	private static SessionFactory sessionFactory = null;

	@BeforeClass
	public static void beforeClass() {

		sessionFactory = HibernateUtils.getSessionFactory();
		User user = new User();
		user.setName("genName");
		add(user);
	}

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
	
	static User getUser(int id) {
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			// return (User) s.get(User.class, id);
			User u = (User) s.get(User.class, id);
			// User user=(User) s.load(User.class,id);
			// System.out.println("----load----"+user);
			// System.out.println(user.getName());
			// load只是准备连接到数据库，当增加上面一句操作时表示有真正的数据库操作，这时它才会去连接数据库 return user;
			return u;
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	@Test
	public void testLoadUser() {
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			User u = (User) s.load(User.class, 1);
			System.out.println("----load----" + u);
			// load只是准备连接到数据库，当增加上面一句操作时表示有真正的数据库操作，这时它才会去连接数据库 return user;
			System.out.println(u.getName());
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	@Test
	public void testGetUser() {
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			User u = (User) s.get(User.class, 1);
			System.out.println(u.getName());
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	@Test
	public void testAddUser() {
		Session s = null;
		Transaction ts = null;
		try {
			s = HibernateUtils.getSession();
			ts = s.beginTransaction();
			User user = new User();
			user.setName("jack");
			user.setDate(new Date());
			s.save(user);
			ts.commit();
		} catch (HibernateException e) {
			if (ts != null)
				ts.rollback();
			throw e;
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
				
		}
	}

	@Test
	public void testAddUserByThreadSession() {
		Session s = null;
		Transaction ts = null;
		try {
			s = HibernateUtils.getThreadSession();
			ts = s.beginTransaction();
			User user = new User();
			user.setName("jack");
			user.setDate(new Date());
			s.save(user);
			ts.commit();
		} catch (HibernateException e) {
			if (ts != null)
				ts.rollback();
			throw e;
		} finally {
			if (s != null)
				//s.close(); //注意这里不能使用close,并注意下面的打印结果
				System.out.println("s=" + s);
		}
	}

	
	@Test
	public void testExecuteNamedQuery() {
		Session s = HibernateUtils.getSession();
		Query q = s.getNamedQuery(User.class.getName()+".selectUserbyId");
		
		q.setInteger("id", 1);
		List<User> lst = q.list();
		for (User u : lst) {
			System.out.println(u.getName());
		}
		
	}
	
	@Test
	public void testAdd5User() {
		Session s = null;
		Transaction ts = null;
		try {
			s = HibernateUtils.getSession();
			ts = s.beginTransaction();

			for (int i = 0; i < 5; i++) {
				User user = new User();
				user.setName("jack" + i);
				user.setDate(new Date());
				s.save(user);

			}
			ts.commit();

		} catch (HibernateException e) {
			if (ts != null)
				ts.rollback();
			throw e;
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}
	
	@Test
	public void testSqlQuery() {
		Session s = HibernateUtils.getSession();
		Query q = s.createSQLQuery("select * from user where id=:id");
		
		q.setInteger("id", 1);
		List lst = q.list();
		for (Object u : lst) {
			Object[] olst = (Object[])u;
			for (Object obj:olst){
				System.out.println(obj);
			}
		}
		
		HibernateUtils.closeSession();
		

	}

	@Test
	public void testListEhcache() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<User> users1 = (List<User>) session.createQuery("from User")
				.setCacheable(true).list();
		for (User user : users1) {
			System.out.println(user.getName());
		}
		session.getTransaction().commit();
		session.close();

		Session session2 = sessionFactory.openSession();
		session2.beginTransaction();
		List<User> users2 = (List<User>) session2.createQuery("from User")
				.setCacheable(true).list();
		for (User user : users2) {
			System.out.println(user.getName());
		}
		session2.getTransaction().commit();
		session2.close();
	}

}
