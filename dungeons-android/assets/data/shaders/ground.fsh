
// Uniform variables.
uniform float u_mazeSize;


// Varying variables.
varying vec4 vPosition;

// Fragment shader entry.
void main ()
{
float shadow = 1;
 vec4 colorResult = vec4(0.0, 0.0, 0.0, 0.0);
	if(vPosition.x < 0.0 || vPosition.x > u_mazeSize || vPosition.y < 0.0 || vPosition.y > u_mazeSize){
         colorResult = vec4(0.0,0.0,0.0,1.0) *	vec4(shadow);
	 }
	gl_FragColor = colorResult;
}