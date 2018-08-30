package com.example.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;

@Component
public class SqlParser {
	String[] foundTables = new String[10000];
	String[] foundColumns = new String[10000];
	int foundTableCount = 0;
	int foundColumnsCount = 0;


	public String parse(String sqlQuery) {
		StringBuffer stringBuffer = new StringBuffer();
		TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
		sqlparser.sqltext = sqlQuery;

		int ret = sqlparser.parse();
		if (ret == 0) {

			TCustomSqlStatement stmt = null;
			for (int i = 0; i < sqlparser.sqlstatements.size(); i++) {
				analyzeStmt(sqlparser.sqlstatements.get(i));
			}

			String[] foundTables2 = new String[foundTableCount];
			for (int k1 = 0; k1 < foundTableCount; k1++) {
				foundTables[k1] = foundTables[k1].toLowerCase();
			}
			System.arraycopy(foundTables, 0, foundTables2, 0, foundTableCount);
			Set set = new HashSet(Arrays.asList(foundTables2));
			Object[] foundTables3 = set.toArray();
			Arrays.sort(foundTables3);

			String[] foundColumns2 = new String[foundColumnsCount];
			for (int k1 = 0; k1 < foundColumnsCount; k1++) {
				foundColumns[k1] = foundColumns[k1].toLowerCase();
			}
			System.arraycopy(foundColumns, 0, foundColumns2, 0, foundColumnsCount);
			// System.out.println("before sort:"+foundColumnsCount);

			Set set2 = new HashSet(Arrays.asList(foundColumns2));
			Object[] foundColumns3 = set2.toArray();
			Arrays.sort(foundColumns3);
			// System.out.println("after sort:"+foundColumns3.length);

			// System.out.println("Tables:");
			stringBuffer.append("Tables:\n");
			for (int j = 0; j < foundTables3.length; j++) {
				// System.out.println(foundTables3[j]);
				stringBuffer.append(foundTables3[j] + "\n");
			}

			// System.out.println("\nColumns:");
			stringBuffer.append("Columns:\n");
			for (int j = 0; j < foundColumns3.length; j++) {
				// System.out.println(foundColumns3[j]);
				stringBuffer.append(foundColumns3[j] + "\n");
			}

		} else {
			// System.out.println(sqlparser.getErrormessage());
			stringBuffer.append(sqlparser.getErrormessage() + "\n");
		}
		return stringBuffer.toString();
	}

	protected void analyzeStmt(TCustomSqlStatement stmt) {
		for (int i = 0; i < stmt.tables.size(); i++) {
			if (stmt.tables.getTable(i).isBaseTable()) {
				if ((stmt.dbvendor == EDbVendor.dbvmssql)
						&& ((stmt.tables.getTable(i).getFullName().equalsIgnoreCase("deleted"))
								|| (stmt.tables.getTable(i).getFullName().equalsIgnoreCase("inserted")))) {
					continue;
				}

				foundTables[foundTableCount] = stmt.tables.getTable(i).getFullName();
				foundTableCount++;
				for (int j = 0; j < stmt.tables.getTable(i).getObjectNameReferences().size(); j++) {
					foundColumns[foundColumnsCount] = stmt.tables.getTable(i).getFullName() + "."
							+ stmt.tables.getTable(i).getObjectNameReferences().getObjectName(j).getColumnNameOnly();
					foundColumnsCount++;
				}
			}
		}

		for (int i = 0; i < stmt.getStatements().size(); i++) {
			analyzeStmt(stmt.getStatements().get(i));
		}
	}
}
