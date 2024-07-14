/*
 * This file was made by M2tM <https://stackoverflow.com/a/29717745>
 */

#version 330 core

smooth in vec4 color;
smooth in vec2 uv;

out vec4 colorResult;

void main(void) {
    float uvX = 1.0 - uv.st.x;
    float uvY = uv.st.y;

    vec4 white = vec4(1, 1, 1, 1);
    vec4 black = vec4(0, 0, 0, 1);

    colorResult = mix(mix(color, white, uvX), black, uvY);
}