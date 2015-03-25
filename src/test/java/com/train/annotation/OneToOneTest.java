package com.train.annotation;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.train.hibernate.entity.annotation.IdCard;
import com.train.hibernate.entity.annotation.Name;
import com.train.hibernate.entity.annotation.Person;
import com.train.hibernate.util.HibernateUtils;

public class OneToOneTest {
	public static void main(String[] args) {
		add();

		//queryPerson(1);
		
		queryCard(1);
	    HibernateUtils.getSessionFactory().close();
	}

	static void add() {
		Session s = null;
		Transaction tr = null;
		try {
			s = HibernateUtils.getSession();
			tr = s.beginTransaction();

			Person person = new Person();
			Name name = new Name();
			name.setFirstName("宏");
			name.setSecondName("Zhang");
			person.setName(name);
			IdCard idCard = new IdCard();
			idCard.setValidity(new Date());

			// 分别注释掉以下两句，看程序执行情况
			person.setCard(idCard);
			idCard.setPerson(person);

			// s.save(person);
			s.save(idCard);
			tr.commit();
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	static Person queryPerson(int id) {
		Session s = null;
		Transaction tr = null;
		try {
			HibernateUtils.getSessionFactory().evict(IdCard.class, id);
			HibernateUtils.getSessionFactory().evict(Person.class, id);
			s = HibernateUtils.getSession();
			tr = s.beginTransaction();
			Person p = (Person) s.get(Person.class, id);
			System.out.println("身份证有效期:");
			System.out.println(p.getCard().getValidity());
			s.evict(p);
			Person p2 = (Person) s.get(Person.class, id);
			System.out.println("p2身份证有效期:");
			System.out.println(p2.getCard().getValidity());
			tr.commit();
			return p;
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

	static IdCard queryCard(int id) {
		Session s = null;
		Transaction tr = null;
		try {
			HibernateUtils.getSessionFactory().evict(IdCard.class, id);
			HibernateUtils.getSessionFactory().evict(Person.class, id);
			s = HibernateUtils.getSession();
			tr = s.beginTransaction();
			IdCard idCard = (IdCard) s.get(IdCard.class, id);
			System.out.println("人的名字：" );
			//System.out.println( idCard.getPerson().getName().getFirstName()+" " +idCard.getPerson().getName().getSecondName());
			// 去掉上一句注释后，发现会查询两次。
			tr.commit();
			return idCard;
		} finally {
			if (s != null)
				HibernateUtils.closeSession();
		}
	}

}
