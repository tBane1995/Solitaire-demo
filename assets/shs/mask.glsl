#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture; // sprite
uniform sampler2D u_mask;    // noise

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    vec4 noise = texture2D(u_mask, v_texCoords);

    float strength = 0.4;

    vec3 finalColor = mix(color.rgb, color.rgb - noise.rgb, strength);

    gl_FragColor = vec4(finalColor, color.a);
}
