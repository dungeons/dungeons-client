package com.kingx.dungeons.graphics.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.graphics.Shader;

public class SpriteRenderer {

    public static final int VERTS_PER_QUAD = 4;
    public static final int INDICES_PER_QUAD = 6;
    public static final int QUADS = 1;
    public static final int VERTEX_SIZE = 9;

    private Mesh mesh;
    private Mesh[] buffers;

    private int idx = 0;
    private int currBufferIdx = 0;
    private float[] vertices;

    private final Matrix4 transformMatrix = new Matrix4();
    private final Matrix4 projectionMatrix = new Matrix4();
    private final Matrix4 combinedMatrix = new Matrix4();

    private boolean drawing = false;

    private boolean blendingDisabled = true;
    private int blendSrcFunc = GL11.GL_SRC_ALPHA;
    private int blendDstFunc = GL11.GL_ONE_MINUS_SRC_ALPHA;

    private final ShaderProgram shader;
    private boolean ownsShader;

    /** number of render calls since last {@link #begin()} **/
    public int renderCalls = 0;

    /**
     * number of rendering calls ever, will not be reset, unless it's done
     * manually
     **/
    public int totalRenderCalls = 0;

    /** the maximum number of sprites rendered in one batch so far **/
    public int maxSpritesInBatch = 0;
    private ShaderProgram customShader = null;
    private int spriteSize;
    private final int buffersCount;
    private int vertexSize;
    private int verticesPerQuad;

    /**
     * Constructs a new SpriteBatch. Sets the projection matrix to an
     * orthographic projection with y-axis point upwards, x-axis point to the
     * right and the origin being in the bottom left corner of the screen. The
     * projection will be pixel perfect with respect to the screen resolution.
     */
    public SpriteRenderer() {
        this(1000);
    }

    /**
     * Constructs a SpriteBatch with the specified size and (if GL2) the default
     * shader. See {@link #SpriteBatch(int, ShaderProgram)}.
     */
    public SpriteRenderer(int size) {
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
    public SpriteRenderer(int size, ShaderProgram defaultShader) {
        this(size, 1, defaultShader);
    }

    /**
     * Constructs a SpriteBatch with the specified size and number of buffers
     * and (if GL2) the default shader. See
     * {@link #SpriteBatch(int, int, ShaderProgram)}.
     */
    public SpriteRenderer(int size, int buffers) {
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
    public SpriteRenderer(int size, int buffers, ShaderProgram defaultShader) {
        this.spriteSize = size;
        this.buffersCount = buffers;
        reset();

        if (Gdx.graphics.isGL20Available() && defaultShader == null) {
            shader = Shader.getShader("sprite");
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
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" //
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

        if (Gdx.graphics.isGL20Available()) {
            if (customShader != null)
                customShader.begin();
            else
                shader.begin();
        } else {
            Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        }
        setupMatrices();

        idx = 0;
        drawing = true;
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

        if (Gdx.graphics.isGL20Available()) {
            if (customShader != null)
                customShader.end();
            else
                shader.end();
        } else {
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }
    }

    private void reset() {

        vertexSize = SpriteRenderer.VERTEX_SIZE;
        verticesPerQuad = SpriteRenderer.VERTS_PER_QUAD * vertexSize;
        vertices = new float[spriteSize * verticesPerQuad];

        short[] indices = new short[spriteSize * SpriteRenderer.INDICES_PER_QUAD * SpriteRenderer.QUADS];
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 3);
            indices[i + 2] = (short) (j + 1);
            indices[i + 3] = (short) (j + 1);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 2);
        }

        this.buffers = new Mesh[buffersCount];

        for (int i = 0; i < buffersCount; i++) {
            this.buffers[i] = new Mesh(VertexDataType.VertexArray, false, vertices.length, indices.length, VertexAttribute.Position(),
                    VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        }

        for (int i = 0; i < buffersCount; i++) {
            this.buffers[i].setIndices(indices);
        }
        mesh = this.buffers[0];

    }

    public void draw(TextureRegion region, Vector3 position, Vector3 size, float rotation) {
        this.draw(region, position, size, rotation, Color.WHITE);
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the
     * region to cover the given width and height.
     */
    public void draw(TextureRegion region, Vector3 position, Vector3 size, float rotation, Color c) {
        if (!drawing)
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

        Texture texture = region.getTexture();
        if (idx == vertices.length) //
            renderMesh();

        float width = size.x;
        float height = size.y;
        float x = position.x;
        float y = position.y;
        float z = position.z;

        float x0;
        float y0;
        float z0;
        float x1;
        float y1;
        float z1;
        float x2;
        float y2;
        float z2;
        float x3;
        float y3;
        float z3;

        y0 = y - height / 2f;
        y1 = y + height / 2f;
        y2 = y + height / 2f;
        y3 = y - height / 2f;

        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        // rotate
        final float cos = (float) Math.cos(rotation);
        final float sin = (float) Math.sin(rotation);

        x0 = x - cos * (width / 2f);
        z0 = z + sin * (width / 2f);

        x1 = x - cos * (width / 2f);
        z1 = z + sin * (width / 2f);

        x2 = x + cos * (width / 2f);
        z2 = z - sin * (width / 2f);

        x3 = x + cos * (width / 2f);
        z3 = z - sin * (width / 2f);

        vertices[idx++] = x0;
        vertices[idx++] = y0;
        vertices[idx++] = z0;
        vertices[idx++] = c.r;
        vertices[idx++] = c.g;
        vertices[idx++] = c.b;
        vertices[idx++] = c.a;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = z1;
        vertices[idx++] = c.r;
        vertices[idx++] = c.g;
        vertices[idx++] = c.b;
        vertices[idx++] = c.a;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = z2;
        vertices[idx++] = c.r;
        vertices[idx++] = c.g;
        vertices[idx++] = c.b;
        vertices[idx++] = c.a;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = z3;
        vertices[idx++] = c.r;
        vertices[idx++] = c.g;
        vertices[idx++] = c.b;
        vertices[idx++] = c.a;
        vertices[idx++] = u2;
        vertices[idx++] = v;
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
        int spritesInBatch = idx / verticesPerQuad;
        if (spritesInBatch > spriteSize)
            spriteSize = spritesInBatch;

        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(spritesInBatch * 6);

        if (blendingDisabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            if (blendSrcFunc != -1)
                Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
        }

        if (Gdx.graphics.isGL20Available()) {
            if (customShader != null)
                mesh.render(customShader, GL10.GL_TRIANGLES, 0, spritesInBatch * 6);
            else
                mesh.render(shader, GL10.GL_TRIANGLES, 0, spritesInBatch * 6);
        } else {
            mesh.render(GL10.GL_TRIANGLES, 0, spritesInBatch * 6);
        }

        idx = 0;
        currBufferIdx++;
        if (currBufferIdx == buffers.length)
            currBufferIdx = 0;
        mesh = buffers[currBufferIdx];
    }

    /** Disposes all resources associated with this SpriteBatch */
    public void dispose() {
        for (int i = 0; i < buffers.length; i++)
            buffers[i].dispose();
        if (ownsShader && shader != null)
            shader.dispose();
    }

    /**
     * Returns the current projection matrix. Changing this will result in
     * undefined behaviour.
     * 
     * @return the currently set projection matrix
     */
    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Returns the current transform matrix. Changing this will result in
     * undefined behaviour.
     * 
     * @return the currently set transform matrix
     */
    public Matrix4 getTransformMatrix() {
        return transformMatrix;
    }

    /**
     * Sets the projection matrix to be used by this SpriteBatch. If this is
     * called inside a {@link #begin()}/{@link #end()} block. the current batch
     * is flushed to the gpu.
     * 
     * @param projection
     *            the projection matrix
     */
    public void setProjectionMatrix(Matrix4 projection) {
        if (drawing)
            flush();
        projectionMatrix.set(projection);
        if (drawing)
            setupMatrices();
    }

    /**
     * Sets the transform matrix to be used by this SpriteBatch. If this is
     * called inside a {@link #begin()}/{@link #end()} block. the current batch
     * is flushed to the gpu.
     * 
     * @param transform
     *            the transform matrix
     */
    public void setTransformMatrix(Matrix4 transform) {
        if (drawing)
            flush();
        transformMatrix.set(transform);
        if (drawing)
            setupMatrices();
    }

    private void setupMatrices() {
        if (!Gdx.graphics.isGL20Available()) {
            GL10 gl = Gdx.gl10;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadMatrixf(projectionMatrix.val, 0);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(transformMatrix.val, 0);
        } else {
            combinedMatrix.set(projectionMatrix).mul(transformMatrix);
            if (customShader != null) {
                customShader.setUniformMatrix("u_projTrans", combinedMatrix);
                customShader.setUniformi("u_texture", 0);
            } else {
                shader.setUniformMatrix("u_projTrans", combinedMatrix);
                shader.setUniformi("u_texture", 0);
            }
        }
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
            setupMatrices();
        }
    }

    /** @return whether blending for sprites is enabled */
    public boolean isBlendingEnabled() {
        return !blendingDisabled;
    }

    static public final int X1 = 0;
    static public final int Y1 = 1;
    static public final int C1 = 2;
    static public final int U1 = 3;
    static public final int V1 = 4;
    static public final int X2 = 5;
    static public final int Y2 = 6;
    static public final int C2 = 7;
    static public final int U2 = 8;
    static public final int V2 = 9;
    static public final int X3 = 10;
    static public final int Y3 = 11;
    static public final int C3 = 12;
    static public final int U3 = 13;
    static public final int V3 = 14;
    static public final int X4 = 15;
    static public final int Y4 = 16;
    static public final int C4 = 17;
    static public final int U4 = 18;
    static public final int V4 = 19;

}