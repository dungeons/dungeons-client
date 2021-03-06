/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.kingx.dungeons.graphics.cube;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.kingx.dungeons.App;
import com.kingx.dungeons.graphics.cube.Cube.AnimationType;
import com.kingx.dungeons.graphics.cube.Cube.CubeSideType;

/**
 * <p>
 * A SpriteBatch is used to draw 2D rectangles that reference a texture
 * (region). The class will batch the drawing commands and optimize them for
 * processing by the GPU.
 * </p>
 * 
 * <p>
 * To draw something with a SpriteBatch one has to first call the
 * {@link CubeRenderer#begin()} method which will setup appropriate render
 * states. When you are done with drawing you have to call
 * {@link CubeRenderer#end()} which will actually draw the things you specified.
 * </p>
 * 
 * <p>
 * All drawing commands of the SpriteBatch operate in screen coordinates. The
 * screen coordinate system has an x-axis pointing to the right, an y-axis
 * pointing upwards and the origin is in the lower left corner of the screen.
 * You can also provide your own transformation and projection matrices if you
 * so wish.
 * </p>
 * 
 * <p>
 * A SpriteBatch is managed. In case the OpenGL context is lost all OpenGL
 * resources a SpriteBatch uses internally get invalidated. A context is lost
 * when a user switches to another application or receives an incoming call on
 * Android. A SpriteBatch will be automatically reloaded after the OpenGL
 * context is restored.
 * </p>
 * 
 * <p>
 * A SpriteBatch is a pretty heavy object so you should only ever have one in
 * your program.
 * </p>
 * 
 * <p>
 * A SpriteBatch works with OpenGL ES 1.x and 2.0. In the case of a 2.0 context
 * it will use its own custom shader to draw all provided sprites. You can set
 * your own custom shader via {@link #setShader(ShaderProgram)}.
 * </p>
 * 
 * <p>
 * A SpriteBatch has to be disposed if it is no longer used.
 * </p>
 * 
 * @author mzechner
 */
public class CubeRenderer implements Disposable {
    private Mesh mesh;
    private Mesh[] buffers;

    private int idx = 0;
    private int currBufferIdx = 0;
    private float[] vertices;

    private boolean drawing = false;

    private final ShaderProgram shader;
    private boolean ownsShader;

    private boolean blendingDisabled = true;
    private int blendSrcFunc = GL11.GL_SRC_ALPHA;
    private int blendDstFunc = GL11.GL_ONE_MINUS_SRC_ALPHA;

    /** number of render calls since last {@link #begin()} **/
    public int renderCalls = 0;

    /**
     * number of rendering calls ever, will not be reset, unless it's done
     * manually
     **/
    public int totalRenderCalls = 0;

    /** the maximum number of sprites rendered in one batch so far **/
    private ShaderProgram customShader = null;
    private int vertexSize;
    private int cubeCount;
    private final int buffersCount;
    private int verticesPerQuad;
    private static int total;

    public static int getTotal() {
        int temp = total;
        total = 0;
        return temp;
    }

    /**
     * Constructs a new SpriteBatch. Sets the projection matrix to an
     * orthographic projection with y-axis point upwards, x-axis point to the
     * right and the origin being in the bottom left corner of the screen. The
     * projection will be pixel perfect with respect to the screen resolution.
     */
    public CubeRenderer() {
        this(1000);
    }

    /**
     * Constructs a SpriteBatch with the specified size and (if GL2) the default
     * shader. See {@link #CubeBatch(int, ShaderProgram)}.
     */
    public CubeRenderer(int size) {
        this(size, null);
    }

    /**
     * <p>
     * Constructs a new SpriteBatch. Sets the projection matrix to an
     * orthographic projection with y-axis point upwards, x-axis point to the
     * right and the origin being in the bottom left corner of the screen. The
     * projection will be pixel perfect with respect to the screen resolution.
     * </p>
     * 
     * <p>
     * The size parameter specifies the maximum size of a single batch in number
     * of sprites
     * </p>
     * 
     * <p>
     * The defaultShader specifies the shader to use. Note that the names for
     * uniforms for this default shader are different than the ones expect for
     * shaders set with {@link #setShader(ShaderProgram)}. See the
     * {@link #createDefaultShader()} method.
     * </p>
     * 
     * @param size
     *            the batch size in number of sprites
     * @param defaultShader
     *            the default shader to use. This is not owned by the
     *            SpriteBatch and must be disposed separately.
     */
    public CubeRenderer(int size, ShaderProgram defaultShader) {
        this(size, 1, defaultShader);
    }

    /**
     * Constructs a SpriteBatch with the specified size and number of buffers
     * and (if GL2) the default shader. See
     * {@link #CubeBatch(int, int, ShaderProgram)}.
     */
    public CubeRenderer(int size, int buffers) {
        this(size, buffers, null);
    }

    /**
     * <p>
     * Constructs a new SpriteBatch. Sets the projection matrix to an
     * orthographic projection with y-axis point upwards, x-axis point to the
     * right and the origin being in the bottom left corner of the screen. The
     * projection will be pixel perfect with respect to the screen resolution.
     * </p>
     * 
     * <p>
     * The size parameter specifies the maximum size of a single batch in number
     * of sprites
     * </p>
     * 
     * <p>
     * The defaultShader specifies the shader to use. Note that the names for
     * uniforms for this default shader are different than the ones expect for
     * shaders set with {@link #setShader(ShaderProgram)}. See the
     * {@link #createDefaultShader()} method.
     * </p>
     * 
     * @param size
     *            the batch size in number of sprites
     * @param buffers
     *            the number of buffers to use. only makes sense with VBOs. This
     *            is an expert function.
     * @param defaultShader
     *            the default shader to use. This is not owned by the
     *            SpriteBatch and must be disposed separately.
     */
    public CubeRenderer(int size, int buffers, ShaderProgram defaultShader) {
        this.cubeCount = size;
        this.buffersCount = buffers;
        reset();

        if (Gdx.graphics.isGL20Available() && defaultShader == null) {
            shader = createDefaultShader();
            ownsShader = true;
        } else
            shader = defaultShader;
    }

    /**
     * Returns a new instance of the default shader used by SpriteBatch for GL2
     * when no shader is specified.
     */
    static public ShaderProgram createDefaultShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color ;\n" //
                + "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
        return shader;
    }

    /**
     * Sets up the SpriteBatch for drawing. This will disable depth buffer
     * writting. It enables blending and texturing. If you have more texture
     * units enabled than the first one you have to disable them before calling
     * this. Uses a screen coordinate system by default where everything is
     * given in pixels. You can specify your own projection and modelview
     * matrices via {@link #setProjectionMatrix(Matrix4)} and
     * {@link #setTransformMatrix(Matrix4)}.
     */
    public void begin() {
        if (drawing)
            throw new IllegalStateException("you have to call SpriteBatch.end() first");
        renderCalls = 0;

        if (vertexSize != CubeVertex.size) {
            reset();
        }

        if (Gdx.graphics.isGL20Available()) {
            if (customShader != null)
                customShader.begin();
            else
                shader.begin();
        } else {
            Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        }

        idx = 0;
        drawing = true;
    }

    private void reset() {

        vertexSize = CubeVertex.size;
        verticesPerQuad = CubeSide.VERTS * vertexSize;
        vertices = new float[cubeCount * verticesPerQuad * Cube.QUADS];

        short[] indices = new short[cubeCount * CubeSide.INDICES * Cube.QUADS];
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 3);
            indices[i + 4] = (short) (j + 1);
            indices[i + 5] = (short) (j + 2);
        }

        this.buffers = new Mesh[buffersCount];

        for (int i = 0; i < buffersCount; i++) {
            this.buffers[i] = new Mesh(VertexDataType.VertexArray, false, vertices.length, indices.length, VertexAttribute.Position(),
                    VertexAttribute.TexCoords(0), VertexAttribute.Normal(), VertexAttribute.ColorUnpacked());
        }

        for (int i = 0; i < buffersCount; i++) {
            this.buffers[i].setIndices(indices);
        }
        mesh = this.buffers[0];

    }

    /**
     * Finishes off rendering. Enables depth writes, disables blending and
     * texturing. Must always be called after a call to {@link #begin()}
     */
    public void end() {
        if (!drawing)
            throw new IllegalStateException("SpriteBatch.begin must be called before end.");
        if (idx > 0)
            renderMesh();
        idx = 0;
        drawing = false;

        GLCommon gl = Gdx.gl;
        gl.glDepthMask(true);
        if (isBlendingEnabled())
            gl.glDisable(GL10.GL_BLEND);

        if (customShader != null)
            customShader.end();
        else
            shader.end();

    }

    public void draw(List<CubeRegion> regions, boolean onlyVisible, boolean drawHidden, boolean drawTransition) {
        for (CubeRegion region : regions) {
            draw(region, onlyVisible, drawHidden, drawTransition);
        }
    }

    public void draw(CubeRegion region, boolean onlyVisible) {
        for (Cube[] temp : region.getCubes()) {
            for (Cube cube : temp) {
                if (cube != null) {
                    draw(cube, onlyVisible);
                }
            }
        }
    }

    public void draw(CubeRegion region, boolean onlyVisible, boolean drawHidden, boolean drawTransition) {
        for (Cube[] temp : region.getCubes()) {
            for (Cube cube : temp) {
                if (cube != null) {
                    draw(cube, onlyVisible, drawHidden, drawTransition);
                }
            }
        }
    }

    public void draw(Cube cube, boolean onlyVisible, boolean drawHidden, boolean drawTransition) {
        //   System.out.println("Drawing " + cube.getX() + " : " + cube.getY());
        if (cube != null && cube.getType() != null) {
            //   System.out.println("not nul and its type is not null either");
            if (!onlyVisible || cube.isVisible()) {
                //    System.out.println("Cube is not suposed to be visible or is visible");
                if (!drawTransition) {
                    //System.out.println("not drawing transition");
                    boolean hidden = cube.isHidden() && !cube.isAnimation();// && cube.getY() != App.getTerrain().getHeight() - 1;
                    if (hidden == drawHidden) {
                        CubeSide[] cubeSides = cube.getSides();
                        for (CubeSide side : cubeSides) {
                            if (side.isVisible()) {
                                if (cube.scale < 1.0f) {
                                    draw(side, cube, hidden);
                                } else {
                                    draw(side, hidden, 1f);
                                }
                            }
                        }
                    }
                } else {
                    if (cube.isAnimation()) {
                        CubeSide[] cubeSides = cube.getSides();
                        for (CubeSide side : cubeSides) {
                            draw(side, true, cube.getAnimationType() == AnimationType.APPEAR ? cube.getAnimationFactor() : 1 - cube.getAnimationFactor());
                        }
                    }
                }

            }
        }

    }

    public void draw(Cube cube, boolean onlyVisible) {
        if (cube != null && cube.getType() != null) {
            if (!onlyVisible || cube.isVisible()) {
                CubeSide[] cubeSides = cube.getSides();
                for (CubeSide side : cubeSides) {
                    if (side.isVisible()) {
                        if (cube.scale < 1.0f) {
                            draw(side, cube, false);
                        } else {
                            draw(side, false, 1f);
                        }
                    }
                }

            }
        }

    }

    public void drawSubregion(CubeRegion region, int x, int y, int radius, boolean onlyVisible) {
        for (Cube[] temp : region.getCubes()) {
            for (Cube cube : temp) {
                if (cube.getX() >= x - radius && cube.getX() <= x + radius) {
                    if (cube.getY() >= y - radius && cube.getY() <= y + radius) {
                        draw(cube, onlyVisible, false, false);
                    }
                }
            }
        }
    }

    private void draw(CubeSide side, boolean hidden, float alpha) {

        for (CubeVertex cubeVert : side.getVerts()) {
            if (CubeVertex.isPositionAttribute()) {
                float[] position = alpha == 1 ? cubeVert.getPosition() : cubeVert.getPositionOffseted();
                vertices[idx++] = position[0];
                vertices[idx++] = position[1];
                vertices[idx++] = position[2];
            }

            if (CubeVertex.isTexCoordsAttribute()) {
                float[] texCords = hidden ? cubeVert.getDefaultTexCoords() : cubeVert.getTexCoords();
                vertices[idx++] = texCords[0];
                vertices[idx++] = texCords[1];
            }

            if (CubeVertex.isNormalAttribute()) {
                float[] normal = cubeVert.getNormal();
                vertices[idx++] = normal[0];
                vertices[idx++] = normal[1];
                vertices[idx++] = normal[2];
            }

            if (CubeVertex.isColorAttribute()) {
                float[] color = cubeVert.getColor();
                vertices[idx++] = color[0];
                vertices[idx++] = color[1];
                vertices[idx++] = color[2];
                vertices[idx++] = alpha;
            }
        }
    }

    private void draw(CubeSide side, Cube cube, boolean hidden) {
        for (CubeVertex cubeVert : side.getVerts()) {
            float[] position = cubeVert.getPosition();
            vertices[idx++] = position[0];

            if (side.getType() != CubeSideType.getCorrectFace(cube.getRegion(), CubeSideType.BACK) && position[1] > cube.mean.y) {
                vertices[idx++] = position[1] - App.UNIT + App.UNIT * cube.scale;
            } else {
                vertices[idx++] = position[1];
            }
            vertices[idx++] = position[2];

            if (CubeVertex.isTexCoordsAttribute()) {
                float[] texCords = hidden ? cubeVert.getDefaultTexCoords() : cubeVert.getTexCoords();
                vertices[idx++] = texCords[0];
                vertices[idx] = texCords[1];
                if (position[1] > cube.mean.y) {
                    if (side.getType() == CubeSideType.getCorrectFace(cube.getRegion(), CubeSideType.LEFT)
                            || side.getType() == CubeSideType.getCorrectFace(cube.getRegion(), CubeSideType.RIGHT)
                            || side.getType() == CubeSideType.getCorrectFace(cube.getRegion(), CubeSideType.FRONT)) {
                        vertices[idx] = texCords[1] + (App.UNIT - cube.scale) * 0.25f;
                    }
                }
                idx++;
            }

            if (CubeVertex.isNormalAttribute()) {
                float[] normal = cubeVert.getNormal();
                vertices[idx++] = normal[0];
                vertices[idx++] = normal[1];
                vertices[idx++] = normal[2];
            }

            if (CubeVertex.isColorAttribute()) {
                float[] color = cubeVert.getColor();
                vertices[idx++] = color[0];
                vertices[idx++] = color[1];
                vertices[idx++] = color[2];
                vertices[idx++] = color[3];
            }
            // return;
        }
    }

    /**
     * Causes any pending sprites to be rendered, without ending the
     * SpriteBatch.
     */
    public void flush() {
        renderMesh();
    }

    private void renderMesh() {
        if (idx == 0)
            return;

        renderCalls++;
        totalRenderCalls++;
        int quadsInBatch = idx / verticesPerQuad;
        if (quadsInBatch / Cube.QUADS > cubeCount)
            cubeCount = quadsInBatch;

        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(quadsInBatch * CubeSide.INDICES);

        total += idx;
        if (blendingDisabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            if (blendSrcFunc != -1)
                Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
        }

        if (customShader != null)
            mesh.render(customShader, App.DEBUG != null && App.isWireframe() ? GL10.GL_LINES : GL10.GL_TRIANGLES, 0, quadsInBatch * CubeSide.INDICES);
        else
            mesh.render(shader, App.DEBUG != null && App.isWireframe() ? GL10.GL_LINES : GL10.GL_TRIANGLES, 0, quadsInBatch * CubeSide.INDICES);

        idx = 0;
        currBufferIdx++;
        if (currBufferIdx == buffers.length)
            currBufferIdx = 0;
        mesh = buffers[currBufferIdx];
    }

    /** Disposes all resources associated with this SpriteBatch */
    @Override
    public void dispose() {
        for (int i = 0; i < buffers.length; i++)
            buffers[i].dispose();
        if (ownsShader && shader != null)
            shader.dispose();
    }

    /**
     * Sets the shader to be used in a GLES 2.0 environment. Vertex position
     * attribute is called "a_position", the texture coordinates attribute is
     * called called "a_texCoord0", the color attribute is called "a_color". See
     * {@link ShaderProgram#POSITION_ATTRIBUTE},
     * {@link ShaderProgram#COLOR_ATTRIBUTE} and
     * {@link ShaderProgram#TEXCOORD_ATTRIBUTE} which gets "0" appened to
     * indicate the use of the first texture unit. The combined transform and
     * projection matrx is is uploaded via a mat4 uniform called "u_projTrans".
     * The texture sampler is passed via a uniform called "u_texture".</p>
     * 
     * Call this method with a null argument to use the default shader.</p>
     * 
     * This method will flush the batch before setting the new shader, you can
     * call it in between {@link #begin()} and {@link #end()}.
     * 
     * @param shader
     *            the {@link ShaderProgram} or null to use the default shader.
     */
    public void setShader(ShaderProgram shader) {
        if (drawing) {
            flush();
            if (customShader != null)
                customShader.end();
            else
                this.shader.end();
        }
        customShader = shader;
        if (drawing) {
            if (customShader != null)
                customShader.begin();
            else
                this.shader.begin();
        }
    }

    /** @return whether blending for sprites is enabled */
    public boolean isBlendingEnabled() {
        return !blendingDisabled;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    /** Disables blending for drawing sprites. */
    public void disableBlending() {
        if (blendingDisabled)
            return;
        renderMesh();
        blendingDisabled = true;
    }

    /** Enables blending for sprites */
    public void enableBlending() {
        if (!blendingDisabled)
            return;
        renderMesh();
        blendingDisabled = false;
    }

    /**
     * Sets the blending function to be used when rendering sprites.
     * 
     * @param srcFunc
     *            the source function, e.g. GL11.GL_SRC_ALPHA. If set to -1,
     *            SpriteBatch won't change the blending function.
     * @param dstFunc
     *            the destination function, e.g. GL11.GL_ONE_MINUS_SRC_ALPHA
     */
    public void setBlendFunction(int srcFunc, int dstFunc) {
        renderMesh();
        blendSrcFunc = srcFunc;
        blendDstFunc = dstFunc;
    }

}
