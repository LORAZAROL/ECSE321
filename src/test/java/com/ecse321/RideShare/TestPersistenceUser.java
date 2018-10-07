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
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "QuickStart" ); 
	    EntityManager entitymanager = emfactory.createEntityManager( );
	    entitymanager.getTransaction( ).begin( );
	    
	    User u = new User("fir", "la", "@mail", "5555555555", "password", true);
	    
	    entitymanager.persist(u);
	    entitymanager.getTransaction( ).commit( );

	    entitymanager.close( );
	    emfactory.close( );
	    
	    System.out.println(u);
	}
}
