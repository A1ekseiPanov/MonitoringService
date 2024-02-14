package repository.jdbc;

import repository.AuditRepository;
import entity.Audit;
import util.ConnectionUtil;

import java.sql.*;

public class JdbcAuditRepository implements AuditRepository {
    private static final String CREATE = """
            INSERT INTO dbo.audit (message)  values (?)""";

    @Override
    public Audit save(Audit audit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtil.get();
            preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);

            connection.setAutoCommit(false);
            preparedStatement.setString(1, audit.getMessage());
            preparedStatement.executeUpdate();
            connection.commit();

            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                audit.setId(key.getLong("id"));
            }
            return audit;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }
}
