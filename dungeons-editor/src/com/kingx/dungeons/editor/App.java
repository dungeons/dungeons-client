package com.kingx.dungeons.editor;

import javax.swing.JFrame;

public class App extends JFrame {

    public App() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        App app = new App();
        app.setSize(800, 640);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Editor editor = new Editor();
        app.add(editor);

        app.validate();
        app.setVisible(true);
        app.pack();
    }

}
