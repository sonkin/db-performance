import org.hsqldb.Server;

public class HSQLDBLauncher {

    public static void main(String[] args) {
            System.out.println("Starting Database");
            Server server = new Server();
            server = new Server();
            server.setDatabasePath(0, "~/books.db");
            server.setDatabaseName(0, "books");
            server.start();
        }
}
