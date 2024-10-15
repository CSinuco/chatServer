import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteChat {
    private static final String SERVIDOR = "localhost"; // Cambia esto si el servidor está en otra máquina
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVIDOR, PUERTO);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Hilo para recibir mensajes del servidor
            Thread recibirMensajes = new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = in.readLine()) != null) {
                        System.out.println(mensaje);
                    }
                } catch (IOException e) {
                    System.err.println("Error al recibir mensajes: " + e.getMessage());
                }
            });

            recibirMensajes.start();

            // Enviar mensajes al servidor
            String input;
            while ((input = teclado.readLine()) != null) {
                out.println(input);
                if (input.equalsIgnoreCase("/quit")) {
                    break; // Permite al usuario salir del chat
                }
            }

            // Cerrar recursos y finalizar
            recibirMensajes.join();
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("El hilo de recepción fue interrumpido: " + e.getMessage());
        }
    }
}
