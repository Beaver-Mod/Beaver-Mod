package me.beavermod.util.misc;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class MathUtil {

    public static float lerp(float start, float end, float amount) {
        return start + amount * (end - start);
    }

    public static double lerp(double start, double end, double amount) {
        return start + amount * (end - start);
    }

    public static Vec3 getVectorForRotation(float yaw, float pitch) {
        float x = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float y = MathHelper.sin(-pitch * 0.017453292F);
        float z = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float pitchDiff = -MathHelper.cos(-pitch * 0.017453292F);
        return new Vec3(x * pitchDiff, y, z * pitchDiff);
    }

    public static boolean hitboxCheck(Vec3 startPosition, AxisAlignedBB hitBox, float yaw, float pitch, float maxDistance) {
        Vec3 rotationVector = getVectorForRotation(yaw, pitch);
        for (float delta = 0.0F; delta <= maxDistance; delta += 0.2F) {
            Vec3 offsetVector = startPosition.addVector(rotationVector.xCoord * delta, rotationVector.yCoord * delta, rotationVector.zCoord * delta);
            if (hitBox.isVecInside(offsetVector)) {
                return true;
            }
        }
        return false;
    }


}
