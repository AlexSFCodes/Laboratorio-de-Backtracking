import javax.swing.*;
import java.awt.*;

public class LaberintoGrafico extends JPanel {
    // 1. Las variables globales ya estaban bien colocadas aquí
    private int llamadas = 0;
    private int retrocesos = 0;
    private long inicio;
    private long fin;

    private int[][] laberinto = {
            {0, 1, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 2}
    };

    private final int TAM = 80;

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Backtracking - Laberinto");
        LaberintoGrafico panel = new LaberintoGrafico();

        ventana.add(panel);
        ventana.setSize(520, 540);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);

        // Corregido: La medición debe envolver al hilo que ejecuta el algoritmo en segundo plano,
        // de lo contrario, la interfaz gráfica se congelaría y el tiempo daría error.
        new Thread(() -> {
            panel.inicio = System.nanoTime(); // <-- INICIO MEDICIÓN

            boolean solucion = panel.resolver(0, 0);

            panel.fin = System.nanoTime(); // <-- FIN MEDICIÓN

            // RESULTADOS (Se imprimen al terminar el hilo)
            System.out.println("Solución encontrada: " + solucion);
            System.out.println("Llamadas recursivas: " + panel.llamadas);
            System.out.println("Retrocesos: " + panel.retrocesos);
            System.out.println("Tiempo (ms): " + (panel.fin - panel.inicio) / 1_000_000.0);
        }).start();
    }

    public void dormir() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean resolver(int fila, int col) {
        // 2. ENTRADA A LA FUNCIÓN: Aquí contamos cada llamada recursiva
        llamadas++;

        repaint();
        dormir();

        // Fuera del tablero
        if (fila < 0 || col < 0 || fila >= laberinto.length || col >= laberinto[0].length) {
            return false;
        }

        // Pared o visitado
        if (laberinto[fila][col] == 1 || laberinto[fila][col] == 9 || laberinto[fila][col] == 5) {
            return false;
        }

        // Salida encontrada
        if (laberinto[fila][col] == 2) {
            return true;
        }

        // Marcar camino actual
        laberinto[fila][col] = 9;

        repaint();
        dormir();

        // Intentar caminos (Arriba, Derecha, Abajo, Izquierda)
        if (resolver(fila - 1, col)) return true;
        if (resolver(fila, col + 1)) return true;
        if (resolver(fila + 1, col)) return true;
        if (resolver(fila, col - 1)) return true;

        // 3. EN EL BACKTRACKING: Si los caminos fallan, retrocedemos
        retrocesos++;

        // BACKTRACKING VISUAL
        laberinto[fila][col] = 5;

        repaint();
        dormir();

        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int fila = 0; fila < laberinto.length; fila++) {
            for (int col = 0; col < laberinto[0].length; col++) {
                switch (laberinto[fila][col]) {
                    case 0: g.setColor(Color.WHITE); break;
                    case 1: g.setColor(Color.BLACK); break;
                    case 2: g.setColor(Color.BLUE); break;
                    case 9: g.setColor(Color.GREEN); break;
                    case 5: g.setColor(Color.RED); break;
                }
                g.fillRect(col * TAM, fila * TAM, TAM, TAM);
                g.setColor(Color.GRAY);
                g.drawRect(col * TAM, fila * TAM, TAM, TAM);
            }
        }
    }
}