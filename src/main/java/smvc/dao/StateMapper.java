package smvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import smvc.model.State;

final class StateMapper implements RowMapper<State> {

	public State mapRow(ResultSet rs, int rowNum) throws SQLException {
		State state = new State();
		state.setName(rs.getString("state"));
		state.setId(rs.getString("id"));
		return state;
	}

}