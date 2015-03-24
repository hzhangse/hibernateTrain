package com.train.xml;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import com.train.hibernate.entity.xml.User;
import com.train.hibernate.util.HibernateUtils;



public class HibernateCacheTest {
	public static void main(String[] args) {
		addUser();
		getUser(1);
		
		Statistics st = HibernateUtils.getSessionFactory().getStatistics();
		System.out.println(st);
		System.out.println("put:" + st.getSecondLevelCachePutCount());
		System.out.println("hit:" + st.getSecondLevelCacheHitCount());
		System.out.println("miss:" + st.getSecondLevelCacheMissCount());
		HibernateUtils.getSessionFactory().close();
	}

	static User getUser(int id) {
		Session s = null;
		User user = null;
		try {
			s = HibernateUtils.getSession();
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());

			// session缓存，当session未关闭时，再查询直接从缓存中获得数据。
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());
			
			
			// 如果我们清掉缓存，再查询时将会重新连库。
			s.evict(user);// 清掉指定的数据
			// s.clear();//清掉当前session缓存中的所有内容
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}

		// 当上面的session关闭后，如果想再获取前面查询的数据，必须重新查库。
		try {
			s = HibernateUtils.getSession();
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
		return user;
	}

	static User deleteUser(int id) {
		Session s = null;
		User user = null;
		try {
			s = HibernateUtils.getSession();
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());

			// session缓存，当session未关闭时，再查询直接从缓存中获得数据。
			user = (User) s.get(User.class, id);
			System.out.println("userName:" + user.getName());

			s.delete(user);
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
		return user;
	}
	
	static User addUser() {
		Session s = null;
		User user = new User();
		try {
			s = HibernateUtils.getSession();
			Transaction tx = s.beginTransaction();
			user = new User();
			user.setName("jack");
			s.save(user);
			tx.commit();
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}

		return user;
	}
}
