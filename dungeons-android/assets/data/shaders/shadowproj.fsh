#ifdef GL_ES
precision highp float; 
#endif

varying vec4 v_lightSpacePosition0;
varying vec4 v_lightSpacePosition1;
varying vec4 v_color;

uniform sampler2D s_shadowMap0;
uniform sampler2D s_shadowMap1;

float unpack(vec4 packedZValue)
{	
	const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
	return dot(packedZValue,unpackFactors);
}

float getShadowFactor(vec4 lightZ, sampler2D s_shadowMap)
{
	vec4 packedZValue = texture2D(s_shadowMap, lightZ.st);
	float unpackedZValue = unpack(packedZValue);
	return float(unpackedZValue > lightZ.z);
}

void main(void) 
{	
	float shadowFactor0=1.0;				

	vec4 lightZ0 = v_lightSpacePosition0 / v_lightSpacePosition0.w;
	lightZ0 = (lightZ0 + 1.0) / 2.0;
	shadowFactor0 = getShadowFactor(lightZ0, s_shadowMap0);


	float shadowFactor1=1.0;				

	vec4 lightZ1 = v_lightSpacePosition1 / v_lightSpacePosition1.w;
	lightZ1 = (lightZ1 + 1.0) / 2.0;
	shadowFactor1 = getShadowFactor(lightZ1, s_shadowMap1);

	if(shadowFactor0 == 0.0 ){
	gl_FragColor = v_color * 0.0;
}else{
	gl_FragColor = v_color * 1;
}
}