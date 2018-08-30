package com.example.demo;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
	@Autowired
	SqlParser sqlParser;

	/**
	 * To get all email from the DB
	 * 
	 * @throws ParseException
	 */
	@Override
	public String doSqlParse(String sqlQuery) {
		return sqlParser.parse(sqlQuery);
	}

}
