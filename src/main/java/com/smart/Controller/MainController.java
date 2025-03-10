package com.smart.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/")
	public String home(Model m) {

		m.addAttribute("title", "Home - Smart Contact Manager");

		return "home";
	}

	@RequestMapping("/about/")
	public String about(Model m) {
 
		m.addAttribute("title", "About - Smart Contact Manager");

		return "about";
	}

	@RequestMapping("/signup/")
	public String signup(Model m,HttpSession session) {
		m.addAttribute("title", "Register - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do-register")
	public ResponseEntity<?> doRegister(
	        @ModelAttribute("user") User user,
	        @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
	        HttpSession session) {

	    try {
	        if (!agreement) {
	            return ResponseEntity.badRequest().body(Map.of("error", "You must agree to the terms & conditions."));
	        }

	        user.setRole("ROLE_USER");
	        user.setActive(true);
	        user.setImageUrl("default.png");
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        User res = this.userRepo.save(user);

	        return ResponseEntity.ok(Map.of("message", "Successfully Registered!"));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Something went wrong: " + e.getMessage()));
	    }
	}

	//handler for custom log in
	
	@RequestMapping("/signin")
	public String login(Model m) {
		
		m.addAttribute("title", "Signin - Smart Contact Manager");
		
		return "login";
	}
	
	//for forget password?
	@RequestMapping("/getMail")
	public String getMail(Model m) {
		return "GetMail";
	}
	
	@PostMapping("/process-email")
	public String processMail(@RequestParam("email") String email,Model m,HttpSession session) {
		
		User user = (User)this.userRepo.getUserByUserName(email);
		
		if(user == null) {
			m.addAttribute("message", new com.smart.helper.Message("Can not find user", "danger"));
			return getMail(m);
		}else {
			Random random = new Random(1000);
			String otp = String.valueOf(random.nextInt(99999999));
			
			String message = "Your OTP from Smart Contact Manager !! "+otp;
			
			String subject = "OTP to Change password !! Smart Contact Manager";
			
			String to = user.getEmail();
			
			sendEmail(message, subject, to, "adyapatil00707@gmail.com");
			m.addAttribute("message", new com.smart.helper.Message("We have sent OTP to your Mail", "success"));
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
	}
	
	@PostMapping("/verify-otp")
	public String changePass(HttpSession session , @RequestParam("otp") String otp,Model m) {
		String myotp = (String)session.getAttribute("myotp");
		
		if(myotp.equals(otp)) {
			return "GetPass";
		}else {
			m.addAttribute("message", new com.smart.helper.Message("You have Entered wrong OTP", "danger"));
			return "verify_otp";
		}
		
	}
	
	@PostMapping("/change-pass")
	public String GetPass(@RequestParam("new_pass") String new_pass , Model m , HttpSession session) {
		
		User user = this.userRepo.getUserByUserName((String)session.getAttribute("email"));
		
		if(passwordEncoder.matches(new_pass, user.getPassword())) {
			m.addAttribute("message", new com.smart.helper.Message("This password is used !! provide new one", "danger"));
			return "GetPass";
		}else {
			user.setPassword(passwordEncoder.encode(new_pass));
			this.userRepo.save(user);
			session.invalidate(); // Invalidate session after password change
			m.addAttribute("message", new com.smart.helper.Message("Password changed Successfully", "success"));
			return "login";
		}
	}
	
  	private static void sendEmail(String message, String subject, String to, String from) {

		// gmail host variable
		String host = "smtp.gmail.com";

		// get system properties
		Properties properties = System.getProperties();

		System.out.println("Properties " + properties);

		// setting important information to properties object

		// setting host
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1 : to get session object

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "zdzl hxbz sglx vfgw");

			}

		});

		session.setDebug(true);

		// step 2 : compose the message [text , multimedia]

		MimeMessage m = new MimeMessage(session);

		try {

			// setting from
			m.setFrom(from);

			// adding reciever

			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject

			m.setSubject(subject);

			// add text to message
			m.setText(message);

			// sending message
			// Step 3 : sending message using Transport class
			Transport.send(m);

			System.out.println("Sent Succefully........");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
