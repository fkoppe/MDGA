package pp.mdga.client;

/**
 * Represents different assets in the application. Each asset may have an associated model path,
 * diffuse texture path, and a size factor. The enum provides multiple constructors to handle
 * varying levels of detail for different assets.
 */
public enum Asset {
    bigTent,
    cardStack,
    cir,
    heer,
    jet,
    jet_noGear("Models/jet/jet_noGear.j3o", "Models/jet/jet_diff.png"),
    lw,
    marine,
    node_home_blue("Models/node_home/node_home.j3o", "Models/node_home/node_home_blue_diff.png"),
    node_wait_blue("Models/node_home/node_home.j3o", "Models/node_home/node_home_blue_diff.png"),
    node_home_black("Models/node_home/node_home.j3o", "Models/node_home/node_home_black_diff.png"),
    node_wait_black("Models/node_home/node_home.j3o", "Models/node_home/node_home_black_diff.png"),
    node_home_green("Models/node_home/node_home.j3o", "Models/node_home/node_home_green_diff.png"),
    node_wait_green("Models/node_home/node_home.j3o", "Models/node_home/node_home_green_diff.png"),
    node_home_yellow("Models/node_home/node_home.j3o", "Models/node_home/node_home_orange_diff.png"),
    node_wait_yellow("Models/node_home/node_home.j3o", "Models/node_home/node_home_orange_diff.png"),
    node_normal,
    node_start("Models/node_normal/node_normal.j3o", "Models/node_normal/node_start_diff.png"),
    node_bonus("Models/node_normal/node_normal.j3o", "Models/node_normal/node_bonus_diff.png"),
    radar,
    ship(0.8f),
    smallTent,
    tank,
    world(1.2f),
    shieldRing("Models/shieldRing/shieldRing.j3o", null),
    treeSmall(1.2f),
    treeBig(1.2f),
    turboCard,
    turboSymbol("Models/turboCard/turboSymbol.j3o", "Models/turboCard/turboCard_diff.png"),
    swapCard,
    swapSymbol("Models/swapCard/swapSymbol.j3o", "Models/swapCard/swapCard_diff.png"),
    shieldCard,
    shieldSymbol("Models/shieldCard/shieldSymbol.j3o", "Models/shieldCard/shieldCard_diff.png"),
    dice,
    missile("Models/missile/AVMT300.obj", "Models/missile/texture.jpg", 0.1f),
    tankShoot("Models/tank/tankShoot_bot.j3o", "Models/tank/tank_diff.png"),
    tankShootTop("Models/tank/tankShoot_top.j3o", "Models/tank/tank_diff.png"),
    treesSmallBackground("Models/treeSmall/treesSmallBackground.j3o", "Models/treeSmall/treeSmall_diff.png", 1.2f),
    treesBigBackground("Models/treeBig/treesBigBackground.j3o", "Models/treeBig/treeBig_diff.png", 1.2f),
    shell;

    private final String modelPath;
    private final String diffPath;
    private final float size;
    private static final String ROOT = "Models/";

    /**
     * Default constructor. Initializes modelPath and diffPath based on the enum name and sets default size to 1.0.
     */
    Asset() {
        String folderFileName = "./" + ROOT + name() + "/" + name();
        this.modelPath = folderFileName + ".j3o";
        this.diffPath = folderFileName + "_diff.png";
        this.size = 1f;
    }

    /**
     * Constructor with specific model path and diffuse texture path.
     *
     * @param modelPath Path to the 3D model file.
     * @param diffPath  Path to the diffuse texture file.
     */
    Asset(String modelPath, String diffPath) {
        this.modelPath = modelPath;
        this.diffPath = diffPath;
        this.size = 1f;
    }

    /**
     * Constructor with specific model path. Diffuse texture path is derived based on enum name.
     *
     * @param modelPath Path to the 3D model file.
     */
    Asset(String modelPath) {
        String folderFileName = "./" + ROOT + name() + "/" + name();
        this.modelPath = modelPath;
        this.diffPath = folderFileName + "_diff.png";
        ;
        this.size = 1f;
    }

    /**
     * Constructor with specific size. Model and texture paths are derived based on enum name.
     *
     * @param size Scaling factor for the asset.
     */
    Asset(float size) {
        String folderFileName = "./" + ROOT + name() + "/" + name();
        this.modelPath = folderFileName + ".j3o";
        this.diffPath = folderFileName + "_diff.png";
        this.size = size;
    }

    /**
     * Constructor with specific model path, diffuse texture path, and size.
     *
     * @param modelPath Path to the 3D model file.
     * @param diffPath  Path to the diffuse texture file.
     * @param size      Scaling factor for the asset.
     */
    Asset(String modelPath, String diffPath, float size) {
        this.modelPath = modelPath;
        this.diffPath = diffPath;
        this.size = size;
    }

    /**
     * Gets the model path for the asset.
     *
     * @return Path to the 3D model file.
     */
    public String getModelPath() {
        return modelPath;
    }

    /**
     * Gets the diffuse texture path for the asset.
     *
     * @return Path to the diffuse texture file, or null if not applicable.
     */
    public String getDiffPath() {
        return diffPath;
    }

    /**
     * Gets the scaling factor for the asset.
     *
     * @return The size of the asset.
     */
    public float getSize() {
        return size;
    }
}
