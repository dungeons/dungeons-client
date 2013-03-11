uniform mat4 u_MVPMatrix;      // A constant representing the combined model/view/projection matrix.
 
attribute vec3 a_position;     // Per-vertex position information we will pass in.
attribute vec2 a_texCoord0;
 
varying vec2 v_texCoord;

void main(){
    v_texCoord = a_texCoord0;
    gl_Position = u_MVPMatrix * vec4(a_position,1.0) ;
}