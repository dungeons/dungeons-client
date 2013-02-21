package engine.system;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import engine.component.MeshComponent;
import engine.component.PositionComponent;
import engine.component.ShaderComponent;

public class RenderGeometry extends EntitySystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<ShaderComponent> sm;
    @Mapper
    ComponentMapper<MeshComponent> mm;

    private final Camera camera;

    private Bag<AtlasRegion> regionsByEntity;
    private List<Entity> sortedEntities;

    public RenderGeometry(Camera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, ShaderComponent.class, MeshComponent.class));
        this.camera = camera;
    }

    @Override
    protected void initialize() {

        sortedEntities = new ArrayList<Entity>();

    }

    @Override
    protected void begin() {
        // batch.setProjectionMatrix(camera.combined);
        //   batch.begin();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (int i = 0; sortedEntities.size() > i; i++) {
            process(sortedEntities.get(i));
        }
    }

    protected void process(Entity e) {
        if (pm.has(e)) {
            PositionComponent pc = pm.getSafe(e);
            ShaderComponent sc = sm.getSafe(e);
            MeshComponent mc = mm.getSafe(e);

            camera.combined.translate(pc.x, pc.y, pc.z);

            sc.shader.begin();
            sc.shader.setUniformMatrix("u_MVPMatrix", camera.combined);
            mc.mesh.render(sc.shader, GL10.GL_TRIANGLES);
            sc.shader.end();

            camera.combined.translate(-pc.x, -pc.y, -pc.z);
        }
    }

    @Override
    protected void end() {
        // batch.end();
    }

    @Override
    protected void inserted(Entity e) {
        /*    Sprite sprite = sm.get(e);
            regionsByEntity.set(e.getId(), regions.get(sprite.name));

            sortedEntities.add(e);

            Collections.sort(sortedEntities, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    Sprite s1 = sm.get(e1);
                    Sprite s2 = sm.get(e2);
                    return s1.layer.compareTo(s2.layer);
                }
            });
            
            mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
            mesh.setVertices(new float[] { -this.getHalfSize(), -this.getHalfSize(), 0, this.getHalfSize(), -this.getHalfSize(), 0, this.getHalfSize(),
                    this.getHalfSize(), 0, -this.getHalfSize(), this.getHalfSize(), 0 });
            mesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
            shader = Shader.getShader("normal");*/
    }

    @Override
    protected void removed(Entity e) {
        sortedEntities.remove(e);
    }

}
