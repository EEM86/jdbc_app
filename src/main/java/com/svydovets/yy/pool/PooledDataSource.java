package com.svydovets.yy.pool;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
@Setter
public class PooledDataSource extends PGSimpleDataSource {
  private Queue<Connection> pool;
  private final int poolSize = 10;

  @SneakyThrows
  public PooledDataSource(String url, String user, String pass) {
    pool = new ConcurrentLinkedDeque<>();
    this.setUrl(url);
    this.setUser(user);
    this.setPassword(pass);
    for (int i = 0; i < poolSize; i++) {
      pool.add(new ConnectionProxy(super.getConnection(), this));
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return pool.poll();
  }

}
