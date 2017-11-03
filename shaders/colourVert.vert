#version 400

layout(location = 0) in vec3 vertexPosition;

uniform mat4 mvp;

void main(){
    gl_Position = mvp * vec4(vertexPosition, 1.0);
}
