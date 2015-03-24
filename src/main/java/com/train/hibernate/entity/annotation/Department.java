package com.train.hibernate.entity.annotation;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Department {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.TableHiLoGenerator")
	@GeneratedValue(generator = "idGenerator")
	int id;
	@OneToMany(targetEntity = Employee.class, fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "dept")
	//@JoinColumn(name="dept_id")
	@Fetch(value=FetchMode.JOIN)
	//@BatchSize(size=2)
	Set<Employee> emps;

	public Set<Employee> getEmps() {
		return emps;
	}

	public void setEmps(Set<Employee> emps) {
		this.emps = emps;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
