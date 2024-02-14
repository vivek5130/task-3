
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE);
    static final int DELAY = 75;
    // holding snake body in array
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R'; // 'R'->right, 'L'>left, 'U'->up 'D'->Down
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this); // it is like a frame rate controle for game loop
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

            }
            // drawing apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // drawing snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { // dealing with head of snake
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics((g.getFont()));
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // aligning text at center
        } else {
            gameOver(g);
        }
    }

    // generate new coordinate for apple
    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // here the jframe cordinates lies in 4th quadrant so subracting makes move
                                         // upwards
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++; // score
            newApple();
        }
    }

    public void checkCollision() {
        // checks if head collide wit body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false; // trigger gameover
            }
        }
        // check if head touches border
        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
            System.out.println("Game over");
        }
    }

    public void gameOver(Graphics g) {
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics((g.getFont()));
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // aligning text at center
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics((g.getFont()));
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // aligning
                                                                                                             // text at
                                                                                                             // center
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();

        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // changing direction of snake using key presses
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
