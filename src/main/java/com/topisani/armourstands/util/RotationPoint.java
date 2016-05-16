package com.topisani.armourstands.util;

import net.minecraft.util.math.Vec3d;

/**
 * @author topisani
 */
public enum RotationPoint {
    HEAD(new Vec3d(0, 1.6, 0)),
    LEFT_ARM(new Vec3d(0, 1.4, 0.25)),
    RIGHT_ARM(new Vec3d(0, 1.4, -0.25)),
    LEFT_LEG(new Vec3d(0, 0.9, 0.25)),
    RIGHT_LEG(new Vec3d(0, 0.9, -0.25));

    public final Vec3d pos;

    RotationPoint(Vec3d vec) {
        pos = vec;
    }

    public static RotationPoint closest(Vec3d pos) {
        RotationPoint closest = LEFT_ARM ;
        double dist = Float.MAX_VALUE;
        for (RotationPoint point : RotationPoint.values()) {
            double pointDist = pos.distanceTo(point.pos);
            if (pointDist < dist) {
                closest = point;
                dist = pointDist;
            }
        }
        return closest;
    }
}

