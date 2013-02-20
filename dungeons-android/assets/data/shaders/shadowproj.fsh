// Fragment shader for rendering the scene with shadows.

// Linear depth calculation.
// You could optionally upload this as a shader parameter.
const float Near = 0.0;
const float Far = 500.0;
const float LinearDepthConstant = 1.0 / (Far - Near);


uniform  vec3 v_lightSpacePosition;

// Uniform variables.
uniform sampler2D DepthMap;


// Varying variables.
varying vec4 vWorldVertex;
varying vec4 vPosition;
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
    
    // Change position to position seen from this perspective
    vec3 depth = vPosition.xyz /  vPosition.w;
    
    // interpolates across z buffer so value si within the bound
    depth.z = length(vWorldVertex.xyz - v_lightSpacePosition) * LinearDepthConstant ;
    
    // Dont know why this is needed.
    depth.z*=5;

    float shadow = 1.0;
    
    // make sure that shadow is computed within lights area (90° area)
    if(depth.x > 0.0 && depth.x < 1.0 && depth.y > 0.5){
        // gets value stored in depth map and compares it to value seen from this perspective
        float shadowDepth = unpack(texture2D(DepthMap, depth.xy));
        // values with higher z value (being farther from camera) are in shadow
        if(depth.z > shadowDepth){
            shadow = 0.5;
        }
    }

    
    // Apply colour and shadow
    gl_FragColor = vColor * vec4(shadow);
}
