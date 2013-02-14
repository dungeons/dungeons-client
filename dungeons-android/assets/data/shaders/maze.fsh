precision mediump float;       // Set the default precision to medium. We don't need as high of a

uniform vec3 u_lightPos;       // The position of the light in eye space.
 
varying vec3 v_position;       
varying vec4 v_color;          
varying vec3 v_normal;         // Interpolated normal for this fragment.

const vec4 c_tint = vec4(1, 0, 0, 1);

void main()
{
   
	// Will be used for attenuation.
    float distance = length(u_lightPos - v_position);
 
    // Get a lighting direction vector from the light to the vertex.
    vec3 lightVector = normalize(u_lightPos - v_position);
 
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
    float diffuse = max(dot(v_normal, lightVector), 0.1);
 
    // Add attenuation.
    diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));
 
 if(distance > 0.5){ 
    // Multiply the color by the diffuse illumination level to get final output color.
          gl_FragColor = c_tint * diffuse;
 }else{
        gl_FragColor = vec4(1,1,0,1);
 }
   
    
    
}