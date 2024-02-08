import util.ConnectionUtil;
import util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class MonitoringServiceApp {
    public static void main(String[] args) {
        try (Connection connection = ConnectionUtil.get()){
            LiquibaseUtil.update(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Context context = new Context();
        context.run();
    }
}