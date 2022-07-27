package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomaskalj.doodlejump.common.DrawUtil;
import lombok.Getter;

public class JetpackPlatform extends Platform {
    @Getter
    private final Jetpack jetpack;

    public JetpackPlatform(float x, float y) {
        super(x, y, "platform.png");
        jetpack = new Jetpack(x + 20, y + texture.getHeight());
    }

    @Override
    public void onRender(float delta, SpriteBatch batch) {
        if (jetpack.getTexture() != null) {
            DrawUtil.drawTexture(batch, jetpack.getTexture(), jetpack.getRectangle().x, jetpack.getRectangle().y);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (jetpack.getTexture() != null) {
            jetpack.getTexture().dispose();
        }
    }
}
