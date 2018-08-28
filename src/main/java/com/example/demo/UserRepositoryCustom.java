package com.example.demo;

import java.util.List;


public interface UserRepositoryCustom {

	List<String> sqlParseAndGetResults(String query);
}
