package Enemies;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntity;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

public class PolarBearEnemy extends Enemy {
	private float gravity = 5f;
	private float movementSpeed = 5f;
	private Direction startFacingDirection;
	private Direction facingDirection;
	private AirGroundState airGroundState;

	public PolarBearEnemy(Point location, Direction facingDirection) {
		super(location.x, location.y, new SpriteSheet(ImageLoader.load("PolarBearEnemy.png"), 29, 26), "WALK_LEFT");

		this.startFacingDirection = facingDirection;
		this.initialize();
	}

	@Override
	public void initialize() {
		super.initialize();
		facingDirection = startFacingDirection;
		if (facingDirection == Direction.RIGHT) {
			currentAnimationName = "WALK_RIGHT";
		} else if (facingDirection == Direction.LEFT) {
			currentAnimationName = "WALK_LEFT";
		}
		airGroundState = AirGroundState.GROUND;
	}

	@Override
	public void update(Player player) {
		float moveAmountX = 0;
		float moveAmountY = 0;

		// add gravity (if in air, this will cause bug to fall)
		moveAmountY += gravity;

		if ((player.getX() > getX())) {
			facingDirection = Direction.LEFT;
			currentAnimationName = "WALK_RIGHT";

			if (facingDirection == Direction.LEFT) {
				moveAmountX += movementSpeed;
			}
		}
		if ((player.getX() < getX())) {
			facingDirection = Direction.RIGHT;
			currentAnimationName = "WALK_LEFT";

			if (facingDirection == Direction.RIGHT) {
				moveAmountX -= movementSpeed;
			}
		}
		// if on ground, walk forward based on facing direction

		// move bug
		moveYHandleCollision(moveAmountY);
		moveXHandleCollision(moveAmountX);

		super.update(player);
	}

	@Override
	public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
		// if bug has collided into something while walking forward,
		// it turns around (changes facing direction)
		if (hasCollided) {
			if (direction == Direction.RIGHT) {
				facingDirection = Direction.LEFT;
				currentAnimationName = "WALK_LEFT";
			} else {
				facingDirection = Direction.RIGHT;
				currentAnimationName = "WALK_RIGHT";
			}
		}
	}

	@Override
	public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
		// if bug is colliding with the ground, change its air ground state to GROUND
		// if it is not colliding with the ground, it means that it's currently in the
		// air, so its air ground state is changed to AIR
		if (direction == Direction.DOWN) {
			if (hasCollided) {
				airGroundState = AirGroundState.GROUND;
			} else {
				airGroundState = AirGroundState.AIR;
			}
		}
	}

	@Override
	public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
		return new HashMap<String, Frame[]>() {
			{
				put("WALK_LEFT", new Frame[] {
						new FrameBuilder(spriteSheet.getSprite(0, 0), 100).withScale(2)
								.withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(2, 5, 29, 19).build(),
						new FrameBuilder(spriteSheet.getSprite(0, 1), 100).withScale(2)
								.withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(2, 5, 29, 19).build() });

				put("WALK_RIGHT",
						new Frame[] {
								new FrameBuilder(spriteSheet.getSprite(0, 0), 100).withScale(2).withBounds(2, 5, 29, 19)
										.build(),
								new FrameBuilder(spriteSheet.getSprite(0, 1), 100).withScale(2).withBounds(2, 5, 29, 19)
										.build() });
			}
		};
	}
}
