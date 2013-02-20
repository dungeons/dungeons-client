// Fragment shader for rendering the scene with shadows.

// Linear depth calculation.
// You could optionally upload this as a shader parameter.
const float Near = 0.0;
const float Far = 500.0;
const float LinearDepthConstant = 1.0 / (Far - Near);


// Uniform variables.
uniform vec3 v_lightSpacePosition;
uniform sampler2D DepthMap;


// Varying variables.
varying vec4 vWorldVertex;
varying vec4 vPosition[4];
varying vec4 vColor;


// Unpack an RGBA pixel to floating point value.
float unpack (vec4 colour)
{
	const vec4 bitShifts = vec4(1.0,
	1.0 / 255.0,
	1.0 / (255.0 * 255.0),
	1.0 / (255.0 * 255.0 * 255.0));
	return dot(colour, bitShifts);
}

// Fragment shader entry.
void main ()
{
	/// THESE COMMENTS REPRESENT WHAT I THINK THIS CODE DOES. I HAVE NO IDEA WHAT IS CORRECT
	
	bool shadow = false;
	
		for(int i = 0; i < 4; i++){
			// Change position to position seen from this perspective
			vec3 depth = vPosition[i].xyz /  vPosition[i].w;
			
			// interpolates across z buffer so value si within the bound
			depth.z = length(vWorldVertex.xyz - v_lightSpacePosition) * LinearDepthConstant ;
			
			// Dont know why this is needed.
			depth.z*=7;

			
			// make sure that shadow is computed within lights area (90° area)
            // FIXME This is not a sollution areas are still overlapping
            float bias = 0.0015;
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

	if(shadow){
	gl_FragColor = vec4(1,1,1,1);
    }else{
	gl_FragColor = vec4(0,0,0,1);
}
}