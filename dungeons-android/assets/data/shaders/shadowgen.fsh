/// <summary>
/// Copyright (C) 2012 Nathaniel Meyer
/// Nutty Software, http://www.nutty.ca
/// All Rights Reserved.
/// 
/// Permission is hereby granted, free of charge, to any person obtaining a copy of
/// this software and associated documentation files (the "Software"), to deal in
/// the Software without restriction, including without limitation the rights to
/// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
/// of the Software, and to permit persons to whom the Software is furnished to do
/// so, subject to the following conditions:
///     1. The above copyright notice and this permission notice shall be included in all
///        copies or substantial portions of the Software.
///     2. Redistributions in binary or minimized form must reproduce the above copyright
///        notice and this list of conditions in the documentation and/or other materials
///        provided with the distribution.
/// 
/// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
/// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
/// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
/// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
/// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
/// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
/// SOFTWARE.
/// </summary>


/// <summary>
/// Fragment shader for rendering the depth values to a texture.
/// </summary>


#ifdef GL_ES
	precision highp float;
#endif


/// <summary>
/// Linear depth calculation.
/// You could optionally upload this as a shader parameter.
/// <summary>
const float Near = 1.0;
const float Far = 30.0;
const float LinearDepthConstant = 1.0 / (Far - Near);


/// <summary>
/// Varying variables.
/// <summary>
varying vec4 vPosition;


/// <summary>
/// Pack a floating point value into an RGBA (32bpp).
/// Used by SSM, PCF, and ESM.
///
/// Note that video cards apply some sort of bias (error?) to pixels,
/// so we must correct for that by subtracting the next component's
/// value from the previous component.
/// </summary>
vec4 pack (float depth)
{
	const vec4 bias = vec4(1.0 / 255.0,
							1.0 / 255.0,
							1.0 / 255.0,
							0.0);

	float r = depth;
	float g = fract(r * 255.0);
	float b = fract(g * 255.0);
	float a = fract(b * 255.0);
	vec4 colour = vec4(r, g, b, a);
	
	return colour - (colour.yzww * bias);
}


/// <summary>
/// Pack a floating point value into a vec2 (16bpp).
/// Used by VSM.
/// </summary>
vec2 packHalf (float depth)
{
	const vec2 bias = vec2(1.0 / 255.0,
							0.0);
							
	vec2 colour = vec2(depth, fract(depth * 255.0));
	return colour - (colour.yy * bias);
}


/// <summary>
/// Fragment shader entry.
/// </summary>
void main ()
{
	// Linear depth
	float linearDepth = length(vPosition) * LinearDepthConstant;

		//
		// Classic shadow mapping algorithm.
		// Store screen-space z-coordinate or linear depth value (better precision)
		//
		//gl_FragColor = pack(gl_FragCoord.z);
		gl_FragColor = pack(linearDepth);
	
}