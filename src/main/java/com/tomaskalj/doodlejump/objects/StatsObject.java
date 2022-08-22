package com.tomaskalj.doodlejump.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsObject {
    @Setter
    private int highScore;
    private int jumps;
    private int shots;
    private int monstersShot;
    private int monstersJumpedOn;

    public void incrementJumps() {
        jumps++;
    }

    public void incrementShots() {
        shots++;
    }

    public void incrementMonstersShot() {
        monstersShot++;
    }

    public void incrementMonstersJumpedOn() {
        monstersJumpedOn++;
    }
}
