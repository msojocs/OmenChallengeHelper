package org.omenhelper.Omen.Body;

/**
 * @Author jiyec
 * @Date 2021/5/29 16:59
 * @Version 1.0
 **/
public class StartBody extends BasicBody{
    private String body = "{\n" +
            "  \"jsonrpc\": \"2.0\",\n" +
            "  \"id\": \"#applicationId#\",\n" +
            "  \"method\": \"mobile.sessions.v2.start\",\n" +
            "  \"params\": {\n" +
            "    \"accountToken\": \"#accountToken#\",\n" +
            "    \"applicationId\": \"#applicationId#\",\n" +
            "    \"externalPlayerId\": \"#externalPlayerId#\",\n" +
            "    \"eventNames\": [\n" +
            "      \"PLAY:OVERWATCH\",\n" +
            "      \"PLAY:HEROES_OF_THE_STORM\",\n" +
            "      \"PLAY:FORTNITE\",\n" +
            "      \"PLAY:THE_DIVISION\",\n" +
            "      \"PLAY:THE_DIVISION_2\",\n" +
            "      \"PLAY:PUBG\",\n" +
            "      \"PLAY:APEX_LEGENDS\",\n" +
            "      \"PLAY:CS_GO\",\n" +
            "      \"PLAY:LEAGUE_OF_LEGENDS\",\n" +
            "      \"PLAY:DOTA_2\",\n" +
            "      \"PLAY:SMITE\",\n" +
            "      \"PLAY:AGE_OF_EMPIRES_2\",\n" +
            "      \"PLAY:STARCRAFT_2\",\n" +
            "      \"PLAY:COMPANY_OF_HEROES_2\",\n" +
            "      \"PLAY:ASSASSINS_CREED_ODYSSEY\",\n" +
            "      \"PLAY:WORLD_OF_WARCRAFT\",\n" +
            "      \"PLAY:WORLD_OF_WARCRAFT_CLASSIC\",\n" +
            "      \"PLAY:SPOTIFY\",\n" +
            "      \"PLAY:RINGS_OF_ELYSIUM\",\n" +
            "      \"PLAY:HEARTHSTONE\",\n" +
            "      \"PLAY:GARRYS_MOD\",\n" +
            "      \"PLAY:GOLF_IT\",\n" +
            "      \"PLAY:DECEIT\",\n" +
            "      \"PLAY:SEVEN_DAYS_TO_DIE\",\n" +
            "      \"PLAY:DOOM_ETERNAL\",\n" +
            "      \"PLAY:STARWARS_JEDI_FALLEN_ORDER\",\n" +
            "      \"PLAY:MINECRAFT\",\n" +
            "      \"PLAY:DEAD_BY_DAYLIGHT\",\n" +
            "      \"PLAY:NETFLIX\",\n" +
            "      \"PLAY:HULU\",\n" +
            "      \"PLAY:PATH_OF_EXILE\",\n" +
            "      \"PLAY:WARTHUNDER\",\n" +
            "      \"PLAY:CALL_OF_DUTY_MODERN_WARFARE\",\n" +
            "      \"PLAY:ROCKET_LEAGUE\",\n" +
            "      \"PLAY:NBA_2K20\",\n" +
            "      \"PLAY:STREET_FIGHTER_V\",\n" +
            "      \"PLAY:DRAGON_BALL_FIGHTER_Z\",\n" +
            "      \"PLAY:GEARS_OF_WAR_5\",\n" +
            "      \"PLAY:FIFA_20\",\n" +
            "      \"PLAY:MASTER_CHIEF_COLLECTION\",\n" +
            "      \"PLAY:RAINBOW_SIX\",\n" +
            "      \"PLAY:UPLAY\",\n" +
            "      \"PLAY:ROBLOX\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:GOLD_LEFT\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:TIME_ELIMINATED\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:THIRD_PLACE_OR_HIGHER\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:SECOND_PLACE_OR_HIGHER\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:PLAYERS_ELIMINATED\",\n" +
            "      \"VERSUS_GAME_API:TEAMFIGHT_TACTICS:TOTAL_DAMAGE_TO_PLAYERS\",\n" +
            "      \"PLAY:MONSTER_HUNTER_WORLD\",\n" +
            "      \"PLAY:WARFRAME\",\n" +
            "      \"PLAY:LEGENDS_OF_RUNETERRA\",\n" +
            "      \"PLAY:VALORANT\",\n" +
            "      \"PLAY:CROSSFIRE\",\n" +
            "      \"PLAY:PALADINS\",\n" +
            "      \"PLAY:TROVE\",\n" +
            "      \"PLAY:RIFT\",\n" +
            "      \"PLAY:ARCHEAGE\",\n" +
            "      \"PLAY:IRONSIGHT\",\n" +
            "      \"GAMIGO_PLACEHOLDER\",\n" +
            "      \"PLAY:TWINSAGA\",\n" +
            "      \"PLAY:AURA_KINGDOM\",\n" +
            "      \"PLAY:SHAIYA\",\n" +
            "      \"PLAY:SOLITAIRE\",\n" +
            "      \"PLAY:TONY_HAWK\",\n" +
            "      \"PLAY:AVENGERS\",\n" +
            "      \"PLAY:FALL_GUYS\",\n" +
            "      \"PLAY:QQ_SPEED\",\n" +
            "      \"PLAY:FIFA_ONLINE_3\",\n" +
            "      \"PLAY:NBA2KOL2\",\n" +
            "      \"PLAY:DESTINY2\",\n" +
            "      \"PLAY:AMONG_US\",\n" +
            "      \"PLAY:MAPLE_STORY\",\n" +
            "      \"PLAY:ASSASSINS_CREED_VALHALLA\",\n" +
            "      \"PLAY:FREESTYLE_STREET_BASKETBALL\",\n" +
            "      \"PLAY:CRAZY_RACING_KART_RIDER\",\n" +
            "      \"PLAY:COD_BLACK_OPS_COLD_WAR\",\n" +
            "      \"PLAY:CYBERPUNK_2077\",\n" +
            "      \"PLAY:HADES\",\n" +
            "      \"PLAY:RUST\",\n" +
            "      \"PLAY:GENSHIN_IMPACT\",\n" +
            "      \"PLAY:ESCAPE_FROM_TARKOV\",\n" +
            "      \"PLAY:RED_DEAD_REDEMPTION_2\",\n" +
            "      \"PLAY:CIVILIZATION_VI\",\n" +
            "      \"PLAY:VALHEIM\",\n" +
            "      \"PLAY:FINAL_FANTASY_XIV\",\n" +
            "      \"PLAY:OASIS\",\n" +
            "      \"PLAY:CASTLE_CRASHERS\",\n" +
            "      \"PLAY:GANG_BEASTS\",\n" +
            "      \"PLAY:SPEEDRUNNERS\",\n" +
            "      \"PLAY:OVERCOOKED_2\",\n" +
            "      \"PLAY:OVERCOOKED_ALL_YOU_CAN_EAT\",\n" +
            "      \"Launch OMEN Command Center\",\n" +
            "      \"Use OMEN Command Center\",\n" +
            "      \"OMEN Command Center Macro Created\",\n" +
            "      \"OMEN Command Center Macro Assigned\",\n" +
            "      \"Mindframe Adjust Cooling Option\",\n" +
            "      \"Connect 2 different OMEN accessories to your PC at the same time\",\n" +
            "      \"Use Omen Reactor\",\n" +
            "      \"Use Omen Photon\",\n" +
            "      \"Launch Game From GameLauncher\",\n" +
            "      \"Image like From ImageGallery\",\n" +
            "      \"Set as background From ImageGallery\",\n" +
            "      \"Download image From ImageGallery\",\n" +
            "      \"overwatch\",\n" +
            "      \"heroesofthestorm\",\n" +
            "      \"heroesofthestorm_x64\",\n" +
            "      \"FortniteClient-Win64-Shipping\",\n" +
            "      \"FortniteClient-Win64-Shipping_BE\",\n" +
            "      \"thedivision\",\n" +
            "      \"thedivision2\",\n" +
            "      \"TslGame\",\n" +
            "      \"r5apex\",\n" +
            "      \"csgo\",\n" +
            "      \"League of Legends\",\n" +
            "      \"dota2\",\n" +
            "      \"smite\",\n" +
            "      \"AoE2DE_s\",\n" +
            "      \"AoK HD\",\n" +
            "      \"AoE2DE\",\n" +
            "      \"sc2\",\n" +
            "      \"s2_x64\",\n" +
            "      \"RelicCoH2\",\n" +
            "      \"acodyssey\",\n" +
            "      \"wow\",\n" +
            "      \"wow64\",\n" +
            "      \"wow_classic\",\n" +
            "      \"wowclassic\",\n" +
            "      \"Spotify\",\n" +
            "      \"Europa_client\",\n" +
            "      \"hearthstone\",\n" +
            "      \"hl2\",\n" +
            "      \"GolfIt-Win64-Shipping\",\n" +
            "      \"GolfIt\",\n" +
            "      \"Deceit\",\n" +
            "      \"7DaysToDie\",\n" +
            "      \"DoomEternal_temp\",\n" +
            "      \"starwarsjedifallenorder\",\n" +
            "      \"Minecraft.Windows\",\n" +
            "      \"net.minecraft.client.main.Main\",\n" +
            "      \"DeadByDaylight-Win64-Shipping\",\n" +
            "      \"4DF9E0F8.Netflix\",\n" +
            "      \"HuluLLC.HuluPlus\",\n" +
            "      \"PathOfExileSteam\",\n" +
            "      \"PathOfExile_x64Steam\",\n" +
            "      \"aces\",\n" +
            "      \"modernwarfare\",\n" +
            "      \"RocketLeague\",\n" +
            "      \"NBA2K20\",\n" +
            "      \"StreetFighterV\",\n" +
            "      \"RED-Win64-Shipping\",\n" +
            "      \"Gears5\",\n" +
            "      \"fifa20\",\n" +
            "      \"MCC-Win64-Shipping\",\n" +
            "      \"MCC-Win64-Shipping-WinStore\",\n" +
            "      \"RainbowSix\",\n" +
            "      \"RainbowSix_BE\",\n" +
            "      \"RainbowSix_Vulkan\",\n" +
            "      \"upc\",\n" +
            "      \"ROBLOXCORPORATION.ROBLOX\",\n" +
            "      \"RobloxPlayerBeta\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_GOLD_LEFT\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_TIME_ELIMINATED\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_THIRD_PLACE_OR_HIGHER\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_SECOND_PLACE_OR_HIGHER\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_PLAYERS_ELIMINATED\",\n" +
            "      \"VERSUS_GAME_API_TEAMFIGHT_TACTICS_TOTAL_DAMAGE_TO_PLAYERS\",\n" +
            "      \"MonsterHunterWorld\",\n" +
            "      \"Warframe.x64\",\n" +
            "      \"lor\",\n" +
            "      \"valorant-Win64-shipping\",\n" +
            "      \"valorant\",\n" +
            "      \"crossfire\",\n" +
            "      \"Paladins\",\n" +
            "      \"trove\",\n" +
            "      \"rift_64\",\n" +
            "      \"rift_x64\",\n" +
            "      \"archeage\",\n" +
            "      \"ironsight\",\n" +
            "      \"Game\",\n" +
            "      \"Game.bin\",\n" +
            "      \"glyph_twinsaga\",\n" +
            "      \"glyph_aurakingdom\",\n" +
            "      \"glyph_shaiya\",\n" +
            "      \"Solitaire\",\n" +
            "      \"THPS12\",\n" +
            "      \"avengers\",\n" +
            "      \"Fallguys_client_game\",\n" +
            "      \"GameApp\",\n" +
            "      \"fifazf\",\n" +
            "      \"NBA2KOL2\",\n" +
            "      \"destiny2\",\n" +
            "      \"Among Us\",\n" +
            "      \"MapleStory\",\n" +
            "      \"ACValhalla\",\n" +
            "      \"FreeStyle\",\n" +
            "      \"KartRider\",\n" +
            "      \"BlackOpsColdWar\",\n" +
            "      \"Cyberpunk2077\",\n" +
            "      \"Hades\",\n" +
            "      \"RustClient\",\n" +
            "      \"GenshinImpact\",\n" +
            "      \"EscapeFromTarkov\",\n" +
            "      \"EscapeFromTarkov_BE\",\n" +
            "      \"RDR2\",\n" +
            "      \"CivilizationVI\",\n" +
            "      \"valheim\",\n" +
            "      \"ffxiv_dx11\",\n" +
            "      \"AD2F1837.OMENSpectate\",\n" +
            "      \"castle\",\n" +
            "      \"Gang Beasts\",\n" +
            "      \"SpeedRunners\",\n" +
            "      \"Overcooked2\",\n" +
            "      \"Overcooked All You Can Eat\"\n" +
            "    ],\n" +
            "    \"location\": {\n" +
            "      \"latitude\": 30.5832367,\n" +
            "      \"longitude\": 103.982384\n" +
            "    },\n" +
            "    \"sdk\": \"custom01\",\n" +
            "    \"sdkVersion\": \"3.0.0\",\n" +
            "    \"appDefaultLanguage\": \"en\",\n" +
            "    \"userPreferredLanguage\": \"en\"\n" +
            "  }\n" +
            "}";
    public StartBody(String applicationId, String sessionToken) {
        super(applicationId, sessionToken);
        this.body = this.body.replaceAll("#applicationId#", applicationId).replaceAll("#accountToken#", sessionToken);
    }

    public String genBody(String externalPlayerId){
        return this.body.replace("#externalPlayerId#", externalPlayerId);
    }

}
