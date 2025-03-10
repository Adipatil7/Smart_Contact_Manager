package com.smart.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ContactRepository contactRepo;

	@ModelAttribute
	public void AddCommonData(Model m, Principal principal) {
		String userName = principal.getName();

		User user = userRepo.getUserByUserName(userName);

		m.addAttribute("user", user);

	}

	@RequestMapping("/index")
	public String dashBoard(Model m, Principal principal) {
		return "normal/user_dashboard";
	}

	@GetMapping("/addContact")
	public String AddContact(Model m) {
		m.addAttribute("title", "Add Contact - Smart Contact Manager");
		Contacts c = new Contacts();
		m.addAttribute("contact", c);
		return "normal/add_contact";
	}
	// handler for show contacts
	// per page n contacts , for now let n = 5
	// current page = 0
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m) {

		User user = (User) m.getAttribute("user");

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contacts> list = this.contactRepo.findContactsByUser(user.getId(), pageable);

		m.addAttribute("contacts", list);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", list.getTotalPages());

		m.addAttribute("title", "View Contacts - Smart Contact Manager");

		return "normal/show_contacts";
	}


	// process add contact form

	@PostMapping("/process-contact")
	public String process_contact(@ModelAttribute Contacts contact, 
	                              Principal principal,
	                              @RequestParam("profileImage") MultipartFile file, 
	                              Model model) {
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	    try {
	        User user = (User)model.getAttribute("user");
	        
	        Contacts existingContact = null;
	        if (contact.getCid() != 0) { // If updating
	            Optional<Contacts> optionalContact = contactRepo.findById(contact.getCid());
	            if (optionalContact.isPresent() && optionalContact.get().getUser().getId() == user.getId()) {
	                existingContact = optionalContact.get();
	            }
	        }
	        System.out.println("x1");
	        if (existingContact != null) {
	            
	            existingContact.setName(contact.getName());
	            existingContact.setEmail(contact.getEmail());
	            existingContact.setPhone(contact.getPhone());
	            existingContact.setDescription(contact.getDescription());
	            System.out.println("x2");
	            if (!file.isEmpty()) {
	            	
	            	//deleting previous image
	            	
	            	String Spath = "C:\\Users\\adity\\Documents\\workspace-spring-tool-suite-4-4.27.0.RELEASE\\SmartContactManager\\target\\classes\\static\\image";
					Spath+=(File.separator+contact.getImageUrl());
					Path path1 = Paths.get(Spath);
					
					Files.deleteIfExists(path1);
	            	
	                existingContact.setImageUrl(file.getOriginalFilename());
	                File saveFile = new ClassPathResource("static/image").getFile();
	                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            }
	            
	            contactRepo.save(existingContact);
	            model.addAttribute("message", new Message("Your Contact is Updated !!", "success"));
	            
	            return showContacts(0, model);
	        } 


	        // Add new contact
	        if (!file.isEmpty()) {
	            contact.setImageUrl(file.getOriginalFilename());
	            File saveFile = new ClassPathResource("static/image").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        } else {
	            contact.setImageUrl("default.png");
	        }
	        
	        contact.setUser(user);
	        user.getContacts().add(contact);
	        userRepo.save(user);
	        System.out.println("x3");
	        model.addAttribute("contact", new Contacts());
	        model.addAttribute("message", new Message("Your Contact is Added !! Add more", "success"));
	        return "normal/add_contact"; // Stay on add contact page

	    } catch (Exception e) {
	        e.printStackTrace(); // Print full error stack trace
	        model.addAttribute("message", new Message("Something went wrong, try again", "danger"));
	        return "normal/add_contact"; // Stay on add contact page in case of error
	    }
	}
	
	
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") int cid, Model m) {

		User user = (User) m.getAttribute("user");

//		List<Contacts>list = user.getContacts();
//		boolean flag = false;
//		for(Contacts c : list) {
//			if(c.getCid() == cid) {
//				m.addAttribute("contact", c);
//				flag = true;
//				break;
//			}
//		}
//		
		Contacts c = this.contactRepo.findById(cid).get();
		if (user.getId() == c.getUser().getId()) {

			m.addAttribute("contact", c);

			return "normal/contact_detail";
		}

		else {
			m.addAttribute("message", new Message("This contact is not yours !! Add new if You want ", "danger"));
			return "normal/add_contact";
		}
	}

	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") int cid,Model m) {
		Contacts c = this.contactRepo.findById(cid).get();
		User user = (User) m.getAttribute("user");
		m.addAttribute("title", "Delete Contact - Smart Contact Manager");
		try {
			if (user.getId() == c.getUser().getId()) {
				
				String Spath = "C:\\Users\\adity\\Documents\\workspace-spring-tool-suite-4-4.27.0.RELEASE\\SmartContactManager\\target\\classes\\static\\image";
				Spath+=(File.separator+c.getImageUrl());
				Path path = Paths.get(Spath);
				
				Files.deleteIfExists(path);
				
				this.contactRepo.deleteById(cid);
				m.addAttribute("message", new Message("Contact deleted Successfully !!","success"));
			}
			else {
				m.addAttribute("message", new Message("This contact is not yours !!", "danger"));	
			}
		}catch(Exception e) {
			m.addAttribute("message", new Message(" Error deleting contact !!"+e.getMessage(), "danger"));
		}
		return showContacts(0, m);
	}
	
	@GetMapping("/update/{cid}")
	public String update(@PathVariable("cid")int cid,Model m) {
		
		m.addAttribute("title", "Update Contact - Smart Contact Manager");
		try {
			Contacts c = this.contactRepo.findById(cid).get();
			User user = (User)m.getAttribute("user");
			if(user.getId() == c.getUser().getId()) {
				m.addAttribute("contact", c);
				return "normal/add_contact";
			}else {
				m.addAttribute("message", new Message("There is no such contact !!", "danger"));
				return showContacts(0, m);
			}
		}catch(Exception e) {
			m.addAttribute("message", new Message("There is no such Contact !!", "danger"));
			return showContacts(0, m);
		}
	}
	
	
	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("title","Profile - Smart Contact Manager");
		return "normal/profile";
	}
	
	// settings handler
	
	@GetMapping("/settings")
	public String settings() {
		
		
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String changepass(@RequestParam("old_pass") String Old_pass,@RequestParam("new_pass") String New_pass,Model m) {
		
		User user = (User)m.getAttribute("user");
		//System.out.println(Old_pass+"HI");
		if(bCryptPasswordEncoder.matches(Old_pass,user.getPassword())) {
			user.setPassword(New_pass);
			m.addAttribute("message" , new Message("Password Changed Successfully !!", "success"));
			return "normal/user_dashboard"; 
		}else {
			m.addAttribute("message" , new Message("Incorrect password !!", "danger"));
			return "normal/settings";
		}
	}
	
	
	
}
