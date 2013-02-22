package engine.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent extends Component {
    public ShaderProgram shader;

    public ShaderComponent(ShaderProgram shader) {
        this.shader = shader;
    }

}
