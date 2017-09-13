package org.thoughts.on.java.dialect;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL94Dialect;

public class MyPostgreSQL94Dialect extends PostgreSQL94Dialect {

	public MyPostgreSQL94Dialect() {
		super();
		this.registerColumnType(Types.OTHER, "jsonb");
	}
}
