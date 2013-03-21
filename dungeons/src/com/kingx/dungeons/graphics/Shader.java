package com.kingx.dungeons.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
    private static Shader instance;

    private enum ShaderType {
        vsh,
        fsh;
    }

    private static class ShaderStructure {
        private final String name;
        private String vertex;
        private String fragment;
        private ShaderProgram program;

        public ShaderStructure(String name) {
            this.name = name;
        }

        public void setVertex(String vertex) {
            this.vertex = vertex;
            compile();
        }

        public void setFragment(String fragment) {
            this.fragment = fragment;
            compile();
        }

        private void compile() {
            if (vertex != null && fragment != null) {
                System.out.println("Compiling shader [" + name + "]");
                program = new ShaderProgram(vertex, fragment);
                if (!program.isCompiled()) {
                    try {
                        throw new ShaderException(name, program);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public ShaderProgram getShader() {
            return program;
        }

    }

    private final HashMap<String, ShaderStructure> map = new HashMap<String, ShaderStructure>();

    public Shader() {
        FileHandle dirHandle;

        // Eclipse side-effect of linking assets, assets are copied in bin folder
        if (Gdx.app.getType() == ApplicationType.Android) {
            dirHandle = Gdx.files.internal("data/shaders");
        } else {
            // ApplicationType.Desktop ..
            dirHandle = Gdx.files.internal("./bin/data/shaders");
        }

        FileHandle[] list = dirHandle.list();

        for (FileHandle f : list) {

            String ext = f.extension();
            String name = f.nameWithoutExtension();

            ShaderStructure ss;
            if (!map.containsKey(name)) {
                ss = new ShaderStructure(name);
            } else {
                ss = map.get(name);
            }

            switch (ShaderType.valueOf(ext)) {
                case vsh:
                    ss.setVertex(f.readString());
                    break;
                case fsh:
                    ss.setFragment(f.readString());
                    break;
            }

            if (!map.containsKey(name)) {
                map.put(name, ss);
            }

        }
    }

    private ShaderStructure getStructure(String name) {
        ShaderStructure value = map.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Specified Shader does not exists: " + name);
        }
        return value;
    }

    public static ShaderProgram getShader(String name) {
        if (instance == null) {
            instance = new Shader();
        }
        return instance.getStructure(name).getShader();
    }

}
