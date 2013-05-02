#ifdef GL_ES
  precision mediump float;
#endif
// Fragment shader for rendering the scene with shadows.

// Uniform variables.
uniform vec4 u_source_color;
uniform vec4 u_ground_color;
uniform vec3 v_lightSpacePosition;
uniform sampler2D u_texture;
uniform float u_sight;
uniform float u_side;
uniform vec4 u_tint;

// Varying variables.
varying vec4 vWorldVertex;
varying vec4 vColor;
varying vec2 v_texCoord;

float round(float x)
{
 return floor(x + 0.5);
}
// Unpack an RGBA pixel to floating point value.
float interpolate (float a, float b, float stage, float gradient)
{
	return a + ((b - a) * stage / gradient);
}

// Fragment shader entry.
void main ()
{
    vec4 worldPostitoin = vWorldVertex;
	bool inSight = false;




	float distance = length(worldPostitoin.xyz - v_lightSpacePosition.xyz);

	float sightDist = distance;
	inSight = sightDist < u_sight ? v_lightSpacePosition.y >= 50.0 : false;

	distance = clamp(distance,0.0,u_sight);
	distance/=u_sight;

	float temp = 1.0-(sightDist/u_sight);
	temp = max(0.2,temp);

	float del = 85.0;
	float r = round (interpolate(u_source_color.r ,u_ground_color.r, distance, 1.0)*del) / del;
	float g = round (interpolate(u_source_color.g ,u_ground_color.g, distance, 1.0)*del) / del;
	float b = round (interpolate(u_source_color.b ,u_ground_color.b, distance, 1.0)*del) / del;

			gl_FragColor = u_source_color * texture2D(u_texture,v_texCoord) * u_tint * temp ;
        
}
