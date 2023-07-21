import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JFrame implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private static final int RAND_POS = WIDTH / DOT_SIZE;
    private static final int DELAY = 100;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int appleX;
    private int appleY;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        initGame();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (key == KeyEvent.VK_UP && !downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                } else if (key == KeyEvent.VK_DOWN && !upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        });

        setVisible(true);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }

        placeApple();
    }

    private void placeApple() {
        int r = (int) (Math.random() * RAND_POS);
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * RAND_POS);
        appleY = r * DOT_SIZE;
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            placeApple();
        }
    }

    private void checkCollision() {
        for (int i = dots - 1; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        } else if (rightDirection) {
            x[0] += DOT_SIZE;
        } else if (upDirection) {
            y[0] -= DOT_SIZE;
        } else if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (inGame) {
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Arial", Font.BOLD, 16);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
