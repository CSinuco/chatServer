import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClienteHandler implements Runnable {
    private Socket socket;
    private List<ClienteHandler> clientes;
    private PrintWriter out;
    private String nombreUsuario;

    public ClienteHandler(Socket socket, List<ClienteHandler> clientes) {
        this.socket = socket;
        this.clientes = clientes;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            this.out = writer;

            // Solicitar y establecer el nombre de usuario
            out.println("Ingrese su nombre de usuario:");
            nombreUsuario = in.readLine();
            System.out.println(nombreUsuario + " se ha conectado.");
            broadcast("** " + nombreUsuario + " se ha conectado **");

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println(nombreUsuario + ": " + mensaje);
                broadcast(nombreUsuario + ": " + mensaje);
            }
        } catch (IOException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientes.remove(this);
            System.out.println(nombreUsuario + " se ha desconectado.");
            broadcast("** " + nombreUsuario + " se ha desconectado **");
        }
    }

    // Método para enviar mensajes a todos los clientes conectados
    private void broadcast(String mensaje) {
        for (ClienteHandler cliente : clientes) {
            cliente.out.println(mensaje);
        }
    }
}
