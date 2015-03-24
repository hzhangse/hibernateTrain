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

import com.train.hibernate.entity.annotation.CommUser;
import com.train.hibernate.util.HibernateUtils;

public class UserLockTest {
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
		CommUser user = new CommUser();
		user.setName("genName");
		add(user);
	}

	static CommUser add(CommUser user) {
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
			CommUser u = (CommUser) s.get(CommUser.class, 1);
			System.out.println(u.getName());
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	
	@Test
	public void updateByUPGRADELock() throws Exception {

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

					CommUser user = (CommUser) s.get(CommUser.class, 1, LockOptions.UPGRADE);

					//barrier.await();

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

					CommUser user = (CommUser) s.get(CommUser.class, 1);
					s.buildLockRequest(LockOptions.READ).lock(user);
					//barrier.await();

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