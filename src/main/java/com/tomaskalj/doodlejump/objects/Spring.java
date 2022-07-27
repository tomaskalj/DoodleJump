package com.tomaskalj.doodlejump.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.tomaskalj.doodlejump.common.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Spring {
    private Texture texture;

    @Setter
    private Texture extendedTexture;
    private final Rectangle rectangle;

    public Spring(float x, float y) {
        texture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "spring.png")));
        extendedTexture = new Texture(Gdx.files.internal(String.format(Constants.FILE_LOCATION, "extended_spring.png")));
        rectangle = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void onRender(DoodleBoy doodleBoy) {
        if (!doodleBoy.isFalling()) {
            return;
        }

        if (doodleBoy.getRectangle().overlaps(rectangle)) {
            doodleBoy.onSpringCollide();
            texture = extendedTexture;
        }
    }
}
