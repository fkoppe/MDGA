package pp.mdga.client.board;


import pp.mdga.client.Asset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for loading and parsing map data from a file.
 * The map contains asset names and coordinates for objects placed on the map.
 */
class MapLoader {
    /**
     * Private constructor to prevent instantiation.
     */
    private MapLoader() {

    }

    /**
     * Loads a map file and parses its contents into a list of assets and their positions.
     * Each line in the map file defines an asset, its coordinates, and its rotation.
     *
     * @param mapName The name of the map file to load. The file is expected to be located in the resources directory.
     * @return A list of {@link AssetOnMap} objects representing the assets placed on the map.
     * @throws IOException              If an error occurs while reading the map file.
     * @throws IllegalArgumentException If the map file contains invalid data.
     */
    public static List<AssetOnMap> loadMap(String mapName) {
        List<AssetOnMap> assetsOnMap = new ArrayList<>();

        try (
                InputStream inputStream = MapLoader.class.getClassLoader().getResourceAsStream(mapName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {

            while (true) {
                String entry = reader.readLine();
                if (entry == null) break;

                entry = entry.trim();

                if (entry.isEmpty()) continue;
                if (entry.charAt(0) == '#') continue;

                String[] parts = entry.trim().split(" ");

                assert (parts.length == 3) : "MapLoader: line has not 3 parts";

                String assetName = parts[0];
                String[] coordinates = parts[1].split(",");

                assert (coordinates.length == 2) : "MapLoade: coordinates are wrong";

                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                float rot = Float.parseFloat(parts[2]);

                Asset asset = getLoadedAsset(assetName);
                assetsOnMap.add(new AssetOnMap(asset, x, y, rot));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return assetsOnMap;
    }

    /**
     * Returns the corresponding {@link Asset} for a given asset name.
     *
     * @param assetName The name of the asset to load.
     * @return The {@link Asset} associated with the given name.
     * @throws IllegalStateException If the asset name is unrecognized.
     */
    private static Asset getLoadedAsset(String assetName) {
        return switch (assetName) {
            case "lw" -> Asset.lw;
            case "cir" -> Asset.cir;
            case "marine" -> Asset.marine;
            case "heer" -> Asset.heer;
            case "node" -> Asset.node_normal;
            case "node_start" -> Asset.node_start;
            case "node_bonus" -> Asset.node_bonus;
            case "node_home_blue" -> Asset.node_home_blue;
            case "node_home_yellow" -> Asset.node_home_yellow;
            case "node_home_black" -> Asset.node_home_black;
            case "node_home_green" -> Asset.node_home_green;
            case "node_wait_blue" -> Asset.node_wait_blue;
            case "node_wait_yellow" -> Asset.node_wait_yellow;
            case "node_wait_black" -> Asset.node_wait_black;
            case "node_wait_green" -> Asset.node_wait_green;
            case "world" -> Asset.world;
            case "jet" -> Asset.jet;
            case "big_tent" -> Asset.bigTent;
            case "small_tent" -> Asset.smallTent;
            case "radar" -> Asset.radar;
            case "ship" -> Asset.ship;
            case "tank" -> Asset.tank;
            case "treeSmall" -> Asset.treeSmall;
            case "treeBig" -> Asset.treeBig;
            case "tank_shoot" -> Asset.tankShoot;
            case "treesBigBackground" -> Asset.treesBigBackground;
            case "treesSmallBackground" -> Asset.treesSmallBackground;
            default -> throw new IllegalStateException("Unexpected value: " + assetName);
        };
    }
}
