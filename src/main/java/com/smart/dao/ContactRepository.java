package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contacts;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contacts, Integer>{
	
	@Query("from Contacts as c where c.user.id =:userId")
	//pageable object requires 2 things :- 1) current page 2)number of objects(contacts) per page
	public Page<Contacts> findContactsByUser(@Param("userId")int userId,Pageable pageable);
	
	//search
	public List<Contacts> findByNameContainingAndUser(String name,User user);
	
}
