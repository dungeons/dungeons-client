package com.kingx.dungeons.editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.kingx.dungeons.geom.MazeBuilder;

@SuppressWarnings("serial")
public class Editor extends JPanel {

    private final Map map;

    public Editor() {
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(240, 240, 240));
        map = new Map(MazeBuilder.getMaze(20));
    }

    /**
     * Add Mouse Motion Listener with drag function
     */
    private void addDragListeners() {
        MouseAdapter listener = new MouseAdapter() {

            private Point anchorPoint;

            @Override
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);

                repaint();

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {

                    int x = e.getX() / Map.BLOCK_SIZE;
                    int y = e.getY() / Map.BLOCK_SIZE;

                    map.switchBlock(x, y);
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    save();
                }
            }
        };
        addMouseMotionListener(listener);
        addMouseListener(listener);
    }

    protected void save() {
        if (JOptionPane.showConfirmDialog(null, "Do you want save ?") == JOptionPane.YES_OPTION) {
            // Serialize data object to a file
            try {
                ObjectOutput out = new ObjectOutputStream(new FileOutputStream("map.dng"));
                out.writeObject(map.getMaze());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            drawMap(g);
        }
    }

    private void drawMap(Graphics g) {
        for (int i = 0; i < map.getMaze().length; i++) {
            for (int j = 0; j < map.getMaze()[i].length; j++) {
                g.setColor(map.getMaze()[i][j] ? Color.DARK_GRAY : Color.GRAY);
                g.fillRect(i * Map.BLOCK_SIZE, j * Map.BLOCK_SIZE, Map.BLOCK_SIZE, Map.BLOCK_SIZE);
            }

        }
    }

    private void removeDragListeners() {
        for (MouseMotionListener listener : getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        for (MouseListener listener : getMouseListeners()) {
            removeMouseListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }

    private static final Dimension minimumSize = new Dimension(100, 100);
    private static final Dimension preferredSize = new Dimension(500, 500);
    private static final Dimension maximumSize = new Dimension(1000, 1000);

    @Override
    public Dimension getMinimumSize() {
        return minimumSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public Dimension getMaximumSize() {
        return maximumSize;
    }

}
