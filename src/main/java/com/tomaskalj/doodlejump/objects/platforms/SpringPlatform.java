package com.tomaskalj.doodlejump.objects.platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomaskalj.doodlejump.common.DrawUtil;
import com.tomaskalj.doodlejump.objects.Spring;
import lombok.Getter;

public class SpringPlatform extends Platform {
    @Getter
    private final Spring spring;

    public SpringPlatform(float x, float y) {
        super(x, y, "platform.png");
        spring = new Spring(x + 20, y + texture.getHeight());
    }

    @Override
    public void onRender(float delta, SpriteBatch batch) {
        DrawUtil.drawTexture(batch, spring.getTexture(), spring.getRectangle().x, spring.getRectangle().y);
    }

    @Override
    public void dispose() {
        super.dispose();
        spring.getTexture().dispose();
        spring.getExtendedTexture().dispose();
    }
}
