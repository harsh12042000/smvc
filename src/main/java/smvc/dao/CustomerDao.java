package smvc.dao;

import java.util.List;

import smvc.model.City;
import smvc.model.Customer;
import smvc.model.State;

public interface CustomerDao {

	public List<Customer> list(final String orderByColumn, final String order, int pageid, int total);
	
	public void saveOrUpdate(Customer customer);
	
	public void delete(int id);
	
	public Customer get(int id);

	public List<State> getStates();

	public int getCount();
	
	public List<City> getCities(String state);
	
	public List<Customer> getSearchRecords(String colName, String value, int pageid, int total, String colname, String orderby);

	public int getTotalSearchRecords(String colName, String searchValue);
}
