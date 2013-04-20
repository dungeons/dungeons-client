attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec3 v_normal;
varying vec2 v_texCoords;

void main()
{
	a_normal = a_normal;
   v_texCoords = a_texCoord0;
   gl_Position =  u_projTrans * a_position;
}
