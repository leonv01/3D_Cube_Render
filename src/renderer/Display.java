package renderer;

import renderer.input.ClickType;
import renderer.input.Mouse;
import renderer.point.MyPoint;
import renderer.shapes.MyPolygon;
import renderer.shapes.Tetrahedron;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Display extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "3D Renderer";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;

    private Tetrahedron tetra;

    private final Mouse mouse;

    public Display() {
        this.frame = new JFrame(title);

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.mouse = new Mouse();

        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.start();
    }

    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "renderer.Display");
        this.thread.start();
    }

    public synchronized void stop() {
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
                render();
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }
        stop();
    }

    private void init() {

        int s = 200;
        MyPoint p1 = new MyPoint(s / 2.0, -s / 2.0, -s / 2.0);
        MyPoint p2 = new MyPoint(s / 2.0, s / 2.0, -s / 2.0);
        MyPoint p3 = new MyPoint(s / 2.0, s / 2.0, s / 2.0);
        MyPoint p4 = new MyPoint(s / 2.0, -s / 2.0, s / 2.0);
        MyPoint p5 = new MyPoint(-s / 2.0, -s / 2.0, -s / 2.0);
        MyPoint p6 = new MyPoint(-s / 2.0, s / 2.0, -s / 2.0);
        MyPoint p7 = new MyPoint(-s / 2.0, s / 2.0, s / 2.0);
        MyPoint p8 = new MyPoint(-s / 2.0, -s / 2.0, s / 2.0);

        this.tetra = new Tetrahedron(
                new MyPolygon(Color.RED, p1, p2, p3, p4),
                new MyPolygon(Color.BLUE, p5, p6, p7, p8),
                new MyPolygon(Color.GREEN, p1, p2, p6, p5),
                new MyPolygon(Color.YELLOW, p1, p5, p8, p4),
                new MyPolygon(Color.ORANGE, p2, p6, p7, p3),
                new MyPolygon(Color.cyan, p4, p3, p7, p8)
        );
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        tetra.render(g);

        g.dispose();
        bs.show();
    }

    ClickType prevMouse = ClickType.Unknown;
    int initalX, initalY;
    double mouseSensitivity = 2.5;
    private void update() {

        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if (this.mouse.getButton() == ClickType.Leftclick) {
            int xDiff = x - initalX;
            int yDiff = y - initalY;

            this.tetra.rotate(true, 0, -yDiff / mouseSensitivity, -xDiff / mouseSensitivity);
        } else if (this.mouse.getButton() == ClickType.RightClick) {
            int xDiff = x - initalX;

            this.tetra.rotate(true, xDiff/mouseSensitivity, 0,0);
        }
        else if(this.mouse.getButton() == ClickType.Unknown && System.currentTimeMillis() > 8 * Math.pow(10,11)){
            this.tetra.rotate(true, 1, 1,1);
        }
        initalX = x;
        initalY = y;
    }
}
