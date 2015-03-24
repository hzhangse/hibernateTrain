package com.train.hibernate.entity.annotation;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class Student {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.TableHiLoGenerator")
	@GeneratedValue(generator = "idGenerator")
	private int id;
	private String name;
	
	@ManyToMany(targetEntity=Teacher.class)
	private Set<Teacher> teachers;
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
	public Set<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}	

}
