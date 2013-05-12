#ifdef GL_ES
  precision mediump float;
#endif
// Fragment shader for rendering the scene with shadows.

// Linear depth calculation.
// You could optionally upload this as a shader parameter.
const float Near = 0.0;
const float Far = 500.0;
const float LinearDepthConstant = 1.0 / (Far - Near);


// Uniform variables.
uniform vec4 u_source_color;
uniform vec4 u_ground_color;
uniform vec3 v_lightSpacePosition;
uniform sampler2D u_texture;
uniform sampler2D DepthMap;
uniform float u_mapOffset;
uniform bool u_useTextures;
uniform float u_sight;
uniform float u_side;
uniform bool u_forceShadow;

uniform float u_worldWidth;

uniform vec3 u_minBound;
uniform vec3 u_maxBound;

uniform vec4 u_tint;

// Varying variables.
varying vec4 vWorldVertex;
varying vec4 vPosition[4];
varying vec4 vColor;
varying vec2 v_texCoord;

float round(float x)
{
 return floor(x + 0.5);
}
// Unpack an RGBA pixel to floating point value.
float unpack (vec4 colour)
{
	const vec4 bitShifts = vec4(1.0,
	1.0 / 255.0,
	1.0 / (255.0 * 255.0),
	1.0 / (255.0 * 255.0 * 255.0));
	return dot(colour, bitShifts);
}
// Unpack an RGBA pixel to floating point value.
float interpolate (float a, float b, float stage, float gradient)
{
	return a + ((b - a) * stage / gradient);
}

// Fragment shader entry.
void main ()
{
	/// THESE COMMENTS REPRESENT WHAT I THINK THIS CODE DOES. I HAVE NO IDEA WHAT IS CORRECT
    vec4 worldPostitoin = vWorldVertex;
	bool shadow = false;
	bool inSight = false;
	vec3 depth;
	float i = 0.0;
	float PI = 3.14159265358979323846264;
	float angle;
	float offset = -PI/4.0;

	if(u_side == 0.0){
			angle = atan(worldPostitoin.y- v_lightSpacePosition.y,worldPostitoin.x - v_lightSpacePosition.x);
		}else if(u_side == 1.0){
			angle = atan(worldPostitoin.y- v_lightSpacePosition.y, v_lightSpacePosition.z - worldPostitoin.z);
		}else if(u_side == 2.0){
			angle = atan(worldPostitoin.y- v_lightSpacePosition.y,v_lightSpacePosition.x - worldPostitoin.x );
		}else{
			angle = atan(worldPostitoin.y- v_lightSpacePosition.y,worldPostitoin.z - v_lightSpacePosition.z);
		}

	if(angle < -PI/2.0+offset){
		i = 1.0;
	}else if(angle < offset){
		i = 2.0;
	}else if(angle < PI/2.0+offset){
		i = 3.0;
	}else if(angle < PI+offset){
		i = 0.0;
	}else{
		i = 1.0;
	}

	// Change position to position seen from this perspective
	depth = vPosition[int(i)].xyz /  vPosition[int(i)].w;


	// interpolates across z buffer so value si within the bound
	// length(worldPostitoin.xy - v_lightSpacePosition.xy) removers the light circle around player but causes wall flickering.

	depth.z = length(worldPostitoin.xy) * LinearDepthConstant;
	// make sure that shadow is computed within lights area (90° area)

	depth.x = mod(i, 2.0) * 0.5 + depth.x / 2.0;
	depth.y = floor(i / 2.0) * 0.5 + depth.y / 2.0;

	// gets value stored in depth map and compares it to value seen from this perspective
	float shadowDepth = unpack(texture2D(DepthMap, depth.xy));

	// values with higher z value (being farther from camera) are in shadow
	if(depth.z > shadowDepth){
		shadow = true;
	}

	float distance = length(worldPostitoin.xyz - v_lightSpacePosition.xyz);
	shadow = distance > u_sight ? true : shadow;

	float sightDist = length(worldPostitoin.xyz - v_lightSpacePosition.xyz);
	inSight = sightDist < u_sight ? v_lightSpacePosition.y >= 50.0 : false;

	distance = clamp(distance,0.0,u_sight);
	distance/=u_sight;

	float temp = 1.0-(sightDist/u_sight);
	temp = max(0.2,temp);

	float del = 85.0;
	float r = round (interpolate(u_source_color.r ,u_ground_color.r, distance, 1.0)*del) / del;
	float g = round (interpolate(u_source_color.g ,u_ground_color.g, distance, 1.0)*del) / del;
	float b = round (interpolate(u_source_color.b ,u_ground_color.b, distance, 1.0)*del) / del;
	if(vWorldVertex.z  >=  u_maxBound.z - 0.01 || vWorldVertex.x  >= u_maxBound.x - 0.01 || vWorldVertex.z  <=  u_minBound.z + 0.01 || vWorldVertex.x  <=  u_minBound.x + 0.01){
		shadow = true;
	}

	if(vWorldVertex.y >= 49.81){
			gl_FragColor = u_source_color * texture2D(u_texture,v_texCoord) * u_tint * temp *vColor;
	}else if(u_forceShadow || shadow ){
		gl_FragColor = u_ground_color * texture2D(u_texture,v_texCoord) * u_tint *vColor;
	}else {
		gl_FragColor = vec4(r,g,b,1.0) * texture2D(u_texture,v_texCoord) * u_tint *vColor;
	}
        
}
