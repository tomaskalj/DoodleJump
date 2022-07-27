package com.tomaskalj.doodlejump.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Ellipse {
    @Setter
    private float x;
    @Setter
    private float y;
    private final float width;
    private final float height;

    public boolean contains(float otherX, float otherY) {
        return (Math.pow((x - otherX), 2) / Math.pow(width, 2)) + (Math.pow((y - otherY), 2) / Math.pow(height, 2)) <= 1;
    }
}
