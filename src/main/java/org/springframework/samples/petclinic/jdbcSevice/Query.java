package org.springframework.samples.petclinic.jdbcSevice;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Query extends Connexion{

	public static void insertIntoDelete(String sql, Object[] values) throws ClassNotFoundException {
		try {
			PreparedStatement preparedStatement = connect().prepareStatement(sql);
			setPreparedStatement(preparedStatement, values);
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}finally {
			disconnect();
		}
	}

	private static void setPreparedStatement(PreparedStatement preparedStatement, Object[] object) throws SQLException {
		for (int i = 0; i< object.length; i++){
			if (object[i] instanceof String){
				preparedStatement.setString(i+1, (String)object[i]);
			}else if (object[i] instanceof Integer){
				preparedStatement.setInt(i+1, (Integer)object[i]);
			}else {
				preparedStatement.setDate(i+1, (Date)object[i]);
			}
		}
		preparedStatement.execute();
	}
}
