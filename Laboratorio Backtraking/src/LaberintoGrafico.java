import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class LaberintoGrafico extends JPanel {
    private int llamadas = 0;
    private int retrocesos = 0;
    private long inicio;
    private long fin;
    private final int TAM = 30;
    private int[][] laberinto;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("---Laboratorio BackTraking---");
        System.out.println("1. Probar con laberinto 5x5");
        System.out.println("2. Probar con laberinto 10x10");
        System.out.println("3. Probar con laberinto 20x20");
        System.out.println("Ingrese opcion");
        LaberintoGrafico panel = new LaberintoGrafico();

        switch (sc.nextLine()) {
            case "1":
                panel.laberinto = new int[][] {
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 1, 1, 0},
                        {0, 0, 0, 0, 2}
                };
                break;
            case "2":
                panel.laberinto = new int[][] {
                        {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                        {1, 0, 1, 1, 0, 1, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 0, 1, 0, 1, 0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 0, 1, 0, 0, 0, 2}
                };
                break;
            case "3":
                panel.laberinto = new int[][] {
                        {0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0},
                        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                        {1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1},
                        {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
                        {0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
                        {0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                        {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                        {0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
                        {0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1},
                        {1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2}
                };
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        JFrame ventana = new JFrame("Backtracking - Laberinto");
        ventana.add(panel);
        ventana.setSize(
                panel.laberinto[0].length * panel.TAM + 50,
                panel.laberinto.length    * panel.TAM + 50
        );
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);

        new Thread(() -> {
            panel.inicio = System.nanoTime();


            boolean solucion = panel.resolver(0, 0);

            panel.fin = System.nanoTime();

            System.out.println("Solución encontrada: " + solucion);
            System.out.println("Llamadas recursivas: " + panel.llamadas);
            System.out.println("Retrocesos:          " + panel.retrocesos);
            System.out.println("Tiempo (ms):         " +
                    (panel.fin - panel.inicio) / 1_000_000.0);
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
        llamadas++;
        repaint();
        dormir();

        if (fila < 0 || col < 0 || fila >= laberinto.length || col >= laberinto[0].length)
            return false;

        if (laberinto[fila][col] == 1 || laberinto[fila][col] == 9 || laberinto[fila][col] == 5)
            return false;

        if (laberinto[fila][col] == 2)
            return true;

        laberinto[fila][col] = 9;
        repaint();
        dormir();
        if (resolver(fila + 1, col)) return true;
        if (resolver(fila, col - 1)) return true;
        if (resolver(fila - 1, col)) return true;
        if (resolver(fila, col + 1)) return true;


        retrocesos++;
        laberinto[fila][col] = 5;
        repaint();
        dormir();

        return false;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto == null) return;

        for (int fila = 0; fila < laberinto.length; fila++) {
            for (int col = 0; col < laberinto[0].length; col++) {
                switch (laberinto[fila][col]) {
                    case 0: g.setColor(Color.WHITE); break;
                    case 1: g.setColor(Color.BLACK); break;
                    case 2: g.setColor(Color.BLUE);  break;
                    case 9: g.setColor(Color.GREEN); break;
                    case 5: g.setColor(Color.RED);   break;
                }
                g.fillRect(col * TAM, fila * TAM, TAM, TAM);
                g.setColor(Color.GRAY);
                g.drawRect(col * TAM, fila * TAM, TAM, TAM);
            }
        }
    }
}