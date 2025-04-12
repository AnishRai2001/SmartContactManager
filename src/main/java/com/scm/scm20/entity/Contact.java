package com.scm.scm20.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Contact {
	@Id
	private String id;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String picture;
	private String description;
	private boolean favorite=false;
	private String linkedInLink;
	private String websiteLink;
	
	@ManyToOne 
	private User user;
	
	@OneToMany(mappedBy ="contact",cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private List<SocialLink>socialLink=new ArrayList<>();

}
