uniform mat4 u_MVPMatrix;      // A constant representing the combined model/view/projection matrix.
 
attribute vec3 a_position;     // Per-vertex position information we will pass in.
 
void main(){
    gl_Position = u_MVPMatrix * vec4(a_position,1.0) ;
}