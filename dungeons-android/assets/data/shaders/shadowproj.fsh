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


// Varying variables.
varying vec4 vWorldVertex;
varying vec4 vPosition[4];
varying vec4 vColor;
varying vec2 v_texCoord;


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
worldPostitoin.z = 0.0;
	bool shadow = false;
	vec3 depth;
		for(int i = 0; i < 4; i++){
			// Change position to position seen from this perspective
			depth = vPosition[i].xyz /  vPosition[i].w;
			
			// interpolates across z buffer so value si within the bound
			// length(worldPostitoin.xy - v_lightSpacePosition.xy) removers the light circle around player but causes wall flickering.
			depth.z = length(worldPostitoin.xy) * (LinearDepthConstant) ;
			// make sure that shadow is computed within lights area (90° area)
            // FIXME This is not a sollution, areas are still overlapping
            float bias = 0.0;
			bool cond = false;

			switch (i) {
			case 0:
				cond = depth.x >= 0.0-bias && depth.x <= 1.0+bias && depth.y >= 0.5-bias;
				break;
			case 1:
				cond = depth.y >= 0.0-bias && depth.y <= 1.0+bias && depth.x >= 0.5-bias;
				break;
			case 2:
				cond = depth.x >= 0.0-bias && depth.x <= 1.0+bias && depth.y <= 0.5+bias;
				break;
			case 3:
				cond = depth.y >= 0.0-bias && depth.y <= 1.0+bias && depth.x <= 0.5+bias;
				break;
			}

			if(cond){
				depth.x = (i % 2) * 0.5 + depth.x / 2.0;
				depth.y = (i / 2) * 0.5 + depth.y / 2.0;
				// gets value stored in depth map and compares it to value seen from this perspective
				float shadowDepth = unpack(texture2D(DepthMap, depth.xy));
				
				// values with higher z value (being farther from camera) are in shadow
				if(depth.z > shadowDepth){
					shadow = true;
				}
			}
		}
		float radius = 5;
		float distance = length(worldPostitoin.xyz - v_lightSpacePosition.xyz);
        shadow = distance > radius ? true : shadow;
		distance = clamp(distance,0,radius);
		distance/=radius;
		
		float del = 85.0;
		float r = round(interpolate(u_source_color.r ,u_ground_color.r, distance, 1.0)*del) / del;
		float g = round(interpolate(u_source_color.g ,u_ground_color.g, distance, 1.0)*del) / del;
		float b = round(interpolate(u_source_color.b ,u_ground_color.b, distance, 1.0)*del) / del;
  
	    if(vWorldVertex.z  >= .99 || shadow){
	  		gl_FragColor = u_ground_color* texture2D(u_texture,v_texCoord);
        }else{
	 		gl_FragColor = vec4(r,g,b,1.0)* texture2D(u_texture,v_texCoord);
        }
        
}