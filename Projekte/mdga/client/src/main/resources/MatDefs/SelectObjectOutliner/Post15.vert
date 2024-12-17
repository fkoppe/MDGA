// Vertex attributes
in vec4 inPosition;
in vec2 inTexCoord;

// Output to fragment shader
out vec2 texCoord;

void main() {
    // Transform position to clip space
    vec2 pos = inPosition.xy * 2.0 - 1.0;
    gl_Position = vec4(pos, 0.0, 1.0);

    // Pass texture coordinates to the fragment shader
    texCoord = inTexCoord;
}
