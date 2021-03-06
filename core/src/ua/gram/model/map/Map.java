package ua.gram.model.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.gram.DDGame;
import ua.gram.controller.factory.VoterFactory;
import ua.gram.controller.voter.TiledMapVoter;
import ua.gram.model.Initializer;
import ua.gram.model.enums.Voter;
import ua.gram.model.prototype.level.MapPrototype;
import ua.gram.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 * @see <a href="https://cdn.tutsplus.com/gamedev/authors/daniel-schuller/jrpg-using-tilemap-layers.png">TiledMap layers parsing</a>
 */
public class Map implements Initializer {

    private final List<TiledMapTileLayer> layers;
    private final MapPrototype prototype;
    private final Path path;
    private final TiledMap tiledMap;
    private final int parseLimit;
    private Spawn spawn;
    private Base base;
    private TiledMapVoter voter;
    private boolean recursion = false;

    public Map(TiledMap map, MapPrototype prototype) {
        this.prototype = prototype;
        tiledMap = map;
        parseLimit = 3 * DDGame.MAX_ENTITIES;
        path = new Path();
        layers = new ArrayList<>(tiledMap.getLayers().getCount());
        initLayers();
        Log.info("Map is OK");
    }

    public Map(DDGame game, MapPrototype prototype) {
        this.prototype = prototype;
        tiledMap = game.getResources().getMap(prototype.name);
        parseLimit = 3 * DDGame.MAX_ENTITIES;
        path = new Path();
        layers = new ArrayList<>(tiledMap.getLayers().getCount());
        initLayers();
        Log.info("Map is OK");
    }

    @Override
    public void init() {
        if (prototype.voter == null) {
            prototype.voter = VoterFactory.TILED_MAP;
        }

        voter = VoterFactory.create(this, prototype.voter);

        HashMap<String, Vector2> points = findMapPoints();

        spawn = new Spawn(points.get(prototype.spawnProperty));
        base = new Base(points.get(prototype.baseProperty));
    }

    private void initLayers() {
        for (MapLayer mapLayer : tiledMap.getLayers()) {
            if (!(mapLayer instanceof TiledMapTileLayer))
                throw new IllegalArgumentException("Provided map is not a TiledMap");
            TiledMapTileLayer layer = (TiledMapTileLayer) mapLayer;
            layers.add(layer);
        }
    }

    private HashMap<String, Vector2> findMapPoints() {
        HashMap<String, Vector2> map = new HashMap<String, Vector2>(2);

        outer:
        {
            for (TiledMapTileLayer layer : layers) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    for (int y = 0; y < layer.getHeight(); y++) {

                        if (!map.containsKey(prototype.spawnProperty)
                                && voter.isSpawn(x, y, Voter.Policy.AFFIRMATIVE)) {
                            map.put(prototype.spawnProperty, new Vector2(x, y));
                        } else if (!map.containsKey(prototype.baseProperty)
                                && voter.isBase(x, y, Voter.Policy.AFFIRMATIVE)) {
                            map.put(prototype.baseProperty, new Vector2(x, y));
                        }

                        if (hasFoundAllPoints(map))
                            break outer;
                    }
                }
            }
        }

        if (!hasFoundAllPoints(map))
            throw new GdxRuntimeException("Could not find map points");

        return map;
    }

    private boolean hasFoundAllPoints(java.util.Map map) {
        return map.containsKey(prototype.baseProperty)
                && map.containsKey(prototype.spawnProperty);
    }

    /**
     * Converts path to array of directions, which Actor should turn to.
     * Searches the map for 'walkable' property, starting from 'spawn' tile.
     * If found one and it is not the previous, it is added to array.
     * If the added one contains 'base' property, search is aborted.
     * <p/>
     * NOTE: Kind of A* path finding.
     */
    public WalkablePath normalizePath(Vector2 lastDir, Vector2 start) {
        if (lastDir == null || start == null)
            throw new NullPointerException("Path normalization is impossible");

        Vector2 lastDirectionCopy = lastDir.cpy();
        Vector2 startPositionCopy = start.cpy();

        WalkablePath path = new WalkablePath();

        boolean isFound = false;
        int count = 0;

        while (!isFound && count < parseLimit) {
            for (Vector2 direction : Path.DIRECTIONS) {
                if (!direction.equals(lastDir) && isInMapBounds(direction, startPositionCopy)) {
                    int currentX = (int) (startPositionCopy.x + direction.x);
                    int currentY = (int) (startPositionCopy.y + direction.y);

                    if (voter.isWalkable(currentX, currentY)) {

                        Vector2 copyDirection = direction.cpy();

                        startPositionCopy.add(copyDirection);
                        path.addDirection(copyDirection);
                        path.addPath(new Vector2(currentX, currentY));

                        lastDir = Path.opposite(copyDirection);

                        if (voter.isBase(currentX, currentY, Voter.Policy.AFFIRMATIVE)) {
                            isFound = true;
                        } else if (voter.isSpawn(currentX, currentY, Voter.Policy.AFFIRMATIVE)) {
                            if (!recursion) {
                                recursion = true;
                                Log.warn("Path normalization was reversed");
                                return normalizePath(Path.opposite(lastDirectionCopy), start);
                            } else
                                throw new GdxRuntimeException("Path normalization error: parsing in wrong direction");
                        }
                    }
                    ++count;
                }
            }
        }

        recursion = false;

        if (!isFound) {
            String cause;

            if (count >= parseLimit)
                cause = "parse limit of " + parseLimit + " was reached";
            else
                cause = "no Base was found";

            throw new GdxRuntimeException("Path normalization error: " + cause);
        }

        if (path.size() < 5) {
            throw new GdxRuntimeException("Path is incorrect");
        }

        Log.info("Path is OK");

        return path;
    }

    public boolean isInMapBounds(Vector2 direction, Vector2 position) {
        return (direction.equals(Path.WEST) && position.x > 0)
                || (direction.equals(Path.SOUTH) && position.y > 0)
                || (direction.equals(Path.EAST) && position.x < getFirstLayer().getWidth() - 1)
                || (direction.equals(Path.NORTH) && position.y < getFirstLayer().getHeight() - 1);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public Base getBase() {
        return base;
    }

    public Path getPath() {
        return path;
    }

    public MapPrototype getPrototype() {
        return prototype;
    }

    public TiledMapVoter getVoter() {
        return voter;
    }

    public List<TiledMapTileLayer> getLayers() {
        return layers;
    }

    public TiledMapTileLayer getFirstLayer() {
        return layers.get(0);
    }
}
