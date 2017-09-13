package org.thoughts.on.java.db;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class MyPostgreSQL9Dialect extends PostgreSQL9Dialect {

	public MyPostgreSQL9Dialect() {
		super();
		registerFunction("calculate", new StandardSQLFunction("calculate"));
	}
}
