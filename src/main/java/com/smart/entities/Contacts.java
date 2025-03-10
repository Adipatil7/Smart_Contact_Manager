package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contacts {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	public void setCid(int cid) {
		this.cid = cid;
	}
	private String name;
	private String nickName;
	private String email;
	private String imageUrl;
	private String phone;
	private String work;
	@Column(length=1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public int getCid() {
		return cid;
	}
	
	@Override
	public String toString() {
		return "Contacts [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", email=" + email + ", imageUrl="
				+ imageUrl + ", phone=" + phone + ", work=" + work + ", description=" + description + ", user=" + user
				+ "]";
	}
}
