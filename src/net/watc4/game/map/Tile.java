package net.watc4.game.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.watc4.game.display.Animation;
import net.watc4.game.utils.geometry.Hitbox;
import net.watc4.game.utils.geometry.RectangleHitbox;

/** A part of the Map. */
public class Tile
{
	/** Its Identifier. Allows better memory usage. */
	public final int id;
	/** True if this Tile is opaque ; i.e. it stops the light. */
	public final boolean isOpaque;
	/** True if this Tile is solid ; i.e. it stops entities. */
	public final boolean isSolid;
	/** The maximum Data this Tile can have. */
	public final byte maxData;
	/** Its sprite. */
	public final Animation sprite;

	/** Creates a new Tile.
	 * 
	 * @param id - Its identifier.
	 * @param sprite - Its Sprite. */
	public Tile(int id, byte maxData, Animation sprite, boolean solid, boolean opaque)
	{
		this.id = id;
		this.maxData = maxData;
		this.sprite = sprite;
		this.isSolid = solid;
		this.isOpaque = opaque;
		TileRegistry.registerTile(this);
	}

	/** @return The Image of this Tile. */
	public BufferedImage getSprite(Map map, int x, int y, int data)
	{
		if (this.sprite == null) return null;
		return this.sprite.getImage();
	}

	/** @param map - The Map.
	 * @param x - The X Position of this Tile (in tiles.)
	 * @param y - The Y Position of this Tile (in tiles.)
	 * @param data- The Tile data.
	 * @return The Hitbox of this Tile. */
	public Hitbox hitbox(Map map, int x, int y, int data)
	{
		return new RectangleHitbox(x * Map.TILESIZE, y * Map.TILESIZE, Map.TILESIZE, Map.TILESIZE);
	}

	/** Renders this Tile.
	 * 
	 * @param g - The Graphics required to draw.
	 * @param map - The Map
	 * @param x - The X Position (in Tiles).
	 * @param y - The Y Position (in Tiles).
	 * @param data - This Tile's Data. */
	public void renderAt(Graphics2D g, Map map, int x, int y, int data)
	{
		BufferedImage image = this.getSprite(map, x, y, data);
		if (image != null) g.drawImage(image, x * Map.TILESIZE, y * Map.TILESIZE, null);
	}

}
