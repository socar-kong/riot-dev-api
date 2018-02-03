package riot_dev_api.connection

import com.google.gson.JsonParser
import riot_dev_api.Global
import riot_dev_api.dto.champion_mastery_v3.ChampionMasteryDTO

class ChampionMasteryConnection : Connection {
    private val HOST: String
    private val URL_BY_SUMMONER_ID: String
    private val URL_BY_CHAMPION_ID: String
    private val URL_TOTAL_MASTERY_SCORE: String
    private val PARAM_API_KEY: String

    constructor(host: String) {
        this.HOST = host;
        this.URL_BY_SUMMONER_ID = "https://" + HOST + Global.ApiPath.CHAMPION_MASTERY_V3 + "champion-masteries/by-summoner/"
        this.URL_BY_CHAMPION_ID = "https://" + HOST + Global.ApiPath.CHAMPION_MASTERY_V3 + "champion-masteries/by-summoner/"
        this.URL_TOTAL_MASTERY_SCORE = "https://" + HOST + Global.ApiPath.CHAMPION_MASTERY_V3 + "scores/by-summoner/"
        this.PARAM_API_KEY = "?api_key="
    }

    /**
     * Get all champion mastery entries sorted by number of champion points descending,
     */
    public fun getChampMasteryList(summonerId: Long, apiKey: String): List<ChampionMasteryDTO> {
        var responde = connectAPI(URL_BY_SUMMONER_ID + summonerId + PARAM_API_KEY + apiKey,0)
        var list = arrayListOf<ChampionMasteryDTO>()
        if(responde.isNotEmpty()){
            var parser = JsonParser()
            var element = parser.parse(responde)
            var arr = element.asJsonArray

            for (item in arr) {
                var champMastery = ChampionMasteryDTO()
                champMastery.chestGranted = item.asJsonObject["chestGranted"].asBoolean
                champMastery.championLevel = item.asJsonObject["championLevel"].asInt
                champMastery.championPoints = item.asJsonObject["championPoints"].asInt
                champMastery.championId = item.asJsonObject["championId"].asLong
                champMastery.playerId = item.asJsonObject["playerId"].asLong
                champMastery.championPointsUntilNextLevel = item.asJsonObject["championPointsUntilNextLevel"].asLong
                champMastery.tokensEarned = item.asJsonObject["tokensEarned"].asInt
                champMastery.championPointsSinceLastLevel = item.asJsonObject["championPointsSinceLastLevel"].asLong
                champMastery.lastPlayTime = item.asJsonObject["lastPlayTime"].asLong
                list.add(champMastery)
            }
        }
        return list
    }

    /**
     * Get a champion mastery by player ID and champion ID.
     */
    public fun getChampMastery(summonerId: Long, championId: Long, apiKey: String): ChampionMasteryDTO {
        var responde = connectAPI(URL_BY_CHAMPION_ID + summonerId + "/by-champion/" + championId + PARAM_API_KEY + apiKey,0)
        var champMastery = ChampionMasteryDTO()
        if(responde.isNotEmpty()){
            var parser = JsonParser()
            var element = parser.parse(responde)

            champMastery.chestGranted = element.asJsonObject["chestGranted"].asBoolean
            champMastery.championLevel = element.asJsonObject["championLevel"].asInt
            champMastery.championPoints = element.asJsonObject["championPoints"].asInt
            champMastery.championId = element.asJsonObject["championId"].asLong
            champMastery.playerId = element.asJsonObject["playerId"].asLong
            champMastery.championPointsUntilNextLevel = element.asJsonObject["championPointsUntilNextLevel"].asLong
            champMastery.tokensEarned = element.asJsonObject["tokensEarned"].asInt
            champMastery.championPointsSinceLastLevel = element.asJsonObject["championPointsSinceLastLevel"].asLong
            champMastery.lastPlayTime = element.asJsonObject["lastPlayTime"].asLong
        }
        return champMastery
    }

    /**
     * Get a player's total champion mastery score, which is the sum of individual champion mastery levels.
     */
    public fun getTotalMasteryScore(summonerId: Long, apiKey: String): Int {
        var responde = connectAPI(URL_TOTAL_MASTERY_SCORE + summonerId + PARAM_API_KEY + apiKey,0)
        var score : Int = -1
        if(responde.isNotEmpty()){
            score = Integer.parseInt(responde)
        }
        return score
    }

}