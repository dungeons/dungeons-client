#ifdef GL_ES
  precision mediump float;
#endif
           
varying vec3 v_normal;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main()
{
  gl_FragColor = vec4(1,0,0,1);
}
