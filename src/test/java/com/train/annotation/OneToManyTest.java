package com.train.annotation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.train.hibernate.entity.annotation.Department;
import com.train.hibernate.entity.annotation.Employee;
import com.train.hibernate.util.HibernateUtils;

public class OneToManyTest {
	private static SessionFactory sessionFactory = null;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = HibernateUtils.getSessionFactory();
		
	}

	
	@Before
	public  void add() {
		Session s = null;
		Transaction tx = null;
		try {
			Department depart = new Department();
			depart.setName("departName");

			Employee emp1 = new Employee();
			emp1.setName("empName1");
			emp1.setDept(depart);
			Employee emp2 = new Employee();
			emp2.setName("empName2");
			emp2.setDept(depart);

			Set<Employee> emps = new HashSet<Employee>();
			emps.add(emp1);
			emps.add(emp2);
			depart.setEmps(emps);

			s = HibernateUtils.getSession();
			tx = s.beginTransaction();
			s.save(depart);
			s.save(emp1);
			s.save(emp2);
			tx.commit();
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}
	
	
	@Test
	public void  query() {	
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			Department depart = (Department) s.get(Department.class, 2);
			System.out.println("employee size:" + depart.getEmps().size());
			
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}
	
	
	public void queryAll() {
		for (int i=0;i<5;i++){
			add();
		}
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			List<Department> lst = s.createQuery("from Department").list();

			
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	@Test
	public void delete() {
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			Transaction tx = s.beginTransaction();
			Department depart = (Department) s.get(Department.class, 1);
			s.delete(depart);
			tx.commit();
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	@Test
	public void update() {
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			Transaction tx = s.beginTransaction();
			Department depart = (Department) s.get(Department.class, 2);

			//depart.setId(100);
			depart.setName("dsfadsf");
			
			// Set<Employee> emps = depart.getEmps();
			// for (Employee emp:emps){
			// emp.setDept(depart);
			// }
			// depart.setEmps(emps);
			s.update(depart);
			tx.commit();
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	public static void main(String[] args) {

	}
}
