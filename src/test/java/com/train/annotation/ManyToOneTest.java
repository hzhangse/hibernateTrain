package com.train.annotation;

import javax.persistence.FetchType;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;

import com.train.hibernate.entity.annotation.Department;
import com.train.hibernate.entity.annotation.Employee;
import com.train.hibernate.util.HibernateUtils;

public class ManyToOneTest {
	private static SessionFactory sessionFactory = null;
	@BeforeClass
	public static void beforeClass() {
		sessionFactory = HibernateUtils.getSessionFactory();
		
	}

	
	static void add() {
		Session s = null;
		Transaction tx = null;
		try {
			Department depart = new Department();
			depart.setName("departName");
			Employee emp = new Employee();
			emp.setName("empName");
			emp.setDept(depart);

			s = HibernateUtils.getSession();
			tx = s.beginTransaction();
			
			s.save(depart);
			s.save(emp);
			 // 交换以上两句的位置，看Hibernate执行的sql语句。会再增加一条更新操作。   
			tx.commit();
			
			
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}
	
	 static Employee query(int empId) {
			Session s = null;
			try {
				s = HibernateUtils.getSession();
				Employee emp = (Employee) s.get(Employee.class, empId);
				//System.out.println("Department Name:" + emp.getDept().getName());
				return emp;
			} finally {
				if (s != null)
					HibernateUtils.closeSession();
			}
		}

	    static Employee query2(int empId) {
			Session s = null;
			try {
				s = HibernateUtils.getSession();
				Employee emp = (Employee) s.get(Employee.class, empId);
				Hibernate.initialize(emp.getDept()); 
				//上一句作用后面会作说明，这里略作了解
				return emp;
			} finally {
				if (s != null)
					HibernateUtils.closeSession();
			}
		}

	public static void main(String[] args) {
		add();
		query(32768);
		//query2(32768);
		HibernateUtils.getSessionFactory().close();
	}

}
