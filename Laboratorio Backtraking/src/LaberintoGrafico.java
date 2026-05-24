import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.Arrays;

public class LaberintoGrafico extends JPanel {

    // variables para contar lo que hace el algoritmo
    private int llamadas = 0;
    private int retrocesos = 0;
    private long inicio;
    private long fin;

    // tamanio de cada celda en pixeles
    private final int TAM = 30;

    // el laberinto como matriz de enteros
    // 0 = libre, 1 = pared, 2 = salida, 9 = camino actual, 5 = retroceso
    private int[][] laberinto;

    // contadores para el panel de estadisticas (RETO 4)
    private int nodosExplorados = 0;
    private int profundidadActual = 0;
    private int profundidadMaxima = 0;

    // coordenadas de la salida, necesarias para la heuristica (RETO 5)
    private int filaSalida;
    private int colSalida;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // menu de opciones
        System.out.println("---Laboratorio BackTraking---");
        System.out.println("1. Probar con laberinto 5x5");
        System.out.println("2. Probar con laberinto 10x10");
        System.out.println("3. Probar con laberinto 20x20");
        System.out.println("4. Probar con laberinto sin solucion 5x5");
        System.out.println("5. Probar con laberinto sin solucion10x10");
        System.out.println("Ingrese opcion");

        LaberintoGrafico panel = new LaberintoGrafico();

        // RETO 1 y 3 - diferentes tamanios de laberinto
        switch (sc.nextLine()) {

            case "1":
                // laberinto pequeño 5x5, tiene solucion
                panel.laberinto = new int[][] {
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 1, 1, 0},
                        {0, 0, 0, 0, 2}
                };
                break;

            case "2":
                // laberinto mediano 10x10, tiene solucion
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
                // laberinto grande 20x20, tiene solucion
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
                // laberinto 5x5 SIN solucion
                // la fila 3 es un muro completo que bloquea el acceso a la salida
                panel.laberinto = new int[][] {
                        {0, 0, 0, 1, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 0, 1, 0},
                        {1, 1, 1, 1, 0},
                        {0, 0, 0, 0, 2}
                };
                break;

            case "5":
                // laberinto 10x10 SIN solucion
                // la columna 5 es una pared vertical que divide el laberinto en dos
                // el inicio esta a la izquierda y la salida a la derecha, imposible cruzar
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
                System.out.println("Opcion no valida.");
                return;
        }

        // buscar donde esta el 2 en el laberinto para guardar las coordenadas de la salida
        // esto lo necesita la heuristica de distancia manhattan (RETO 5)
        for (int i = 0; i < panel.laberinto.length; i++) {
            for (int j = 0; j < panel.laberinto[0].length; j++) {
                if (panel.laberinto[i][j] == 2) {
                    panel.filaSalida = i;
                    panel.colSalida  = j;
                }
            }
        }

        // crear la ventana con el tamanio justo para el laberinto + el panel de stats
        JFrame ventana = new JFrame("Backtracking - Laberinto");
        ventana.add(panel);
        ventana.setSize(
                panel.laberinto[0].length * panel.TAM + 50,
                panel.laberinto.length    * panel.TAM + 110  // espacio extra para las estadisticas
        );
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);

        // correr el algoritmo en un hilo separado para que la ventana no se congele
        new Thread(() -> {
            panel.inicio = System.nanoTime();

            boolean solucion = panel.resolver(0, 0);

            panel.fin = System.nanoTime();

            // imprimir resultados en consola al terminar
            System.out.println("Solucion encontrada: " + solucion);
            System.out.println("Llamadas recursivas: " + panel.llamadas);
            System.out.println("Retrocesos:          " + panel.retrocesos);
            System.out.println("Tiempo (ms):         " +
                    (panel.fin - panel.inicio) / 1_000_000.0);
        }).start();
    }

    // pausa para que la animacion se pueda ver paso a paso
    public void dormir() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean resolver(int fila, int col) {

        // contar cada vez que el metodo se llama
        llamadas++;

        // actualizar profundidad actual y guardar la maxima alcanzada (RETO 4)
        profundidadActual++;
        if (profundidadActual > profundidadMaxima)
            profundidadMaxima = profundidadActual;

        repaint();
        dormir();

        // si nos salimos del laberinto, no es un camino valido
        if (fila < 0 || col < 0 || fila >= laberinto.length || col >= laberinto[0].length) {
            profundidadActual--;
            return false;
        }

        // si es pared, ya fue visitado (9) o ya se retrocedio (5), tampoco sirve
        if (laberinto[fila][col] == 1 || laberinto[fila][col] == 9 || laberinto[fila][col] == 5) {
            profundidadActual--;
            return false;
        }

        // llegamos a la salida
        if (laberinto[fila][col] == 2) {
            profundidadActual--;
            return true;
        }

        // marcar la celda como parte del camino actual
        nodosExplorados++;
        laberinto[fila][col] = 9;
        repaint();
        dormir();

        // RETO 5 - heuristica distancia Manhattan
        // en vez de explorar siempre en el mismo orden, ordenamos los movimientos
        // segun cual nos acerca mas a la salida
        int[][] movimientos = {
                {fila + 1, col},  // abajo
                {fila, col + 1},  // derecha
                {fila - 1, col},  // arriba
                {fila, col - 1}   // izquierda
        };

        // el movimiento con menor distancia a la salida va primero
        Arrays.sort(movimientos, (a, b) ->
                distancia(a[0], a[1]) - distancia(b[0], b[1])
        );

        // probar cada movimiento en el orden que decidio la heuristica
        for (int[] mov : movimientos) {
            if (resolver(mov[0], mov[1])) return true;
        }

        // ninguno funciono, hacemos backtracking
        // marcamos la celda en rojo (5) para mostrar que fue un camino sin salida
        retrocesos++;
        laberinto[fila][col] = 5;
        repaint();
        dormir();

        profundidadActual--;
        return false;
    }

    // calcula la distancia manhattan entre una celda y la salida
    // entre mas chico el numero, mas cerca estamos de llegar
    private int distancia(int fila, int col) {
        return Math.abs(fila - filaSalida) + Math.abs(col - colSalida);
    }

    // dibuja el laberinto y el panel de estadisticas en pantalla
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto == null) return;

        // dibujar cada celda del laberinto con su color correspondiente
        for (int fila = 0; fila < laberinto.length; fila++) {
            for (int col = 0; col < laberinto[0].length; col++) {
                switch (laberinto[fila][col]) {
                    case 0: g.setColor(Color.WHITE); break; // celda libre
                    case 1: g.setColor(Color.BLACK); break; // pared
                    case 2: g.setColor(Color.BLUE);  break; // salida
                    case 9: g.setColor(Color.GREEN); break; // camino actual
                    case 5: g.setColor(Color.RED);   break; // retroceso
                }
                g.fillRect(col * TAM, fila * TAM, TAM, TAM);
                g.setColor(Color.GRAY);
                g.drawRect(col * TAM, fila * TAM, TAM, TAM);
            }
        }

        // panel de estadisticas en tiempo real (RETO 4)
        int panelY = laberinto.length * TAM + 5;

        // fondo oscuro para el panel
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, panelY, getWidth(), 60);

        g.setFont(new Font("Consolas", Font.BOLD, 13));

        // mostrar cada contador con su color
        g.setColor(Color.WHITE);
        g.drawString("Llamadas recursivas: " + llamadas, 10, panelY + 18);

        g.setColor(Color.YELLOW);
        g.drawString("Nodos explorados:    " + nodosExplorados, 10, panelY + 34);

        g.setColor(Color.CYAN);
        g.drawString("Profundidad actual:  " + profundidadActual
                + "   |   Maxima: " + profundidadMaxima, 10, panelY + 50);
    }
}