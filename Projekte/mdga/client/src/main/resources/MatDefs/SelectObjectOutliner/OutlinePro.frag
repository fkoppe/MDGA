// Uniform samplers
uniform sampler2D m_Texture;
uniform sampler2D m_OutlineDepthTexture;
uniform sampler2D m_DepthTexture;

// Input texture coordinates from the vertex shader
in vec2 texCoord;

// Uniforms for resolution, outline color, and width
uniform vec2 m_Resolution;
uniform vec4 m_OutlineColor;
uniform float m_OutlineWidth;

// Output variable for fragment color
out vec4 fragColor;

void main() {
    vec4 depth = texture(m_OutlineDepthTexture, texCoord);
    vec4 depth1 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, m_OutlineWidth)) / m_Resolution);
    vec4 depth2 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, -m_OutlineWidth)) / m_Resolution);
    vec4 depth3 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, m_OutlineWidth)) / m_Resolution);
    vec4 depth4 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, -m_OutlineWidth)) / m_Resolution);
    vec4 depth5 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, m_OutlineWidth)) / m_Resolution);
    vec4 depth6 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, -m_OutlineWidth)) / m_Resolution);
    vec4 depth7 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(m_OutlineWidth, 0.0)) / m_Resolution);
    vec4 depth8 = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-m_OutlineWidth, 0.0)) / m_Resolution);

    vec4 color = texture(m_Texture, texCoord);

    float ratio = 0.0;
    if (depth == vec4(0.0) &&
        (depth1 != depth || depth2 != depth || depth3 != depth || depth4 != depth ||
         depth5 != depth || depth6 != depth || depth7 != depth || depth8 != depth)) {
        float dist = m_OutlineWidth;
        vec4 nearDepth;

        // Iterate to find the distance to the nearest edge
        for (float i = 0.0; i < m_OutlineWidth; i++) {
            if (depth1 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(i, i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth2 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(i, -i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth3 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-i, i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth4 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-i, -i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth5 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth6 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(0.0, -i)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth7 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(i, 0.0)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            } else if (depth8 != depth) {
                nearDepth = texture(m_OutlineDepthTexture, (texCoord * m_Resolution + vec2(-i, 0.0)) / m_Resolution);
                if (nearDepth != depth) { dist = i; break; }
            }
        }

        // Calculate ratio for outline blending
        ratio = clamp(1.0 - dist / m_OutlineWidth, 0.0, 1.0);

        // Blend the outline color with the base color
        fragColor = color * (1.0 - ratio) + m_OutlineColor * ratio;
    } else {
        // No outline, use the base texture color
        fragColor = color;
    }

    // Optional: Debugging outline visualization
    // fragColor = vec4(0.0, 1.0 - ratio, 0.0, 1.0);
}
