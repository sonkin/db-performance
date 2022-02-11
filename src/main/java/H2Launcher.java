import org.h2.tools.Server;

import java.sql.SQLException;

public class H2Launcher {

    public static void main(String[] args) throws SQLException {
        Server h2Server = Server.createTcpServer().start();
        if (h2Server.isRunning(true)) {
            System.out.println(h2Server.getStatus());
        } else {
            throw new RuntimeException("Could not start H2 server.");
        }
    }
}
