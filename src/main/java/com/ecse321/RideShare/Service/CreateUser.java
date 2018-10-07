package com.ecse321.RideShare.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ecse321.RideShare.model.User;



public class CreateUser {
	
	public static void main() {
		  
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
