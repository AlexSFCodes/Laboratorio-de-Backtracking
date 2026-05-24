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
    private int nodosExplorados = 0;
    private int profundidadActual = 0;
    private int profundidadMaxima = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("---Laboratorio BackTraking---");
        System.out.println("1. Probar con laberinto 5x5");
        System.out.println("2. Probar con laberinto 10x10");
        System.out.println("3. Probar con laberinto 20x20");
        System.out.println("4. Probar con laberinto sin solucion 5x5");
        System.out.println("5. Probar con laberinto sin solucion10x10");
        System.out.println("Ingrese opcion");
        LaberintoGrafico panel = new LaberintoGrafico();
        //RETO #1 Y #3
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

            case "4":
                panel.laberinto = new int[][] {
                        {0, 0, 0, 1, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 0, 1, 0},
                        {1, 1, 1, 1, 0},
                        {0, 0, 0, 0, 2}
                };
                break;

            case "5":
                panel.laberinto = new int[][] {
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                        {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
                        {0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 2}
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
                panel.laberinto.length    * panel.TAM + 110
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
        profundidadActual++;                                          // sube al entrar
        if (profundidadActual > profundidadMaxima)
            profundidadMaxima = profundidadActual;

        repaint();
        dormir();

        if (fila < 0 || col < 0 || fila >= laberinto.length || col >= laberinto[0].length) {
            profundidadActual--;                                      // baja al salir
            return false;
        }

        if (laberinto[fila][col] == 1 || laberinto[fila][col] == 9 || laberinto[fila][col] == 5) {
            profundidadActual--;
            return false;
        }

        if (laberinto[fila][col] == 2) {
            profundidadActual--;
            return true;
        }

        nodosExplorados++;                                            // celda vAlida nueva
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

        profundidadActual--;                                          // baja al retroceder
        return false;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto == null) return;

        // --- dibujar laberinto (igual que antes) ---
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

        // --- panel de estadisticas abajo RETO 4---
        int panelY = laberinto.length * TAM + 5;   // justo debajo del laberinto

        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, panelY, getWidth(), 60);      // fondo oscuro

        g.setFont(new Font("Consolas", Font.BOLD, 13));

        g.setColor(Color.WHITE);
        g.drawString("Llamadas recursivas: " + llamadas,       10, panelY + 18);

        g.setColor(Color.YELLOW);
        g.drawString("Nodos explorados:    " + nodosExplorados, 10, panelY + 34);

        g.setColor(Color.CYAN);
        g.drawString("Profundidad actual:  " + profundidadActual
                + "   |   Máxima: " + profundidadMaxima,    10, panelY + 50);
    }
}