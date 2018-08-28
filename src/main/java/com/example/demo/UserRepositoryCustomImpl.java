package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * To get all email from the DB
	 */
	@Override
	public List<String> sqlParseAndGetResults(String sqlQuery) {
		/*
		 * Query query = entityManager.createQuery(sqlQuery); return
		 * query.getResultList();
		 */
		return jdbcTemplate.queryForList(sqlQuery, String.class);
	}

}
