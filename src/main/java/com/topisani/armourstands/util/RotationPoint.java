package com.topisani.armourstands.util;

import net.minecraft.util.math.Vec3d;

/**
 * @author topisani
 */
public enum RotationPoint {
    HEAD(new Vec3d(0, 1.6, 0)),
    LEFT_ARM(new Vec3d(0.2, 1.3, 0)),
    RIGHT_ARM(new Vec3d(-0.2, 1.3, 0)),
    LEFT_LEG(new Vec3d(0.2, 0.9, 0)),
    RIGHT_LEG(new Vec3d(-0.2, 0.9, 0)),
    BASE(new Vec3d(0, 0, 0));

    private final Vec3d pos;

    RotationPoint(Vec3d vec) {
        pos = vec;
    }

    public Vec3d getPos(float yaw) {
        Vec3d newPos = pos.rotateYaw((float) (-yaw / 180.0F * Math.PI));
        return newPos;
    }

    public static RotationPoint closest(Vec3d pos, float yaw) {
        RotationPoint closest = LEFT_ARM ;
        double dist = Float.MAX_VALUE;
        for (RotationPoint point : RotationPoint.values()) {
            double pointDist = pos.distanceTo(point.getPos( yaw));
            if (pointDist < dist) {
                closest = point;
                dist = pointDist;
            }
        }
        return closest;
    }
}

