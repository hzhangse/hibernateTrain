package com.train.hibernate.entity.annotation;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Employee {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.TableHiLoGenerator")
	@GeneratedValue(generator = "idGenerator")
	int id;
	String name;
	@ManyToOne(targetEntity=Department.class,fetch = FetchType.LAZY)
	@JoinColumn(name="dept_id", nullable=true, updatable=true)
	Department dept;

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

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}
}
