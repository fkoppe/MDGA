package pp.mdga.client.board;

import pp.mdga.client.Asset;

/**
 * Record for holding Asset information
 */
record AssetOnMap(Asset asset, int x, int y, float rot) {
}
