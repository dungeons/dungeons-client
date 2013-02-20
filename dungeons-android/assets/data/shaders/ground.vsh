// Vertex shader for rendering the depth values to a texture.

// Attributes.
attribute vec3 a_position;

// Uniform variables.
uniform mat4 PVMMatrix;

// Varying variables.
varying vec4 vPosition;

// Vertex shader entry.
void main ()
{
    vPosition = vec4(a_position , 1.0);
    gl_Position = PVMMatrix  * vPosition;
}
