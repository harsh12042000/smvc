package smvc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import smvc.model.City;
import smvc.model.Customer;
import smvc.model.State;

public class CustomerDaoImpl implements CustomerDao {

	private JdbcTemplate jdbcTemplate;

	public CustomerDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Customer> list(final String orderByColumn, final String order, int pageid, int total) {

		String sql = "select c.*, s.state as StateName, c.city_id as cityId, ci.city as cityName "
				+ "from tbl_customer c "
				+ "left join tbl_state s on s.id = c.state_id "
				+ "left join tbl_city ci on ci.id = c.city_id ";
		String orderBy = orderByColumn;
		if(orderByColumn.equalsIgnoreCase("id")) {
			orderBy = "c." + orderByColumn ;
		}
		sql += "order by " + orderBy + " " + order 
				+ " limit " +(pageid-1)+","+total; 
		
		List <Customer> customers = jdbcTemplate.query(sql, new CustomerMapper()); 
		return customers;
	}

	@Override
	public void saveOrUpdate(Customer customer) {
		
		String cityId = null;
        
        Optional<City> checkCityId = Optional.ofNullable(customer.getCity());
        if(checkCityId.isPresent() && checkCityId.get().getId() != null) {
            cityId = checkCityId.get().getId().toString();
        }
		
		if (customer.getId() > 0) {
	        String sql = "UPDATE tbl_customer SET first_name=?, last_name=?, email=?, gender=?, mobile_number=?, dob=?, url=?, address_1=?, address_2=?, address_3=?, "
                    + "city_id=?, pincode=?, state_id=? WHERE id=?";
	        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getEmail(),
					customer.getGender(), customer.getMobileNo(), customer.getDob(), customer.getUrl(),
					customer.getAddress1(), customer.getAddress2(), customer.getAddress3(), cityId, customer.getPincode(),
					(customer.getState().getId().equals("") ? null : customer.getState().getId()), customer.getId());
		} else {
			String sql = "INSERT INTO tbl_customer (first_name, last_name, email, gender, mobile_number, dob, url, address_1, address_2, address_3, city_id, pincode, state_id)"
					+ " VALUES (?, ?, ?, ?, ?, ? ,? ,?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getEmail(),
					customer.getGender(), customer.getMobileNo(), customer.getDob(), customer.getUrl(),
					customer.getAddress1(), customer.getAddress2(), customer.getAddress3(), cityId, customer.getPincode(),
					(customer.getState().getId().equals("") ? null : customer.getState().getId()));
		}
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM tbl_customer WHERE id=?";
	    jdbcTemplate.update(sql, id);
	}

	@Override
	public Customer get(int id) {
		String sql = "select c.*, s.state as StateName, c.city_id as cityId, ci.city as cityName "
				+ "from tbl_customer c "
				+ "left join tbl_state s on s.id = c.state_id "
				+ "left join tbl_city ci on ci.id = c.city_id "
				+ " where c.id = ?";

		Object[] params = new Object[1];
		params[0] = id;
	    return jdbcTemplate.queryForObject(sql, params, new CustomerMapper());
	}

	@Override
	public List<State> getStates() {
		String sql = "SELECT * FROM tbl_state";
		List<State> listStates = jdbcTemplate.query(sql, new StateMapper());

		return listStates;
	}

	@Override
	public int getCount() {
		final String sql = "select count(*) from tbl_customer"; 
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}

	@Override
	public List<City> getCities(String state) {
		final String sql = "select * from tbl_city where state_id = ?";
		Object[] params = new Object[1];
		params[0] = state;
		List<City> cities = jdbcTemplate.query(sql, params, new CityMapper());
		return cities;
	}
	
	@Override
	public List<Customer> getSearchRecords(String colName, String value, int pageid, int total, String orderByColumn, String order) {
		
		StringBuilder sql = new StringBuilder("select c.*, s.state as StateName, c.city_id as cityId, ci.city as cityName "
	            + "from tbl_customer c "
	            + "left join tbl_state s on s.id = c.state_id "
	            + "left join tbl_city ci on ci.id = c.city_id ");
		
		List<Object> params = new ArrayList<Object>();
		
		if(colName.equals("name")) {
			sql.append("where first_name like ? OR last_name like ?");
			params.add(value + "%");
			params.add(value + "%");
		} else if(colName.equals("sex")) {
			sql.append("where gender like ?");
			params.add(value + "%");
		} else if(colName.equals("email")) {
			sql.append("where email like ?");
			params.add(value + "%");
		}
		
		String orderBy = orderByColumn;
		if(orderByColumn.equalsIgnoreCase("id")) {
			orderBy = "c." + orderByColumn ;
		}

		sql.append(" order by " + orderBy + " " + order);
		sql.append(" limit " + (pageid-1)+ "," +total);
	    
	    List<Customer> customers = jdbcTemplate.query(sql.toString(), params.toArray(), new CustomerMapper());
	    return customers;
	}

	@Override
	public int getTotalSearchRecords(String colName, String value) {
		
		StringBuilder sql = new StringBuilder("select count(*) "
	            + "from tbl_customer c "
	            + "left join tbl_state s on s.id = c.state_id "
	            + "left join tbl_city ci on ci.id = c.city_id ");
		
		List<Object> params = new ArrayList<Object>();
		
		if(colName.equals("name")) {
			sql.append("where first_name like ? OR last_name like ?");
			params.add(value + "%");
			params.add(value + "%");
		} else if(colName.equals("sex")) {
			sql.append("where gender like ?");
			params.add(value + "%");
		} else if(colName.equals("email")) {
			sql.append("where email like ?");
			params.add(value + "%");
		}
		return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
	}
}
