package com.invictus.territory.api;

import com.invictus.territory.model.Territory;
import java.util.List;
import java.util.UUID;

public interface ITerritoryAPI {

    Territory getTerritory(String id);

    List<Territory> getAllTerritories();

    List<Territory> getTerritoriesByOwner(UUID clanId);

    boolean claimTerritory(Territory territory, UUID clanId);

    boolean unclaimTerritory(Territory territory, UUID clanId);

    boolean isVulnerable(Territory territory);
}