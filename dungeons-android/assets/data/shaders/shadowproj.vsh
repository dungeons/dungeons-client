#ifdef GL_ES
precision highp float; 
#endif

attribute vec3 a_position;

uniform vec4 u_color;
uniform mat4 u_projTrans;
uniform mat4 u_lightProjTrans0;
uniform mat4 u_lightProjTrans1;

varying vec4 v_color;
varying vec4 v_lightSpacePosition0;
varying vec4 v_lightSpacePosition1;


void main(void) 
{
	v_color = u_color;
	gl_Position = u_projTrans * vec4(a_position,1.0) ;
	v_lightSpacePosition0  = u_lightProjTrans0 * vec4(a_position,1.0) ;
	v_lightSpacePosition1  = u_lightProjTrans1 * vec4(a_position,1.0) ;
}