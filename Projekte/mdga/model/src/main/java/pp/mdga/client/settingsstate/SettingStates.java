package pp.mdga.client.settingsstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;

public abstract class SettingStates extends ClientState {
    public SettingStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }
}
