package engine.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class InputComponent extends Component {
    public Vector2 vector;

    public InputComponent(Vector2 vector) {
        this.vector = vector;
    }

}
