package pp.mdga.client.acoustic;

/**
 * A record that encapsulates a sound asset along with its playback settings:
 * the relative volume (subVolume) and a delay before it starts playing.
 */
record SoundAssetDelayVolume(SoundAsset asset, float subVolume, float delay) {
}
