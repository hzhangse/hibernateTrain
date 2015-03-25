package com.train.hibernate.entity.annotation;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,include="non-lazy")
public class Person {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.TableHiLoGenerator")
	@GeneratedValue( generator = "idGenerator")
	private int id;
	@OneToOne(targetEntity = IdCard.class,fetch=FetchType.LAZY,mappedBy="person",optional=false,orphanRemoval=true)
	private IdCard card;
	public IdCard getCard() {
		return card;
	}
	public void setCard(IdCard card) {
		this.card = card;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "firstname" , column  =  @Column(name = "first_name" ) ),
		@AttributeOverride(name = "secondname" , column  =  @Column(name = "second_name" ) )
		} )
	private Name name;	
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	
}
