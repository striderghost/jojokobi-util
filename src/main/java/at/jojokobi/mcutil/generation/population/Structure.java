package at.jojokobi.mcutil.generation.population;

import at.jojokobi.mcutil.generation.TerrainGenUtil;

import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;

public abstract class Structure {
	
	private int width = 0;
	private int length = 0;
	private int height = 0;
	private int chance = 4;
	private int xModifier = 1;
	private int zModifier = 1;
	private Environment dimension = null;
	
	public Structure(int width, int length, int height, int chance) {
		this.width = width;
		this.length = length;
		this.height = height;
		this.chance = chance;
	}

	public int getTerrainHeight(Location place) {
		return TerrainGenUtil.getTerrainHeight(place);
	}

	public int calculateMidPlacementY(int width, int length, Location place) {
		return (this.getTerrainHeight(place) + this.getTerrainHeight(place.clone().add(width, 0, 0)) + this.getTerrainHeight(place.clone().add(width, 0, length)) + this.getTerrainHeight(place.clone().add(0, 0, length)))/4;
	}

	public int calculateMaxPlacementY(int width, int length, Location place) {
		return Math.max(Math.max(this.getTerrainHeight(place), this.getTerrainHeight(place.clone().add(width, 0, 0))), Math.max(this.getTerrainHeight(place.clone().add(width, 0, length)), this.getTerrainHeight(place.clone().add(0, 0, length))));
	}

	public int calculatePlacementY (int width, int length, Location place) {
		return calculateMaxPlacementY(width, length, place);
	}
	
	public abstract List<StructureInstance<? extends Structure>> generate(Location loc, long seed);
	
	public abstract String getIdentifier ();
	
	public List<StructureInstance<? extends Structure>> generateNaturally(Chunk chunk, long seed) {
		Location place = new Location(chunk.getWorld(), chunk.getX() * TerrainGenUtil.CHUNK_WIDTH, 1, chunk.getZ() * TerrainGenUtil.CHUNK_LENGTH);
		place.setY(calculatePlacementY(getWidth(), getLength(), place));
		return generateNaturally(place, seed);
	}
	
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		return generate(place, seed);
	}
	
	public boolean canGenerate (Chunk chunk, long seed) {
		Random random = new Random (TerrainGenUtil.generateValueBasedSeed(seed, chunk.getX() * TerrainGenUtil.CHUNK_WIDTH + getxModifier(), 1, chunk.getZ() * TerrainGenUtil.CHUNK_LENGTH + getzModifier()));
		return chance > 0 && random.nextInt(chance) == 0 && (dimension == null || dimension == chunk.getWorld().getEnvironment());
	}
	
	public int getxModifier() {
		return xModifier;
	}

	public int getzModifier() {
		return zModifier;
	}

	public void setzModifier(int yFactor) {
		this.zModifier = yFactor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}
	
	public Environment getDimension() {
		return dimension;
	}

	public void setDimension(Environment dimension) {
		this.dimension = dimension;
	}

	public void setxModifier(int xModifier) {
		this.xModifier = xModifier;
	}
	
	public StructureInstance<? extends Structure> getStandardInstance (World world) {
		return getStandardInstance(new Location(world, 0, 0, 0));
	}
	
	public abstract StructureInstance<? extends Structure> getStandardInstance (Location location);
	
	protected long generateValueBeasedSeed (Location loc, long seed) {
		return TerrainGenUtil.generateValueBasedSeed(seed, loc.getBlockX() + getxModifier(), 1, loc.getBlockZ() + getzModifier());
	}

}
