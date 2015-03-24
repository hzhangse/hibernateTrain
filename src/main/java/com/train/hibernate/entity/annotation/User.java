package com.train.hibernate.entity.annotation;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OptimisticLocking;


@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,include="non-lazy")
@NamedQuery(name="com.train.hibernate.entity.annotation.User.selectUserbyId",query="from User where id=:id")
@NamedNativeQuery(name="selectUserSqlsss",query="select * from user where id=:id")
@OptimisticLocking()
public class User {
	@Version
	private int version;
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.TableHiLoGenerator")
	@GeneratedValue( generator = "idGenerator")
	int id;
	String name;
	Date date;
}
