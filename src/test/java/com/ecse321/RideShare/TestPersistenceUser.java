package com.ecse321.RideShare;
import javax.persistence.*;
import com.ecse321.RideShare.model.*;
import java.util.List;
import java.util.logging.Logger;
 
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class TestPersistenceUser {
	
	@Test
	public void init() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuickStart");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		User pass = new User("a","b","c","d","e",true);
		em.persist(pass);
		em.getTransaction().commit();
		emf.close();
	}
}
