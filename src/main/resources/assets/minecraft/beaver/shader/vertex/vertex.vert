/*
 * This file was created by Jinthium <https://github.com/Jinthium>
 */

#version 120

void main() {
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}