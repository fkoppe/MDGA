package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.InputButton;
import pp.mdga.client.button.MenuButton;
import pp.mdga.client.view.MainView;

import java.util.Random;

/**
 * The {@code StartDialog} class represents the initial dialog in the application,
 * allowing the user to input their name, host or join a game, or exit the application.
 */
public class StartDialog extends Dialog {
    private InputButton nameInput;

    private MenuButton hostButton;
    private MenuButton joinButton;
    private MenuButton endButton;

    private final MainView view;

    /**
     * Constructs a {@code StartDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The main view used for navigation and interaction with the dialog.
     */
    public StartDialog(MdgaApp app, Node node, MainView view) {
        super(app, node);

        this.view = view;

        nameInput = new InputButton(app, node, "Name: ", 16);

        hostButton = new MenuButton(app, node, () -> view.forward(true), "Spiel hosten");
        joinButton = new MenuButton(app, node, () -> view.forward(false), "Spiel beitreten");
        endButton = new MenuButton(app, node, app::stop, "Spiel beenden");

        float offset = 2.8f;

        nameInput.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.15f;

        hostButton.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.25f;

        joinButton.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.25f;

        endButton.setPos(new Vector2f(0, 1.8f));
    }

    /**
     * Called when the dialog is shown. Displays the name input field and all buttons.
     */
    @Override
    protected void onShow() {
        nameInput.show();

        hostButton.show();
        joinButton.show();
        endButton.show();
    }

    /**
     * Called when the dialog is hidden. Hides the name input field and all buttons.
     */
    @Override
    protected void onHide() {
        nameInput.hide();

        hostButton.hide();
        joinButton.hide();
        endButton.hide();
    }

    /**
     * Updates the state of the name input field. This method is called periodically to synchronize the dialog state.
     */
    public void update() {
        nameInput.update();
    }

    /**
     * Retrieves the name entered by the user. If no name is provided, a random name is generated.
     *
     * @return The user's name or a randomly generated name.
     */
    public String getName() {
        String name = nameInput.getString();

        if (name == null || name.trim().isEmpty()) {
            String[] names = {
                    "PixelPirat",
                    "NoobJäger",
                    "LagMeister",
                    "KnopfDrücker",
                    "SpawnCamper",
                    "AFKHeld",
                    "RageQuitter",
                    "GameOverPro",
                    "Checkpoint",
                    "RespawnHeld",
                    "Teebeutel",
                    "GlitchHexer",
                    "QuickScope",
                    "LootSammler",
                    "EpicLauch",
                    "KartoffelPro",
                    "StilleKlinge",
                    "TastenHeld",
                    "PixelKrieger",
                    "HacknSlash",
                    "JoystickJoe",
                    "SpawnFalle",
                    "OneHitWanda",
                    "CamperKing",
                    "GameGenie",
                    "HighPing",
                    "CheesePro",
                    "Speedy",
                    "GigaGamer",
                    "LevelNoob",
                    "SkillTobi",
                    "HeadshotMax",
                    "PentaPaul",
                    "CritKarl",
                    "ManaLeerer",
                    "Nachlader",
                    "ClutchKönig",
                    "FriendlyFe",
                    "ZonenHeld",
                    "SchleichKatze",
                    "ShotgunPro",
                    "SniperUdo",
                    "BossHunter",
                    "HeldenNoob",
                    "KillFranz",
                    "FragKarl",
                    "TeamNiete",
                    "LootPaul",
                    "UltraNoob",
                    "ProfiScout",
                    "PunkteKlaus",
                    "KrüppelKill",
                    "PixelNinja",
                    "NoobCrusher",
                    "LagBoss",
                    "SpawnKing",
                    "AFKSlayer",
                    "RespawnPro",
                    "Killjoy",
                    "GameBreaker",
                    "FastFingers",
                    "LootKing",
                    "QuickFlick",
                    "SilentShot",
                    "HackGod",
                    "GlitchHero",
                    "SpeedyBot",
                    "AimWizard",
                    "FragMaster",
                    "OneTapPro",
                    "KnifeLord",
                    "MetaHunter",
                    "PingWarrior",
                    "KeyBash",
                    "ClutchPro",
                    "ScopeBot",
                    "TrollMage",
                    "PowerLooter",
                    "TankHero",
                    "CampLord",
                    "SmurfSlayer",
                    "SkillThief",
                    "SniperGod",
                    "LevelHack",
                    "GhostAim",
                    "BossTamer",
                    "ShotgunJoe",
                    "AimRider",
                    "KillCount",
                    "PixelManiac",
                    "TrollOver",
                    "SneakPro",
                    "ReloadKing",
                    "SpawnTrap",
                    "LagLover",
                    "MetaHater",
                    "BoomMaker",
                    "WipeLord",
                    "CarryPro",
                    "ProBaiter",
                    "GameWarden",
                    "KartoffelKönig",
                    "SaufenderWolf",
                    "WurstGriller",
                    "Flitzekacke",
                    "BratwurstBub",
                    "Hoppeldoppels",
                    "BananenMensch",
                    "KlopapierGuru",
                    "SchnitzelKing",
                    "NerdNomade",
                    "Dönertänzer",
                    "GlitzerGurke",
                    "SchinkenShrek",
                    "KäseKalle",
                    "SchokoSchnecke",
                    "KeksKämpfer",
                    "QuarkPiraten",
                    "Müslimonster",
                    "KnuddelNase",
                    "FantaFighter",
                    "SchnapsSaurier",
                    "Wackelpudding",
                    "ZitronenZock",
                    "FettWurst",
                    "PlüschPanda",
                    "Zuckerschnur",
                    "FluffiKopf",
                    "DonutDöner",
                    "VollpfostenX",
                    "Waschlappen",
                    "Witzepumper",
                    "ToastTraum",
                    "FroschFighter",
                    "KrümelTiger",
                    "RegenWolke",
                    "PuddingPower",
                    "KoffeinKrieger",
                    "SpeckSchlumpf",
                    "SuperSuppe",
                    "BierBärchen",
                    "FischBär",
                    "Flauschi",
                    "Schokomonster",
                    "ChaosKäse",
                    "FlitzLappen",
                    "WurstWombat",
                    "KrümelMensch",
                    "PuddingBär",
                    "ZickZack",
                    "Schwabel",
                    "Fluffi",
                    "RülpsFrosch",
                    "PommesPapa",
                    "QuarkBär",
                    "KnusperKönig",
                    "ToastBrot",
                    "Ploppster",
                    "Schleimschwein",
                    "Äpfelchen",
                    "Knallbonbon",
                    "KaffeeKopf",
                    "WackelWurst",
                    "RennKeks",
                    "BröselBub",
                    "ZockerBrot",
                    "BierWurm",
                    "StinkFlummi",
                    "SchlumpfKing",
                    "PurzelBär",
                    "FlinkFluff",
                    "PloppPudel",
                    "Schnorchel",
                    "FliegenKopf",
                    "PixelPommes",
                    "SchwipsWürst",
                    "WutzBär",
                    "KnuddelKeks",
                    "FantaFlumm",
                    "ZockerKäse",
                    "LachHäufchen",
                    "GurkenGuru",
                    "PonySchnitzel",
                    "NudelNinja",
                    "VulkanKeks",
                    "WasserToast",
                    "MenschSalat",
                    "KampfKohl",
                    "SockenZirkus",
                    "SchwimmBärchen",
                    "TanzenderPudel",
                    "PizzamarktMensch",
                    "ZahnarztZocker",
                    "RollerRudi",
                    "PupsPilot",
                    "WitzigeZwiebel",
                    "Pillenschlucker",
                    "ZwiebelReiter",
                    "HüpfenderKaktus",
                    "AsteroidenAlf",
                    "ChaosKarotte",
                    "WolkenFurz",
                    "Krümelmonster",
                    "WackelBiene",
            };

            Random random = new Random();
            name = names[random.nextInt(names.length)];
        }

        return name;
    }
}
