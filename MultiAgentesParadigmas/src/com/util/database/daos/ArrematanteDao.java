package com.util.database.daos;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.util.database.pojos.Arrematante;

public class ArrematanteDao {
	
	private SessionFactory session;
	
	public ArrematanteDao()
	{
		try
		{
			Configuration config= new Configuration().configure();
			ServiceRegistry serviceRegisty=(ServiceRegistry) new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
			
			session=config.buildSessionFactory((org.hibernate.service.ServiceRegistry) serviceRegisty);
			
		}catch(Throwable ex) 
		{ 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         
	         throw new ExceptionInInitializerError(ex); 
	         
		}
	}
	
	public void addArrematante(Arrematante arrematante)
	{
		Session session = this.session.openSession();
		Transaction tx=null;
		
		try
		{
			tx=session.beginTransaction();
			session.save(arrematante);
			
			tx.commit();
			
			
		}catch(HibernateException e) 
		{
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         
	         
	      }finally {
	         session.close(); 
	      }

		
		/*
		
			   public void listEmployees( ){
			      Session session = factory.openSession();
			      Transaction tx = null;
			      try{
			         tx = session.beginTransaction();
			         List employees = session.createQuery("FROM Employee").list(); 
			         for (Iterator iterator = 
			                           employees.iterator(); iterator.hasNext();){
			            Employee employee = (Employee) iterator.next(); 
			            System.out.print("First Name: " + employee.getFirstName()); 
			            System.out.print("  Last Name: " + employee.getLastName()); 
			            System.out.println("  Salary: " + employee.getSalary()); 
			         }
			         tx.commit();
			      }catch (HibernateException e) {
			         if (tx!=null) tx.rollback();
			         e.printStackTrace(); 
			      }finally {
			         session.close(); 
			      }
			   }
			   // Method to UPDATE salary for an employee //
			   public void updateEmployee(Integer EmployeeID, int salary ){
			      Session session = factory.openSession();
			      Transaction tx = null;
			      try{
			         tx = session.beginTransaction();
			         Employee employee = 
			                    (Employee)session.get(Employee.class, EmployeeID); 
			         employee.setSalary( salary );
					 session.update(employee); 
			         tx.commit();
			      }catch (HibernateException e) {
			         if (tx!=null) tx.rollback();
			         e.printStackTrace(); 
			      }finally {
			         session.close(); 
			      }
			   }
			   // Method to DELETE an employee from the records //
			   public void deleteEmployee(Integer EmployeeID){
			      Session session = factory.openSession();
			      Transaction tx = null;
			      try{
			         tx = session.beginTransaction();
			         Employee employee = 
			                   (Employee)session.get(Employee.class, EmployeeID); 
			         session.delete(employee); 
			         tx.commit();
			      }catch (HibernateException e) {
			         if (tx!=null) tx.rollback();
			         e.printStackTrace(); 
			      }finally {
			         session.close(); 
			      }
			   }

		*/
		
	}
	
	
	
	

}
