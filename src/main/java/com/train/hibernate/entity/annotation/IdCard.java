package com.train.hibernate.entity.annotation;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,include="non-lazy")
public class IdCard {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.ForeignGenerator", parameters = { @Parameter(name = "property", value = "person") })
	@GeneratedValue(generator = "idGenerator")
	private int id;
	private Date validity;
	@OneToOne(targetEntity = Person.class,fetch=FetchType.LAZY)
	@JoinColumn(name="person_id")
	private Person person;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
