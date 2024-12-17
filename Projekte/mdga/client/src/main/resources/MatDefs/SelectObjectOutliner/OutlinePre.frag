// Input texture coordinates from the vertex shader
in vec2 texCoord;

// Output variable for the fragment color
out vec4 fragColor;

// Uniform samplers for textures
uniform sampler2D m_Texture;
uniform sampler2D m_NormalsTexture;
uniform sampler2D m_DepthTexture;

void main() {
    // Sample the texture at the given texture coordinates
    vec4 color = texture(m_Texture, texCoord);

    // Assign the color to the output variable
    fragColor = color;
}
