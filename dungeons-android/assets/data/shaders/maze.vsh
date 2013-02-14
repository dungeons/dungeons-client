uniform mat4 u_MVPMatrix;      // A constant representing the combined model/view/projection matrix.
uniform mat4 u_MVMatrix;       // A constant representing the combined model/view matrix.
 
attribute vec4 a_position;     // Per-vertex position information we will pass in.
attribute vec3 a_normal;       // Per-vertex normal information we will pass in.
 
varying vec3 v_position;       // This will be passed into the fragment shader.
varying vec3 v_normal;         // This will be passed into the fragment shader.
 
// The entry point for our vertex shader.
void main()
{
    // Transform the vertex into eye space.
    //v_position = vec3(u_MVMatrix * a_position);
    v_position = a_position;
 
 
    // Transform the normal's orientation into eye space.
    v_normal = vec3(u_MVMatrix * vec4(a_normal, 0.0));
 
    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    gl_Position = u_MVPMatrix * a_position;
}