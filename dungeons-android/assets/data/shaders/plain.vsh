// Vertex shader for rendering the scene with shadows.
#ifdef GL_ES
  precision mediump float;
#endif
// Attributes.
attribute vec3 a_position;
attribute vec2 a_texCoord0;

// Uniform variables.
uniform mat4 ProjectionMatrix;
uniform mat4 ViewMatrix;

uniform vec4 color;
uniform float u_side;
uniform vec3 u_minBound;
uniform vec3 u_maxBound;

// The scale matrix is used to push the projected vertex into the 0.0 - 1.0 region.
// Similar in role to a * 0.5 + 0.5, where -1.0 < a < 1.0.
const mat4 ScaleMatrix = mat4(0.5, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0.5, 1.0);


// Varying variables.
varying vec4 vWorldVertex;
varying vec3 vWorldNormal;
varying vec3 vViewVec;
varying vec4 vColor;
varying vec2 v_texCoord;


// Vertex shader entry.
void main ()
{
    vColor = color;
    v_texCoord = a_texCoord0;
    
    // Standard basic lighting preperation
    vWorldVertex = vec4(a_position, 1.0);
    gl_Position = ProjectionMatrix * ViewMatrix * vWorldVertex;

}
