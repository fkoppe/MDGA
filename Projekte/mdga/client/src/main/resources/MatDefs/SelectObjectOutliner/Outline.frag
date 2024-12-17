// Samplers for textures
uniform sampler2D m_Texture;
uniform sampler2D m_OutlineDepthTexture;

// Input texture coordinates from the vertex shader
in vec2 texCoord;

// Resolution of the screen and outline settings
uniform vec2 m_Resolution;
uniform vec4 m_OutlineColor;
uniform float m_OutlineWidth;

// Output color of the fragment
out vec4 fragColor;

void main() {
    // Sample depth textures at various offsets
    vec4 depth = texture(m_OutlineDepthTexture, texCoord);
    vec4 depth1 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, m_OutlineWidth)) / m_Resolution);
    vec4 depth2 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, -m_OutlineWidth)) / m_Resolution);
    vec4 depth3 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, m_OutlineWidth)) / m_Resolution);
    vec4 depth4 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, -m_OutlineWidth)) / m_Resolution);
    vec4 depth5 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, m_OutlineWidth)) / m_Resolution);
    vec4 depth6 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, -m_OutlineWidth)) / m_Resolution);
    vec4 depth7 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, 0.0)) / m_Resolution);
    vec4 depth8 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, 0.0)) / m_Resolution);

    // Sample the main texture
    vec4 color = texture(m_Texture, texCoord);

    // Check if an outline should be applied
    bool isEdge = (depth == vec4(0.0)) &&
                  (depth1 != depth || depth2 != depth || depth3 != depth || depth4 != depth ||
                   depth5 != depth || depth6 != depth || depth7 != depth || depth8 != depth);

    // Output the final color
    fragColor = isEdge ? m_OutlineColor : color;
}
