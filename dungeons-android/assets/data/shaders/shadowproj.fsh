// Fragment shader for rendering the scene with shadows.

// Linear depth calculation.
// You could optionally upload this as a shader parameter.
const float Near = 0.0;
const float Far = 500.0;
const float LinearDepthConstant = 1.0 / (Far - Near);


// Uniform variables.
uniform  vec3 v_lightSpacePosition;
uniform sampler2D DepthMap[4];


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
    
    float shadow = 1.0;
    
    for(int i = 0; i < 4; i++){
    // Change position to position seen from this perspective
    vec3 depth = vPosition[i].xyz /  vPosition[i].w;
    
    // interpolates across z buffer so value si within the bound
    depth.z = length(vWorldVertex.xyz - v_lightSpacePosition) * LinearDepthConstant ;
    
    // Dont know why this is needed.
    depth.z*=5;

    
    // make sure that shadow is computed within lights area (90° area)
   
   bool cond = false;
   switch (i) {
  case 0:
  cond = depth.x >= 0.0 && depth.x <= 1.0 && depth.y > 0.5;
    break;
  case 1:
  cond = depth.y >= 0.0 && depth.y <= 1.0 && depth.x >= 0.5;
    break;
  case 2:
  cond = depth.x >= 0.0 && depth.x <= 1.0 && depth.y < 0.5;
    break;
  case 3:
  cond = depth.y >= 0.0 && depth.y <= 1.0 && depth.x <= 0.5;
    break;
}
   
   
   if(cond){
    
        // gets value stored in depth map and compares it to value seen from this perspective
        float shadowDepth = unpack(texture2D(DepthMap[i], depth.xy));
        
        // values with higher z value (being farther from camera) are in shadow
        if(depth.z > shadowDepth){
            shadow = 0.5;
        }
   }
    }

    
    // Apply colour and shadow
    gl_FragColor = vColor * vec4(shadow);
}
