package com.invictus.territory.data;

import org.bukkit.block.Biome;
import org.bukkit.Sound;

public class TerritoryData {

    public static class TerritoryInfo {
        public String id;
        public String displayName;
        public String description;
        public String tag;
        public int number;
        public Biome biome;
        public String primaryColor;
        public String secondaryColor;
        public Sound ambientSound;
        public float soundPitch;

        public TerritoryInfo(String id, String displayName, String description, String tag,
                             int number, Biome biome, String primaryColor, String secondaryColor,
                             Sound ambientSound, float soundPitch) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
            this.tag = tag;
            this.number = number;
            this.biome = biome;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.ambientSound = ambientSound;
            this.soundPitch = soundPitch;
        }
    }

    // GRECIA
    public static final TerritoryInfo[] GREECE_TERRITORIES = {
            new TerritoryInfo("monte_olimpo", "Monte Olimpo", "Territorio legendario. Fragmentos divinos.",
                    "⚡ DESCENSO DE ZEUS", 1, Biome.WINDSWEPT_HILLS, "§b", "§3", Sound.MUSIC_DISC_WAIT, 0.8f),

            new TerritoryInfo("esparta", "Esparta", "Zona PvP brutal. +10% DAÑO PVP",
                    "⚔ PVP BRUTAL", 2, Biome.BADLANDS, "§4", "§c", Sound.MUSIC_DISC_WARD, 1.3f),

            new TerritoryInfo("bosque_artemisa", "Bosque de Artemisa", "PvE intenso. Pieles raras y artefactos de caza.",
                    "🏹 PVE", 3, Biome.FOREST, "§2", "§a", Sound.MUSIC_DISC_STRAD, 0.9f),

            new TerritoryInfo("atenas_antigua", "Atenas Antigua", "Centro económico. Reduce impuestos del clan.",
                    "💰 ECONOMÍA", 4, Biome.PLAINS, "§e", "§6", Sound.MUSIC_DISC_CHIRP, 1.0f),

            new TerritoryInfo("acantilados_poseidon", "Acantilados de Poseidón", "Genera recursos marinos raros.",
                    "🌊 MARINO", 5, Biome.BEACH, "§b", "§3", Sound.MUSIC_DISC_MALL, 0.85f),

            new TerritoryInfo("santuario_apolo", "Santuario de Apolo", "Misiones diarias disponibles.",
                    "✨ MISIONES", 6, Biome.SUNFLOWER_PLAINS, "§d", "§5", Sound.MUSIC_DISC_WAIT, 0.95f),

            new TerritoryInfo("ruinas_delfos", "Ruinas de Delfos", "Profecías. Revela eventos futuros.",
                    "🔮 PROFECÍA", 7, Biome.DESERT, "§d", "§5", Sound.MUSIC_DISC_STRAD, 1.1f),

            new TerritoryInfo("valle_ares", "Valle de Ares", "PvP constante en zona de guerra.",
                    "⚔ PVP", 8, Biome.SAVANNA, "§c", "§4", Sound.MUSIC_DISC_WARD, 1.2f),

            new TerritoryInfo("coloso_rodas", "Coloso de Rodas", "Evento boss mundial. 20+ JUG.",
                    "👹 BOSS", 9, Biome.TAIGA, "§4", "§c", Sound.MUSIC_DISC_STRAD, 1.15f),

            new TerritoryInfo("islas_egeas", "Islas Egeas", "Difícil de defender. Recursos insulares.",
                    "⚠ PELIGROSO", 10, Biome.BEACH, "§9", "§1", Sound.MUSIC_DISC_MALL, 0.9f)
    };

    // EGIPTO
    public static final TerritoryInfo[] EGYPT_TERRITORIES = {
            new TerritoryInfo("piramide_keops", "Pirámide de Keops", "Drop de reliquias legendarias.",
                    "💎 RELIQUIAS", 11, Biome.DESERT, "§6", "§e", Sound.MUSIC_DISC_WARD, 1.1f),

            new TerritoryInfo("valle_reyes", "Valle de los Reyes", "PvE de alto nivel.",
                    "⚔ ALTO NIVEL", 12, Biome.DESERT, "§c", "§4", Sound.MUSIC_DISC_STRAD, 1.15f),

            new TerritoryInfo("oasis_bastet", "Oasis de Bastet", "Zona segura temporal.",
                    "✓ SEGURO", 13, Biome.JUNGLE, "§a", "§2", Sound.MUSIC_DISC_WAIT, 0.9f),

            new TerritoryInfo("desierto_set", "Desierto de Set", "Mobs agresivos en todo el territorio.",
                    "⚠ PELIGROSO", 14, Biome.DESERT, "§c", "§4", Sound.MUSIC_DISC_WARD, 1.2f),

            new TerritoryInfo("templo_anubis", "Templo de Anubis", "Evento especial nocturno.",
                    "🌙 NOCTURNO", 15, Biome.DARK_FOREST, "§8", "§0", Sound.MUSIC_DISC_STRAD, 1.05f),

            new TerritoryInfo("rio_nilo", "Río Nilo", "Recursos económicos constantes.",
                    "💰 RECURSOS", 16, Biome.RIVER, "§b", "§3", Sound.MUSIC_DISC_MALL, 0.95f),

            new TerritoryInfo("biblioteca_thoth", "Biblioteca de Thoth", "Bonificación de experiencia global.",
                    "⭐ +XP GLOBAL", 17, Biome.DARK_FOREST, "§d", "§5", Sound.MUSIC_DISC_WAIT, 0.85f),

            new TerritoryInfo("necropolis", "Necrópolis", "Boss constante activo.",
                    "👹 BOSS", 18, Biome.DARK_FOREST, "§4", "§c", Sound.MUSIC_DISC_STRAD, 1.2f),

            new TerritoryInfo("templo_solar_ra", "Templo Solar de Ra", "Eventos durante el día solar.",
                    "☀ SOLAR", 19, Biome.SUNFLOWER_PLAINS, "§e", "§6", Sound.MUSIC_DISC_CHIRP, 1.0f),

            new TerritoryInfo("laberinto_sobek", "Laberinto de Sobek", "Dungeon de instancia.",
                    "🗝 DUNGEON", 20, Biome.SWAMP, "§9", "§1", Sound.MUSIC_DISC_MALL, 1.05f)
    };

    // CENTRAL
    public static final TerritoryInfo[] CENTRAL_TERRITORIES = {
            new TerritoryInfo("ruinas_imperio", "Ruinas del Primer Imperio", "Territorio más valioso. Fragmentos legendarios.",
                    "👑 LEGENDARIO", 21, Biome.PLAINS, "§4", "§c", Sound.MUSIC_DISC_STRAD, 1.3f),

            new TerritoryInfo("arena_titanes", "Arena de los Titanes", "Clan Wars automáticas.",
                    "⚔ CLAN WAR", 22, Biome.BADLANDS, "§c", "§4", Sound.MUSIC_DISC_WARD, 1.25f),

            new TerritoryInfo("puerta_inframundo", "Puerta del Inframundo", "Bosses constantes activos.",
                    "👹 BOSSES", 23, Biome.NETHER_WASTES, "§8", "§0", Sound.MUSIC_DISC_STRAD, 1.2f),

            new TerritoryInfo("ciudad_perdida", "Ciudad Perdida", "Recursos únicos exclusivos.",
                    "💎 ÚNICO", 24, Biome.JUNGLE, "§d", "§5", Sound.MUSIC_DISC_MALL, 1.1f),

            new TerritoryInfo("trono_dominador", "👑 TRONO DEL DOMINADOR", "El territorio central. Quién lo controla domina el servidor.",
                    "🏆 DOMINIO", 25, Biome.END_MIDLANDS, "§4", "§c", Sound.MUSIC_DISC_STRAD, 1.35f)
    };
}