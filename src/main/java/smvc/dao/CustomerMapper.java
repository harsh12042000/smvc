package smvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import smvc.model.City;
import smvc.model.Customer;
import smvc.model.State;

final class CustomerMapper implements RowMapper<Customer>{

	  public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
	   
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   State state = new State();
		   City city = new City();
		   Customer customer = new Customer();
		   customer.setId(rs.getInt("id"));
		   customer.setFirstName(rs.getString("first_name"));
		   customer.setLastName(rs.getString("last_name"));
		   customer.setGender(rs.getString("gender"));
		   customer.setEmail(rs.getString("email"));
		   customer.setMobileNo(rs.getString("mobile_number"));
		   customer.setDob(sdf.format(rs.getDate("dob")));
		   customer.setAddress1(rs.getString("address_1"));
		   customer.setAddress2(rs.getString("address_2"));
		   
		   city.setId(rs.getInt("cityId"));
		   city.setCity(rs.getString("cityName"));
		   city.setState_id(rs.getString("state_id"));
		   
		   customer.setAddress3(rs.getString("address_3"));
		   customer.setPincode(rs.getString("pincode"));
		   customer.setUrl(rs.getString("url"));
		   
		   state.setId(rs.getString("state_id"));
		   state.setName(rs.getString("stateName"));
		   customer.setState(state);
		   customer.setCity(city);
		   return customer;
	  }
	  
}
