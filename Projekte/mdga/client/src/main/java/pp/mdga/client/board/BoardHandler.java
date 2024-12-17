package pp.mdga.client.board;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.animation.*;
import pp.mdga.client.gui.DiceControl;
import pp.mdga.game.Color;

import java.util.*;

/**
 * BoardHandler is responsible for managing the game board in the MDGA client, including handling
 * the initialization, movement, and management of game pieces and assets.
 * It works closely with the MdgaApp to create and manipulate 3D models of assets, track player pieces,
 * and manage movement across the game board.
 */
public class BoardHandler {
    // Constants defining the grid size and elevation of the board
    private static final float GRID_SIZE = 1.72f;
    private static final float GRID_ELEVATION = 0.0f;
    private static final String MAP_NAME = "Maps/map.mdga";

    // The main application instance for accessing game assets and logic
    private final MdgaApp app;

    // Collection of in-game assets and board elements
    private ArrayList<NodeControl> infield;
    private Map<UUID, PieceControl> pieces;
    private Map<Color, List<AssetOnMap>> colorAssetsMap;
    private Map<Color, List<NodeControl>> homeNodesMap;
    private Map<Color, List<NodeControl>> waitingNodesMap;
    private Map<Color, List<PieceControl>> waitingPiecesMap;
    private Map<Color, Map<UUID, NodeControl>> waitingNodes;
    private Map<UUID, Color> pieceColor;

    private final Node rootNodeBoard;
    private final Node rootNode;
    private final FilterPostProcessor fpp;

    private boolean isInitialised;

    // Flags and lists for handling piece selection and movement
    private List<PieceControl> selectableOwnPieces;
    private List<PieceControl> selectableEnemyPieces;
    private Map<PieceControl, NodeControl> selectedPieceNodeMap;
    private List<NodeControl> outlineNodes;
    private PieceControl selectedOwnPiece;
    private PieceControl selectedEnemyPiece;
    private DiceControl diceControl;
    //Radar Position for Matrix animation
    private Vector3f radarPos;
    //TankTop for shellAnimation
    private TankTopControl tankTop;

    /**
     * Creates a new BoardHandler.
     *
     * @param app      The main application instance
     * @param rootNode The root node where the board will be attached
     * @param fpp      The post-processor for effects like shadows or filters
     * @throws RuntimeException if the app is null
     */
    public BoardHandler(MdgaApp app, Node rootNode, FilterPostProcessor fpp) {
        if (app == null) throw new RuntimeException("app is null");

        this.app = app;
        this.fpp = fpp;
        rootNodeBoard = new Node("Board Root Node");
        this.rootNode = rootNode;
        isInitialised = false;
    }

    /**
     * Initializes the game board by setting up the pieces and nodes.
     */
    public void init() {
        isInitialised = true;
        selectableOwnPieces = new ArrayList<>();
        selectableEnemyPieces = new ArrayList<>();
        selectedPieceNodeMap = new HashMap<>();
        outlineNodes = new ArrayList<>();
        selectedOwnPiece = null;
        selectedEnemyPiece = null;
        initMap();
        rootNode.attachChild(rootNodeBoard);
    }

    /**
     * Shuts down the board handler by detaching all board-related nodes and clearing selected pieces.
     */
    public void shutdown() {
        clearSelectable();
        isInitialised = false;
        rootNode.detachChild(rootNodeBoard);
    }

    /**
     * Adds an asset to the map of player assets, ensuring that the player does not have too many assets.
     *
     * @param col        The color of the player
     * @param assetOnMap The asset to be added
     * @throws RuntimeException if there are too many assets for the player
     */
    private void addFigureToPlayerMap(Color col, AssetOnMap assetOnMap) {
        List<AssetOnMap> inMap = addItemToMapList(colorAssetsMap, col, assetOnMap);
        if (inMap.size() > 4) throw new RuntimeException("to many assets for " + col);
    }

    /**
     * Initializes the map with the assets loaded from the map file and corresponding nodes.
     */
    private void initMap() {
        pieces = new HashMap<>();
        colorAssetsMap = new HashMap<>();
        infield = new ArrayList<>(40);
        homeNodesMap = new HashMap<>();
        waitingNodesMap = new HashMap<>();
        waitingPiecesMap = new HashMap<>();
        pieceColor = new HashMap<>();
        diceControl = new DiceControl(app.getAssetManager());
        diceControl.create(new Vector3f(0, 0, 0), 0.7f, true);
        waitingNodes = new HashMap<>();
        waitingNodes.put(Color.AIRFORCE, new HashMap<>());
        waitingNodes.put(Color.ARMY, new HashMap<>());
        waitingNodes.put(Color.NAVY, new HashMap<>());
        waitingNodes.put(Color.CYBER, new HashMap<>());


        List<AssetOnMap> assetOnMaps = MapLoader.loadMap(MAP_NAME);

        for (AssetOnMap assetOnMap : assetOnMaps) {
            switch (assetOnMap.asset()) {
                case lw -> addFigureToPlayerMap(assetToColor(Asset.lw), assetOnMap);
                case heer -> addFigureToPlayerMap(assetToColor(Asset.heer), assetOnMap);
                case cir -> addFigureToPlayerMap(assetToColor(Asset.cir), assetOnMap);
                case marine -> addFigureToPlayerMap(assetToColor(Asset.marine), assetOnMap);
                case node_normal, node_bonus, node_start ->
                        infield.add(displayAndControl(assetOnMap, new NodeControl(app, fpp)));
                case node_home_black -> addHomeNode(homeNodesMap, Color.AIRFORCE, assetOnMap);
                case node_home_blue -> addHomeNode(homeNodesMap, Color.NAVY, assetOnMap);
                case node_home_green -> addHomeNode(homeNodesMap, Color.ARMY, assetOnMap);
                case node_home_yellow -> addHomeNode(homeNodesMap, Color.CYBER, assetOnMap);
                case node_wait_black -> addHomeNode(waitingNodesMap, Color.AIRFORCE, assetOnMap);
                case node_wait_blue -> addHomeNode(waitingNodesMap, Color.NAVY, assetOnMap);
                case node_wait_green -> addHomeNode(waitingNodesMap, Color.ARMY, assetOnMap);
                case node_wait_yellow -> addHomeNode(waitingNodesMap, Color.CYBER, assetOnMap);
                case radar -> addRadar(assetOnMap);
                case tankShoot -> addTankShoot(assetOnMap);

                default -> displayAsset(assetOnMap);
            }
        }
    }

    private void addTankShoot(AssetOnMap assetOnMap) {
        displayAsset(assetOnMap);
        tankTop = displayAndControl(new AssetOnMap(Asset.tankShootTop, assetOnMap.x(), assetOnMap.y(), assetOnMap.rot()), new TankTopControl());
    }

    private void addRadar(AssetOnMap assetOnMap) {
        radarPos = gridToWorld(assetOnMap.x(), assetOnMap.y());
        displayAsset(assetOnMap);
    }

    /**
     * Converts an asset to its corresponding color.
     *
     * @param asset The asset to be converted
     * @return The color associated with the asset
     * @throws RuntimeException if the asset is invalid
     */
    private Color assetToColor(Asset asset) {
        return switch (asset) {
            case lw -> Color.AIRFORCE;
            case heer -> Color.ARMY;
            case marine -> Color.NAVY;
            case cir -> Color.CYBER;
            default -> throw new RuntimeException("invalid asset");
        };
    }

    /**
     * Creates a 3D model of an asset and adds it to the board.
     *
     * @param asset The asset to be displayed
     * @param pos   The position of the asset on the board
     * @param rot   The rotation of the asset
     * @return The Spatial representation of the asset
     */
    private Spatial createModel(Asset asset, Vector3f pos, float rot) {
        String modelName = asset.getModelPath();
        String texName = asset.getDiffPath();
        Spatial model = app.getAssetManager().loadModel(modelName);
        model.scale(asset.getSize());
        model.rotate((float) Math.toRadians(0), 0, (float) Math.toRadians(rot));
        model.setLocalTranslation(pos);
        model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(texName));
        mat.setBoolean("UseMaterialColors", true); // Required for Material Colors
        mat.setColor("Diffuse", new ColorRGBA(1, 1, 1, 1)); // White color with full alpha
        mat.setColor("Ambient", new ColorRGBA(1, 1, 1, 1)); // Ambient color with full alpha
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        model.setMaterial(mat);

        rootNodeBoard.attachChild(model);
        return model;
    }

    /**
     * Converts grid coordinates to world space.
     *
     * @param x The x-coordinate on the grid
     * @param y The y-coordinate on the grid
     * @return The corresponding world position
     */
    public static Vector3f gridToWorld(int x, int y) {
        return new Vector3f(GRID_SIZE * x, GRID_SIZE * y, GRID_ELEVATION);
    }

    /**
     * Displays an asset on the map at the given position with the specified rotation.
     *
     * @param assetOnMap The asset to be displayed.
     * @return A spatial representation of the asset at the specified location and rotation.
     */
    private Spatial displayAsset(AssetOnMap assetOnMap) {
        int x = assetOnMap.x();
        int y = assetOnMap.y();
        return createModel(assetOnMap.asset(), gridToWorld(x, y), assetOnMap.rot());
    }

    /**
     * Adds a visual representation of an asset to the scene, attaches a control to it, and returns the control.
     *
     * @param assetOnMap The asset to be displayed in the 3D environment.
     * @param control    The control to be added to the spatial representing the asset.
     * @param <T>        The type of control, extending {@code AbstractControl}.
     * @return The control that was added to the spatial.
     */
    private <T extends AbstractControl> T displayAndControl(AssetOnMap assetOnMap, T control) {
        Spatial spatial = displayAsset(assetOnMap);
        spatial.addControl(control);
        return control;
    }

    /**
     * Moves a piece in the 3D environment to the location of a specified node.
     *
     * @param pieceControl The control managing the piece to be moved.
     * @param nodeControl  The control managing the target node to which the piece will move.
     */
    private void movePieceToNode(PieceControl pieceControl, NodeControl nodeControl) {
        pieceControl.setLocation(nodeControl.getLocation());
    }

    /**
     * Adds a home node for a specific player color, attaching it to the map of home nodes.
     *
     * @param map        The map storing lists of home nodes by player color.
     * @param color      The color associated with the home nodes to be added.
     * @param assetOnMap The asset representing the home node in the 3D environment.
     * @throws RuntimeException if more than 4 home nodes are added for a single color.
     */
    private void addHomeNode(Map<Color, List<NodeControl>> map, Color color, AssetOnMap assetOnMap) {
        List<NodeControl> homeNodes = addItemToMapList(map, color, displayAndControl(assetOnMap, new NodeControl(app, fpp)));
        if (homeNodes.size() > 4) throw new RuntimeException("too many homeNodes for " + color);
    }

    /**
     * Calculates the rotation angle required to move a piece from one position to another.
     *
     * @param prev The previous position.
     * @param next The target position.
     * @return The rotation angle in degrees.
     */
    private float getRotationMove(Vector3f prev, Vector3f next) {
        Vector3f direction = next.subtract(prev).normalizeLocal();
        //I had to reverse dir.y, because then it worked.
        float newRot = (float) Math.toDegrees(Math.atan2(direction.x, -direction.y));
        if (newRot < 0) newRot += 360;
        return newRot;
    }

    /**
     * Recursively moves a piece from its current index to the destination index,
     * to keep track of the piece rotation.
     *
     * @param uuid      The UUID of the piece to move.
     * @param curIndex  The current index of the piece.
     * @param moveIndex The target index to move the piece to.
     */
    private void movePieceRek(UUID uuid, int curIndex, int moveIndex) {
        if (curIndex == moveIndex) return;

        int nextIndex = (curIndex + 1) % infield.size();

        PieceControl pieceControl = pieces.get(uuid);
        NodeControl nodeCur = infield.get(curIndex);
        NodeControl nodeMove = infield.get(nextIndex);

        pieceControl.setRotation(getRotationMove(nodeCur.getLocation(), nodeMove.getLocation()));

        movePieceToNode(pieceControl, nodeMove);


        movePieceRek(uuid, nextIndex, moveIndex);
    }

    /**
     * Adds an item to a list in a map. If the key does not exist in the map, a new list is created.
     *
     * @param map  The map containing lists of items.
     * @param key  The key associated with the list in the map.
     * @param item The item to be added to the list.
     * @param <T>  The type of items in the list.
     * @param <E>  The type of the key in the map.
     * @return The updated list associated with the specified key.
     */
    private <T, E> List<T> addItemToMapList(Map<E, List<T>> map, E key, T item) {
        List<T> list = map.getOrDefault(key, new ArrayList<>());
        list.add(item);
        map.put(key, list);
        return list;
    }

    /**
     * Removes an item from a list in a map. If the key does not exist in the map, a new list is created.
     *
     * @param map  The map containing lists of items.
     * @param key  The key associated with the list in the map.
     * @param item The item to be removed from the list.
     * @param <T>  The type of items in the list.
     * @param <E>  The type of the key in the map.
     */
    private <T, E> void removeItemFromMapList(Map<E, List<T>> map, E key, T item) {
        List<T> list = map.getOrDefault(key, new ArrayList<>());
        list.remove(item);
        map.put(key, list);
    }

    /**
     * Calculates the mean position of the waiting nodes for a specific color.
     *
     * @param color The color associated with the waiting nodes.
     * @return The mean position of the waiting nodes as a {@code Vector3f}.
     */
    private Vector3f getWaitingPos(Color color) {
        return getMeanPosition(waitingNodesMap.get(color).stream().map(NodeControl::getLocation).toList());
    }

    /**
     * Gets the mean position of a list of vectors.
     *
     * @param vectors The list of vectors.
     * @return The mean position as a Vector3f.
     */
    private static Vector3f getMeanPosition(List<Vector3f> vectors) {
        if (vectors.isEmpty()) return new Vector3f(0, 0, 0);

        Vector3f sum = new Vector3f(0, 0, 0);
        for (Vector3f v : vectors) {
            sum.addLocal(v);
        }
        return sum.divide(vectors.size());
    }

    /**
     * Adds a player to the game by associating a color and a list of UUIDs to corresponding assets and waiting nodes.
     *
     * @param color the color of the player
     * @param uuid  the list of UUIDs representing the player's assets
     * @throws RuntimeException if the number of assets or waiting nodes does not match the provided UUIDs
     */
    public void addPlayer(Color color, List<UUID> uuid) {

        List<AssetOnMap> playerAssets = colorAssetsMap.get(color);
        if (playerAssets == null) throw new RuntimeException("Assets for Player color are not defined");
        if (uuid.size() != playerAssets.size())
            throw new RuntimeException("UUID array and playerAssets are not the same size");

        List<NodeControl> waitNodes = waitingNodesMap.get(color);
        if (waitNodes.size() != playerAssets.size())
            throw new RuntimeException("waitNodes size does not match playerAssets size");


        for (int i = 0; i < playerAssets.size(); i++) {
            AssetOnMap assetOnMap = playerAssets.get(i);
            UUID pieceUuid = uuid.get(i);

            // Initialize PieceControl
            PieceControl pieceControl = displayAndControl(assetOnMap, new PieceControl(assetOnMap.rot(), app.getAssetManager(), app, fpp));
            pieceControl.setRotation(assetOnMap.rot());

            // Assign piece to waiting node
            NodeControl waitNode = getNextWaitingNode(color);
            waitingNodes.get(color).put(pieceUuid, waitNode);

            // Move piece to node
            movePieceToNode(pieceControl, waitNode);

            // Update mappings
            pieces.put(pieceUuid, pieceControl);
            pieceColor.put(pieceUuid, color);
            addItemToMapList(waitingPiecesMap, color, pieceControl);
        }
    }

    /**
     * Moves a piece to its corresponding home node based on the given index.
     *
     * @param uuid  the UUID of the piece to move
     * @param index the index of the home node to move the piece to
     * @throws RuntimeException if the UUID is not mapped to a color or if the home nodes are not properly defined
     */
    private void executeHomeMove(UUID uuid, int index) {

        Color color = pieceColor.get(uuid);
        if (color == null) throw new RuntimeException("uuid is not mapped to a color");

        List<NodeControl> homeNodes = homeNodesMap.get(color);

        if (homeNodesMap.size() != 4) throw new RuntimeException("HomeNodes for" + color + " are not properly defined");

        PieceControl pieceControl = pieces.get(uuid);
        NodeControl nodeControl = homeNodes.get(index);
        movePieceToNode(pieceControl, nodeControl);

        //rotate piece in direction of homeNodes
        NodeControl firstHomeNode = homeNodes.get(0);
        NodeControl lastHomeNode = homeNodes.get(homeNodes.size() - 1);

        pieceControl.setRotation(getRotationMove(firstHomeNode.getLocation(), lastHomeNode.getLocation()));
        app.getModelSynchronize().animationEnd();
        app.getModelSynchronize().animationEnd();
    }

    /**
     * Starts the movement of a piece to a target node based on the given index.
     *
     * @param uuid      the UUID of the piece to move
     * @param nodeIndex the index of the target node to move the piece to
     * @throws RuntimeException         if the UUID is not mapped to a color or the piece control is not found
     * @throws IllegalArgumentException if the node index is invalid
     */
    private void executeStartMove(UUID uuid, int nodeIndex) {

        // Farbe des Pieces abrufen
        Color color = pieceColor.get(uuid);
        if (color == null) throw new RuntimeException("UUID is not mapped to a color");

        // PieceControl abrufen
        PieceControl pieceControl = pieces.get(uuid);
        if (pieceControl == null) throw new RuntimeException("PieceControl not found for UUID: " + uuid);

        // Zielknoten abrufen und prüfen
        if (nodeIndex < 0 || nodeIndex >= infield.size()) {
            throw new IllegalArgumentException("Invalid nodeIndex: " + nodeIndex);
        }
        NodeControl targetNode = infield.get(nodeIndex);

        movePieceToNode(pieceControl, targetNode);

        removeItemFromMapList(waitingPiecesMap, color, pieceControl);
        waitingNodes.get(color).remove(uuid);
        app.getModelSynchronize().animationEnd();
        app.getModelSynchronize().animationEnd();
    }

    /**
     * Moves a piece from its current position to the target position based on the given indexes.
     *
     * @param uuid      the UUID of the piece to move
     * @param curIndex  the current index of the piece
     * @param moveIndex the target index of the move
     */
    private void executeMove(UUID uuid, int curIndex, int moveIndex) {

        movePieceRek(uuid, curIndex, moveIndex);
        app.getModelSynchronize().animationEnd();
        app.getModelSynchronize().animationEnd();
    }

    /**
     * Throws a piece to the next available waiting node and updates the waiting node mapping.
     *
     * @param uuid the UUID of the piece to throw
     * @throws RuntimeException if the UUID is not mapped to a color or if no available waiting nodes are found
     */
    private void throwPiece(UUID uuid) {

        // Farbe des Pieces abrufen
        Color color = pieceColor.get(uuid);
        if (color == null) throw new RuntimeException("UUID is not mapped to a color");

        // PieceControl abrufen
        PieceControl pieceControl = pieces.get(uuid);
        if (pieceControl == null) throw new RuntimeException("PieceControl not found for UUID: " + uuid);

        // Nächste freie Waiting Node abrufen
        NodeControl nextWaitNode = getNextWaitingNode(color);
        if (nextWaitNode == null) {
            throw new IllegalStateException("No available waiting nodes for color: " + color);
        }

        // Bewegung durchführen
        movePieceToNode(pieceControl, nextWaitNode);

        // Waiting Nodes aktualisieren
        waitingNodes.get(color).put(uuid, nextWaitNode);

        // Synchronisation oder Animation
        pieceControl.rotateInit();
        app.getAcousticHandler().playSound(MdgaSound.LOSE);
        app.getModelSynchronize().animationEnd();
    }


    /**
     * Activates the shield for the specified piece.
     *
     * @param uuid the UUID of the piece to shield
     */
    public void shieldPiece(UUID uuid) {
        pieces.get(uuid).activateShield();
    }

    /**
     * Deactivates the shield for the specified piece.
     *
     * @param uuid the UUID of the piece to unshield
     */
    public void unshieldPiece(UUID uuid) {
        pieces.get(uuid).deactivateShield();
    }

    /**
     * Suppresses the shield for the specified piece.
     *
     * @param uuid the UUID of the piece to suppress the shield
     */
    public void suppressShield(UUID uuid) {

        pieces.get(uuid).suppressShield();
    }

    /**
     * Swaps the positions and rotations of two pieces.
     *
     * @param p1   the first piece to swap
     * @param p2   the second piece to swap
     * @param loc1 the original location of the first piece
     * @param rot1 the original rotation of the first piece
     * @param loc2 the original location of the second piece
     * @param rot2 the original rotation of the second piece
     */
    private void executeSwap(PieceControl p1, PieceControl p2, Vector3f loc1, float rot1, Vector3f loc2, float rot2) {
        p1.setLocation(loc2);
        p2.setLocation(loc1);

        p1.setRotation(rot2);
        p2.setRotation(rot1);

        app.getModelSynchronize().animationEnd();
    }

    /**
     * Outlines the possible move nodes for a list of pieces based on the move indices and whether it's a home move.
     *
     * @param pieces     the list of UUIDs representing the pieces to outline
     * @param moveIndexe the list of indices for the target move nodes
     * @param homeMoves  the list indicating whether the move is a home move
     * @throws RuntimeException if the sizes of the input lists do not match
     */
    public void setSelectableMove(List<UUID> pieces, List<Integer> moveIndexe, List<Boolean> homeMoves) {
        if (pieces.size() != moveIndexe.size() || pieces.size() != homeMoves.size())
            throw new RuntimeException("arrays are not the same size");

        selectableEnemyPieces.clear();
        selectableOwnPieces.clear();
        selectedOwnPiece = null;
        selectedEnemyPiece = null;

        for (int i = 0; i < pieces.size(); i++) {
            UUID uuid = pieces.get(i);
            PieceControl pieceControl = this.pieces.get(uuid);
            NodeControl nodeControl;

            if (homeMoves.get(i)) {
                Color color = pieceColor.get(uuid);
                nodeControl = homeNodesMap.get(color).get(moveIndexe.get(i));
            } else {
                nodeControl = infield.get(moveIndexe.get(i));
            }
            pieceControl.selectableOwn();
            nodeControl.selectableOwn();
            outlineNodes.add(nodeControl);
            selectableOwnPieces.add(pieceControl);
            selectedPieceNodeMap.put(pieceControl, nodeControl);
        }
    }

    /**
     * Outlines the pieces that can be swapped based on the provided own and enemy pieces.
     *
     * @param ownPieces   the list of UUIDs representing the player's pieces
     * @param enemyPieces the list of UUIDs representing the enemy's pieces
     */
    public void setSelectableSwap(List<UUID> ownPieces, List<UUID> enemyPieces) {

        selectableEnemyPieces.clear();
        selectableOwnPieces.clear();
        selectedOwnPiece = null;
        selectedEnemyPiece = null;

        for (UUID uuid : ownPieces) {
            PieceControl p = pieces.get(uuid);
            p.selectableOwn();
            selectableOwnPieces.add(p);
        }
        for (UUID uuid : enemyPieces) {
            PieceControl p = pieces.get(uuid);
            p.selectableEnemy();
            selectableEnemyPieces.add(p);
        }
    }

    /**
     * Outlines the pieces that can be shielded based on the provided list of pieces.
     *
     * @param pieces the list of UUIDs representing the pieces to be shielded
     */
    public void setSelectableShield(List<UUID> pieces) {
        selectableOwnPieces.clear();
        selectableEnemyPieces.clear();
        selectedOwnPiece = null;
        selectedEnemyPiece = null;

        for (UUID uuid : pieces) {
            PieceControl p = this.pieces.get(uuid);
            p.selectableOwn();
            selectableOwnPieces.add(p);
        }
    }

    /**
     * Selects a piece from either the own or enemy pieces based on the input and deselects others if needed.
     *
     * @param pieceSelected the PieceControl instance representing the piece selected by the user
     */
    public void pieceSelect(OutlineOEControl selected) {
        PieceControl piece = getPieceByOE(selected);
        NodeControl node = selectedPieceNodeMap.get(piece);

        boolean isSelected = piece.isSelected();
        if (selectableOwnPieces.contains(piece)) {
            for (PieceControl p : selectableOwnPieces) {
                p.selectOff();
                NodeControl n = selectedPieceNodeMap.get(p);
                if (n != null) n.selectOff();
            }
            if (!isSelected) {
                piece.selectOn();
                if (node != null) node.selectOn();
                selectedOwnPiece = piece;
            } else {
                piece.selectOff();
                if (node != null) node.selectOff();;
                selectedOwnPiece = null;
            }
        } else if (selectableEnemyPieces.contains(piece)) {
            for (PieceControl p : selectableEnemyPieces) {
                p.selectOff();
            }
            if (!isSelected) {
                piece.selectOn();
                selectedEnemyPiece = piece;
            } else {
                piece.selectOff();
                selectedEnemyPiece = null;
            }
        } else throw new RuntimeException("pieceSelected is not in own/enemySelectablePieces");

        app.getModelSynchronize().select(getKeyByValue(pieces, selectedOwnPiece), getKeyByValue(pieces, selectedEnemyPiece));
    }

    public void hoverOn(OutlineOEControl hover) {
        PieceControl piece = getPieceByOE(hover);
        NodeControl node = selectedPieceNodeMap.get(piece);

        piece.hoverOn();
        if(node != null) node.hoverOn();
    }

    public void hoverOff(OutlineOEControl hover) {
        PieceControl piece = getPieceByOE(hover);
        NodeControl node = selectedPieceNodeMap.get(piece);

        piece.hoverOff();
        if(node != null) node.hoverOff();
    }

    private PieceControl getPieceByOE(OutlineOEControl control){
        PieceControl piece;
        if (control instanceof PieceControl p){
            piece = p;
        }
        else if (control instanceof NodeControl n){
            piece = getKeyByValue(selectedPieceNodeMap, n);
        }
        else throw new RuntimeException("selected is not instanceof piece or node");
        return piece;
    }

    /**
     * Clears all highlighted, selectable, and selected pieces and nodes.
     */
    public void clearSelectable() {
        for (PieceControl p : selectableOwnPieces) {
            p.selectableOff();
            NodeControl n = selectedPieceNodeMap.get(p);
            if(n != null) n.selectableOff();
        }
        for (PieceControl p : selectableEnemyPieces) {
            p.selectableOff();
        }

        outlineNodes.clear();
        selectableEnemyPieces.clear();
        selectableOwnPieces.clear();
        selectedPieceNodeMap.clear();
        selectedEnemyPiece = null;
        selectedOwnPiece = null;
    }

    /**
     * Displays the dice for the specified color at the appropriate position.
     *
     * @param color the color of the player whose dice should be displayed
     */
    public void showDice(Color color) {
        rootNodeBoard.attachChild(diceControl.getSpatial());
        diceControl.setPos(getWaitingPos(color).add(new Vector3f(0, 0, 4)));
        diceControl.spin();
    }

    /**
     * Hides the dice from the view.
     */
    public void hideDice() {
        diceControl.hide();
    }

    private <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Animates the movement of a piece from its current index to a target index.
     *
     * @param uuid      the UUID of the piece to animate
     * @param curIndex  the current index of the piece
     * @param moveIndex the target index to animate the piece to
     */
    public void movePiece(UUID uuid, int curIndex, int moveIndex) {

        pieces.get(uuid).getSpatial().addControl(new MoveControl(
                infield.get(curIndex).getLocation(),
                infield.get(moveIndex).getLocation(),
                () -> executeMove(uuid, curIndex, moveIndex)));
    }

    /**
     * Animates the movement of a piece to its home position based on the given home index.
     *
     * @param uuid      the UUID of the piece to animate
     * @param homeIndex the index of the home node to move the piece to
     */
    public void movePieceHome(UUID uuid, int homeIndex) {
        pieces.get(uuid).getSpatial().addControl(new MoveControl(
                pieces.get(uuid).getLocation(),
                homeNodesMap.get(pieceColor.get(uuid)).get(homeIndex).getLocation(),
                () -> executeHomeMove(uuid, homeIndex)));
    }

    /**
     * Animates the start of the movement of a piece to a target index.
     *
     * @param uuid      the UUID of the piece to animate
     * @param moveIndex the target index to animate the piece to
     */
    public void movePieceStart(UUID uuid, int moveIndex) {
        pieces.get(uuid).getSpatial().addControl(new MoveControl(
                pieces.get(uuid).getLocation(),
                infield.get(moveIndex).getLocation(),
                () -> executeStartMove(uuid, moveIndex)
        ));
    }

    /**
     * Animates the throwing of a piece to the next available waiting node.
     *
     * @param uuid the UUID of the piece to animate
     */
    private void throwPieceAnim(UUID uuid) {
        pieces.get(uuid).getSpatial().addControl(new MoveControl(
                pieces.get(uuid).getLocation(), getNextWaitingNode(pieceColor.get(uuid)).getLocation(),
                () -> throwPiece(uuid))
        );
    }

    public void throwPiece(UUID uuid, Color throwColor) {
        switch (throwColor) {
            case ARMY -> throwShell(uuid);
            case NAVY -> throwMissile(uuid);
            case CYBER -> throwMatrix(uuid);
            case AIRFORCE -> throwBomb(uuid);
            default -> throw new RuntimeException("invalid color");
        }
    }

    /**
     * Animates the throwing of a piece to the next available waiting node.
     *
     * @param uuid the UUID of the piece to animate
     */
    private void throwBomb(UUID uuid) {
        Vector3f targetPoint = pieces.get(uuid).getLocation();

        JetAnimation anim = new JetAnimation(app, rootNode, targetPoint, 40, 6, () -> throwPieceAnim(uuid));
        anim.start();
    }

    private void throwMatrix(UUID uuid) {
        app.getAcousticHandler().playSound(MdgaSound.MATRIX);
        Spatial piece = pieces.get(uuid).getSpatial();
        piece.addControl(new MatrixAnimation(app, radarPos, () -> {
            throwPieceAnim(uuid);
        }));
    }

    private void throwMissile(UUID uuid) {
        Vector3f targetPoint = pieces.get(uuid).getLocation();

        MissileAnimation anim = new MissileAnimation(app, rootNode, targetPoint, 2f, () -> throwPieceAnim(uuid));
        anim.start();
    }

    private void throwShell(UUID uuid) {
        pieces.get(uuid).getSpatial().addControl(new ShellAnimation(tankTop, app, () -> throwPieceAnim(uuid)));
    }

    /**
     * Animates the swapping of two pieces by swapping their positions and rotations.
     *
     * @param piece1 the UUID of the first piece
     * @param piece2 the UUID of the second piece
     */
    public void swapPieces(UUID piece1, UUID piece2) {
        PieceControl piece1Control = pieces.get(piece1);
        PieceControl piece2Control = pieces.get(piece2);

        Vector3f loc1 = piece1Control.getLocation().clone();
        Vector3f loc2 = piece2Control.getLocation().clone();
        float rot1 = piece1Control.getRotation();
        float rot2 = piece2Control.getRotation();

        piece1Control.getSpatial().addControl(new MoveControl(
                piece1Control.getLocation().clone(),
                piece2Control.getLocation().clone(),
                () -> {
                }
        ));
        piece2Control.getSpatial().addControl(new MoveControl(
                piece2Control.getLocation().clone(),
                piece1Control.getLocation().clone(),
                () -> executeSwap(piece1Control, piece2Control, loc1, rot1, loc2, rot2)
        ));
    }

    /**
     * Retrieves the next available waiting node for the specified color.
     *
     * @param color the color of the player to get the next waiting node for
     * @return the next available NodeControl for the specified color
     * @throws IllegalStateException if no available waiting nodes are found for the color
     */
    private NodeControl getNextWaitingNode(Color color) {
        List<NodeControl> nodes = waitingNodesMap.get(color);

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalStateException("Keine verfügbaren Warteschleifen-Knoten für die Farbe " + color);
        }

        for (NodeControl node : nodes) {
            if (!waitingNodes.getOrDefault(color, new HashMap<>()).containsValue(node)) {
                return node;
            }
        }

        throw new IllegalStateException("Keine freien Nodes im Wartebereich für die Farbe " + color);
    }
}
