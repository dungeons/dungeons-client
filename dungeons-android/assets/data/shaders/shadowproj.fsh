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
 
     vec4 clr = vec4(1,0,0.2,1);
    // vWorldNormal is interpolated when passed into the fragment shader.
    // We need to renormalize the vector so that it stays at unit length.

    
    // Calculate shadow amount
    vec3 depth = vPosition.xyz / vPosition.w;
    //depth.z = length(vWorldVertex.xyz - v_lightSpacePosition) * LinearDepthConstant ;
    float shadow = 1.0;

    float shadowDepth = unpack(texture2D(DepthMap, depth.xy));
        if ( depth.z > shadowDepth ){
            shadow = 0.5;
            }
           
            if(depth.x < 0.0 ){
      clr = vec4(0.5,1,0.2,1);
            shadow = 1.0;
            }
            if( depth.x  < 500){
      clr = vec4(0.7,0,0.8,1);
            shadow = 1.0;
            }
            
            if( length(depth.xx)  > 100 ){
      clr = vec4(0.2,0.5,1,1);
            shadow = 1.0;
            }
    
    //
    // Apply colour and shadow
    //
    gl_FragColor = clr * vec4(shadow);
}
