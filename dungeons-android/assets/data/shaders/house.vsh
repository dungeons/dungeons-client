uniform mat4 u_projTrans;      // A constant representing the combined model/view/projection matrix.
uniform mat4 u_viewTrans;       // A constant representing the combined model/view matrix.
uniform vec3 u_positionOffset;       // A constant representing the combined model/view matrix.

attribute vec3 a_position;     // Per-vertex position information we will pass in.
attribute vec3 a_normal;       // Per-vertex normal information we will pass in.
attribute vec2 a_texCoord0;

varying vec3 v_Position;       // This will be passed into the fragment shader.
varying vec3 v_Normal;         // This will be passed into the fragment shader.
varying vec2 v_texCoord;


// The entry point for our vertex shader.
void main()
{
    v_texCoord = a_texCoord0;
    vec4 objectPos =  vec4(a_position,1);
    objectPos.y += u_positionOffset.y;
    objectPos.x += u_positionOffset.x;
    objectPos.z += u_positionOffset.z;
    // Transform the vertex into eye space.
    v_Position =  vec3(u_viewTrans * objectPos);
    // Transform the normal's orientation into eye space.
    v_Normal = vec3(u_viewTrans * vec4(a_normal, 0.0));

    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    gl_Position = u_projTrans * objectPos;
}
