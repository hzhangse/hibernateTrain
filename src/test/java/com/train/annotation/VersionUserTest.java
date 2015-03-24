package com.train.annotation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.train.hibernate.entity.annotation.User;
import com.train.hibernate.util.HibernateUtils;

public class VersionUserTest {
	BlockingQueue<Throwable> bq = new LinkedBlockingQueue<Throwable>(100);;
	final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {

		public void run() {
			System.out.println("结束....");
			testGetUser();
		}
	});
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
	public void updateByOptimisticLock() throws Exception {

		final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {

			public void run() {
				System.out.println("结束....");
			}
		});
		final CountDownLatch overLatch = new CountDownLatch(2);
		Runnable r = new Runnable() {
			public void run() {
				Session s = HibernateUtils.getSession();
				Transaction tx = s.beginTransaction();
				try {

					User user = (User) s.get(User.class, 1);

					barrier.await();

					System.out.println(user.getName());
					user.setName(Thread.currentThread().getName()
							+ user.getName());
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					bq.add(e);

				} finally {
					if (s != null)
						HibernateUtils.closeSession();
					overLatch.countDown();
				}
			}

		};
		try {
			Thread t1 = new Thread(r);
			Thread t2 = new Thread(r);
			t1.start();
			t2.start();

			overLatch.await();
			while (!bq.isEmpty()) {
				Throwable t = bq.poll();
				throw t;

			}
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();

		}

		// synchronized (tc) {
		// while (t1.isAlive() || t2.isAlive()) {
		// try {
		// tc.wait();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// tc.notify();
		// }
		// pool.shutdown();
	}
	
	
	@Test
	public void updateByReadLock() throws Exception {

		final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {

			public void run() {
				System.out.println("结束....");
			}
		});
		final CountDownLatch overLatch = new CountDownLatch(2);
		Runnable r = new Runnable() {
			public void run() {
				Session s = HibernateUtils.getSession();
				Transaction tx = s.beginTransaction();
				try {

					User user = (User) s.get(User.class, 1, LockOptions.READ);

					barrier.await();

					System.out.println(user.getName());
					user.setName(Thread.currentThread().getName()
							+ user.getName());
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					bq.add(e);

				} finally {
					if (s != null)
						HibernateUtils.closeSession();
					overLatch.countDown();
				}
			}

		};
		try {
			Thread t1 = new Thread(r);
			Thread t2 = new Thread(r);
			t1.start();
			t2.start();

			overLatch.await();
			while (!bq.isEmpty()) {
				Throwable t = bq.poll();
				throw t;

			}
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();

		}

	}
	
}