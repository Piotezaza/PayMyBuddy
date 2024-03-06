package com.pmb.paymybuddy;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService us;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("PayMyBuddy started.");
		List<User> liste = us.getUsers();
		for (int i = 0; i < liste.size(); i++) {
			User user = liste.get(i);
			List<User> contactList = user.getContacts();
			System.out.println(user);
			//System.out.println(user.getPrenom() + " a " + contactList.size() + " contact(s), son compte PMB est le n°" + user.getCompte_pmb().getId() + ", voici l'iban de son compte bancaire : " + (user.getCompte_bancaire() == null ? "compte bancaire non défini." : user.getCompte_bancaire().getIban()));
			//System.out.println(user.getPrenom() + " a fait " + user.getCompte_pmb().getDebits().size() + " transfert(s) et en a reçu " + user.getCompte_pmb().getCredits().size());
		}
	}
}
