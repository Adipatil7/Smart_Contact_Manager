package com.smart.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.User;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactRepository contactRepo;
	
	
	//search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?>search( @PathVariable("query") String query ,Principal principal){
		
		System.out.println(query);
		
		User user = this.userRepo.getUserByUserName(principal.getName());
		
		List<Contacts> contacts  = this.contactRepo.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
	}
	
	
}
