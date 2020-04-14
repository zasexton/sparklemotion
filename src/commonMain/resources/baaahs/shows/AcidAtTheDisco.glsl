// Acid at the Disco
// From: https://www.shadertoy.com/view/4sfXRB

// shadertoy emulation
#define iTime time
#define iResolution resolution

uniform float time;
uniform vec2 resolution;
// SPARKLEMOTION GADGET: Beat { "name": "beat" }
uniform float sm_beat;

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy / iResolution.xy;
    float time = iTime*0.75;
    float depth = sin(uv.y*2.0+sin(time)*1.5+1.0+sin(uv.x*3.0+time*1.2))*cos(uv.y*2.0+time)+sin((uv.x*3.0+time));
    float texey = (uv.x-0.5);
    float xband = sin(sqrt(uv.y/uv.y)*16.0/(depth)+time*(3.0));
    float final = (
    sin(texey/abs(depth)*32.0+time*16.0+sin(uv.y*uv.x*32.0*sin(depth*3.0)))*(depth)*xband
    );


    fragColor = vec4(final*abs(sin(time)),(-final*sin(time)*2.0),(sm_beat-final),1.0)*1.5;
}

void main() {
    mainImage(gl_FragColor, gl_FragCoord.xy);
}

