import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JTextArea;
import java.io.*;

public class GestorTurnos {
    private LinkedList<Cliente> colaClientes; 
    private List<Cliente> historialAtendidos; 
    private int totalAtendidos = 0;
    private int contadorNormal = 1;
    private int contadorPreferencial = 1;
    
    // Archivos para guardar los datos
    private final String ARCHIVO_HISTORIAL = "historial_atendidos.txt";
    private final String ARCHIVO_FILA = "fila_guardada.txt";

    public GestorTurnos() {
        this.colaClientes = new LinkedList<>();
        this.historialAtendidos = new ArrayList<>();
        cargarFilaDesdeArchivo(); // Al iniciar, carga los datos guardados
    }

    // NUEVO: Genera el turno y recibe los datos
    public String agregarCliente(String nombre, String id, String tramite, boolean esPreferencial) {
        String turno;
        if (esPreferencial) {
            turno = String.format("P-%03d", contadorPreferencial++);
            colaClientes.addFirst(new Cliente(turno, nombre, id, tramite, esPreferencial));
        } else {
            turno = String.format("A-%03d", contadorNormal++);
            colaClientes.addLast(new Cliente(turno, nombre, id, tramite, esPreferencial));
        }
        return turno; // Retornamos el turno para mostrarlo en el Ticket
    }

    public Cliente buscarCliente(String identificacion) {
        for (Cliente c : colaClientes) {
            if (c.getIdentificacion().equals(identificacion)) return c;
        }
        return null;
    }

    public boolean cancelarTurno(String identificacion) {
        Cliente cliente = buscarCliente(identificacion);
        if (cliente != null) {
            colaClientes.remove(cliente);
            return true;
        }
        return false;
    }

    public void atenderClienteEnArea(JTextArea area) {
        if (colaClientes.isEmpty()) {
            area.setText("⚠️ No hay clientes en la fila para atender.");
            return;
        }
        Cliente atendido = colaClientes.poll(); 
        historialAtendidos.add(atendido);
        totalAtendidos++;
        
        // Guardar en archivo de texto
        guardarEnHistorial(atendido);

        area.setText("🔔 ATENDIENDO A:\n" + atendido + "\n");
        area.append("⏱️ Tiempo de espera: " + atendido.getTiempoEspera() + "\n\n");
        mostrarFilaEnArea(area, "--- FILA RESTANTE ---");
    }

    private void guardarEnHistorial(Cliente c) {
        try (FileWriter fw = new FileWriter(ARCHIVO_HISTORIAL, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(c.getTurno() + " | " + c.getNombre() + " | " + c.getIdentificacion() + " | " + c.getTipoTramite());
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE PERSISTENCIA DE LA FILA
    // ==========================================
    public void guardarFilaEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_FILA))) {
            for (Cliente c : colaClientes) {
                pw.println(c.getTurno() + ";" + c.getNombre() + ";" + c.getIdentificacion() + ";" + c.getTipoTramite() + ";" + c.isEsPreferencial());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar la fila: " + e.getMessage());
        }
    }

    private void cargarFilaDesdeArchivo() {
        File file = new File(ARCHIVO_FILA);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    String turno = partes[0];
                    String nombre = partes[1];
                    String id = partes[2];
                    String tramite = partes[3];
                    boolean preferencial = Boolean.parseBoolean(partes[4]);
                    
                    colaClientes.add(new Cliente(turno, nombre, id, tramite, preferencial));
                    
                    if (preferencial) {
                        int num = Integer.parseInt(turno.split("-")[1]);
                        if (num >= contadorPreferencial) contadorPreferencial = num + 1;
                    } else {
                        int num = Integer.parseInt(turno.split("-")[1]);
                        if (num >= contadorNormal) contadorNormal = num + 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la fila: " + e.getMessage());
        }
    }
    // ==========================================

    public void mostrarFilaEnArea(JTextArea area, String titulo) {
        if (colaClientes.isEmpty()) {
            area.append("\n⚠️ La fila está vacía.\n");
            return;
        }
        area.append("\n" + titulo + "\n");
        int posicion = 1;
        for (Cliente c : colaClientes) {
            area.append(posicion + ". " + c + "\n");
            posicion++;
        }
    }

    public void mostrarHistorialEnArea(JTextArea area) {
        if (historialAtendidos.isEmpty()) {
            area.setText("⚠️ No hay clientes atendidos en esta sesión.");
            return;
        }
        area.setText("--- HISTORIAL DE ATENDIDOS (Sesión actual) ---\n");
        for (Cliente c : historialAtendidos) {
            area.append(c + "\n");
        }
    }

    public void mostrarEstadisticas(JTextArea area) {
        area.setText("📊 ESTADÍSTICAS DEL SISTEMA\n\n");
        area.append("Total de clientes atendidos: " + totalAtendidos + "\n");
        area.append("Clientes actualmente en espera: " + colaClientes.size() + "\n\n");
        
        long retiros = historialAtendidos.stream().filter(c -> c.getTipoTramite().equalsIgnoreCase("Retiro")).count();
        long consignaciones = historialAtendidos.stream().filter(c -> c.getTipoTramite().equalsIgnoreCase("Consignación")).count();
        
        area.append("Trámites más solicitados (en historial):\n");
        area.append("- Retiros: " + retiros + "\n");
        area.append("- Consignaciones: " + consignaciones + "\n");
    }

    // Métodos para las métricas y el historial
    public int getCantidadEnEspera() { return colaClientes.size(); }
    public int getTotalAtendidos() { return totalAtendidos; }
    public List<Cliente> getHistorialAtendidos() { return historialAtendidos; }
}