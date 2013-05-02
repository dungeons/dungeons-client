precision mediump float;       // Set the default precision to medium. We don't need as high of a
                               // precision in the fragment shader.
uniform vec3 u_lightSourcePos;       // The position of the light in eye space.
uniform sampler2D u_texture;
uniform vec4 u_source_color;
uniform vec4 u_ground_color;
uniform float u_sight;

varying vec3 v_Position;       // Interpolated position for this fragment.
varying vec2 v_texCoord;
varying vec3 v_Normal;         // Interpolated normal for this fragment.

// Unpack an RGBA pixel to floating point value.
float interpolate (float a, float b, float stage, float gradient)
{
	return a + ((b - a) * stage / gradient);
}

// The entry point for our fragment shader.
void main()
{
	gl_FragColor = texture2D(u_texture,v_texCoord) * ((15.0-length(v_Position))/10.0);
}
