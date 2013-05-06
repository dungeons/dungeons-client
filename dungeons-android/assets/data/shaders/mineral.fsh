#ifdef GL_ES
  precision mediump float;
#endif
           
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_position;
uniform sampler2D u_texture;

void main()
{
  gl_FragColor = texture2D(u_texture, v_texCoords);
}
