package com.kingx.dungeons.trash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.kingx.dungeons.Logic;
import com.kingx.dungeons.entity.Entity;


public class Actor  {
/*
    // Singleton
    private static final Actor instance = new Actor();

    private Camera camera;
    private Camera eyes;

    public static Actor getActor() {
        return instance;
    }

    
    private Actor() {
        super(null);
        this.setActive(true);
    }

    public Camera setUpCamera(int width, int height) {
        camera = new PerspectiveCamera(67, width, height);

        camera.position.set(entity.getPosition());
        camera.position.z = 10;
        camera.direction.x = 0;
        camera.direction.y = 0;
        camera.direction.z = -1;
        
        return camera;
    }

    private Vector3 moveVector = new Vector3();
    private float rotate = 0f;

   

    private static final float MOVE_COEF = 1f;
    private static final float ROTATE_COEF = 1f;

    @Override
    protected void doUpdate(float delta) {
        App.Logic.move(this, moveVector.x*delta, moveVector.y*delta);
        /*
        entity.setPosition(camera.position);
        Vector3 temp = moveVector.cpy();
        entity.getPosition().add(temp.mul(delta * MOVE_COEF));

        camera.position.set(entity.getPosition());
        camera.rotate(rotateVector, ROTATE_COEF);
        */
        /*
    }

    // Movement methods
    public void moveX(int i) {
        moveVector.x += i;
    }

    public void moveY(int i) {
        moveVector.y += i;
    }


    public void rotate(int i) {
        rotate += i;
    }
    
    public Camera getEyes() {
        return camera;
    }


    public Camera getCamera() {
        return camera;
    }


    @Override
    protected void puppetControlGained(Entity entity) {
        
    }
    
    

*/


}
