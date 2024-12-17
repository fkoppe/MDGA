package pp.mdga.client.animation;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;

import static pp.mdga.client.Util.linInt;

public class FadeControl extends ActionControl {
    private float duration; // Duration of the fade effect
    private float timeElapsed = 0;
    private boolean init = false;
    private float startAlpha;
    private float endAlpha;

    public FadeControl(float duration, float startAlpha, float endAlpha, Runnable actionAfter) {
        super(actionAfter);
        this.duration = duration;
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
    }

    public FadeControl(float duration, float startAlpha, float endAlpha) {
        this(duration, startAlpha, endAlpha, null);
    }

    @Override
    protected void initSpatial() {
        init = true;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!init) return;

        timeElapsed += tpf;
        float t = timeElapsed / duration; // Calculate progress (0 to 1)

        if (t >= 1) {
            // Fade complete
            t = 1;
            init = false;
            spatial.removeControl(this);
            action();
        }

        float alpha = linInt(startAlpha, endAlpha, t); // Interpolate alpha

        // Update the material's alpha
        if (spatial instanceof Geometry geometry) {
            Material mat = geometry.getMaterial();
            if (mat != null) {
                ColorRGBA diffuse = (ColorRGBA) mat.getParam("Diffuse").getValue();
                mat.setColor("Diffuse", new ColorRGBA(diffuse.r, diffuse.g, diffuse.b, alpha));

                ColorRGBA ambient = (ColorRGBA) mat.getParam("Ambient").getValue();
                mat.setColor("Ambient", new ColorRGBA(ambient.r, ambient.g, ambient.b, alpha));

                // Disable shadows when the object is nearly invisible
                if (alpha <= 0.1f) {
                    geometry.setShadowMode(RenderQueue.ShadowMode.Off);
                } else {
                    geometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                }
            } else throw new RuntimeException("Material is null");
        } else throw new RuntimeException("Spatial is not instance of Geometry");
    }
}
