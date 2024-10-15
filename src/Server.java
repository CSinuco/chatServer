
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static List<ClienteHandler> clientes = new CopyOnWriteArrayList<>();
    private static final int PUERTO = 5000;
    private static final int HILOS = 50;
    public static void main(String[] args) {
    ExecutorService pool = Executors.newFixedThreadPool(HILOS);
    //Alternativa de Try with resources
    try (ServerSocket servidor = new ServerSocket(PUERTO)){
        
        System.out.println("Servidor iniciado, escuchando en el puerto "+ PUERTO);
        while (true) {
            Socket socket = servidor.accept();
            System.out.println("Nuevo cliente conectado: "+ socket.getInetAddress()); 

            ClienteHandler clienteHandler = new ClienteHandler(socket, clientes);
            clientes.add(clienteHandler);
            pool.execute(clienteHandler);
        }
    } catch (Exception e) {
        System.out.println("Error el el server "+ e);

    }
    finally{
        pool.shutdown();
    }


}
}
    

