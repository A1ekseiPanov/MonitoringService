import util.ConnectionUtil;
import util.LiquibaseUtil;

public class MonitoringServiceApp {

    public static void main(String[] args) {
        LiquibaseUtil.update(ConnectionUtil.get());
        Context context = new Context();
        context.run();
    }
}