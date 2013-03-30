// Vertex shader for rendering the scene with shadows.

// Attributes.
attribute vec3 a_position;
attribute vec2 a_texCoord0;

// Uniform variables.
uniform mat4 ProjectionMatrix;
uniform mat4 ViewMatrix;

uniform mat4 LightSourceProjectionViewMatrix[4];
uniform vec4 color;
uniform float u_side;
uniform float u_worldWidth;

// The scale matrix is used to push the projected vertex into the 0.0 - 1.0 region.
// Similar in role to a * 0.5 + 0.5, where -1.0 < a < 1.0.
const mat4 ScaleMatrix = mat4(0.5, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0.5, 1.0);


// Varying variables.
varying vec4 vWorldVertex;
varying vec3 vWorldNormal;
varying vec3 vViewVec;
varying vec4 vPosition[4];
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
   vec4 local;
if(u_side == 0.0){
    local = vec4(a_position.x,a_position.y,0.0, 1.0);
}else if(u_side == 1.0){ 
    local = vec4(u_worldWidth - 1.0,a_position.y,a_position.z, 1.0);
}else if(u_side == 2.0){ 
    local = vec4(a_position.x,a_position.y,-u_worldWidth + 2.0, 1.0);
}else if(u_side == 3.0){ 
    local = vec4(1.0,a_position.y,a_position.z, 1.0);
}
    
    // Project the vertex from the light's point of view
    vPosition[0] = ScaleMatrix * LightSourceProjectionViewMatrix[0] * local;
    vPosition[1] = ScaleMatrix * LightSourceProjectionViewMatrix[1] * local;
    vPosition[2] = ScaleMatrix * LightSourceProjectionViewMatrix[2] * local;
    vPosition[3] = ScaleMatrix * LightSourceProjectionViewMatrix[3] * local;
}
