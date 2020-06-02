package Scene;

import Enemies.BugEnemy;
import Engine.GraphicsHandler;
import Engine.Keyboard;
import GameObject.*;
import Utils.MathUtils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class MapEntity extends GameObject {
    protected MapEntityStatus mapEntityStatus = MapEntityStatus.ACTIVE;
    protected boolean isRespawnable = true;
    protected float startPositionX, startPositionY;
    private float moveAmountX, moveAmountY;
    protected float amountMovedX, amountMovedY;

    public MapEntity(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(spriteSheet, x, y, startingAnimation);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(BufferedImage image, float x, float y, String startingAnimation) {
        super(image, x, y, startingAnimation);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(BufferedImage image, float x, float y) {
        super(image, x, y);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(BufferedImage image, float x, float y, float scale) {
        super(image, x, y, scale);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(BufferedImage image, float x, float y, float scale, ImageEffect imageEffect) {
        super(image, x, y, scale, imageEffect);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public MapEntity(BufferedImage image, float x, float y, float scale, ImageEffect imageEffect, Rectangle bounds) {
        super(image, x, y, scale, imageEffect, bounds);
        this.startPositionX = x;
        this.startPositionY = y;
    }

    public void initialize() {
        this.amountMovedX = 0;
        this.amountMovedY = 0;
    }

    public int getStartPositionX() {
        return (int)startPositionX;
    }

    public int getStartPositionY() {
        return (int)startPositionY;
    }

    public MapEntityStatus getMapEntityStatus() {
        return mapEntityStatus;
    }

    public void setMapEntityStatus(MapEntityStatus mapEntityStatus) {
        this.mapEntityStatus = mapEntityStatus;
    }

    public boolean isRespawnable() {
        return isRespawnable;
    }

    public void setIsRespawnable(boolean isRespawnable) {
        this.isRespawnable = isRespawnable;
    }

    public void calibrate(Map map) {
        setX(getCalibratedXLocation(map));
        setY(getCalibratedYLocation(map));
    }

    private float getCalibratedXLocation(Map map) {
        return getStartPositionX() + amountMovedX + MathUtils.getRemainder(getXRaw()) - map.getCamera().getAmountMovedX();
    }

    private float getCalibratedYLocation(Map map) {
        return getStartPositionY() + amountMovedY + MathUtils.getRemainder(getYRaw()) - map.getCamera().getAmountMovedY();
    }

    @Override
    public void moveX(float dx) {
        moveAmountX += dx;
        super.moveX(dx);
    }

    @Override
    public void moveY(float dy) {
        moveAmountY += dy;
        super.moveY(dy);
    }

    public void update(Keyboard keyboard, Map map, Player player) {
        super.update();
        amountMovedX += (int)moveAmountX;
        amountMovedY += (int)moveAmountY;
        moveAmountX = 0;
        moveAmountY = 0;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
